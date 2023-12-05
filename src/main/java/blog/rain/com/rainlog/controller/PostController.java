package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.service.*;
import blog.rain.com.rainlog.pojo.User;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

import java.awt.print.Book;
import java.util.HashMap;

public class PostController {
    private PostService postService = null;
    private UserService userService = null;
    private FollowService followService = null;

    private BookmarkService bookmarkService = null;

    private LikeService likeService = null;

    private CommentService commentService = null;

    public String publishPost(String content, HttpSession session, String... images) {
        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();

        postService.publishPost(userId, content, images, session.getServletContext().getRealPath("/"));

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("postResult", true);
        Gson gson = new Gson();
        String json = gson.toJson(jsonMap);
        return "json:" + json;
    }
}
