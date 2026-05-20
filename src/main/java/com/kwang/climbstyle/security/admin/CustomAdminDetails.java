package com.kwang.climbstyle.security.admin;

import com.kwang.climbstyle.domain.admin.entity.AdminEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 관리자 인증 정보
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
public class CustomAdminDetails implements UserDetails {

    private final AdminEntity adminEntity;

    public CustomAdminDetails(AdminEntity adminEntity) {
        this.adminEntity = adminEntity;
    }

    public Integer adminNo() {
        return adminEntity.getAdminNo();
    }

    public String adminNickname() {
        return adminEntity.getAdminNickname();
    }

    public String adminImageUrl() {
        return adminEntity.getAdminImageUrl();
    }

    @Override
    public String getUsername() {
        return adminEntity.getAdminId();
    }

    @Override
    public String getPassword() {
        return adminEntity.getAdminPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : adminEntity.getAdminRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
