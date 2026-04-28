package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.domain.user.dto.EmailVerificationData;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailVerificationRequest;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    static final String SESSION_KEY = "EMAIL_VERIFICATION";

    private static final int EXPIRY_MINUTES = 5;

    private final JavaMailSender mailSender;

    private final UserRepository userRepository;

    private final TemplateEngine templateEngine;

    @Transactional(readOnly = true)
    public void sendVerificationCode(UserEmailRequest request, HttpSession session) {
        final String email = request.getUserEmail();

        if (userRepository.existUserEmail(email)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED);
        }

        final String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        final LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRY_MINUTES);

        session.setAttribute(SESSION_KEY, new EmailVerificationData(email, code, expiresAt));

        try {
            Context context = new Context();
            context.setVariable("code", code);
            String html = templateEngine.process("mail/email-verification", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[ClimbStyle] 이메일 인증번호 안내");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }

    public void verifyCode(UserEmailVerificationRequest request, HttpSession session) {
        final String email = request.getUserEmail();
        final String code = request.getVerificationCode();

        EmailVerificationData data = (EmailVerificationData) session.getAttribute(SESSION_KEY);

        if (data == null || !StringUtils.equals(data.getEmail(), email)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_VERIFICATION_CODE_MISMATCH);
        }
        if (LocalDateTime.now().isAfter(data.getExpiresAt())) {
            session.removeAttribute(SESSION_KEY);
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_VERIFICATION_EXPIRED);
        }
        if (!StringUtils.equals(data.getCode(), code)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_VERIFICATION_CODE_MISMATCH);
        }

        data.setVerified(true);
        session.setAttribute(SESSION_KEY, data);
    }

    public void checkEmailVerified(String email, HttpSession session) {
        EmailVerificationData data = (EmailVerificationData) session.getAttribute(SESSION_KEY);

        if (data == null || !data.isVerified() || !StringUtils.equals(data.getEmail(), email)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_NOT_VERIFIED);
        }

        session.removeAttribute(SESSION_KEY);
    }
}
