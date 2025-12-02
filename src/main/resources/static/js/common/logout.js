function logout() {
    fetch("/logout", {
        method: "POST",
        credentials: "include"
    })
        .then(async response => {
            if (!response.ok) {
                alert("로그아웃에 실패했습니다. 다시 시도해주세요.");
                return;
            }

            alert("로그아웃되었습니다. 메인페이지로 이동합니다.");
            window.location.href = "/";
        })
        .catch(() => {
            alert("일시적인 문제가 발생했습니다. 지속될 경우 관리자한테 문의하세요.");
        });
}
