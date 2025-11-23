function updateCombinedAddress() {
    let roadAddr = document.getElementById("user_roadAddress").value || "";
    let detailAddress = document.getElementById("user_detailAddress").value || "";

    let combinedAddress = (roadAddr + " " + detailAddress).trim();

    let target = document.getElementById("userAddress");
    if (target) {
        target.value = combinedAddress;
    }
}

function user_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            let roadAddr = data.roadAddress;
            let zonecode = data.zonecode;

            document.getElementById("user_postcode").value = zonecode;
            document.getElementById("user_roadAddress").value = roadAddr;

            let guideTextBox = document.getElementById("guide");

            if (data.autoRoadAddress) {
                let expRoadAddr = data.autoRoadAddress;
                guideTextBox.innerHTML = "(예상 도로명 주소 : " + expRoadAddr + ")";
                guideTextBox.style.display = "block";
            } else {
                guideTextBox.innerHTML = "";
                guideTextBox.style.display = "none";
            }

            updateCombinedAddress();
        }
    }).open();
}

document.addEventListener("DOMContentLoaded", function () {
    let detailInput = document.getElementById("user_detailAddress");

    if (detailInput) {
        detailInput.addEventListener("input", updateCombinedAddress);
    }
});
