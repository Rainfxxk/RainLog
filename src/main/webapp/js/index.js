let header_bar_box = document.getElementsByClassName("header_bar_box")[0]

let navigation_sidebar_items = document.getElementsByClassName("navigation_sidebar_item")
let last_clicked_navication_sidebar_item = navigation_sidebar_items[0]

let content_sidebar_box = document.getElementsByClassName("content_sidebar_box")[0]
let content_sidebar = document.getElementsByClassName("content_sidebar")[0]

let content_sidebar_hotlist = document.getElementsByClassName("content_sidebar_hotlist")[0]

getTopTen()

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


