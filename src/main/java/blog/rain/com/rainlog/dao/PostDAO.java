package blog.rain.com.rainlog.dao;

import blog.rain.com.rainlog.pojo.Post;

import java.util.List;

public interface PostDAO {
    public int addPost(Post post);

    public int increaseViewNum(int postId);

    public int decreaseViewNum(int postId);

    public int increaseLikeNum(int postId);

    public int decreaseLikeNum(int postId);

    public int increaseCommentNum(int postId);

    public int decreaseCommentNum(int postId);

    public int increaseBookmarkNum(int postId);

    public int decreaseBookmarkNum(int postId);

    public int deletePost(int postId);

    public List<Post> selectByUserId(int userId);

    public List<Post> selectAll();

    public int updatePostImagePath(Post post);

    public List<Post> selectInPage(int start, int end);

    List<Post> getBookmarkPost(int userId);
}
