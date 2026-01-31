const FeedImageUploader = {
    maxCount: 10,
    maxSize: 5 * 1024 * 1024,
    allowedExts: ["jpg", "jpeg", "png"],
    allowedMimes: ["image/jpeg", "image/png"],

    imageList: [],
    currentIndex: 0,
    dragFromIndex: null,

    init: function() {
        const fileInput = document.getElementById("feedFile");
        const dropZone = document.getElementById("feedDrop");
        const pickBtn = document.getElementById("pickBtn");
        const removeAllBtn = document.getElementById("removeAllBtn");
        const addMoreBtn = document.getElementById("addMoreBtn");
        const resetBtn = document.getElementById("resetBtn");

        if (!fileInput || !dropZone || !pickBtn) {
            console.error("필수 요소를 찾을 수 없습니다.");
            return;
        }

        fileInput.multiple = true;

        pickBtn.addEventListener("click", function() {
            fileInput.click();
        });

        dropZone.addEventListener("click", function(e) {
            const isDropZone = e.target === dropZone;
            const isDropInner = e.target.classList.contains("u-drop-inner");

            if (isDropZone || isDropInner) {
                fileInput.click();
            }
        });

        fileInput.addEventListener("change", function(e) {
            FeedImageUploader.handleFiles(e.target.files);
            fileInput.value = "";
        });

        dropZone.addEventListener("dragover", function(e) {
            e.preventDefault(); // 기본 동작 막기
            dropZone.classList.add("dragover");
        });

        dropZone.addEventListener("dragleave", function() {
            dropZone.classList.remove("dragover");
        });

        dropZone.addEventListener("drop", function(e) {
            e.preventDefault();
            dropZone.classList.remove("dragover");
            FeedImageUploader.handleFiles(e.dataTransfer.files);
        });

        if (addMoreBtn) {
            addMoreBtn.addEventListener("click", function(e) {
                e.stopPropagation();
                fileInput.click();
            });
        }

        if (removeAllBtn) {
            removeAllBtn.addEventListener("click", function() {
                if (FeedImageUploader.imageList.length === 0) {
                    return;
                }

                const confirmed = confirm("모든 이미지를 삭제하시겠습니까?");
                if (confirmed) {
                    FeedImageUploader.removeAllImages();
                }
            });
        }

        if (resetBtn) {
            resetBtn.addEventListener("click", function() {
                FeedImageUploader.removeAllImages();
            });
        }

        this.render();
    },

    handleFiles: function(files) {
        if (!files || files.length === 0) {
            return;
        }

        if (this.imageList.length >= this.maxCount) {
            alert("최대 " + this.maxCount + "개까지만 업로드 가능합니다.");
            return;
        }

        const remainingSlots = this.maxCount - this.imageList.length;

        const filesArr = Array.from(files);

        const filesToAdd = filesArr.slice(0, remainingSlots);

        for (let i = 0; i < filesToAdd.length; i++) {
            const file = filesToAdd[i];

            const isValid = this.validateFile(file);
            if (!isValid) {
                continue;
            }

            this.addImage(file);
        }

        if (filesArr.length > remainingSlots) {
            alert(remainingSlots + "개만 추가되었습니다. (최대 " + this.maxCount + "개)");
        }
    },

    validateFile: function(file) {
        if (!file || !file.name) {
            alert("파일명이 없습니다.");
            return false;
        }

        if (file.size <= 0) {
            alert("파일 크기가 0입니다.");
            return false;
        }

        if (file.size > this.maxSize) {
            alert("파일 크기는 5MB 이하만 가능합니다.");
            return false;
        }

        const fileName = file.name;
        const dotIndex = fileName.lastIndexOf('.');
        const ext = fileName.substring(dotIndex + 1).toLowerCase();

        const isExtAllowed = this.allowedExts.indexOf(ext) !== -1;
        if (!isExtAllowed) {
            alert(ext + " 파일은 업로드할 수 없습니다.\n허용: " + this.allowedExts.join(', '));
            return false;
        }

        const isMimeAllowed = this.allowedMimes.indexOf(file.type) !== -1;
        if (!isMimeAllowed) {
            alert("이미지 형식이 올바르지 않습니다. (JPG/PNG만 가능)");
            return false;
        }

        for (let i = 0; i < this.imageList.length; i++) {
            const img = this.imageList[i];
            const isSameName = img.name === file.name;
            const isSameSize = img.size === file.size;
            const isSameTime = img.lastModified === file.lastModified;

            if (isSameName && isSameSize && isSameTime) {
                alert("같은 파일이 이미 있습니다: " + file.name);
                return false;
            }
        }

        return true;
    },

    addImage: function(file) {
        const self = this;
        const reader = new FileReader();

        reader.onload = function(e) {
            self.imageList.push({
                file: file,
                name: file.name,
                size: file.size,
                lastModified: file.lastModified,
                url: e.target.result // Base64 데이터 URL
            });

            self.currentIndex = self.imageList.length - 1;

            self.render();
        };

        reader.readAsDataURL(file);
    },

    removeImage: function(index) {
        // 인덱스 범위 체크
        if (index < 0 || index >= this.imageList.length) {
            return;
        }

        this.imageList.splice(index, 1);

        if (this.currentIndex >= this.imageList.length) {
            this.currentIndex = Math.max(0, this.imageList.length - 1);
        }

        this.render();
    },

    removeAllImages: function() {
        this.imageList = [];
        this.currentIndex = 0;

        const fileInput = document.getElementById("feedFile");
        if (fileInput) {
            fileInput.value = "";
        }

        this.render();
    },

    updateMainImage: function() {
        const preview = document.getElementById("feedPreview");

        if (!preview) {
            return;
        }

        const img = preview.querySelector("img");

        if (!img || this.imageList.length === 0) {
            return;
        }

        const currentImage = this.imageList[this.currentIndex];
        img.src = currentImage.url;
        img.alt = "피드 이미지 " + (this.currentIndex + 1);
    },

    bindThumbDrag: function(thumbEl, imgObj) {
        const self = this;

        thumbEl.setAttribute("draggable", "true");

        thumbEl.addEventListener("dragstart", function() {
            self.dragFromIndex = self.imageList.indexOf(imgObj);
            thumbEl.style.opacity = "0.5";
        });

        thumbEl.addEventListener("dragend", function() {
            thumbEl.style.opacity = "1";
        });

        thumbEl.addEventListener("dragover", function(e) {
            e.preventDefault();
        });

        thumbEl.addEventListener("drop", function(e) {
            e.preventDefault();

            const toIndex = self.imageList.indexOf(imgObj);
            const fromIndex = self.dragFromIndex;

            if (fromIndex === null || fromIndex === undefined) {
                return;
            }

            if (fromIndex === toIndex) {
                return;
            }

            const moved = self.imageList.splice(fromIndex, 1)[0];
            self.imageList.splice(toIndex, 0, moved);

            if (self.currentIndex === fromIndex) {
                self.currentIndex = toIndex;
            } else if (fromIndex < self.currentIndex && toIndex >= self.currentIndex) {
                self.currentIndex = self.currentIndex - 1;
            } else if (fromIndex > self.currentIndex && toIndex <= self.currentIndex) {
                self.currentIndex = self.currentIndex + 1;
            }

            self.dragFromIndex = null;
            self.render();
        });
    },

    render: function() {
        const dropInner = document.getElementById("dropInner");
        const preview = document.getElementById("feedPreview");
        const thumbList = document.getElementById("thumbList");

        if (this.imageList.length === 0) {
            if (dropInner) {
                dropInner.hidden = false;
            }
            if (preview) {
                preview.hidden = true;
            }
            return;
        }

        if (dropInner) {
            dropInner.hidden = true;
        }
        if (preview) {
            preview.hidden = false;
        }

        this.updateMainImage();

        if (thumbList) {
            thumbList.innerHTML = "";

            for (let i = 0; i < this.imageList.length; i++) {
                const imgObj = this.imageList[i];
                const isActive = i === this.currentIndex;

                const thumb = document.createElement("div");
                thumb.className = isActive ? "u-thumb is-active" : "u-thumb";

                thumb.innerHTML =
                    '<img src="' + imgObj.url + '" alt="썸네일 ' + (i + 1) + '">' +
                    '<span class="u-order">' + (i + 1) + '</span>' +
                    '<button class="u-thumb-delete" type="button" aria-label="이미지 삭제">×</button>';

                const self = this;
                const index = i;

                thumb.addEventListener("click", function() {
                    self.currentIndex = index;
                    self.render();
                });

                const deleteBtn = thumb.querySelector(".u-thumb-delete");
                deleteBtn.addEventListener("click", function(e) {
                    e.stopPropagation();
                    self.removeImage(index);
                });

                this.bindThumbDrag(thumb, imgObj);

                thumbList.appendChild(thumb);
            }
        }
    },

    getFiles: function() {
        const files = [];
        for (let i = 0; i < this.imageList.length; i++) {
            files.push(this.imageList[i].file);
        }
        return files;
    }
};

document.addEventListener('DOMContentLoaded', function() {
    FeedImageUploader.init();
});