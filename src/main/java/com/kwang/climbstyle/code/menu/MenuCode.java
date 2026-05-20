package com.kwang.climbstyle.code.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum MenuCode {
    ABOUT("서비스", "/about", "Y", MenuGroup.NAV),
    FEED("피드", "/feeds", "Y", MenuGroup.NAV),
    RANKING("랭킹", "/rankings/realtime", "Y", MenuGroup.NAV),
    SHOP("공지사항", "/notices", "Y", MenuGroup.NAV),
    FAQ("FAQ", "/faqs", "Y", MenuGroup.NAV),

    LOGIN("로그인", "/auth/login", "Y", MenuGroup.CTA),
    REGISTER("가입하기", "/auth/register", "Y", MenuGroup.CTA),
    ;

    private final String menuName;

    private final String menuUrl;

    private final String menuUseYn;

    private final MenuGroup menuGroup;

    public static List<MenuCode> getNavMenus() {
        List<MenuCode> result = new ArrayList<>();
        for (MenuCode menuCode : values()) {
            if (menuCode.getMenuGroup() == MenuGroup.NAV && StringUtils.equals(menuCode.menuUseYn, "Y")) {
                result.add(menuCode);
            }
        }

        return result;
    }

    public static List<MenuCode> getCtaMenus() {
        List<MenuCode> result = new ArrayList<>();
        for (MenuCode menuCode : values()) {
            if (menuCode.menuGroup == MenuGroup.CTA && StringUtils.equals(menuCode.menuUseYn, "Y")) {
                result.add(menuCode);
            }
        }

        return result;
    }

    public enum MenuGroup {
        NAV, CTA
    }
}