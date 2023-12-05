package blog.rain.com.rainlog.dao;

import blog.rain.com.rainlog.pojo.Like;

import java.util.List;

public interface LikeDAO {
    public int addLike(Like like);

    public int deleteLike(int userId, int topicId, String topicType);

    public List<Like> selectLike(int userId, int topicId, String topicType);
}
