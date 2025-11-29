package com.kwang.climbstyle.code.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCode {
    FEED("피드", "/feed", "Y", MenuGroup.NAV),
    RANKING("랭킹", "/ranking", "Y", MenuGroup.NAV),
    SHOP("샵", "/shop", "Y", MenuGroup.NAV),
    FAQ("FAQ", "/faq", "Y", MenuGroup.NAV),

    LOGIN("로그인", "/auth/login", "Y", MenuGroup.CTA),
    REGISTER("가입하기", "/auth/register", "Y", MenuGroup.CTA),
    ;

    private final String menuName;

    private final String menuUrl;

    private final String menuUseYn;

    private final MenuGroup menuGroup;

    public enum MenuGroup {
        NAV, CTA
    }
}