package blog.rain.com.rainlog.service.impl;

import blog.rain.com.rainlog.dao.CommentDAO;
import blog.rain.com.rainlog.pojo.Comment;
import blog.rain.com.rainlog.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

public class CommentServiceImpl implements CommentService {
    private CommentDAO commentDAO = null;

    @Override
    public Comment commentBlog(String commentContent, int blogId, int authorId, int userId) {
        Comment comment = new Comment();
        comment.setCommentContent(commentContent);
        comment.setUserId(userId);
        comment.setTopicId(blogId);
        comment.setTopicType("blog");
        comment.setAuthorId(authorId);
        comment.setCommentTime(LocalDateTime.now());

        int commentId = commentDAO.addComment(comment);
        comment.setCommentId(commentId);

        return comment;
    }

    @Override
    public boolean deleteBlogComment(int commentId) {
        return commentDAO.deleteComment(commentId) == 1;
    }

    @Override
    public Comment commentPost(String commentContent, int postId, int authorId, int userId) {
        Comment comment = new Comment();
        comment.setCommentContent(commentContent);
        comment.setUserId(userId);
        comment.setTopicId(postId);
        comment.setTopicType("post");
        comment.setAuthorId(authorId);
        comment.setCommentTime(LocalDateTime.now());

        int commentId = commentDAO.addComment(comment);
        comment.setCommentId(commentId);

        return comment;

    }

    @Override
    public boolean deletePostComment(int commentId) {
        return commentDAO.deleteComment(commentId) == 1;
    }

    @Override
    public List<Comment> getBlogComments(int blogId) {
        return commentDAO.selectComment(blogId, "blog");
    }

    @Override
    public List<Comment> getPostComments(int blogId) {
        return commentDAO.selectComment(blogId, "post");
    }
}
