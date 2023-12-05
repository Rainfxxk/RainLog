package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.pojo.Blog;
import blog.rain.com.rainlog.pojo.Comment;
import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.service.*;
import blog.rain.com.rainlog.util.JsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class BlogController {
    private BlogService blogService = null;
    private UserService userService = null;

    private FollowService followService = null;

    private BookmarkService bookmarkService = null;

    private LikeService likeService = null;

    private CommentService commentService = null;


    public String edit() {
        return "page:edit";
    }

    public String index(int blogId, HttpSession session) {
        return "page:article";
    }

    public String getTopTen() {
        List<Blog> topTen = blogService.getTopTen();

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("topTen", topTen);

        return "json:" +
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                        .create()
                        .toJson(jsonMap);
    }

    public String getBlogDetail(int blogId, HttpSession session) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        Blog blog = blogService.getBlog(blogId);
        blogService.increaseViewNum(blogId);
        User author = null;
        User user = (User) session.getAttribute("user");

        if (user == null ) {
            author = userService.getUserInfo(blog.getUserId());
            jsonMap.put("isLogin", false);
            jsonMap.put("isSelf", false);
            jsonMap.put("isFollow", false);
            jsonMap.put("isBookmark", false);
            jsonMap.put("isLike", false);
        }
        else {
            jsonMap.put("isLogin", true);
            jsonMap.put("loginAvatarPath", user.getAvatarPath());
            jsonMap.put("loginId", user.getUserId());
            if (blog.getUserId() == user.getUserId()) {
                author = user;
                jsonMap.put("isSelf", true);
            }
            else {
                jsonMap.put("isSelf", false);
                jsonMap.put("isFollow", followService.isFollow(user.getUserId(), blog.getUserId()));
            }

            jsonMap.put("isBookmarkBlog", bookmarkService.isBookmarkBlog(user.getUserId(), blogId));
            jsonMap.put("isLikeBlog", likeService.isLikeBlog(user.getUserId(), blogId));
        }

        jsonMap.put("blogId", blog.getBlogId());
        jsonMap.put("blogTitle", blog.getTitle());
        String blogPath = session.getServletContext().getRealPath("/") + blog.getContentPath();
        try {
            jsonMap.put("blogContent", blog.getBlog(blogPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jsonMap.put("coverPath", blog.getCoverPath());

        jsonMap.put("publishTime", DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00").format(blog.getPublishTime()));
        jsonMap.put("viewNum", blog.getViewNum());
        jsonMap.put("likeNum", blog.getLikeNum());
        jsonMap.put("commentNum", blog.getCommentNum());
        jsonMap.put("bookmarkNum", blog.getBookmarkNum());

        jsonMap.put("authorName", author.getUserName());
        jsonMap.put("authorId", author.getUserId());
        jsonMap.put("authorAvatarPath", author.getAvatarPath());

        return "json:" + new Gson().toJson(jsonMap);
    }

    public String publishBlog(String title, String content, String cover, HttpSession session) {
        User user = (User) session.getAttribute("user");
        HashMap<String, Object> jsonMap = new HashMap<>();
        Gson gson = new Gson();

        if (user == null) {
            jsonMap.put("isLogin", false);
            return "json:" + gson.toJson(jsonMap);
        }
        jsonMap.put("isLogin", true);

        int userId = user.getUserId();

        String rootPath = session.getServletContext().getRealPath("/");
        Blog blog = blogService.publishBlog(userId, title, content, cover, rootPath);

        jsonMap.put("blogResult", true);
        return "json:" + gson.toJson(jsonMap);
    }
}
