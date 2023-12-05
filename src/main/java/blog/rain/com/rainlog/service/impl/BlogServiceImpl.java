package blog.rain.com.rainlog.service.impl;

import blog.rain.com.rainlog.dao.BlogDAO;
import blog.rain.com.rainlog.pojo.Blog;
import blog.rain.com.rainlog.service.BlogService;
import blog.rain.com.rainlog.util.ImageUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

public class BlogServiceImpl implements BlogService {
    private BlogDAO blogDAO = null;

    @Override
    public List<Blog> getTopTen() {
        return blogDAO.selectTopTen();
    }

    @Override
    public Blog publishBlog(int userId, String title, String content, String cover, String rootPath) {
        Blog blog = new Blog();
        blog.setPublishTime(LocalDateTime.now());
        blog.setUserId(userId);
        blog.setTitle(title);

        int blogId = blogDAO.addBlog(blog);
        blog.setBlogId(blogId);

        String imageType = ImageUtil.getImageType(cover);
        String base64 = ImageUtil.getBase64(cover);
        byte[] imageDecode = Base64.getDecoder().decode(base64);
        String imagePath = "/img/blog/" + userId + "/" + blogId + "/cover." + imageType;
        try {
            ImageUtil.saveImage(imageDecode, rootPath + imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        blog.setCoverPath(imagePath);
        blogDAO.updateCoverPath(blog);

        String contentPath = "/img/blog/" + userId + "/" + blogId + "/content.md";
        blog.setContentPath(contentPath);
        blogDAO.updateContentPath(blog);
        try {
            blog.saveBlog(content, rootPath + contentPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return blog;
    }

    @Override
    public Blog getBlog(int blogId) {
        Blog blog = blogDAO.selectByBlogId(blogId).get(0);
        return blog;
    }

    @Override
    public void increaseBookmarkNum(int blogId) {
        blogDAO.increaseBookmarkNum(blogId);
    }

    @Override
    public void decreaseBookmarkNum(int blogId) {
        blogDAO.decreaseBookmarkNum(blogId);
    }

    @Override
    public void increaseCommentNum(int blogId) {
        blogDAO.increaseCommentNum(blogId);
    }

    @Override
    public void decreaseCommentNum(int blogId) {
        blogDAO.decreaseCommentNum(blogId);
    }

    @Override
    public void increaseLikeNum(int blogId) {
        blogDAO.increaseLikeNum(blogId);
    }

    @Override
    public void decreaseLikeNum(int blogId) {
        blogDAO.decreaseLikeNum(blogId);
    }

    @Override
    public void increaseViewNum(int blogId) {
        blogDAO.increaseViewNum(blogId);
    }
}
