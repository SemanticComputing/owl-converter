function determineMethod() {
    if (document.getElementById("method-post").checked)
        document.form.method = "post";
    else
        document.form.method = "get";
}