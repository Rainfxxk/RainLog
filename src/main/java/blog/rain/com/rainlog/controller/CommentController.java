package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.pojo.Comment;
import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.service.BlogService;
import blog.rain.com.rainlog.service.CommentService;
import blog.rain.com.rainlog.service.PostService;
import blog.rain.com.rainlog.service.UserService;
import blog.rain.com.rainlog.util.JsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentController {
    private CommentService commentService = null;

    private UserService userService = null;

    private BlogService blogService = null;

    private PostService postService = null;

    public String getBlogComment(int blogId) {
        List<Comment> blogComments = commentService.getBlogComments(blogId);
        List<User> users = new ArrayList<>();

        for (Comment comment : blogComments) {
            User user = userService.getUserInfo(comment.getUserId());
            users.add(user);
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("comments", blogComments);
        jsonMap.put("users", users);

        return "json:" +
            new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                .create()
                .toJson(jsonMap);
    }

    public String commentBlog(String commentContent, int blogId, int authorId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        blogService.increaseCommentNum(blogId);
        Comment comment = commentService.commentBlog(commentContent, blogId, authorId, user.getUserId());

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("commentResult", true);
        jsonMap.put("commentInfo", comment);
        jsonMap.put("userInfo", user);

        return "json:" +
            new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                .create()
                .toJson(jsonMap);
    }

    public String deleteBlogComment(int commentId, int blogId) {
        commentService.deleteBlogComment(commentId);
        blogService.decreaseCommentNum(blogId);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("deleteCommentResult", true);

        return "json:" + new Gson().toJson(jsonMap);
    }

    public String getPostComment(int postId) {
        List<Comment> postComments = commentService.getPostComments(postId);
        List<User> users = new ArrayList<>();

        for (Comment comment : postComments) {
            User user = userService.getUserInfo(comment.getUserId());
            users.add(user);
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("comments", postComments);
        jsonMap.put("users", users);

        return "json:" +
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                        .create()
                        .toJson(jsonMap);
    }

    public String commentPost(String commentContent, int postId, int authorId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.increaseCommentNum(postId);
        Comment comment = commentService.commentPost(commentContent, postId, authorId, user.getUserId());

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("commentResult", true);
        jsonMap.put("commentInfo", comment);
        jsonMap.put("userInfo", user);

        return "json:" +
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter())
                        .create()
                        .toJson(jsonMap);
    }

    public String deletePostComment(int commentId, int postId) {
        commentService.deletePostComment(commentId);
        postService.decreaseCommentNum(postId);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("deleteCommentResult", true);

        return "json:" + new Gson().toJson(jsonMap);
    }
}
