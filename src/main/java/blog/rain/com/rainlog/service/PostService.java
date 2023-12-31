package blog.rain.com.rainlog.service;

import blog.rain.com.rainlog.pojo.Post;

import java.util.List;

public interface PostService {

    public Post publishPost(int userId, String content, String[] images, String rootPath);

    public Post getPost(int postId);

    void increaseBookmarkNum(int postId);

    void decreaseBookmarkNum(int postId);

    void increaseCommentNum(int postId);

    void decreaseCommentNum(int postId);

    void increaseLikeNum(int postId);

    void decreaseLikeNum(int postId);

    void increaseViewNum(int postId);

    List<Post> getPostInPage(int pageNum);

    List<Post> getUserPost(int userId);

    List<Post> getBookmarkPost(int userId);
}
