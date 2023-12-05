package blog.rain.com.rainlog.dao.impl;

import blog.rain.com.rainlog.dao.BookmarkDAO;
import blog.rain.com.rainlog.frame.basedao.BaseDAO;
import blog.rain.com.rainlog.pojo.Bookmark;

import java.util.List;

public class BookmarkDAOImpl extends BaseDAO<Bookmark> implements BookmarkDAO {

    @Override
    public int addBookmark(Bookmark bookmark) {
        return executeUpdate("insert bookmark values (null, ?, ?, ?, ?)",
                bookmark.getUserId(),
                bookmark.getTopicId(),
                bookmark.getTopicType(),
                bookmark.getAuthorId());
    }

    @Override
    public int deleteBookmark(int userId, int topicId, String type) {
        return executeUpdate("delete from bookmark where user_id = ? and topic_id = ? and topic_type = ?", userId, topicId, type);
    }

    @Override
    public List<Bookmark> selectBookmark(int userId, int topicId, String topicType) {
        return executeQuery("select * from bookmark where user_id = ? and topic_id = ? and topic_type = ?", userId, topicId, topicType);
    }
}
