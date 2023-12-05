package blog.rain.com.rainlog.dao;

import blog.rain.com.rainlog.pojo.Blog;

import java.util.List;

public interface BlogDAO {
    public int addBlog(Blog blog);

    int updateCoverPath(Blog blog);

    int updateContentPath(Blog blog);

    public int increaseLikeNum(int blogId);

    public int increaseViewNum(int blogId);

    public int increaseCommentNum(int blogId);

    public int increaseBookmarkNum(int blogId);

    public int deleteBlog(int blogId);

    public List<Blog> selectByBlogId(int blogId);

    int decreaseLikeNum(int blogId);

    int decreaseViewNum(int blogId);

    int decreaseCommentNum(int blogId);

    int decreaseBookmarkNum(int blogId);

    public List<Blog> selectByUserId(int userId);

    public List<Blog> selectTopTen();

    public List<Blog> selectAll();

}
