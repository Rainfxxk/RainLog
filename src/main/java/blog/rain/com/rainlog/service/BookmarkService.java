package blog.rain.com.rainlog.service;

public interface BookmarkService {
    int bookmarkBlog(int userId, int blogId, int authorId);

    boolean cancelBookmarkBlog(int userId, int blogId);

    boolean isBookmarkBlog(int userId, int blogId);

    int bookmarkPost(int userId, int postId, int authorId);

    boolean cancelBookmarkPost(int userId, int postId);

    boolean isBookmarkPost(int userId, int postId);
}
