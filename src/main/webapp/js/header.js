let userId = 56

let content_topic_selector_items = document.getElementsByClassName("content_topic_selector_item")
let last_clicked_topic_selector_item = content_topic_selector_items[0]
let topic_selector_selected_class = "content_topic_selector_item_selected"

let login_method_item = document.getElementsByClassName("login_method_item")
let last_clicked_login_method_item = login_method_item[0]
let last_clicked_login_method_item_class = "login_method_item_clicked"

let login_form_id = document.getElementsByClassName("login_form_id")[0]
let login_form_pwd = document.getElementsByClassName("login_form_pwd")[0]
let login_form_id_box = document.getElementsByClassName("login_form_id_box")[0]
let send_captcha = document.getElementsByClassName("send_captcha")[0]
let login_tip = document.getElementsByClassName("login_tip")[0]
let login_button = document.getElementsByClassName("login_button")[0]

let user_page_link = document.getElementsByClassName("user_page_link")[0]
let go_login_avatar_entry = document.getElementsByClassName("go_login_avatar_entry")[0]
let go_login_text = document.getElementsByClassName("go_login_text")[0]
let login_panel = document.getElementsByClassName("login_task_panel")[0]

let photo_selector = document.getElementsByClassName("post_image_selector_input")[0]
let post_image_grid = document.getElementsByClassName("post_image_grid")[0]
let post_image_selector = document.getElementsByClassName("post_image_selector")[0]

let post_icon = document.getElementsByClassName("post_icon")[0]
let post_task_panel = document.getElementsByClassName("post_task_panel")[0]
let post_text = document.getElementsByClassName("post_text")[0]

let edit_blog_link = document.getElementsByClassName("edit_blog_link")[0]

function login(user) {
  let avatarPath = user.avatarPath

  login_panel.style.display = "none"
  console.log(avatarPath)
  go_login_avatar_entry.style.background = "url(" + avatarPath + ")"
  go_login_avatar_entry.style.backgroundSize = "cover"
  go_login_text.style.display = "none"
  go_login_avatar_entry.onclick = (event) => {}

  post_icon.onclick = openPostPanel
  user_page_link.href = "/user/page?userId=" + user.userId
}

/*
* This part is used to implement the content-topic-selector Button
*/
for (index in content_topic_selector_items) {
  let item = content_topic_selector_items[index]
  
  item.onclick = (e) => {
    if (last_clicked_topic_selector_item != null) {
      last_clicked_topic_selector_item.classList.remove(topic_selector_selected_class)
    }

    item.classList.add(topic_selector_selected_class)
    last_clicked_topic_selector_item = item
  }
}


// implement the login-method button


let method = "pwd"

login_form_id.onclick = (event) => {
  login_tip.style.display = "none"
}

login_form_pwd.onclick = (event) => {
  login_tip.style.display = "none"
}

for (index in login_method_item) {
  let item = login_method_item[index]

  item.onclick = (e) => {
    login_tip.style.display = "none"

    send_captcha.children[0].innerHTML= "发送验证码"
    send_captcha.onclick = sendCaptcha
    clearInterval(sendCaptchaCountTimer)

    if (last_clicked_login_method_item != null) {
      last_clicked_login_method_item.classList.remove(last_clicked_login_method_item_class)
    }
    login_form_id.value = ""
    login_form_pwd.value = ""

    item.classList.add(last_clicked_login_method_item_class)
    last_clicked_login_method_item = item

    method = item.dataset.method

    if (method == "pwd") {
      login_form_id.setAttribute("placeholder", "手机号/邮箱/用户名")
      login_form_pwd.setAttribute("placeholder", "密码")
      login_button.innerHTML = "登录"
      send_captcha.style.display = "none"
    }
    else if (method == "sms") {
      login_form_id.setAttribute("placeholder", "手机号")
      login_form_pwd.setAttribute("placeholder", "验证码")
      login_button.innerHTML = "登录/注册"
      send_captcha.style.display = "flex"
    }
    else {
      login_form_id.setAttribute("placeholder", "邮箱")
      login_form_pwd.setAttribute("placeholder", "验证码")
      login_button.innerHTML = "登录/注册"
      send_captcha.style.display = "flex"
    }
  }
}

login_button.onclick = (event) => {
  console.log("login")
  let id = login_form_id.value
  let pwd = login_form_pwd.value
  let url = "/user/"

  if (id == "") {
    if (method == "pwd") {
      login_tip.innerHTML = "请输入用户名"
    }
    else {
      login_tip.innerHTML = "请输入" + (method == "sms"? "手机号" : "邮箱")
    }
    login_tip.style.display = "flex"
    return
  }

  let form_data = new FormData()
  if (method == "pwd") {
    url = url + "login"
    form_data.append("account", id)
    form_data.append("password", pwd)
  }
  else {
    url = url + "loginByCaptcha"
    form_data.append("type", method)
    form_data.append("address", id)
    form_data.append("captcha", pwd)
  }

  fetch(url, {
    method: "POST",
    body: form_data,
  })
  .then((response) => {
    return response.json()
  })
  .then((data) => {
    console.log(data)
    if (typeof data.loginResult == "undefined") {
      login_tip.innerHTML = "请发送验证码"
      login_tip.style.display = "flex"
    }
    else if (data.loginResult) {
      location.reload(true)
    }
    else {
      if (method == "pwd") {
        login_tip.innerHTML = "用户名或密码错误"
        login_tip.style.display = "flex"
      }
      else {
        login_tip.innerHTML = "验证码错误"
        login_tip.style.display = "flex"
      }
    }
  })
}


var sendCaptchaCount = 60
let sendCaptchaCountTimer

function sendCaptchaCountDown() {
  sendCaptchaCount = 60
  send_captcha.children[0].innerHTML= sendCaptchaCount
  sendCaptchaCount--

  send_captcha.onclick = event => {}

  sendCaptchaCountTimer = setInterval(() => {
    send_captcha.children[0].innerHTML= sendCaptchaCount
    sendCaptchaCount--

    if (sendCaptchaCount == 0) {
      send_captcha.children[0].innerHTML= "发送验证码"
      send_captcha.onclick = sendCaptcha
      clearInterval(sendCaptchaCountTimer)
    }
  }, 1000)
}

function sendCaptcha(event) {
  let id = login_form_id.value

  if (id == "") {
    login_tip.innerHTML = "请输入" + (method == "sms"? "手机号" : "邮箱")
    login_tip.style.display = "flex"
    return
  }

  sendCaptchaCountDown()

  let form_data = new FormData()
  form_data.append("type", method)
  form_data.append("address", id)

  fetch("/user/sendCaptcha", {
    method: "POST",
    body: form_data
  })
}

send_captcha.onclick = sendCaptcha


/*
* this is used to implement the login func
*/


go_login_avatar_entry.onclick = (e) => {
  console.log("clicked!")
  login_panel.style.display = "flex"
}

login_panel.onmousedown = (e) => {
  if (e.target == login_panel) {
    login_panel.style.display = "none"
    login_form_id.value = ""
    login_form_pwd.value = ""
  }
}


/*
* This is used to implement post func
*/

post_icon.onclick = (event) => {
  alert("请先登录")
}

function openPostPanel(e){
  post_task_panel.style.display = "flex"
}

post_task_panel.onmousedown = (e) => {
  if (e.target == post_task_panel) {
    post_task_panel.style.display = "none"
    clearPostPanel()
  }
}

/*
* implement the post textarea auto confit the height
 */

lineHeight = post_text.clientHeight / 2

post_text.onkeyup = (event) => {
  if (lineHeight == 0) {
    lineHeight = post_text.clientHeight / 2
  }

  content = post_text.value
  lineNum = content.split("\n").length
  console.log(content.split("\n")+ lineNum)
  post_text.style.height = lineHeight * (lineNum + 1) + "px"
}

// 选择图片并预览

let photoNum = 0;

function closePostImage(item) {
  photoNum--
  item.remove()
  if (photoNum < 9) {
    post_image_selector.style.display = "flex"
  }
  console.log("remove")
}

function createImagePreviewItem() {
  let i = document.createElement("i")
  i.classList.add("fa")
  i.classList.add("fa-close")
  i.setAttribute("aria-hidden", "true")

  let close_post_image_icon = document.createElement("div")
  close_post_image_icon.classList.add("close_post_image_icon")
  close_post_image_icon.appendChild(i)

  let post_image_grid_item_background = document.createElement("div")
  post_image_grid_item_background.classList.add("post_image_grid_item_background")
  post_image_grid_item_background.appendChild(close_post_image_icon)

  let post_image_grid_item = document.createElement("div")
  post_image_grid_item.classList.add("post_image_grid_item")
  post_image_grid_item.appendChild(post_image_grid_item_background)

  close_post_image_icon.onclick = (event) => {
    closePostImage(post_image_grid_item)
  }

  return post_image_grid_item
}

function getImageUrl(file) {
  return URL.createObjectURL(file)
}

function createImagePreview(path) {
  let item = createImagePreviewItem()
  item.style.backgroundImage = `url(${path})`
  return item
}

function addImages(files) {
  for (index in files) {
    let item = createImagePreview(getImageUrl(files[index]))
    console.log(item instanceof Node)
    post_image_grid.insertAdjacentElement("afterbegin", item)
  }
}

photo_selector.onchange = (event) => {

  console.log(event.target.files.length)

  if (photoNum + event.target.files.length > 9) {
    alert("最多选择 9 张图片!")
    return
  }
  else {
    photoNum += event.target.files.length
    if (photoNum == 9) {
      post_image_selector.style.display = "none"
    }
    addImages(event.target.files)
  }

  photo_selector.select()
}

let post_button = document.getElementsByClassName("post_button")[0]

post_button.onclick = (event) => {
  let content = post_text.value
  let post_images = post_image_grid.children
  let imagesURL = []

  let formData = new FormData()
  formData.append("motion", "test")
  formData.append("content", content)

  for (let i = 0; i < post_images.length - 1; i++) {
    let post_iamge = post_images[i]
    background = post_iamge.style.backgroundImage
    imagesURL.push(background.slice(5, -2))
  }

  let promises = imagesURL.map((imageurl) => {
    return fetch(imageurl)
    .then(response => response.blob())
    .then((blob) => {
      return new Promise((resolve, reject) => {
        const fileReader = new FileReader();
        fileReader.onload = () => {
          console.log("fileReader successfully read")
          resolve(fileReader.result)
        }
        fileReader.readAsDataURL(blob)
      });
    })
  })

  console.log(promises)

  Promise.all(promises)
  .then((results) => {
    console.log("formData append images")
    results.forEach(result => {
      console.log(result)
      formData.append("images", result)
    })
  })
  .then(() => {
    console.log(formData.get("images"))
    fetch("/post/publishPost", {
      method: "POST",
      body: formData,
    })
    .then(response => response.json())
    .then(data => {
      if (data.postResult) {
        clearPostPanel()
      }
    })
  })
}

function clearPostPanel() {
  post_text.value = ""
  post_text.onkeyup(new window.Event("keyup"))
  while (post_image_grid.children.length > 1){
    post_image_grid.removeChild(post_image_grid.children[0])
  }

  post_image_selector.style.display = "flex"
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
      login(data.user)

    }
  })
}

getLoginState()