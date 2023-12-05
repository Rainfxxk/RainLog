package blog.rain.com.rainlog.service;

import blog.rain.com.rainlog.pojo.Comment;

import java.util.List;

public interface CommentService {
    Comment commentBlog(String commentContent, int blogId, int authorId, int userId);

    boolean deleteBlogComment(int commentId);

    Comment commentPost(String commentContent, int postId, int authorId, int userId);

    boolean deletePostComment(int commentId);

    List<Comment> getBlogComments(int blogId);

    List<Comment> getPostComments(int blogId);
}
