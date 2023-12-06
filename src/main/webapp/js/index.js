let header_bar_box = document.getElementsByClassName("header_bar_box")[0]

let navigation_sidebar_items = document.getElementsByClassName("navigation_sidebar_item")
let last_clicked_navication_sidebar_item = navigation_sidebar_items[0]

let content_sidebar_box = document.getElementsByClassName("content_sidebar_box")[0]
let content_sidebar = document.getElementsByClassName("content_sidebar")[0]

let content_sidebar_hotlist = document.getElementsByClassName("content_sidebar_hotlist")[0]

let content_container_panel = document.getElementsByClassName("content_container_panel")[0]

let user = null

let pageNum = 0;
let isEnd = false

getLoginState()
getTopTen()
getContentInfo()

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
* This part is used to implement the navigation sidebar Button
*/
let navigation_clicked_class = "sidebar_item_clicked"

for (index in navigation_sidebar_items) {
  let item = navigation_sidebar_items[index]

  item.onclick = (e) => {
    
    if (last_clicked_navication_sidebar_item != null) {
      last_clicked_navication_sidebar_item.classList.remove(navigation_clicked_class)
    }

    item.classList.add(navigation_clicked_class)
    last_clicked_navication_sidebar_item = item
  }
}

/*
* This part is used to implement the sticky sidebar
*/
let headerTop = header_bar_box.clientHeight + header_bar_box.getBoundingClientRect().top
let sidebarTop = content_sidebar_box.getBoundingClientRect().top
let sidebarHeight = content_sidebar_box.clientHeight
let viewportHeight = window.innerHeight

let isGetingContent = false

function cumputeScrollThreshold() {
  let result = 0
  let sidebarOffset = sidebarHeight - viewportHeight + sidebarTop

  if (sidebarOffset < 0) {
    sidebarOffset = 0
  }

  result = (sidebarTop - headerTop) + sidebarOffset

  return result
}


function windowScrollEvent(event){
  let scrollY = window.scrollY
  let scrollThreshold = cumputeScrollThreshold()

  if (scrollY >= scrollThreshold) {
    content_sidebar.style.transform = `translateY(-${scrollThreshold}px)`
    content_sidebar.style.position = "fixed"
    console.log(content_sidebar.style.transform)
  }
  else {
    content_sidebar.style.transform = ""
    content_sidebar.style.position = "static"
  }


  let scrollHeight = document.documentElement.scrollHeight
  let scrollTop = document.documentElement.scrollTop
  let clientHeight = document.documentElement.clientHeight

  if (Math.abs(scrollHeight - scrollTop - clientHeight) < 1) {
    if (!isGetingContent) {
      isGetingContent = true
      getContentInfo()
    }
  }
}

function setSideBarScroll() {
  headerTop = header_bar_box.clientHeight + header_bar_box.getBoundingClientRect().top
  sidebarTop = content_sidebar_box.getBoundingClientRect().top
  sidebarHeight = content_sidebar_box.clientHeight
  viewportHeight = window.innerHeight
  content_sidebar_box.style.width = `${content_sidebar.clientWidth}`
  
  window.onscroll = windowScrollEvent
}


/*
* implement the hotlist
*/

function generateHotTopItem(blogId, title) {
  let sidebar_item = document.createElement("a")
  sidebar_item.classList.add("sidebar_item")
  sidebar_item.href = "/blog?blogId="+blogId

  let div = document.createElement("div")
  div.innerHTML = title

  sidebar_item.appendChild(div)

  return sidebar_item
}

function addHotTopItem(blogInfo) {
  sidebar_item = generateHotTopItem(blogInfo.blogId, blogInfo.title)
  content_sidebar_hotlist.insertAdjacentElement("beforeend", sidebar_item)
}

function getTopTen() {
  fetch("/blog/getTopTen", {
    method:"POST"
  })
  .then(response => response.json())
  .then(json => {
    console.log(json)
    let topTen = json.topTen

    for (let i = 0; i < topTen.length; i++) {
      addHotTopItem(topTen[i])
    }

    setSideBarScroll()
  })
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

    let content_image_background = document.createElement("div")
    content_image_background.classList.add("content_image_background")
    content_image_background.style.backgroundImage = `url(${imagePaths[i]})`

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

function getContentInfo() {
  if (isEnd) {
    return
  }

  let blogInfos
  let postInfos

  let formData = new FormData()
  formData.append("pageNum", pageNum)

  return fetch("/blog/getBlogInfo", {
    method: "POST",
    body: formData
  })
  .then(response => response.json())
  .then(json => {
    blogInfos = json.blogInfos
    
    return fetch("/post/getPostInfo", {
      method: "POST",
      body: formData
    })
  })
  .then(response => response.json())
  .then(json => {
    postInfos = json.postInfos

    console.log(blogInfos)
    console.log(postInfos)

    if (postInfos.length == 0 && blogInfos.length == 0) {
      isEnd = true
    }

    let i = 0
    let minlength = blogInfos.length >= postInfos.length? postInfos.length : blogInfos.length
    for (; i < minlength; i++) {
      content_container_panel.insertAdjacentElement("afterbegin", generateContentItem(blogInfos[i], "blog"))
      content_container_panel.insertAdjacentElement("afterbegin", generateContentItem(postInfos[i], "post"))
    }

    if (i < blogInfos.length) {
      for (; i < blogInfos.length; i++) {
        content_container_panel.insertAdjacentElement("afterbegin", generateContentItem(blogInfos[i], "blog"))
      }
    }
    
    if (i < postInfos.length) {
      for (; i < postInfos.length; i++) {
        content_container_panel.insertAdjacentElement("afterbegin", generateContentItem(postInfos[i], "post"))
      }
    }

    pageNum++
    isGetingContent = false
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

function deleteContentComment(comment, commentId) {
}

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
