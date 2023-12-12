package blog.rain.com.rainlog.dao.impl;

import blog.rain.com.rainlog.dao.PostDAO;
import blog.rain.com.rainlog.frame.basedao.BaseDAO;
import blog.rain.com.rainlog.pojo.Post;

import java.util.List;

public class PostDAOImpl extends BaseDAO<Post> implements PostDAO {
    @Override
    public int addPost(Post post) {
        return executeUpdate("insert into post values (null, ?, ?, ?, ?, ?, ?, ?, ?)",
                post.getUserId(),
                post.getContent(),
                post.getImagePath(),
                post.getPublishTime(),
                post.getViewNum(),
                post.getLikeNum(),
                post.getCommentNum(),
                post.getBookmarkNum());
    }

    @Override
    public int updatePostImagePath(Post post) {
        return executeUpdate("update post set image_path = ? where post_id = ?",
                post.getImagePath(),
                post.getPostId());
    }

    @Override
    public int increaseViewNum(int postId) {
        return executeUpdate("update post set view_num = view_num + 1 where post_id = ?", postId);
    }

    @Override
    public int decreaseViewNum(int postId) {
        return executeUpdate("update post set view_num = view_num - 1 where post_id = ?", postId);
    }

    @Override
    public int increaseLikeNum(int postId) {
        return executeUpdate("update post set like_num = like_num + 1 where post_id = ?", postId);
    }

    @Override
    public int decreaseLikeNum(int postId) {
        return executeUpdate("update post set like_num = like_num - 1 where post_id = ?", postId);
    }

    @Override
    public int increaseCommentNum(int postId) {
        return executeUpdate("update post set comment_num = comment_num + 1 where post_id = ?", postId);
    }

    @Override
    public int decreaseCommentNum(int postId) {
        return executeUpdate("update post set comment_num = comment_num - 1 where post_id = ?", postId);
    }

    @Override
    public int increaseBookmarkNum(int postId) {
        return executeUpdate("update post set bookmark_num = bookmark_num + 1 where post_id = ?", postId);
    }

    @Override
    public int decreaseBookmarkNum(int postId) {
        return executeUpdate("update post set bookmark_num = bookmark_num - 1 where post_id = ?", postId);
    }

    @Override
    public int deletePost(int postId) {
        return executeUpdate("delete from post where post_id = ?", postId);
    }

    @Override
    public List<Post> selectByUserId(int userId) {
        return executeQuery("select * from post where user_id = ?", userId);
    }

    @Override
    public List<Post> selectAll() {
        return executeQuery("select * from post");
    }

    @Override
    public List<Post> selectInPage(int start, int end) {
        return executeQuery("select * from post order by publish_time limit ?, ?", start, end);
    }

    @Override
    public List<Post> getBookmarkPost(int userId) {
        return executeQuery("select post.* from post inner join bookmark on post.post_id = bookmark.topic_id where bookmark.user_id = ? and bookmark.topic_type = 'post'", userId);
    }
}
