const API = {
    callJson(url, options = {}) {
        const token = document.querySelector('meta[name="_csrf"]').content;
        const header = document.querySelector('meta[name="_csrf_header"]').content;

        return fetch(url, {
            ...options,
            headers: {
                [header]: token,
                "Content-Type": "application/json",
                ...options.headers
            },
            credentials: "include"
        });
    },

    callFormData(url, options = {}) {
        const token = document.querySelector('meta[name="_csrf"]').content;
        const header = document.querySelector('meta[name="_csrf_header"]').content;

        return fetch(url, {
            ...options,
            headers: {
                [header]: token,
                ...options.headers
            },
            credentials: "include"
        });
    }
}