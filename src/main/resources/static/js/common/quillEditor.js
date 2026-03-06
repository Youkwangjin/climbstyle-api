const quill = new Quill('#quill-editor', {
    theme: 'snow',
    modules: {
        toolbar: [
            [{ header: [1, 2, 3, false] }],
            ['bold', 'italic', 'underline', 'strike'],
            [{ color: [] }, { background: [] }],
            [{ list: 'ordered' }, { list: 'bullet' }],
            [{ align: [] }],
            ['link', 'image'],
            ['clean']
        ]
    }
});

document.querySelector('.notice-form').addEventListener('submit', function () {
    document.getElementById('content').value = quill.root.innerHTML;
});

let selectedFiles = [];

document.getElementById('attachFiles').addEventListener('change', function () {
    const newFiles = Array.from(this.files);
    selectedFiles = [...selectedFiles, ...newFiles].slice(0, 5);
    renderFileList();
});

function renderFileList() {
    const list = document.getElementById('fileList');
    list.innerHTML = '';

    selectedFiles.forEach((file, i) => {
        const li = document.createElement('li');
        li.className = 'file-item';
        li.innerHTML = `
            <span class="file-item-name">${file.name}</span>
            <span class="file-item-size">${(file.size / 1024).toFixed(0)} KB</span>
            <button type="button" class="file-item-remove" aria-label="파일 삭제" onclick="removeFile(${i})">
                ${Icons.xSmall}
            </button>
        `;
        list.appendChild(li);
    });
}

function removeFile(index) {
    selectedFiles.splice(index, 1);
    renderFileList();
}