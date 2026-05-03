package com.kwang.climbstyle.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmailVerificationData implements Serializable {

    private String email;
    private String code;
    private LocalDateTime expiresAt;
    private boolean verified;

    public EmailVerificationData(String email, String code, LocalDateTime expiresAt) {
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
        this.verified = false;
    }
}
