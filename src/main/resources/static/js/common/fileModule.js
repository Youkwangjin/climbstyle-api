let fileModule = (function() {
    let Util = {
        copy: function copy(obj, target) {
            for (let prop in obj) {
                if (typeof obj[prop] === "object" && obj[prop] !== null) {
                    target[prop] = copy(obj[prop], target[prop] || {});
                } else {
                    target[prop] = obj[prop];
                }
            }
            return target;
        }
    };

    let FileModule = function(init) {
        this.fileList = [];
        this.setting = {
            count: 5,
            size: 209715200,
            exts: [],
            callback: {
                add: function() {},
                del: function() {}
            }
        }
        this.elem = (function(){
            let input = document.createElement("input");
            input.setAttribute("type", "file");
            input.setAttribute("multiple","");
            return input;
        })();

        Util.copy(init, this.setting);

        this.elem.addEventListener("change", function() {
            for (let i=0; i < this.elem.files.length; i++) {
                let file = this.elem.files[i];
                if (!this.add({
                    file: file,
                    name: file.name,
                    size: file.size,
                    fileId: null
                }))break;
            }
            this.elem.value = "";
        }.bind(this));
    };

    FileModule.prototype.validation = function(file) {
        if (!file) {
            return false;
        }
        if (!file.name) {
            alert("파일명이 존재하지 않습니다.");
            return false;
        }
        if (!file.size) {
            alert("파일 사이즈가 0 입니다.");
            return false;
        }
        if (this.fileList.length >= this.setting.count) {
            alert("첨부 가능한 파일 갯수를 초과하였습니다. : " + this.setting.count);
            return false;
        }
        let _exist = false;
        for (let i=0; i<this.fileList.length; i++) {
            if (this.fileList[i].name === file.name) {
                _exist = true;
                break;
            }
        }
        if (_exist) {
            alert("같은 이름의 파일 존재합니다. : " + file.name);
            return false;
        }
        let _ext = file.name.substring(file.name.lastIndexOf('.') + 1, file.name.length).toLowerCase();
        if (this.setting.exts.length && this.setting.exts.indexOf(_ext) === -1) {
            alert("허용되지 않는 확장자입니다. : " + _ext);
            return false;
        }
        if (file.size > this.setting.size) {
            alert("허용되지 않는 파일 사이즈입니다. : " + file.size);
            return false;
        }
        return true;
    };

    FileModule.prototype.add = function(file) {
        if (!this.validation(file)) {
            return false;
        }
        this.fileList.push(file);
        this.setting.callback.add(this);
        return true;
    };

    FileModule.prototype.del = function(file) {
        let index = this.fileList.indexOf(file);
        if (index !== -1) {
            this.fileList.splice(index, 1);
        }
    };

    return function(elem, init) {
        let f = new FileModule(elem, init);

        return {
            callback: function(event, callback) {
                f.setting.callback[event] = callback;
            },
            add: function(file) {
                file ? f.add(file):f.elem.click();
            },
            del: function(file) {
                f.del(file);
            },
            list: function() {
                return f.fileList;
            }
        };
    };
})();