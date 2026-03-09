package com.kwang.climbstyle.domain.role.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity {

    private Integer userRoleNo;

    private Integer userNo;

    private Integer roleNo;
}
