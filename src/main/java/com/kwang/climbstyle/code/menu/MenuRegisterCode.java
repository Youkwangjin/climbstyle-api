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

    U0001("U0001", "내 피드",            MenuTarget.USER),
    U0002("U0002", "마이페이지",          MenuTarget.USER),
    U0003("U0003", "주문/결제 내역",       MenuTarget.USER),
    U0004("U0004", "1:1 문의",          MenuTarget.USER),

    A0001("A0001", "회원",              MenuTarget.ADMIN),
    A0002("A0002", "콘텐츠",            MenuTarget.ADMIN),
    A0003("A0003", "상품",              MenuTarget.ADMIN),
    A0004("A0004", "회원 관리",         MenuTarget.ADMIN),
    A0005("A0005", "공지사항 관리",     MenuTarget.ADMIN),
    A0006("A0006", "1:1 문의 관리",    MenuTarget.ADMIN),
    A0007("A0007", "FAQ 관리",         MenuTarget.ADMIN),
    A0008("A0008", "상품 관리",         MenuTarget.ADMIN),
    A0009("A0009", "주문 관리",         MenuTarget.ADMIN),
    A0010("A0010", "배너 관리",         MenuTarget.ADMIN),
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
