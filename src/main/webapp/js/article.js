let coverImage = document.getElementsByClassName("cover_image")[0]
let title = document.getElementsByClassName("title")[0]
let publishTime = document.getElementsByClassName("publish_time")[0]
let viewNum = document.getElementsByClassName("view_num")[0]
let likeNum = document.getElementsByClassName("like_num")[0]
let commentNum = document.getElementsByClassName("comment_num")[0]
let authorLink = document.getElementsByClassName("author_link")[0]
let author_avatar = document.getElementsByClassName("author_avatar")[0]
let author_name = document.getElementsByClassName("author_name")[0]
let follow_button = document.getElementsByClassName("follow_button")[0]
let markdown = document.getElementsByClassName("markdown")[0]

let bookmark_button = document.getElementsByClassName("bookmark_button")[0]
let bookmark_button_num = document.getElementsByClassName("bookmark_button_num")[0]
let like_button = document.getElementsByClassName("like_button")[0]
let like_button_num = document.getElementsByClassName("like_button_num")[0]

let comment_panel = document.getElementsByClassName("comment_panel")[0]
let comment_input_box = document.getElementsByClassName("comment_input_box")[0]
let comment_input_avatar = document.getElementsByClassName("comment_input_avatar")[0]
let comment_input = document.getElementsByClassName("comment_input")[0]
let comment_button = document.getElementsByClassName("comment_button")[0]
let comment_box = document.getElementsByClassName("comment_box")[0]

let blogDetail

let isGetComment = true
window.addEventListener("scroll", (event) => {
    let scrollHeight = document.documentElement.scrollHeight
    let scrollTop = document.documentElement.scrollTop
    let clientHeight = document.documentElement.clientHeight

    if (Math.abs(scrollHeight - scrollTop - clientHeight) < 1 && isGetComment) {
        isGetComment = !isGetComment
        let formData = new FormData()

        formData.append("blogId", blogDetail.blogId)

        fetch("/comment/getBlogComment", {
            method:"POST",
            body: formData
        })
        .then((response) => response.json())
        .then((json) => {
            console.log(json)
            let comments = json.comments
            let users = json.users
            for (let i = 0; i < comments.length; i++) {
                let comment = generateComment(comments[i], users[i])
                comment_box.insertAdjacentElement("afterbegin", comment)
            }
        })
    }
})

function remindLogin() {
    alert("请先登录")
}

function generateComment(commentInfo, userInfo) {
    let comment = document.createElement("div")
    comment.classList.add("comment")
    comment.classList.add("flex_box")

    let commenterLink = document.createElement("a")
    commenterLink.classList.add("commenter_link")
    commenterLink.href = "/user/page?userId=" + userInfo.userId

    let commenter_avatar = document.createElement("div")
    commenter_avatar.classList.add("commenter_avatar")
    commenter_avatar.style.backgroundImage = `url(${userInfo.avatarPath})`
    commenterLink.appendChild(commenter_avatar)
    comment.appendChild(commenterLink)

    let comment_container_box = document.createElement("div")
    comment_container_box.classList.add("comment_container_box")
    comment_container_box.classList.add("flex_box")
    comment_container_box.classList.add("flex_column")

    let comment_container = document.createElement("dib")
    comment_container.classList.add("flex_box")
    comment_container.classList.add("comment_container")

    let comment_content_box = document.createElement("div")
    comment_content_box.classList.add("comment_content_box")

    let commenter_name_link = document.createElement("commenter_name_link")
    commenter_name_link.classList.add("commenter_name_link")
    commenter_name_link.href = "/user/page?userId=" + userInfo.userId

    let commenter_name = document.createElement("div")
    commenter_name.classList.add("commenter_name")
    commenter_name.innerHTML = userInfo.userName
    commenter_name_link.appendChild(commenter_name)

    let comment_content = document.createElement("div")
    comment_content.classList.add("comment_content")
    comment_content.innerHTML = commentInfo.commentContent
    
    let comment_time = document.createElement("div")
    comment_time.classList.add("comment_time")
    comment_time.innerHTML = "评论时间: " + commentInfo.commentTime

    comment_content_box.appendChild(commenter_name_link)
    comment_content_box.appendChild(comment_content)
    comment_content_box.appendChild(comment_time)
    
    let delete_button = document.createElement("div")
    delete_button.classList.add("delete_button")
    delete_button.classList.add("flex_box")
    delete_button.innerHTML = "<i class=\"fa fa-lg fa-trash-o\" aria-hidden=\"true\" /></i>"

    comment_container.appendChild(comment_content_box)

    if (blogDetail.isLogin) {
        if (blogDetail.loginId == blogDetail.authorId || comment.userId == blogDetail.loginId) {
            comment_container.appendChild(delete_button)
            delete_button.onclick = (event) => {
                deleteBlogComment(comment, commentInfo.commentId)
            }
        }
    }

    comment_container_box.appendChild(comment_container)
    comment_container_box.appendChild(document.createElement("hr"))
    comment.appendChild(comment_container_box)
    return comment
}

function getNumStr(num) {
    if (num < 1000) {
        return num
    }
    else if (num > 1000 && num < 10000) {
        return Math.floor(num / 1000) + 'k'
    }
    else {
        return Math.floor(num / 10000) + '.' + Math.floor((num % 10000) / 1000) + 'w'
    }
}

function getBlogDetail() {
    let formData = new FormData()

    let href = window.location.href
    let rex = /[?&]([^&]*)=([^&]*)/g
    const parameters = href.matchAll(rex)
    for (const parameter of parameters) {
        if (parameter[1] == "blogId") {
            formData.append("blogId", parseInt(parameter[2]))
        }
    }

    fetch("/blog/getBlogDetail", {
        method: "POST",
        body: formData,
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        blogDetail = json
        coverImage.style.backgroundImage = `url(${json.coverPath})`
        title.innerHTML = json.blogTitle
        publishTime.innerHTML = json.publishTime
        viewNum.innerHTML = getNumStr(json.viewNum)
        likeNum.innerHTML = getNumStr(json.likeNum)
        commentNum.innerHTML = getNumStr(json.commentNum)
        authorLink.href = "/user/page?userId=" + json.authorId
        authorLink.dataset.userId = json.authorId
        author_avatar.style.backgroundImage = `url(${json.authorAvatarPath})`
        author_name.innerHTML = json.authorName

        if (json.isSelf) {
            follow_button.style.display = "none"
        }
        else {
            if (json.isFollow) {
                follow_button.innerHTML = "取消关注"
            }
            else {
                follow_button.innerHTML = "关注"
            }
        }

        if (json.isBookmarkBlog) {
            bookmark_button.onclick = cancelBookmarkBlog
            bookmark_button.style.color = "#386bf3"
        }
        else {
            bookmark_button.onclick = bookmarkBlog
            bookmark_button.style.color = "#9c9c9c"
        }

        if (json.isLikeBlog) {
            like_button.onclick = cancelLikeBlog
            like_button.style.color = "#386bf3"
        }
        else {
            like_button.onclick = likeBlog
            like_button.style.color = "#9c9c9c"
        }

        comment_button.onclick = commentBlog

        if (!json.isLogin) {
            follow_button.onclick = remindLogin
            bookmark_button.onclick = remindLogin
            like_button.onclick = remindLogin
            comment_button.onclick = remindLogin
        }
        else {
            comment_input_avatar.style.backgroundImage = `url(${blogDetail.loginAvatarPath})`
        }

        console.log("marked.parse: " + marked.parse(json.blogContent))
        markdown.innerHTML = marked.parse(json.blogContent)
        bookmark_button_num.innerHTML = getNumStr(json.bookmarkNum)
        like_button_num.innerHTML = getNumStr(json.likeNum)

    })
}

function bookmarkBlog() {
    let formData = new FormData()

    formData.append("blogId", blogDetail.blogId)
    formData.append("authorId", blogDetail.authorId)

    fetch("/bookmark/bookmarkBlog", {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then((json) => {
        if (json.bookmarkResult) {
            blogDetail.bookmarkNum += 1
            let bookmarkNumStr = getNumStr(blogDetail.bookmarkNum)
            bookmark_button_num.innerHTML = bookmarkNumStr
            bookmark_button.style.color = "#386bf3"
            bookmark_button.onclick = cancelBookmarkBlog
        }
    })
}

function cancelBookmarkBlog() {
    let formData = new FormData()

    formData.append("blogId", blogDetail.blogId)
    formData.append("bookmarkId", blogDetail.bookmarkId)

    fetch("/bookmark/cancelBookmarkBlog", {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        if (json.cancelBookmarkResult) {
            blogDetail.bookmarkNum -= 1
            let bookmarkNumStr = getNumStr(blogDetail.bookmarkNum)
            bookmark_button_num.innerHTML = bookmarkNumStr
            bookmark_button.style.color = "#9c9c9c"
            bookmark_button.onclick = bookmarkBlog
        }
    })
}

function likeBlog() {
    let formData = new FormData()
    formData.append("blogId", blogDetail.blogId)
    formData.append("authorId", blogDetail.authorId)

    fetch("/like/likeBlog", {
        method: "POST",
        body: formData,
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        if (json.likeResult) {
            blogDetail.likeNum += 1
            let likeNumStr = getNumStr(blogDetail.likeNum)
            like_button_num.innerHTML = likeNumStr
            likeNum.innerHTML = likeNumStr
            like_button.style.color = "#386bf3"
            like_button.onclick = cancelLikeBlog
        }
    })
}

function cancelLikeBlog() {
    let formData = new FormData

    formData.append("blogId", blogDetail.blogId)

    fetch("/like/cancelLikeBlog", {
        method: "Post",
        body: formData,
    })
    .then((response) => response.json())
    .then((json) => {
        if (json.cancelLikeResult) {
            blogDetail.likeNum -= 1
            let likeNumStr = getNumStr(blogDetail.likeNum)
            like_button_num.innerHTML = likeNumStr
            likeNum.innerHTML = likeNumStr
            like_button.style.color = "#9c9c9c"
            like_button.onclick = likeBlog
        }
    })
}

function commentBlog() {
    let content = comment_input.value

    if (content == "") {
        alert("评论不允许为空")
        return
    }

    let formData = new FormData()

    formData.append("commentContent", content)
    formData.append("blogId", blogDetail.blogId)
    formData.append("authorId", blogDetail.authorId)

    fetch("comment/commentBlog", {
        method: "POST",
        body: formData,
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        if (json.commentBlogResult) {
            let comment = generateComment(json.commentInfo, json.userInfo)
            comment_box.insertAdjacentElement("afterbegin", comment)
            comment_input.value = ""
        }
    })
}

function deleteBlogComment(comment, commentId) {
    let formData = new FormData()
    formData.append("commentId", commentId)
    formData.append("blogId", blogDetail.blogId)

    fetch("/comment/deleteBlogComment", {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then(json => {
        console.log(json)
        if (json.deleteCommentResult) {
            console.log("delete")
            comment_box.removeChild(comment)
        }
    })
}


getBlogDetail()