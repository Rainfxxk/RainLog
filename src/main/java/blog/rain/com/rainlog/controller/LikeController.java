package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.service.BlogService;
import blog.rain.com.rainlog.service.LikeService;
import blog.rain.com.rainlog.service.PostService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;

public class LikeController {
    private LikeService likeService = null;
    private BlogService blogService = null;
    private PostService postService = null;

    public String likeBlog(int blogId, int authorId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        blogService.increaseLikeNum(blogId);

        int likeId = likeService.likeBlog(user.getUserId(), blogId, authorId);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("likeResult", true);

        return "json:" + new Gson().toJson(jsonMap);
    }

    public String cancelLikeBlog(int blogId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        blogService.decreaseLikeNum(blogId);

        boolean result = likeService.cancelLikeBlog(user.getUserId(), blogId);
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cancelLikeResult", result);
        return "json:" + new Gson().toJson(jsonMap);
    }

    public String likePost(int postId, int authorId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.increaseLikeNum(postId);

        int likeId = likeService.likeBlog(user.getUserId(), postId, authorId);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("likeResult", true);

        return "json:" + new Gson().toJson(jsonMap);
    }

    public String cancelLikePost(int postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.decreaseLikeNum(postId);

        boolean result = likeService.cancelLikeBlog(user.getUserId(), postId);
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cancelLikeResult", result);
        return "json:" + new Gson().toJson(jsonMap);
    }
}
