package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.pojo.Post;
import blog.rain.com.rainlog.service.*;
import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.util.JsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpSession;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class PostController {
    private PostService postService = null;
    private UserService userService = null;
    private FollowService followService = null;

    private BookmarkService bookmarkService = null;

    private LikeService likeService = null;

    private CommentService commentService = null;

    public String publishPost(String content, HttpSession session, String... images) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();

        postService.publishPost(userId, content, images, session.getServletContext().getRealPath("/"));

        jsonMap.put("postResult", true);
        Gson gson = new Gson();
        String json = gson.toJson(jsonMap);
        return "json:" + json;
    }

    public String getUserPost(int userId, HttpSession session) {
        List<Post> posts = postService.getUserPost(userId);
        User user = (User) session.getAttribute("user");

        for (Post post : posts) {
            User author = userService.getUserInfo(post.getUserId());
            post.setUser(author);
            postService.increaseViewNum(post.getPostId());

            if (user == null) {
                post.setBookmark(false);
                post.setLike(false);
            }
            else {
                post.setBookmark( bookmarkService.isBookmarkPost(user.getUserId(), post.getPostId()));
                post.setLike(likeService.isLikePost(user.getUserId(), post.getPostId()));
            }
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("postInfos", posts);

        return "json:" + new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                .create()
                .toJson(jsonMap);

    }

    public String getBookmarkPost(int userId, HttpSession session) {
        List<Post> posts = postService.getBookmarkPost(userId);
        User user = (User) session.getAttribute("user");

        for (Post post : posts) {
            User author = userService.getUserInfo(post.getUserId());
            post.setUser(author);
            postService.increaseViewNum(post.getPostId());

            if (user == null) {
                post.setBookmark(false);
                post.setLike(false);
            }
            else {
                post.setBookmark( bookmarkService.isBookmarkPost(user.getUserId(), post.getPostId()));
                post.setLike(likeService.isLikePost(user.getUserId(), post.getPostId()));
            }
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("postInfos", posts);

        return "json:" + new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                .create()
                .toJson(jsonMap);

    }

    public String getPostInfo(int pageNum, HttpSession session) {
        List<Post> posts = postService.getPostInPage(pageNum);
        User user = (User) session.getAttribute("user");

        for (Post post : posts) {
            User author = userService.getUserInfo(post.getUserId());
            post.setUser(author);
            postService.increaseViewNum(post.getPostId());

            if (user == null) {
                post.setBookmark(false);
                post.setLike(false);
            }
            else {
                post.setBookmark( bookmarkService.isBookmarkPost(user.getUserId(), post.getPostId()));
                post.setLike(likeService.isLikePost(user.getUserId(), post.getPostId()));
            }
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("postInfos", posts);

        return "json:" + new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                .create()
                .toJson(jsonMap);
    }
}
