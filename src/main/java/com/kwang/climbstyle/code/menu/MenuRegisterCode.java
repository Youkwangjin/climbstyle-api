package com.kwang.climbstyle.code.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 등록 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-26
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum MenuRegisterCode {

    U0100("U0100", "마이페이지",          MenuTarget.USER),
    U0200("U0200", "내 피드",            MenuTarget.USER),
    U0300("U0300", "주문/결제 내역",      MenuTarget.USER),
    U0400("U0400", "1:1 문의",          MenuTarget.USER),

    A0100("A0100", "회원",              MenuTarget.ADMIN),
    A0101("A0101", "회원 관리",         MenuTarget.ADMIN),

    A0200("A0200", "콘텐츠",            MenuTarget.ADMIN),
    A0201("A0201", "공지사항 관리",     MenuTarget.ADMIN),
    A0202("A0202", "1:1 문의 관리",    MenuTarget.ADMIN),
    A0203("A0203", "FAQ 관리",         MenuTarget.ADMIN),
    A0204("A0204", "배너 관리",         MenuTarget.ADMIN),

    A0300("A0300", "상품",              MenuTarget.ADMIN),
    A0301("A0301", "상품 관리",         MenuTarget.ADMIN),
    A0302("A0302", "주문 관리",         MenuTarget.ADMIN),

    A0400("A0400", "메뉴",              MenuTarget.ADMIN),
    A0401("A0401", "메뉴 관리",         MenuTarget.ADMIN),
    ;

    private final String code;

    private final String displayName;

    private final MenuTarget target;

    public static List<MenuRegisterCode> getUserCodes() {
        List<MenuRegisterCode> result = new ArrayList<>();
        for (MenuRegisterCode item : values()) {
            if (item.target == MenuTarget.USER) {
                result.add(item);
            }
        }
        return result;
    }

    public static List<MenuRegisterCode> getAdminCodes() {
        List<MenuRegisterCode> result = new ArrayList<>();
        for (MenuRegisterCode item : values()) {
            if (item.target == MenuTarget.ADMIN) {
                result.add(item);
            }
        }
        return result;
    }

    public enum MenuTarget {
        USER, ADMIN
    }
}
