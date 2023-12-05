package blog.rain.com.rainlog.dao.impl;

import blog.rain.com.rainlog.dao.CommentDAO;
import blog.rain.com.rainlog.frame.basedao.BaseDAO;
import blog.rain.com.rainlog.pojo.Comment;

import java.util.List;

public class CommentDAOImpl extends BaseDAO<Comment> implements CommentDAO {
    @Override
    public int addComment(Comment comment) {
        return executeUpdate("insert into comment values (null, ?, ?, ?, ?, ?, ?)",
                comment.getCommentContent(),
                comment.getUserId(),
                comment.getTopicId(),
                comment.getTopicType(),
                comment.getAuthorId(),
                comment.getCommentTime());
    }

    @Override
    public int deleteComment(int commentId) {
        return executeUpdate("delete from comment where comment_id = ?", commentId);
    }

    @Override
    public List<Comment> selectComment(int topicId, String topicType) {
        return executeQuery("select * from comment where topic_id = ? and topic_type = ?",
                topicId,
                topicType);
    }
}
