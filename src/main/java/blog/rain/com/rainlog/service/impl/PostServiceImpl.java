package blog.rain.com.rainlog.service.impl;

import blog.rain.com.rainlog.dao.PostDAO;
import blog.rain.com.rainlog.pojo.Post;
import blog.rain.com.rainlog.service.PostService;
import blog.rain.com.rainlog.util.ImageUtil;
import com.google.gson.Gson;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostServiceImpl implements PostService {
    private PostDAO postDAO = null;

    public Post publishPost(int userId, String content, String[] images, String rootPath) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        post.setPublishTime(LocalDateTime.now());
        post.setCommentNum(0);
        post.setLikeNum(0);
        post.setViewNum(0);

        int postId = postDAO.addPost(post);
        post.setPostId(postId);

        if (images == null) {
            return post;
        }

        for (int i = 0; i < images.length; i++) {
            String image = images[i];
            String type = "png";
            String base64 = null;

            type = ImageUtil.getImageType(image);
            base64 = ImageUtil.getBase64(image);
            byte[] decode = Base64.getDecoder().decode(base64);
            String imagePath = "/img/post/" + userId + "/" + postId + "/" + i + "." + type;
            try {
                ImageUtil.saveImage(decode, rootPath + imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            post.addImagePath(imagePath);
            postDAO.updatePostImagePath(post);
        }

        return post;
    }

    @Override
    public Post getPost(int postId) {
        return null;
    }

    @Override
    public void increaseBookmarkNum(int postId) {
        postDAO.increaseBookmarkNum(postId);
    }

    @Override
    public void decreaseBookmarkNum(int postId) {
        postDAO.decreaseBookmarkNum(postId);
    }

    @Override
    public void increaseCommentNum(int postId) {
        postDAO.increaseCommentNum(postId);
    }

    @Override
    public void decreaseCommentNum(int postId) {
        postDAO.decreaseCommentNum(postId);
    }

    @Override
    public void increaseLikeNum(int postId) {
        postDAO.increaseLikeNum(postId);
    }

    @Override
    public void decreaseLikeNum(int postId) {
        postDAO.decreaseLikeNum(postId);
    }

    @Override
    public void increaseViewNum(int postId) {
        postDAO.increaseViewNum(postId);
    }

    @Override
    public List<Post> getPostInPage(int pageNum) {
        return postDAO.selectInPage(pageNum * 10, 10);
    }
}
