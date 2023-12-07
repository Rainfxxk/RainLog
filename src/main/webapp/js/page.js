let user_avatar = document.getElementsByClassName("user_avatar")[0]
let user_name = document.getElementsByClassName("user_name")[0]
let fan = document.getElementsByClassName("fun")[0]
let fan_num = document.getElementsByClassName("fan_num")[0]
let follow = document.getElementsByClassName("follow")[0]
let follow_num = document.getElementsByClassName("follow_num")[0]
let user_signature = document.getElementsByClassName("user_signature")[0]
let follow_button = document.getElementsByClassName("follow_button")[0]
let edit_profile_button = document.getElementsByClassName("edit_profile_button")[0]

let profile_panel = document.getElementsByClassName("profile_panel")[0]
let avatar_selector_input = document.getElementsByClassName("avatar_selector_input")[0]
let avatar_background = document.getElementsByClassName("avatar_background")[0]
let user_name_input = document.getElementsByClassName("user_name_input")[0]
let personality_signature_input = document.getElementsByClassName("personality_signature_input")[0]
let profile_button = document.getElementsByClassName("profile_button")[0]
let cancel_profile_button = document.getElementsByClassName("cancel_profile_button")[0]

let navigations = document.getElementsByClassName("page_navigation_item")
let post_navigation = navigations[0]
let blog_navigation = navigations[1]
let bookmark_navigation = navigations[2]
let follow_navigation = navigations[3]
let content_boxes = document.getElementsByClassName("content_box")

let post_box = document.getElementsByClassName("post_box")[0]
let blog_box = document.getElementsByClassName("blog_box")[0]
let collection_box = document.getElementsByClassName("collection_box")[0]
let follow_box = document.getElementsByClassName("follow_box")[0]



let userInfo = null
let user = null

function getLoginState() {
  let formData = new FormData()

  fetch("/user/getLoginState?userId=33", {
    method: "POST",
    body: formData,
  }).then((response) => {
    return response.json()
  }).then((data) => {
    console.log(data)
    if (data.loginState) {
      user = data.user
    }
  })
}

function getUserInfo() {
    let formData = new FormData()

    let href = window.location.href
    let rex = /[?&]([^&]*)=([^&]*)/g
    const parameters = href.matchAll(rex)
    for (const parameter of parameters) {
        if (parameter[1] == "userId") {
            formData.append("userId", parseInt(parameter[2]))
        }
    }

    fetch("/user/getUserInfo", {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        userInfo = json
        user_avatar.style.backgroundImage = `url(${json.avatarPath})`
        user_name.innerHTML = json.userName
        fan_num.innerHTML = json.fanNum
        follow_num.innerHTML = json.followNum
        user_signature.innerHTML = json.personalitySignature
        avatar_background.style.backgroundImage = `url(${json.avatarPath})`
        user_name_input.value = json.userName
        personality_signature_input.value = json.personalitySignature

        if (json.isSelf) {
            edit_profile_button.style.display = "flex"
            follow_button.style.display = "none"
        } else {
            edit_profile_button.style.display = "none"
            follow_button.style.display = "flex"

            if (json.isFollow) {
                follow_button.innerHTML = "取消关注"
            }
            else {
                follow_button.innerHTML = "关注"
            }
        }

      post_navigation.onclick(new window.Event("click"))
    })
}

/*
* implyment the follow button
*/

follow_button.onclick = (event) => {
    let url
    let innerHTML
    console.log(follow_button.innerHTML)
    if (follow_button.innerHTML == "关注") {
        url = "/follow/followUser"
        innerHTML = "取消关注"
    }
    else {
        url = "/follow/cancelFollowUser"
        innerHTML = "关注"
    }

    let formData = new FormData()

    formData.append("toId", userInfo.userId)

    fetch(url, {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then((json) => {
        if (json.followResult == "noLogin") {
            alert("请先登录")
        }
        else if (json.followResult) {
            follow_button.innerHTML = innerHTML
        }
    })
}

/*
* imply click the nevigator item change the page content
*/

let last_clicked_navigation = navigations[0]
let last_show_box = content_boxes[0]

function switchNacigation(item){
    last_clicked_navigation.classList.remove("page_navigation_selected")
    item.classList.add("page_navigation_selected")
    last_clicked_navigation = item
    
    last_show_box.classList.remove("selected_content_box")
    let show_box_class = item.dataset.show
    let show_box = document.getElementsByClassName(show_box_class)[0]
    show_box.classList.add("selected_content_box")
    last_show_box = show_box
}

post_navigation.onclick = (event) => {
    let formData = new FormData()
    formData.append("userId", userInfo.userId)

    fetch("/post/getUserPost", {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then(json => {
        let postInfos = json.postInfos

        for (let i = 0; i < postInfos.length; i++) {
            let post = generateContentItem(postInfos[i], "post")
            post_box.insertAdjacentElement("beforeend", post)
        }

        switchNacigation(post_navigation)
    })
}

blog_navigation.onclick = (event) => {
    let formData = new FormData()
    formData.append("userId", userInfo.userId)

    fetch("/blog/getUserBlog", {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then(json => {
        let blogInfos = json.blogInfos

        for (let i = 0; i < blogInfos.length; i++) {
            let blog = generateContentItem(blogInfos[i], "blog")
            blog_box.insertAdjacentElement("beforeend", blog)
        }

        switchNacigation(blog_navigation)
    })
}

bookmark_navigation.onclick = (event) => {
    let formData = new FormData()
    formData.append("userId", userInfo.userId)

    fetch("/blog/getBookmarkBlog", {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then(json => {
        let blogInfos = json.blogInfos

        fetch("/post/getBookmarkPost", {
            method: "POST",
            body: formData
        })
        .then((response) => response.json())
        .then(json => {
            let postInfos = json.postInfos

            let i = 0
            let minlength = blogInfos.length >= postInfos.length? postInfos.length : blogInfos.length
            for (; i < minlength; i++) {
            collection_box.insertAdjacentElement("afterbegin", generateContentItem(blogInfos[i], "blog"))
            collection_box.insertAdjacentElement("afterbegin", generateContentItem(postInfos[i], "post"))
            }

            if (i < blogInfos.length) {
                for (; i < blogInfos.length; i++) {
                    collection_box.insertAdjacentElement("afterbegin", generateContentItem(blogInfos[i], "blog"))
                }
            }
            
            if (i < postInfos.length) {
                for (; i < postInfos.length; i++) {
                    collection_box.insertAdjacentElement("afterbegin", generateContentItem(postInfos[i], "post"))
                }
            }

            switchNacigation(bookmark_navigation)
        })
    })
}

follow_navigation.onclick = (event) => {
    let formData = new FormData()
    formData.append("userId", userInfo.userId)

    fetch("/user/getFollowUser", {
      method: "POST",
      body: formData
    })
    .then(response => response.json())
    .then((json) => {
      let followUsers = json.followUsers

      for (let i = 0; i < followUsers.length; i++) {
        let followItem = generateFollowItem(followUsers[i])
        follow_box.insertAdjacentElement("beforeend", followItem)
      }

      switchNacigation(follow_navigation)
    })
}


/*
* editProfile Panel
*/


edit_profile_button.onclick = (event) => {
    profile_panel.style.display = "flex"
}

profile_panel.onclick = (event) => {
    if (event.target == profile_panel) {
        profile_panel.style.display = "none"
    }
}

/*
* choose avatar
*/


function getImageUrl(file) {
  return URL.createObjectURL(file)
}

avatar_selector_input.onchange = (event) => {
    let file = event.target.files[0]
    let path = getImageUrl(file)
    avatar_background.style.backgroundImage = `url(${path})`
}

profile_button.onclick = (event) => {
    let userName = user_name_input.value
    let personalitySignature = personality_signature_input.value
    let avatarUrl = avatar_background.style.backgroundImage.slice(5, -2)
    console.log(avatarUrl)

    fetch(avatarUrl)
    .then((response) => response.blob())
    .then((blob) => {
        new Promise((resolve, reject) => {
            let fileReader = new FileReader()
            fileReader.onload = () => resolve(fileReader.result)
            fileReader.readAsDataURL(blob)
        })
        .then((avatar) => {
            let formData = new FormData()

            formData.append("userName", userName)
            formData.append("personalitySignature", personalitySignature)
            formData.append("avatar", avatar)

            fetch("/user/changeInfo", {
                method: "POST",
                body: formData
            })
            .then((response) => response.json())
            .then((json) => {
                if (json.changeResult) {
                    location.reload(true)
                }
            })
        })
    })
}

cancel_profile_button.onclick = (event) => {
    avatar_background.style.backgroundImage = `url(${userInfo.avatarPath})`
    user_name_input.value = userInfo.userName
    personality_signature_input.value = userInfo.personalitySignature
}

/*
* 收藏、点赞内容
*/

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

function bookmarkContent(eventSrc, contentInfo, contentType) {
  console.log(contentInfo)
  console.log(contentType)
  if (user == null) {
    alert("请先登录")
    return
  }

  let url
  let formData = new FormData()

  if (contentType == "blog"){
    console.log("blog")
    formData.append("blogId", contentInfo.blogId)
    formData.append("authorId", contentInfo.userId)
    url = "/bookmark/bookmarkBlog"
  }
  else {
    formData.append("postId", contentInfo.postId)
    formData.append("authorId", contentInfo.userId)
    url = "/bookmark/bookmarkPost"
  }

  fetch(url, {
      method: "POST",
      body: formData
  })
  .then((response) => response.json())
  .then((json) => {
      if (json.bookmarkResult) {
          contentInfo.bookmarkNum += 1
          let bookmarkNumStr = getNumStr(contentInfo.bookmarkNum)
          eventSrc.children[1].innerHTML = bookmarkNumStr
          eventSrc.style.color = "#386bf3"
          eventSrc.onclick = (event) => {cancelBookmarkContent(eventSrc, contentInfo, contentType)}
      }
  })

}

function cancelBookmarkContent(eventSrc, contentInfo, contentType) {
  console.log(contentInfo)
  if (user == null) {
    alert("请先登录")
    return
  }

  let url
  let formData = new FormData()

  if (contentType == "blog"){
    console.log("blog")
    formData.append("blogId", contentInfo.blogId)
    url = "/bookmark/cancelBookmarkBlog"
  }
  else {
    formData.append("postId", contentInfo.postId)
    url = "/bookmark/cancelBookmarkPost"
  }

    fetch(url, {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        if (json.cancelBookmarkResult) {
            contentInfo.bookmarkNum -= 1
            let bookmarkNumStr = getNumStr(contentInfo.bookmarkNum)
            eventSrc.children[1].innerHTML = bookmarkNumStr
            eventSrc.style.color = "#9c9c9c"
            eventSrc.onclick = (event) => {bookmarkContent(eventSrc, contentInfo, contentType)} 
        }
    })
}

function likeContent(eventSrc, contentInfo, contentType) {
  console.log(contentInfo)
  if (user == null) {
    alert("请先登录")
    return
  }

  let url
  let formData = new FormData()

  if (contentType == "blog"){
    console.log("blog")
    formData.append("blogId", contentInfo.blogId)
    formData.append("authorId", contentInfo.userId)
    url = "/like/likeBlog"
  }
  else {
    formData.append("postId", contentInfo.postId)
    formData.append("authorId", contentInfo.userId)
    url = "/like/likePost"
  }

    fetch(url, {
        method: "POST",
        body: formData,
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        if (json.likeResult) {
            contentInfo.likeNum += 1
            let likeNumStr = getNumStr(contentInfo.likeNum)
            eventSrc.children[1].innerHTML = likeNumStr
            eventSrc.style.color = "#386bf3"
            eventSrc.onclick = (event) => {cancelLikeContent(eventSrc, contentInfo, contentType)}
        }
    })
}

function cancelLikeContent(eventSrc, contentInfo, contentType) {
  console.log(contentInfo)
  if (user == null) {
    alert("请先登录")
    return
  }

  let url
  let formData = new FormData()

  if (contentType == "blog"){
    console.log("blog")
    formData.append("blogId", contentInfo.blogId)
    formData.append("authorId", contentInfo.userId)
    url = "/like/cancelLikeBlog"
  }
  else {
    formData.append("postId", contentInfo.postId)
    formData.append("authorId", contentInfo.userId)
    url = "/like/cancelLikePost"
  }

    fetch(url, {
        method: "Post",
        body: formData,
    })
    .then((response) => response.json())
    .then((json) => {
        if (json.cancelLikeResult) {
            contentInfo.likeNum -= 1
            let likeNumStr = getNumStr(contentInfo.likeNum)
            eventSrc.children[1].innerHTML = likeNumStr
            eventSrc.style.color = "#9c9c9c"
            eventSrc.onclick = (event) => {likeContent(eventSrc, contentInfo, contentType)}
        }
    })
}

/*
* 动态生成元素
*/

function generateComment(contentInfo, contentType, commentInfo, userInfo) {
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

    if (user != null) {
        if (commentInfo.userId == user.userId) {
            comment_container.appendChild(delete_button)
            delete_button.onclick = (event) => {
                let formData = new FormData()
                formData.append("commentId", commentInfo.commentId)

                if (contentType == "blog") {
                  formData.append("blogId", contentInfo.blogId)
                  url = "/comment/deleteBlogComment"
                }
                else {
                  formData.append("postId", contentInfo.postId)
                  url = "/comment/deletePostComment"
                }

                fetch(url, {
                    method: "POST",
                    body: formData
                })
                .then(response => response.json())
                .then(json => {
                    console.log(json)
                    if (json.deleteCommentResult) {
                        console.log("delete")
                        comment.parentElement.removeChild(comment)
                    }
                })
            }
        }
    }

    comment_container_box.appendChild(comment_container)
    comment_container_box.appendChild(document.createElement("hr"))
    comment.appendChild(comment_container_box)
    return comment
}

function generateCommentPanel(contentInfo, contentType) {
  let comment_panel = document.createElement("div")
  comment_panel.classList.add("comment_panel")
  comment_panel.classList.add("flex_box")
  comment_panel.classList.add("flex_column")

  let h3 = document.createElement("h3")
  h3.innerHTML = "评论"
  
  let comment_input_box = document.createElement("comment_input_box")
  comment_input_box.classList.add("comment_input_box")
  comment_input_box.classList.add("flex_box")

  let comment_input_avatar = document.createElement("div")
  comment_input_avatar.classList.add("comment_input_avatar")

  if (user != null) {
    comment_input_avatar.style.backgroundImage = `url(${user.avatarPath})`
  }

  let comment_input = document.createElement("textarea")
  comment_input.classList.add("comment_input")
  comment_input.rows=3
  comment_input.placeholder="在这里留下你的痕迹"
  let comment_button = document.createElement("div")
  comment_button.innerHTML = "评论"
  comment_button.classList.add("comment_button")
  comment_button.classList.add("flex_box")

  comment_input_box.appendChild(comment_input_avatar)
  comment_input_box.appendChild(comment_input)
  comment_input_box.appendChild(comment_button)

  let hr = document.createElement("hr")
  let comment_box = document.createElement("div")
  comment_box.classList.add("comment_box")
  comment_box.classList.add("flex_box")
  comment_box.classList.add("flex_column")

  comment_panel.appendChild(h3)
  comment_panel.appendChild(comment_input_box)
  comment_panel.appendChild(hr)
  comment_panel.appendChild(comment_box)

  comment_button.onclick = () => {
    if (user == null) {
      alert("请先登录")
      return
    }

    let content = comment_input.value

    if (content == "") {
        alert("评论不允许为空")
        return
    }

    let formData = new FormData()
    let url

    formData.append("commentContent", content)

    if (contentType == "blog") {
      formData.append("blogId", contentInfo.blogId)
      formData.append("authorId", contentInfo.userId)
      url = "comment/commentBlog"
    }
    else {
      formData.append("postId", contentInfo.postId)
      formData.append("authorId", contentInfo.userId)
      url = "comment/commentPost"
    }

    fetch(url, {
        method: "POST",
        body: formData,
    })
    .then((response) => response.json())
    .then((json) => {
        console.log(json)
        if (json.commentResult) {
            let comment = generateComment(contentInfo, contentType, json.commentInfo, json.userInfo)
            comment_box.insertAdjacentElement("afterbegin", comment)
            comment_input.value = ""
        }
    })
  }

  return comment_panel
}

function generateContentItem(contentInfo, type) {
  let content_container = document.createElement("div")
  content_container.classList.add("content_container")
  content_container.classList.add("flex_box")
  content_container.classList.add("flex_row")

  let content_container_box = document.createElement("div")
  content_container_box.classList.add("content_container_box")
  content_container_box.classList.add("flex_box")
  content_container_box.classList.add("flex_row")

  let avatar_link = document.createElement("a")
  avatar_link.href = "/user/page?userId=" + contentInfo.userId
  let avatar = document.createElement("div")
  avatar.classList.add("avatar")
  avatar.style.backgroundImage = `url(${contentInfo.user.avatarPath})`
  avatar_link.appendChild(avatar)

  let content_topic_box = document.createElement("div")
  content_topic_box.classList.add("content_topic_box")
  content_topic_box.classList.add("flex_box")
  content_topic_box.classList.add("flex_column")

  let userNameLink = document.createElement("a")
  userNameLink.href = "/user/page?userId=" + contentInfo.user.userId
  let userName = document.createElement("div")
  userName.innerHTML = contentInfo.user.userName
  userName.classList.add("user_name")
  userNameLink.appendChild(userName)

  let topic_info = document.createElement("span")
  topic_info.classList.add("topic_info")
  topic_info.innerHTML = "发布时间: " + contentInfo.publishTime

  let content
  if (type == "blog") {
    content = generateBlogItem(contentInfo)
  }
  else {
    content = generatePostItem(contentInfo)
  }

  let content_footer = document.createElement("ul")
  content_footer.classList.add("content_footer")
  content_footer.classList.add("flex_box")
  content_footer.classList.add("flex_row")

  let comment = document.createElement("li")
  comment.classList.add("comment")
  
  let comment_icon = document.createElement("i")
  comment_icon.classList.add("comment_icon")
  comment_icon.classList.add("fa")
  comment_icon.classList.add("fa-commenting-o")
  comment_icon.ariaHidden = true

  let comment_num = document.createElement("span")
  comment_num.classList.add("comment_num")
  comment_num.innerHTML = getNumStr(contentInfo.commentNum)

  comment.appendChild(comment_icon)
  comment.appendChild(comment_num)

  let bookmark = document.createElement("li")
  bookmark.classList.add("bookmark")
  
  let bookmark_icon = document.createElement("i")
  bookmark_icon.classList.add("bookmark_icon")
  bookmark_icon.classList.add("fa")
  bookmark_icon.classList.add("fa-star-o")
  bookmark_icon.ariaHidden = true

  let bookmark_num = document.createElement("span")
  bookmark_num.classList.add("bookmark_num")
  bookmark_num.innerHTML = getNumStr(contentInfo.bookmarkNum)

  bookmark.appendChild(bookmark_icon)
  bookmark.appendChild(bookmark_num)

  bookmark.onclick = bookmarkContent

  let like = document.createElement("li")
  like.classList.add("like")
  
  let like_icon = document.createElement("i")
  like_icon.classList.add("like_icon")
  like_icon.classList.add("fa")
  like_icon.classList.add("fa-thumbs-o-up")
  like_icon.ariaHidden = true

  let like_num = document.createElement("span")
  like_num.classList.add("like_num")
  like_num.innerHTML = getNumStr(contentInfo.likeNum)

  like.appendChild(like_icon)
  like.appendChild(like_num)

  if (contentInfo.isBookmark) {
    bookmark.style.color = "#386bf3"
    bookmark.onclick = (event) => {cancelBookmarkContent(bookmark, contentInfo, type)}
  }
  else {
    bookmark.onclick = (event) => {bookmarkContent(bookmark, contentInfo, type)} 
  }

  if (contentInfo.isLike) {
    like.style.color = "#386bf3"
    like.onclick = (event) => {cancelLikeContent(like, contentInfo, type)}
  }
  else {
    like.onclick = (event) => {likeContent(like, contentInfo, type)}
  }

  content_footer.appendChild(comment)
  content_footer.appendChild(bookmark)
  content_footer.appendChild(like)

  let view_num = document.createElement("span")
  view_num.classList.add("view_num")
  view_num.innerHTML = "阅读量: " + getNumStr(contentInfo.viewNum)

  let comment_panel = generateCommentPanel(contentInfo, type)
  let comment_box = comment_panel.children[3]

  content_topic_box.appendChild(userNameLink)
  content_topic_box.appendChild(topic_info)
  content_topic_box.appendChild(content)
  content_topic_box.appendChild(content_footer)
  content_topic_box.appendChild(view_num)
  content_topic_box.appendChild(comment_panel)

  comment_panel.style.display = "none"
  comment.onclick = (event) => {
    if (comment_box.children.length == 0) {
      getContentComment(comment_box, contentInfo, type)
    }

    if (comment_panel.style.display == "none") {
      comment_panel.style.display = "flex"
    }
    else {
      comment_panel.style.display = "none"
    }
  }

  content_container_box.appendChild(avatar_link)
  content_container_box.appendChild(content_topic_box)
  content_container.appendChild(content_container_box)

  return content_container
}

function generatePostItem(postInfo) {
  let post = document.createElement("div")

  let content_text = document.createElement("pre")
  content_text.classList.add("content_text")
  content_text.innerHTML = postInfo.content
  post.appendChild(content_text)

  if (postInfo.imagePath != "") {
    let imagePaths = postInfo.imagePath.split(";")
    post.appendChild(generateImageGrid(imagePaths))
  }

  return post
}

function generateImageGrid(imagePaths) {
  let content_image_grid = document.createElement("div")
  content_image_grid.classList.add("content_image_grid")

  for (let i = 0; i < imagePaths.length; i++) {
    let content_image = document.createElement("div")
    content_image.classList.add("content_image")
    content_image.style.backgroundImage = `url(${imagePaths[i]})`

    let content_image_background = document.createElement("div")
    content_image_background.classList.add("content_image_background")

    content_image.appendChild(content_image_background)
    content_image_grid.appendChild(content_image)
  }

  return content_image_grid
}

function generateBlogItem(blogInfo) {
  let article_link = document.createElement("a")
  article_link.href = "/blog?blogId=" + blogInfo.blogId
  article_link.classList.add("article_link")

  let article = document.createElement("div")
  article.classList.add("article")
  article.style.backgroundImage = `url(${blogInfo.coverPath})`

  let article_background = document.createElement("div")
  article_background.classList.add("article_background")
  article_background.classList.add("flex_box")
  article_background.classList.add("flex_column")

  let article_title = document.createElement("div")
  article_title.innerHTML = blogInfo.title
  article_title.classList.add("article_title")

  article_background.appendChild(article_title)
  article.appendChild(article_background)
  article_link.appendChild(article)

  return article_link
}

function generateFollowItem(followedUserInfo) {
    let follow_container = document.createElement("div")
    follow_container.classList.add("follow_container")
    follow_container.classList.add("content_container")
    follow_container.classList.add("flex_box")

    let followed_avatar = document.createElement("a")
    followed_avatar.classList.add("followed_avatar")
    followed_avatar.classList.add("avatar")
    followed_avatar.href = "/user/page?userId=" + followedUserInfo.userId

    let avatar = document.createElement("div")
    avatar.style.backgroundImage = `url(${followedUserInfo.avatarPath})`
    followed_avatar.appendChild(avatar)

    let followed_username = document.createElement("a")
    followed_username.classList.add("followed_useranme")

    let followed_username_div = document.createElement("div")
    followed_username_div.innerHTML = followedUserInfo.userName
    followed_username.appendChild(followed_username_div)

    let cancel_follow_button = document.createElement("div")
    cancel_follow_button.classList.add("cancel_follow_button")

    let cancelFollow = () => {
      if (userInfo.userId == user.userId) {
        cancel_follow_button.innerHTML = "取消关注"
        cancel_follow_button.onclick = (event) => {
          followButton(user.userId, followedUserInfo.userId, true)
          .then(json => {
            console.log(json)

            if (json.followResult) {
              follow()
            }
          })
        }
      }
      else {
        cancel_follow_button.innerHTML = "已关注"
      }
    }

    let follow = () => {
      cancel_follow_button.innerHTML = "关注"
      cancel_follow_button.onclick = (event) => {
        followButton(user.userId, followedUserInfo.userId, false)
        .then(json => {
          console.log(json)

          if (json.followResult) {
            cancelFollow()
          }
        })
      }
    }

    if (followedUserInfo.isFollowed) {
      cancelFollow()
    }
    else {
      follow()
    }

    follow_container.appendChild(followed_avatar)
    follow_container.appendChild(followed_username)
    follow_container.appendChild(cancel_follow_button)

    return follow_container
}

function getContentComment(comment_box, contentInfo, contentType) {
  let formData = new FormData()
  let url

  if (contentType == "blog") {
    formData.append("blogId", contentInfo.blogId)
    url = "/comment/getBlogComment"
  }
  else {
    formData.append("postId", contentInfo.postId)
    url = "/comment/getBlogComment"
    url = "/comment/getPostComment"
  }

  fetch(url, {
      method:"POST",
      body: formData
  })
  .then((response) => response.json())
  .then((json) => {
      console.log(json)
      let comments = json.comments
      let users = json.users
      for (let i = 0; i < comments.length; i++) {
          let comment = generateComment(contentInfo, contentType, comments[i], users[i])
          comment_box.insertAdjacentElement("afterbegin", comment)
      }
  })
}

async function followButton(fromId, toId, isCancel) {
  let url
  let formData = new FormData()

  if (isCancel) {
    url = "/follow/cancelFollowUser"
  }
  else {
    url = "/follow/followUser"
  }

  formData.append("fromId", fromId)
  formData.append("toId", toId)

  const response = await fetch(url, {
    method: "POST",
    body: formData
  })
  return await response.json()
}


getUserInfo()
getLoginState()