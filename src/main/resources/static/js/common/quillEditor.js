const quill = new Quill("#quill-editor", {
    theme: "snow",
    modules: {
        toolbar: [
            [{ header: [1, 2, 3, false] }],
            ["bold", "italic", "underline", "strike"],
            [{ color: [] }, { background: [] }],
            [{ list: "ordered" }, { list: "bullet" }],
            [{ align: [] }],
            ["link", "image"],
            ["clean"]
        ]
    }
});

quill.getModule("toolbar").addHandler("image", function () {
    const input = document.createElement("input");
    input.setAttribute("type", "file");
    input.setAttribute("accept", "image/*");
    input.click();

    input.addEventListener("change", function () {
        const file = input.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        API.callFormData("/api/v1/notices/images", {
            method: "POST",
            body: formData,
        })
            .then(async response => {
                const body = await response.json();
                if (!response.ok) {
                    alert(body.message);
                    return;
                }
                const range = quill.getSelection();
                quill.insertEmbed(range.index, "image", body.data.imageUrl);
            })
            .catch(() => {
                alert("이미지 업로드에 실패했습니다.");
            });
    })
});

const noticeForm = document.querySelector(".notice-form");
if (noticeForm) {
    noticeForm.addEventListener("submit", function () {
        document.getElementById("content").value = quill.root.innerHTML;
    });
}