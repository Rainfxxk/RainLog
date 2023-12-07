package blog.rain.com.rainlog.service;

import blog.rain.com.rainlog.pojo.Blog;

import java.util.List;

public interface BlogService {
    public Blog publishBlog(int userId, String title, String content, String cover, String rootPath);

    public Blog getBlog(int blogId);

    void increaseBookmarkNum(int blogId);

    void decreaseBookmarkNum(int blogId);

    void increaseCommentNum(int blogId);

    void decreaseCommentNum(int blogId);

    void increaseLikeNum(int blogId);

    void decreaseLikeNum(int blogId);

    void increaseViewNum(int blogId);

    List<Blog> getTopTen();

    List<Blog> getBlogInPage(int pageNum);

    List<Blog> getUserBlog(int userId);

    List<Blog> getBookmarkBlog(int userId);
}
