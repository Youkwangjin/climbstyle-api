package com.kwang.climbstyle.domain.admin.service;

import com.kwang.climbstyle.code.user.UserSuspendCategory;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AdminUserMailService {

    private static final String SUSPEND_MAIL_SUBJECT = "[ClimbStyle] 계정 이용 정지 안내";

    private static final String SUSPEND_MAIL_TEMPLATE = "mail/user-suspend";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public void sendSuspendMail(UserEntity user, UserSuspendCategory suspendCategory,
                                String suspendReason, LocalDateTime suspendUntil) {
        final String suspendUntilText;
        if (suspendUntil == null) {
            suspendUntilText = "영구 정지";
        } else {
            suspendUntilText = suspendUntil.format(DATE_FORMATTER) + "까지";
        }

        Context context = new Context();
        context.setVariable("nickname", user.getUserNickname());
        context.setVariable("categoryDescription", suspendCategory.getDescription());
        context.setVariable("reason", suspendReason);
        context.setVariable("suspendUntil", suspendUntilText);

        String html = templateEngine.process(SUSPEND_MAIL_TEMPLATE, context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(user.getUserEmail());
            helper.setSubject(SUSPEND_MAIL_SUBJECT);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("정지 안내 메일 발송에 실패했습니다.", e);
        }
    }
}
