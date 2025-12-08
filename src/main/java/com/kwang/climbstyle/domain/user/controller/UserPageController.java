package com.kwang.climbstyle.domain.user.controller;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.user.dto.response.UserProfileResponse;
import com.kwang.climbstyle.domain.user.service.UserService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;

    @GetMapping("/my/profile")
    public String myProfile(Model model) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        UserProfileResponse userProfile = userService.selectUserByNo(userNo);

        model.addAttribute("profile", userProfile);
        return "my/profile";
    }

    @GetMapping("/my/profile/update")
    public String myProfileUpdate(Model model) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.UNAUTHORIZED_ERROR);
        }

        UserProfileResponse userProfile = userService.selectUserByNo(userNo);

        model.addAttribute("profile", userProfile);
        return "my/profile-update";
    }
}
