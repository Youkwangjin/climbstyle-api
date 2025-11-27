package com.kwang.climbstyle.security.user;

import com.kwang.climbstyle.code.user.UserDeleteCode;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Integer getUserNo() {
        return userEntity.getUserNo();
    }

    @Override
    public String getUsername() {
        return userEntity.getUserId();
    }

    @Override
    public String getPassword() {
        return userEntity.getUserPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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

    @Override
    public boolean isEnabled() {
        return !Objects.equals(userEntity.getUserDeleteYn(), UserDeleteCode.INACTIVE.getCode());
    }
}
