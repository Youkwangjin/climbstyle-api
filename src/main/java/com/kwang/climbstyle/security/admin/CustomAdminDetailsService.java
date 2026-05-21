package com.kwang.climbstyle.security.admin;

import com.kwang.climbstyle.domain.admin.entity.AdminEntity;
import com.kwang.climbstyle.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 관리자 인증 정보 로드 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
        AdminEntity adminEntity = adminRepository.selectAdminById(adminId);
        if(adminEntity == null){
            throw new UsernameNotFoundException("Admin not found with id: " + adminId);
        }

        return new CustomAdminDetails(adminEntity);
    }
}
