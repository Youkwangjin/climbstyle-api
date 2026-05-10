package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.code.user.VerificationPurpose;
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

/**
 * 이메일 인증 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final int EXPIRY_MINUTES = 5;

    private final JavaMailSender mailSender;

    private final UserRepository userRepository;

    private final TemplateEngine templateEngine;

    /**
     * 이메일 인증 코드 발송 (요청 객체)
     */
    @Transactional(readOnly = true)
    public void sendCode(VerificationPurpose purpose, UserEmailRequest request, HttpSession session) {
        sendCode(purpose, request.getUserEmail(), session);
    }

    /**
     * 이메일 인증 코드 발송 (이메일 직접 전달)
     */
    @Transactional(readOnly = true)
    public void sendCode(VerificationPurpose purpose, String email, HttpSession session) {
        validateForPurpose(purpose, email);

        final String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        final LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRY_MINUTES);
        session.setAttribute(purpose.getSessionKey(), new EmailVerificationData(email, code, expiresAt));

        sendMail(purpose, email, code);
    }

    /**
     * 이메일 인증 코드 검증
     */
    public void verifyCode(VerificationPurpose purpose, UserEmailVerificationRequest request, HttpSession session) {
        final String email = request.getUserEmail();
        final String code = request.getVerificationCode();
        final String sessionKey = purpose.getSessionKey();
        EmailVerificationData data = (EmailVerificationData) session.getAttribute(sessionKey);

        if (data == null || !StringUtils.equals(data.getEmail(), email)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_VERIFICATION_CODE_MISMATCH);
        }
        if (LocalDateTime.now().isAfter(data.getExpiresAt())) {
            session.removeAttribute(sessionKey);
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_VERIFICATION_EXPIRED);
        }
        if (!StringUtils.equals(data.getCode(), code)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_VERIFICATION_CODE_MISMATCH);
        }

        data.setVerified(true);
        session.setAttribute(sessionKey, data);
    }

    /**
     * 이메일 인증 완료 여부 확인
     */
    public void checkVerified(VerificationPurpose purpose, String email, HttpSession session) {
        final String sessionKey = purpose.getSessionKey();
        EmailVerificationData data = (EmailVerificationData) session.getAttribute(sessionKey);

        if (data == null || !data.isVerified() || !StringUtils.equals(data.getEmail(), email)) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_NOT_VERIFIED);
        }

        session.removeAttribute(sessionKey);
    }

    /**
     * 인증 목적별 이메일 사전 검증
     */
    private void validateForPurpose(VerificationPurpose purpose, String email) {
        switch (purpose) {
            case REGISTER -> {
                if (userRepository.existUserEmail(email)) {
                    throw new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED);
                }
            }
            case FIND_ID, FIND_PW -> {
                if (userRepository.selectNonOAuthUserByEmail(email) == null) {
                    throw new ClimbStyleException(UserErrorCode.USER_FIND_ID_NOT_FOUND);
                }
            }
        }
    }

    /**
     * 인증 목적에 맞는 이메일 발송
     */
    private void sendMail(VerificationPurpose purpose, String email, String code) {
        try {
            Context context = new Context();
            context.setVariable("code", code);
            String html = templateEngine.process(purpose.getTemplate(), context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject(purpose.getSubject());
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }
}
