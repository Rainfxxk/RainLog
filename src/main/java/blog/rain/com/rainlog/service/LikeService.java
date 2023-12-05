package blog.rain.com.rainlog.service;

public interface LikeService {

    int likeBlog(int userId, int blogId, int authorId);

    boolean cancelLikeBlog(int userId, int blogId);

    boolean isLikeBlog(int userId, int blogId);

    int likePost(int userId, int postId, int authorId);

    boolean cancelLikePost(int userId, int postId);

    boolean isLikePost(int userId, int postId);
}
