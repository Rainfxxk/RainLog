package blog.rain.com.rainlog.dao.impl;

import blog.rain.com.rainlog.dao.LikeDAO;
import blog.rain.com.rainlog.frame.basedao.BaseDAO;
import blog.rain.com.rainlog.pojo.Like;

import java.util.List;

public class LikeDAOImpl extends BaseDAO<Like> implements LikeDAO {
    @Override
    public int addLike(Like like) {
        return executeUpdate("insert `like` values (null, ?, ?, ? ,?)",
                like.getUserId(),
                like.getTopicId(),
                like.getTopicType(),
                like.getAuthorId());
    }

    @Override
    public int deleteLike(int userId, int topicId, String topicType) {
        return executeUpdate("delete from `like` where user_id = ? and topic_id = ? and topic_type = ?",
                userId,
                topicId,
                topicType);
    }

    @Override
    public List<Like> selectLike(int userId, int topicId, String topicType) {
        return executeQuery("select * from `like` where user_id = ? and topic_id = ? and topic_type = ?",
                userId,
                topicId,
                topicType);
    }
}
