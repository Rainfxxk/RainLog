package blog.rain.com.rainlog.dao;

import blog.rain.com.rainlog.pojo.Comment;

import java.util.List;

public interface CommentDAO {
    public int addComment(Comment comment);

    public int deleteComment(int CommentId);

    public List<Comment> selectComment(int topicId, String topicType);
}
