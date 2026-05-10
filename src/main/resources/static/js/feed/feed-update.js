function openFeedEdit(feedNo) {
    closeFeedDetail();

    fetch(`/api/v1/feeds/${feedNo}`)
        .then(response => response.json())
        .then(data => {
            const feed = data.data;
            const modal = document.getElementById("feedEditModal");

            modal.dataset.feedNo = feedNo;

            document.getElementById("editUsername").textContent = feed.userNickName;
            document.getElementById("editDate").textContent = formatDate(feed.feedCreated);

            const editAvatar = document.querySelector("#feedEditModal .detail-avatar");
            if (feed.userImageUrl) {
                editAvatar.innerHTML = `<img id="editUserImage" src="${feed.userImageUrl}" alt="프로필" />`;
            } else {
                editAvatar.innerHTML = "";
            }

            document.getElementById("editFeedTitle").value = feed.feedTitle;
            document.getElementById("editFeedContent").value = feed.feedContent;
            document.getElementById("editFeedVisibleYn").value = feed.feedVisibleYn;
            document.getElementById("editFeedLikeVisibleYn").value = feed.feedLikeVisibleYn;

            renderEditImages(feed.feedFilePaths);

            modal.classList.add("is-active");
            document.body.style.overflow = "hidden";
        })
        .catch(() => {
            alert("피드를 불러오는데 실패했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}

function closeFeedEdit() {
    const modal = document.getElementById("feedEditModal");
    modal.classList.remove("is-active");
    document.body.style.overflow = "";

    delete modal.dataset.feedNo;
}

function renderEditImages(imagePaths) {
    const container = document.getElementById("editSliderImages");
    container.innerHTML = `<img src="${imagePaths[0]}" alt="대표 이미지" />`;
}

function submitFeedEdit() {
    const modal = document.getElementById("feedEditModal");
    const feedNo = parseInt(modal.dataset.feedNo);

    if (!feedNo) {
        alert("피드 정보를 찾을 수 없습니다. 지속될 경우 관리자에게 문의하세요.");
        return;
    }

    if (!Validator.feedUpdate() || !confirm("피드를 수정하시겠습니까?")) {
        return;
    }

    API.callJson(`/api/v1/feeds/${feedNo}`, {
        method: "PATCH",
        body: JSON.stringify({
            feedTitle: document.getElementById("editFeedTitle").value.trim(),
            feedContent: document.getElementById("editFeedContent").value.trim(),
            feedVisibleYn: document.getElementById("editFeedVisibleYn").value,
            feedLikeVisibleYn : document.getElementById("editFeedLikeVisibleYn").value
        })
    })
        .then(async response => {
            const result = await response.json();

            if (response.ok) {
                alert(result.message);

                closeFeedEdit();
                openFeedDetail(feedNo);
            } else {
                alert(result.message);
            }
        })
        .catch(() => {
            alert("일시적인 문제가 발생했습니다. 지속될 경우 관리자에게 문의하세요.");
        });
}