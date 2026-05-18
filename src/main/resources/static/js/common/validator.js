const Validator = {
    rules: {
        required(value, fieldName) {
            if (!value || value.trim() === "") {
                alert(`${fieldName}을(를) 입력해주세요.`);
                return false;
            }
            return true;
        },

        minLength(value, fieldName, min) {
            if (value.length < min) {
                alert(`${fieldName}은(는) ${min}자 이상 입력해주세요.`);
                return false;
            }
            return true;
        },

        maxLength(value, fieldName, max) {
            if (value.length > max) {
                alert(`${fieldName}은(는) ${max}자 이하로 입력해주세요.`);
                return false;
            }
            return true;
        },

        range(value, fieldName, min, max) {
            if (value.length < min || value.length > max) {
                alert(`${fieldName}은(는) ${min}~${max}자로 입력해주세요.`);
                return false;
            }
            return true;
        },

        email(value, fieldName) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(value)) {
                alert(`올바른 ${fieldName} 형식이 아닙니다.`);
                return false;
            }
            return true;
        },

        password(value, fieldName) {
            const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/;
            if (!passwordRegex.test(value)) {
                alert(`${fieldName}은(는) 영문, 숫자, 특수문자를 포함한 8~20자로 입력해주세요.`);
                return false;
            }
            return true;
        },

        userId(value, fieldName) {
            const userIdRegex = /^[a-zA-Z0-9]{4,20}$/;
            if (!userIdRegex.test(value)) {
                alert(`${fieldName}은(는) 영문, 숫자만 사용 가능하며 4~20자로 입력해주세요.`);
                return false;
            }
            return true;
        },

        nickname(value, fieldName) {
            const nicknameRegex = /^(?!\.)(?!.*\.\.)[가-힣a-zA-Z0-9_.]{2,20}(?<!\.)$/;
            if (!nicknameRegex.test(value)) {
                alert(`${fieldName}은(는) 한글, 영문, 숫자, _, .을 사용 가능하며 2~20자로 입력해주세요. (.은 맨 앞/뒤, 연속 사용 불가)`);
                return false;
            }
            return true;
        },

        match(value, compareValue, fieldName) {
            if (value !== compareValue) {
                alert(`${fieldName}이(가) 일치하지 않습니다.`);
                return false;
            }
            return true;
        },

        minValue(value, fieldName, min) {
            if (parseFloat(value) < min) {
                alert(`${fieldName}은(는) ${min} 이상이어야 합니다.`);
                return false;
            }
            return true;
        },

        maxValue(value, fieldName, max) {
            if (parseFloat(value) > max) {
                alert(`${fieldName}은(는) ${max} 이하여야 합니다.`);
                return false;
            }
            return true;
        },

        fileSize(file, fieldName, maxSize) {
            if (file && file.size > maxSize) {
                const mb = (maxSize / (1024 * 1024)).toFixed(0);
                alert(`${fieldName}은(는) ${mb}MB 이하만 업로드 가능합니다.`);
                return false;
            }
            return true;
        },

        fileExtension(file, fieldName, allowedExts) {
            if (file) {
                const ext = file.name.split('.').pop().toLowerCase();
                if (!allowedExts.includes(ext)) {
                    alert(`${fieldName}은(는) ${allowedExts.join(', ')} 형식만 가능합니다.`);
                    return false;
                }
            }
            return true;
        },

        url(value, fieldName) {
            try {
                new URL(value);
                return true;
            } catch {
                alert(`올바른 ${fieldName} 형식이 아닙니다.`);
                return false;
            }
        }
    },

    register() {
        const userId = document.getElementById("userId").value.trim();
        const userPassword = document.getElementById("userPassword").value.trim();
        const passwordConfirm = document.getElementById("passwordConfirm").value.trim();
        const userNm = document.getElementById("userNm").value.trim();
        const userEmail = document.getElementById("userEmail").value.trim();
        const userNickname = document.getElementById("userNickname").value.trim();

        if (!this.rules.required(userId, "아이디")) return false;
        if (!this.rules.userId(userId, "아이디")) return false;
        if (!this.rules.required(userPassword, "비밀번호")) return false;
        if (!this.rules.password(userPassword, "비밀번호")) return false;
        if (!this.rules.match(userPassword, passwordConfirm, "비밀번호")) return false;
        if (!this.rules.required(userNm, "이름")) return false;
        if (!this.rules.required(userEmail, "이메일")) return false;
        if (!this.rules.email(userEmail, "이메일")) return false;
        if (!this.rules.required(userNickname, "닉네임")) return false;
        return this.rules.nickname(userNickname, "닉네임");
    },

    login() {
        const userId = document.getElementById("userId").value.trim();
        const userPassword = document.getElementById("userPassword").value.trim();

        if (!this.rules.required(userId, "아이디")) return false;
        return this.rules.required(userPassword, "비밀번호");
    },

    profile() {
        const userName = document.getElementById("userNm").value.trim();
        const userNickname = document.getElementById("userNickname").value.trim();
        const userProfileImg = document.getElementById("userProfileImg").files[0];

        if (!this.rules.required(userName, "이름")) return false;
        if (!this.rules.required(userNickname, "닉네임")) return false;
        if (!this.rules.nickname(userNickname, "닉네임")) return false;
        return this.rules.fileSize(userProfileImg, "프로필 이미지", 5 * 1024 * 1024);
    },

    password() {
        const currentPassword = document.getElementById("currentPassword").value.trim();
        const newPassword = document.getElementById("userPassword").value.trim();
        const confirmPassword = document.getElementById("newUserPassword").value.trim();

        if (!this.rules.required(currentPassword, "현재 비밀번호")) return false;
        if (!this.rules.required(newPassword, "새 비밀번호")) return false;
        if (!this.rules.password(newPassword, "새 비밀번호")) return false;
        return this.rules.match(newPassword, confirmPassword, "새 비밀번호");
    },

    _validateFeed(titleId, visibilityId) {
        const feedTitle = document.getElementById(titleId).value.trim();
        const feedVisibleYn = document.getElementById(visibilityId).value.trim();

        if (!this.rules.required(feedTitle, "제목")) return false;
        if (!this.rules.minLength(feedTitle, "제목", 2)) return false;
        if (!this.rules.maxLength(feedTitle, "제목", 50)) return false;
        if (!feedVisibleYn) {
            alert("공개 설정을 선택해주세요.");
            return false;
        }

        if (feedVisibleYn === "N") {
            if (!confirm("게시글을 비공개로 설정하면 랭킹 시스템에서 제외됩니다.")) {
                return false;
            }
        }

        return true;
    },

    feed() {
        if (!this._validateFeed("feedTitle", "feedVisibleYn")) {
            return false;
        }

        if (typeof FeedImageUploader === "undefined") {
            alert("이미지 업로더를 찾을 수 없습니다.");
            return false;
        }

        const images = FeedImageUploader.getFiles();
        if (images.length === 0) {
            alert("이미지를 최소 1장 이상 선택해주세요.");
            return false;
        }

        for (let i = 0; i < images.length; i++) {
            const file = images[i];

            if (!this.rules.fileSize(file, "이미지", 5 * 1024 * 1024)) {
                return false;
            }

            if (!this.rules.fileExtension(file, "이미지", ["jpg", "jpeg", "png"])) {
                return false;
            }
        }

        return true;
    },

    feedUpdate() {
        if (!this._validateFeed("editFeedTitle", "editFeedVisibleYn")) {
            return false;
        }

        const feedLikeVisibleYn = document.getElementById("editFeedLikeVisibleYn").value.trim();

        if (feedLikeVisibleYn === "N") {
            if (!confirm("좋아요를 비공개로 설정하면 랭킹 시스템에서 제외됩니다.")) {
                return false;
            }
        }

        return true;
    },

    comment() {
        const feedCommentContent = document.getElementById("feedCommentContent").value.trim();
        if (!this.rules.required(feedCommentContent, "댓글")) return false;
        return this.rules.maxLength(feedCommentContent, "댓글", 500);
    },

    notice() {
        const noticeCategory = document.getElementById("noticeCategory").value.trim();
        const noticeTitle = document.getElementById("noticeTitle").value.trim();
        const noticeContent = quill.getText();

        if (!noticeCategory) {
            alert("카테고리를 선택해주세요.");
            return false;
        }

        if (!this.rules.required(noticeTitle, "제목")) return false;
        if (!this.rules.maxLength(noticeTitle, "제목", 100)) return false;

        if (!noticeContent || noticeContent === "") {
            alert("내용을 입력해주세요.");
            return false;
        }

        return true;
    },

    inquiryAnswer() {
        if (!quill.getText().trim()) {
            alert("답변 내용을 입력해주세요.");
            return false;
        }
        return true;
    },

    inquiry() {
        const inquiryTitle = document.getElementById("inquiryTitle").value.trim();
        const inquiryContent = document.getElementById("inquiryContent").value.trim();

        if (!this.rules.required(inquiryTitle, "제목")) return false;
        if (!this.rules.maxLength(inquiryTitle, "제목", 50)) return false;

        if (!this.rules.required(inquiryContent, "내용")) return false;
        return this.rules.maxLength(inquiryContent, "내용", 2000);
    },

    faq() {
        const faqQuestion = document.getElementById("faqQuestion").value.trim();
        const faqAnswer = document.getElementById("faqAnswer").value.trim();

        if (!this.rules.required(faqQuestion, "질문")) return false;
        if (!this.rules.maxLength(faqQuestion, "질문", 200)) return false;
        return this.rules.required(faqAnswer, "답변");
    },

    checkField(fieldId, fieldName) {
        const value = document.getElementById(fieldId)?.value.trim();

        if (!value) {
            alert(`${fieldName}을(를) 입력한 후 중복 확인을 진행해 주세요.`);
            return false;
        }

        return true;
    },

    clear(fields) {
        fields.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.value = "";
        });
    }
};