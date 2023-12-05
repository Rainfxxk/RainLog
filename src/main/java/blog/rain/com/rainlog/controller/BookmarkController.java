package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.service.BlogService;
import blog.rain.com.rainlog.service.BookmarkService;
import blog.rain.com.rainlog.service.PostService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;

public class BookmarkController {
    private BookmarkService bookmarkService = null;
    private BlogService blogService = null;
    private PostService postService = null;

    public String bookmarkBlog(int blogId, int authorId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        blogService.increaseBookmarkNum(blogId);

        int bookmarkId = bookmarkService.bookmarkBlog(user.getUserId(), blogId, authorId);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("bookmarkResult", true);

        return "json:" + new Gson().toJson(jsonMap);
    }

    public String cancelBookmarkBlog(int blogId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        blogService.decreaseBookmarkNum(blogId);

        boolean result = bookmarkService.cancelBookmarkBlog(user.getUserId(), blogId);
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cancelBookmarkResult", result);
        return "json:" + new Gson().toJson(jsonMap);
    }

    public String bookmarkPost(int postId, int authorId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.increaseBookmarkNum(postId);

        int bookmarkId = bookmarkService.bookmarkPost(user.getUserId(), postId, authorId);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("bookmarkResult", true);

        return "json:" + new Gson().toJson(jsonMap);
    }

    public String cancelBookmarkPost(int postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.decreaseBookmarkNum(postId);

        boolean result = bookmarkService.cancelBookmarkPost(user.getUserId(), postId);
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cancelResult", result);
        return "json:" + new Gson().toJson(jsonMap);
    }
}
