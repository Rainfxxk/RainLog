package blog.rain.com.rainlog.dao;

import blog.rain.com.rainlog.pojo.Blog;
import blog.rain.com.rainlog.pojo.Bookmark;

import java.util.List;

public interface BookmarkDAO {

    public int addBookmark(Bookmark bookmark);

    int deleteBookmark(int userId, int blogId, String type);

    List<Bookmark> selectBookmark(int userId, int topicId, String topicType);
}
