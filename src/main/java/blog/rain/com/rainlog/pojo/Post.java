package blog.rain.com.rainlog.pojo;

import java.time.LocalDateTime;

public class Post {
    private int postId;

    private int userId;

    private String content;

    private String ImagePath;

    private LocalDateTime publishTime;

    private int viewNum;

    private int likeNum;

    private int commentNum;

    private int bookmarkNum;

    public Post() {
    }

    public Post(int postId, int userId, String content, String imagePath, LocalDateTime publishTime, int viewNum, int likeNum, int commentNum, int bookmarkNum) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        ImagePath = imagePath;
        this.publishTime = publishTime;
        this.viewNum = viewNum;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
        this.bookmarkNum = bookmarkNum;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getBookmarkNum() {
        return bookmarkNum;
    }

    public void setBookmarkNum(int bookmarkNum) {
        this.bookmarkNum = bookmarkNum;
    }

    public void addImagePath(String imagePath) {
        String imagePaths = this.getImagePath();

        if (imagePaths == null) {
            this.setImagePath(imagePath);
        }
        else {
            imagePaths = this.getImagePath() + ";" + imagePath;
            this.setImagePath(imagePaths);
            System.out.println(this.getImagePath());
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                ", publishTime=" + publishTime +
                ", viewNum=" + viewNum +
                ", likeNum=" + likeNum +
                ", commentNum=" + commentNum +
                ", bookmarkNum=" + bookmarkNum +
                '}';
    }
}
