let selector_input = document.getElementsByClassName("cover_image_selector_input")[0]
let select = document.getElementsByClassName("cover_image_selector")[0]

let open_file_input = document.getElementsByClassName("open_file_input")[0]
let edit_input = document.getElementsByClassName("edit_input")[0]
let markdown = document.getElementsByClassName("markdown")[0]


function parse() {
    let markdown_source = edit_input.value
    markdown.innerHTML = marked.parse(markdown_source)
}

edit_input.oninput = (event) => parse()


/*
/ select the cover image
*/

function getImageUrl(file) {
    return URL.createObjectURL(file)
}

selector_input.onchange = (event) => {
    console.log(selector_input.value)
    let path = getImageUrl(event.target.files[0])
    select.style.backgroundImage = `url(${path})`
}

/*
* 打开本地 markdown 文件
*/

open_file_input.onchange = (event) => {
    let file = event.target.files[0]
    let file_name_without_suffix = file.name.substring(0, file.name.lastIndexOf('.'))
    var reader = new FileReader()
    reader.readAsText(file);
    reader.onload = function () {
        title_input.value = file_name_without_suffix
        edit_input.value = this.result;
        parse()
    }
}

/*
* 保存草稿到本地
*/

let save_file_box = document.getElementsByClassName("save_file_box")[0]
let title_input = document.getElementsByClassName("title_input")[0]

save_file_box.onclick = (event) => {
    let title = title_input.value
    let markdown_sourse = edit_input.value
    const blob = new Blob([markdown_sourse], { type: "text/plain;charset=utf-8" })
    const objectURL = URL.createObjectURL(blob)
    const aTag = document.createElement('a')
    aTag.href = objectURL

    if (title == '') {
        title = "draft"
    }
    aTag.download = `${title}.md`
    aTag.click()
}

/*
* markdown 同步滚动
*/

edit_input.onscroll = (event) => {
    markdown.scrollTo({
        top: edit_input.scrollTop / edit_input.scrollHeight * markdown.scrollHeight,
        behavior: "smooth"
    })
}

/*
* post
*/

let edit_button = document.getElementsByClassName("edit_button")[0]

edit_button.onclick = (event) => {
    let title = title_input.value
    let content = edit_input.value

    if (title == "" || content == "") {
        alert("不允许标题或内容为空！")
        return
    }

    if (selector_input.value == "") {
        alert("请选择封面图片")
        return;
    }

    let formData = new FormData()
    formData.append("title", title)
    formData.append("content", content)

    let imageUrl = select.style.backgroundImage.slice(5, -2)

    fetch(imageUrl)
        .then(response => response.blob())
        .then((blob) => {
            return new Promise((resolve, reject) => {
                const fileReader = new FileReader();
                fileReader.onload = () => {
                    resolve(fileReader.result)
                }
                fileReader.readAsDataURL(blob)
            })
        })
        .then((cover) => {
            console.log(cover)
            formData.append("cover", cover)
        })
        .then(() => {
            fetch("/blog/publishBlog", {
                method: "post",
                body: formData
            })
            .then((response) => response.json())
            .then((json) => {
                if (!json.isLogin) {
                    alert("请先登录")
                    return
                }

                if (json.blogResult) {
                    title_input.value = ""
                    edit_input.value = ""
                    edit_input.oninput()
                    select.style.backgroundimage = ""
                    alert("发布成功")
                }
            })
        })
}