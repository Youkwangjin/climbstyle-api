let currentImageIndex = 0;
let totalImages = 0;
const currentUserNo = parseInt(document.getElementById("current-user-meta")?.dataset.userNo || "0");

/**
 * @typedef {Object} FeedDetailData
 * @property {string} userNickName
 * @property {string} feedCreated
 * @property {string} userImageUrl
 * @property {string} feedTitle
 * @property {string} feedContent
 * @property {number} feedLikeCount
 * @property {boolean} isLiked
 * @property {number} feedCommentCount
 * @property {string[]} feedFilePaths
 * @property {FeedComment[]} feedCommentList
 * @property {boolean} isAuthor
 */

/**
 * @typedef {Object} FeedComment
 * @property {string} userNickname
 * @property {string} userImageUrl
 * @property {number} feedCommentNo
 * @property {string} feedCommentContent
 * @property {string} feedCommentCreated
 */

function openFeedDetail(feedNo) {
    fetch(`/api/v1/feeds/${feedNo}`)
        .then(response => response.json())
        .then(data => {
            const feed = data.data;
            const modal = document.getElementById("feedDetailModal");

            modal.dataset.feedNo = feedNo;

            currentImageIndex = 0;

            modal.classList.add("is-active");
            document.body.dataset.scrollY = window.scrollY;
            document.body.style.overflow = "hidden";

            document.getElementById("detailUsername").textContent = feed.userNickName;
            document.getElementById("detailDate").textContent = formatDate(feed.feedCreated);

            const detailAvatar = document.querySelector(".detail-avatar");
            if (feed.userImageUrl) {
                detailAvatar.innerHTML = `<img id="detailUserImage" src="${feed.userImageUrl}" alt="프로필" />`;
            } else {
                detailAvatar.innerHTML = "";
            }

            document.getElementById("detailTitle").textContent = feed.feedTitle;
            document.getElementById("detailContent").textContent = feed.feedContent;

            document.getElementById("detailLikeCount").textContent = feed.feedLikeCount;
            document.getElementById("detailCommentCount").textContent = feed.feedCommentCount;

            renderImages(feed.feedFilePaths);
            renderComments(feed.feedCommentList);

            const likeCountEl = document.getElementById("detailLikeCount");
            const likeCountBtn = document.getElementById("detailLikeCountBtn");
            const isLikeHidden = feed.feedLikeVisibleYn === 'N';
            likeCountEl.style.display = isLikeHidden ? "none" : "";
            if (likeCountBtn) likeCountBtn.style.display = isLikeHidden ? "none" : "";
            updateLikeStatus(feed.feedLikeCount, feed.isLiked);
            syncListCardComment(feedNo, feed.feedCommentCount);

            updateMoreButton(feed.isAuthor);
        })
        .catch(() => {
            alert("피드를 불러오는데 실패했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}

function closeFeedDetail() {
    const modal = document.getElementById("feedDetailModal");
    modal.classList.remove("is-active");
    document.body.style.overflow = "";
    window.scrollTo(0, parseInt(document.body.dataset.scrollY || "0"));

    const sliderImages = document.getElementById("sliderImages");
    if (sliderImages) {
        sliderImages.style.transform = "translateX(0)";
    }

    document.querySelectorAll(".slider-dot").forEach((dot, index) => {
        dot.classList.toggle("is-active", index === 0);
    });

    currentImageIndex = 0;
}

function renderImages(imagePaths) {
    if (!imagePaths || imagePaths.length === 0) {
        return;
    }

    totalImages = imagePaths.length;
    currentImageIndex = 0;

    const sliderImages = document.getElementById("sliderImages");
    const sliderDots = document.getElementById("sliderDots");

    sliderImages.innerHTML = imagePaths.map(path =>
        `<img src="${path}" alt="피드 이미지" />`
    ).join("");

    sliderImages.style.transform = "translateX(0)";

    if (totalImages > 1) {
        sliderDots.innerHTML = imagePaths.map((_, index) =>
            `<span class="slider-dot ${index === 0 ? "is-active" : ""}" onclick="goToImage(${index})"></span>`
        ).join("");
    } else {
        sliderDots.innerHTML = "";
    }

    updateSliderButtons();
}

function prevImage() {
    if (currentImageIndex > 0) {
        currentImageIndex--;
        updateSlider();
    }
}

function nextImage() {
    if (currentImageIndex < totalImages - 1) {
        currentImageIndex++;
        updateSlider();
    }
}

function goToImage(index) {
    currentImageIndex = index;
    updateSlider();
}

function updateSlider() {
    const sliderImages = document.getElementById("sliderImages");
    sliderImages.style.transform = `translateX(-${currentImageIndex * 100}%)`;

    document.querySelectorAll(".slider-dot").forEach((dot, index) => {
        dot.classList.toggle("is-active", index === currentImageIndex);
    });

    updateSliderButtons();
}

function updateSliderButtons() {
    const prevBtn = document.querySelector(".slider-prev");
    const nextBtn = document.querySelector(".slider-next");

    prevBtn.disabled = currentImageIndex === 0;
    nextBtn.disabled = currentImageIndex === totalImages - 1;

    if (totalImages <= 1) {
        prevBtn.style.display = "none";
        nextBtn.style.display = "none";
    } else {
        prevBtn.style.display = "flex";
        nextBtn.style.display = "flex";
    }
}

function renderComments(comments) {
    const container = document.getElementById("detailComments");

    if (!comments || comments.length === 0) {
        container.innerHTML = "<p style=\"text-align: center; color: var(--muted); padding: 40px 0;\">댓글이 없습니다.</p>";
        return;
    }

    const parentComments = comments.filter(c => !c.feedCommentParentNo);

    const replyMap = {};
    comments.forEach(comment => {
        if (comment.feedCommentParentNo) {
            if (!replyMap[comment.feedCommentParentNo]) {
                replyMap[comment.feedCommentParentNo] = [];
            }
            replyMap[comment.feedCommentParentNo].push(comment);
        }
    });

    container.innerHTML = parentComments.map(comment => `
        <div class="comment-item">
            <div class="comment-avatar">
                ${comment.userImageUrl ? `<img src="${comment.userImageUrl}" alt="프로필" />` : ''}
            </div>
            <div class="comment-body">
                <div class="comment-content">
                    <span class="comment-username">${comment.userNickname}</span>
                    <span class="comment-text">${comment.feedCommentContent}</span>
                </div>
                <div class="comment-meta">
                    <span class="comment-date">${formatDate(comment.feedCommentCreated)}</span>
                    <span class="comment-reply" onclick="replyToComment(${comment.feedCommentNo}, '${comment.userNickname}')">답글 달기</span>
                    ${comment.userNo === currentUserNo ? `<span class="comment-delete" onclick="deleteComment(${comment.feedCommentNo})">삭제</span>` : ''}
                </div>

                ${replyMap[comment.feedCommentNo] ? `
                    <div class="comment-replies">
                        ${replyMap[comment.feedCommentNo].map(reply => `
                            <div class="comment-item reply-item">
                                <div class="comment-avatar">
                                    ${reply.userImageUrl ? `<img src="${reply.userImageUrl}" alt="프로필" />` : ''}
                                </div>
                                <div class="comment-body">
                                    <div class="comment-content">
                                        <span class="comment-username">${reply.userNickname}</span>
                                        <span class="comment-text">${reply.feedCommentContent}</span>
                                    </div>
                                    <div class="comment-meta">
                                        <span class="comment-date">${formatDate(reply.feedCommentCreated)}</span>
                                        ${reply.userNo === currentUserNo ? `<span class="comment-delete" onclick="deleteComment(${reply.feedCommentNo})">삭제</span>` : ''}
                                    </div>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                ` : ''}
            </div>
        </div>
    `).join("");
}

function updateLikeStatus(likeCount, isLiked) {
    const likeCountSpan = document.getElementById("detailLikeCount");
    const likeIcon = document.getElementById("detailLikeIcon");
    const likeBtn = document.getElementById("detailLikeBtn");

    likeCountSpan.textContent = likeCount ?? 0;

    if (isLiked) {
        likeBtn.classList.add("is-liked");
        likeIcon.innerHTML = Icons.heartFilled;
    } else {
        likeBtn.classList.remove("is-liked");
        likeIcon.innerHTML = Icons.heart;
    }
}

function likeFromDetail() {
    const feedNo = getCurrentFeedNo();

    API.callJson(`/api/v1/feeds/${feedNo}/like`, {
        method: "POST"
    })
        .then(async response => {
            const result = await response.json();

            if (response.ok) {
                const { isLiked, feedLikeCount } = result.data;
                updateLikeStatus(feedLikeCount, isLiked);
                syncListCard(feedNo, isLiked, feedLikeCount);
            } else {
                alert(result.message);
            }
        })
        .catch(() => {
            alert("일시적인 문제가 발생했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}

function syncListCard(feedNo, isLiked, feedLikeCount) {
    const card = document.querySelector(`.f-card[data-feed-no="${feedNo}"]`)
        || document.querySelector(`.podium-card[data-feed-no="${feedNo}"]`);
    if (!card) return;

    const likeBtn = card.querySelector(".f-like-icon-btn[data-feed-no]");
    if (likeBtn && typeof window.updateLikeButton === "function") {
        window.updateLikeButton(likeBtn, isLiked, feedLikeCount);
    }
}

function syncListCardComment(feedNo, feedCommentCount) {
    const card = document.querySelector(`.f-card[data-feed-no="${feedNo}"]`)
        || document.querySelector(`.podium-card[data-feed-no="${feedNo}"]`);
    if (!card) return;

    const countSpan = card.querySelector(".comment-count");
    if (countSpan) {
        countSpan.textContent = feedCommentCount;
    }
}

function openLikeList(feedNo) {

    const modal = document.getElementById("feedLikeListModal");
    const body = document.getElementById("likeListBody");

    body.innerHTML = '<p class="like-list-loading">불러오는 중...</p>';
    modal.classList.add("is-active");

    fetch(`/api/v1/feeds/${feedNo}/likes`)
        .then(response => response.json())
        .then(result => {
            if (!result.data || result.data.length === 0) {
                body.innerHTML = '<p class="like-list-empty">아직 좋아요가 없어요.</p>';
                return;
            }
            body.innerHTML = result.data.map(user => `
                <div class="like-user-item">
                    <div class="like-user-avatar">
                        ${user.userImageUrl ? `<img src="${user.userImageUrl}" alt="${user.userNickname}" />` : ''}
                    </div>
                    <span class="like-user-name">${user.userNickname}</span>
                </div>
            `).join('');
        })
        .catch(() => {
            body.innerHTML = '<p class="like-list-empty">목록을 불러오지 못했습니다.</p>';
        });
}

function closeLikeList() {
    const modal = document.getElementById("feedLikeListModal");
    modal.classList.remove("is-active");
}

function updateMoreButton(isAuthor) {
    const moreWrapper = document.querySelector(".detail-more-wrapper");

    if (!moreWrapper) {
        return;
    }

    if (isAuthor) {
        moreWrapper.style.display = "block";
    } else {
        moreWrapper.style.display = "none";
    }
}

function toggleMoreMenu(event) {
    event.stopPropagation();
    const dropdown = document.getElementById("moreDropdown");
    dropdown.classList.toggle("is-active");
}

function deleteComment(feedCommentNo) {
    const feedNo = parseInt(document.getElementById("feedDetailModal")?.dataset.feedNo);
    if (!confirm("댓글을 삭제하시겠습니까?")) return;

    API.callJson(`/api/v1/feeds/${feedNo}/comments/${feedCommentNo}`, { method: "DELETE" })
        .then(async response => {
            const result = await response.json();
            if (response.ok) {
                refreshComments(feedNo);
            } else {
                alert(result.message);
            }
        })
        .catch(() => {
            alert("일시적인 문제가 발생했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}

function refreshComments(feedNo) {
    fetch(`/api/v1/feeds/${feedNo}`)
        .then(response => response.json())
        .then(data => {
            const feed = data.data;

            document.getElementById("detailCommentCount").textContent = feed.feedCommentCount;
            renderComments(feed.feedCommentList);
            syncListCardComment(feedNo, feed.feedCommentCount);
        })
        .catch(() => {
            alert("댓글을 불러오는데 실패했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}

function submitComment() {
    if (!Validator.comment() || !confirm("댓글을 저장하시겠습니까?")) {
        return;
    }

    const feedNo = getCurrentFeedNo();

    API.callJson(`/api/v1/feeds/${feedNo}/comments`, {
        method: "POST",
        body: JSON.stringify({
            feedCommentContent: document.getElementById("feedCommentContent").value.trim(),
            feedCommentParentNo: document.getElementById("feedCommentParentNo").value.trim()
        })
    })
        .then(async response => {
            const body = await response.json();

            if (response.ok) {
                alert(body.message);

                document.getElementById("feedCommentContent").value = "";
                document.getElementById("feedCommentParentNo").value = "";

                cancelReply();
                refreshComments(feedNo);
            } else {
                alert(body.message);
            }
        })
        .catch(() => {
            alert("일시적인 문제가 발생했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}

function replyToComment(commentNo, username) {
    const parentNoInput = document.getElementById("feedCommentParentNo");
    const commentInput = document.getElementById("feedCommentContent");
    const commentForm = document.querySelector(".detail-comment-form");

    parentNoInput.value = commentNo;

    commentForm.classList.add("is-replying");

    showReplyInfo(username);

    commentInput.focus();
    commentInput.placeholder = `${username}님에게 답글 작성 중...`;
}

function showReplyInfo(username) {
    const commentForm = document.querySelector(".detail-comment-form");

    const existingInfo = commentForm.querySelector(".reply-info");
    if (existingInfo) {
        existingInfo.remove();
    }

    const replyInfo = document.createElement("div");
    replyInfo.className = "reply-info";
    replyInfo.innerHTML = `
        <span><strong>${username}</strong>님에게 답글 작성 중</span>
        <button type="button" class="reply-cancel" onclick="cancelReply()">
            ${Icons.xSmall}
        </button>
    `;

    commentForm.insertBefore(replyInfo, commentForm.firstChild);
}

function cancelReply() {
    const parentNoInput = document.getElementById("feedCommentParentNo");
    const commentInput = document.getElementById("feedCommentContent");
    const commentForm = document.querySelector(".detail-comment-form");

    parentNoInput.value = "";
    commentInput.placeholder = "댓글을 입력하세요...";
    commentForm.classList.remove("is-replying");

    const replyInfo = commentForm.querySelector(".reply-info");
    if (replyInfo) {
        replyInfo.remove();
    }
}

function updateFeed() {
    const feedNo = getCurrentFeedNo();
    if (feedNo) {
        openFeedEdit(feedNo);
    }
}

function deleteFeed() {
    const feedNo = getCurrentFeedNo();
    if (!feedNo) return;

    if (!confirm("정말 삭제하시겠습니까?")) {
        return;
    }

    API.callJson(`/api/v1/feeds/${feedNo}`, {
        method: "DELETE"
    })
        .then(async response => {
            const result = await response.json();
            if (response.ok) {
                alert(result.message);

                closeFeedDetail();

                location.reload();
            } else {
                alert(result.message);
            }
        })
        .catch(() => {
            alert("일시적인 문제가 발생했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}

function getCurrentFeedNo() {
    const modal = document.getElementById("feedDetailModal");

    if (modal) {
        return parseInt(modal.dataset.feedNo);
    }

    return null;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diff = Math.floor((now - date) / 1000);

    if (diff < 60) return "방금 전";
    if (diff < 3600) return `${Math.floor(diff / 60)}분 전`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}시간 전`;
    if (diff < 604800) return `${Math.floor(diff / 86400)}일 전`;

    return date.toLocaleDateString("ko-KR", { year: "numeric", month: "long", day: "numeric" });
}

document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
        closeLikeList();
        closeFeedDetail();
    }
});

document.addEventListener("keydown", (e) => {
    const modal = document.getElementById("feedDetailModal");
    if (!modal.classList.contains("is-active")) return;

    if (e.key === "ArrowLeft") prevImage();
    if (e.key === "ArrowRight") nextImage();
});

document.addEventListener("click", () => {
    const dropdown = document.getElementById("moreDropdown");
    if (dropdown) {
        dropdown.classList.remove("is-active");
    }
});