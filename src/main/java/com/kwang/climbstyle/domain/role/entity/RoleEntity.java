package com.kwang.climbstyle.domain.role.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    private Integer roleNo;

    private String roleName;

    private String roleDescription;

    private LocalDateTime roleCreated;

    private LocalDateTime roleUpdated;
}
