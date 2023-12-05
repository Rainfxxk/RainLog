package blog.rain.com.rainlog.service.impl;

import blog.rain.com.rainlog.dao.LikeDAO;
import blog.rain.com.rainlog.pojo.Like;
import blog.rain.com.rainlog.service.LikeService;

public class LikeServiceImpl implements LikeService {
    private LikeDAO likeDAO = null;

    @Override
    public int likeBlog(int userId, int blogId, int authorId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setTopicId(blogId);
        like.setTopicType("blog");
        like.setAuthorId(authorId);

        int likeId = likeDAO.addLike(like);
        return likeId;
    }

    @Override
    public boolean cancelLikeBlog(int userId, int blogId) {
        return likeDAO.deleteLike(userId, blogId, "blog") == 1;
    }

    @Override
    public boolean isLikeBlog(int userId, int blogId) {
        return likeDAO.selectLike(userId, blogId, "blog").size() == 1;
    }

    @Override
    public int likePost(int userId, int postId, int authorId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setTopicId(postId);
        like.setTopicType("post");
        like.setAuthorId(authorId);

        int likeId = likeDAO.addLike(like);
        return likeId;
    }

    @Override
    public boolean cancelLikePost(int userId, int postId) {
        return likeDAO.deleteLike(userId, postId, "post") == 1;
    }

    @Override
    public boolean isLikePost(int userId, int postId) {
        return likeDAO.selectLike(userId, postId, "post").size() == 1;
    }
}
