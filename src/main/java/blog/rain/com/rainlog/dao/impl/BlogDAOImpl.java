package blog.rain.com.rainlog.dao.impl;

import blog.rain.com.rainlog.dao.BlogDAO;
import blog.rain.com.rainlog.frame.basedao.BaseDAO;
import blog.rain.com.rainlog.pojo.Blog;

import java.util.List;

public class BlogDAOImpl extends BaseDAO<Blog> implements BlogDAO {
    @Override
    public int addBlog(Blog blog) {
        return executeUpdate("insert into blog values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                blog.getUserId(),
                blog.getTitle(),
                blog.getContentPath(),
                blog.getCoverPath(),
                blog.getPublishTime(),
                blog.getViewNum(),
                blog.getLikeNum(),
                blog.getCommentNum(),
                blog.getBookmarkNum());
    }

    @Override
    public int updateCoverPath(Blog blog) {
        return executeUpdate("update blog set cover_path = ? where blog_id = ?",
                blog.getCoverPath(),
                blog.getBlogId());
    }

    @Override
    public int updateContentPath(Blog blog) {
        return executeUpdate("update blog set content_path = ? where blog_id = ?",
                blog.getContentPath(),
                blog.getBlogId());
    }

    @Override
    public int increaseLikeNum(int blogId) {
        return executeUpdate("update blog set like_num = like_num + 1 where blog_id = ?", blogId);
    }

    @Override
    public int increaseViewNum(int blogId) {
        return executeUpdate("update blog set view_num = view_num + 1 where blog_id = ?", blogId);
    }

    @Override
    public int increaseCommentNum(int blogId) {
        return executeUpdate("update blog set comment_num = comment_num + 1 where blog_id = ?", blogId);
    }

    @Override
    public int increaseBookmarkNum(int blogId) {
        return executeUpdate("update blog set bookmark_num = bookmark_num + 1 where blog_id = ?", blogId);
    }

    @Override
    public int deleteBlog(int blogId) {
        return executeUpdate("delete from blog where blog_id = ?", blogId);
    }

    @Override
    public List<Blog> selectByBlogId(int blogId) {
        return executeQuery("select * from blog where blog_id = ?", blogId);
    }

    @Override
    public int decreaseLikeNum(int blogId) {
        return executeUpdate("update blog set like_num = like_num - 1 where blog_id = ?", blogId);
    }

    @Override
    public int decreaseViewNum(int blogId) {
        return executeUpdate("update blog set view_num = view_num - 1 where blog_id = ?", blogId);
    }

    @Override
    public int decreaseCommentNum(int blogId) {
        return executeUpdate("update blog set comment_num = comment_num - 1 where blog_id = ?", blogId);
    }

    @Override
    public int decreaseBookmarkNum(int blogId) {
        return executeUpdate("update blog set bookmark_num = bookmark_num - 1 where blog_id = ?", blogId);
    }

    @Override
    public List<Blog> selectByUserId(int userId) {
        return executeQuery("select * from blog where user_id = ? order by publish_time", userId);
    }

    @Override
    public List<Blog> selectTopTen() {
        return executeQuery("select * from blog order by (view_num + like_num + comment_num + bookmark_num) limit 10");
    }

    @Override
    public List<Blog> selectAll() {
        return executeQuery("select * from blog order by publish_time");
    }

    @Override
    public List<Blog> selectInPage(int start, int end) {
        return executeQuery("select * from blog order by publish_time limit ?, ?", start, end);
    }

    @Override
    public List<Blog> selectBookmarkBlog(int userId) {
        return executeQuery("select blog.* from blog inner join bookmark on blog.blog_id = bookmark.topic_id where bookmark.user_id = ? and bookmark.topic_type = 'blog'", userId);
    }
}
