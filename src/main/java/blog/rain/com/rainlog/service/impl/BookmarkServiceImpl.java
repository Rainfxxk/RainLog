package blog.rain.com.rainlog.service.impl;

import blog.rain.com.rainlog.dao.BookmarkDAO;
import blog.rain.com.rainlog.pojo.Bookmark;
import blog.rain.com.rainlog.service.BookmarkService;

public class BookmarkServiceImpl implements BookmarkService {
    private BookmarkDAO bookmarkDAO = null;

    @Override
    public int bookmarkBlog(int userId, int blogId, int authorId) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(userId);
        bookmark.setTopicType("blog");
        bookmark.setTopicId(blogId);
        bookmark.setAuthorId(authorId);

        int bookmarkId = bookmarkDAO.addBookmark(bookmark);
        return bookmarkId;
    }

    @Override
    public boolean cancelBookmarkBlog(int userId, int blogId) {
        return bookmarkDAO.deleteBookmark(userId, blogId, "blog") == 1;
    }

    @Override
    public boolean isBookmarkBlog(int userId, int blogId) {
        return bookmarkDAO.selectBookmark(userId, blogId, "blog").size() == 1;
    }

    @Override
    public int bookmarkPost(int userId, int postId, int authorId) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(userId);
        bookmark.setTopicType("post");
        bookmark.setTopicId(postId);
        bookmark.setAuthorId(authorId);

        int bookmarkId = bookmarkDAO.addBookmark(bookmark);
        return bookmarkId;
    }

    @Override
    public boolean cancelBookmarkPost(int userId, int postId) {
        return bookmarkDAO.deleteBookmark(userId, postId, "post") == 1;
    }

    @Override
    public boolean isBookmarkPost(int userId, int postId) {
        return bookmarkDAO.selectBookmark(userId, postId, "post").size() == 1;
    }
}
