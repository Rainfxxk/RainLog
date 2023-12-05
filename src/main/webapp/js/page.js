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

let userInfo

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

let navigations = document.getElementsByClassName("page_navigation_item")
let content_boxes = document.getElementsByClassName("content_box")
let last_clicked_navigation = navigations[0]
let last_show_box = content_boxes[0]

for (index in navigations) {
    let item = navigations[index]
    item.onclick = (event) => {
        last_clicked_navigation.classList.remove("page_navigation_selected")
        item.classList.add("page_navigation_selected")
        last_clicked_navigation = item
        
        last_show_box.classList.remove("selected_content_box")
        let show_box_class = item.dataset.show
        let show_box = document.getElementsByClassName(show_box_class)[0]
        show_box.classList.add("selected_content_box")
        last_show_box = show_box
    }
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

getUserInfo()