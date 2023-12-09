package blog.rain.com.rainlog.pojo;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;

public class Blog{
    private int blogId;

    private int userId;

    private String title;

    private String contentPath;

    private String coverPath;

    private LocalDateTime publishTime;

    private int viewNum;

    private int likeNum;

    private int commentNum;

    private int bookmarkNum;

    private User user;

    private Boolean isBookmark;

    private Boolean isLike;

    public Blog() {
    }

    public Blog(int blogId, int userId, String title, String contentPath, String coverPath, LocalDateTime publishTime, int viewNum, int likeNum, int commentNum, int bookmarkNum) {
        this.blogId = blogId;
        this.userId = userId;
        this.title = title;
        this.contentPath = contentPath;
        this.coverPath = coverPath;
        this.publishTime = publishTime;
        this.viewNum = viewNum;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
        this.bookmarkNum = bookmarkNum;
    }

    public void saveBlog(String content, String contentPath) throws IOException {
        File blogFile = new File(contentPath);

        if (!blogFile.getParentFile().exists()) {
            blogFile.getParentFile().mkdirs();
        }

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        fileWriter = new FileWriter(blogFile);
        bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(content);
        bufferedWriter.flush();
    }

    public String getBlog(String contentPath) throws IOException {
        File blogFile = new File(contentPath);

        FileReader fileReader = new FileReader(blogFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getBookmark() {
        return isBookmark;
    }

    public void setBookmark(Boolean bookmark) {
        isBookmark = bookmark;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", contentPath='" + contentPath + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", publishTime=" + publishTime +
                ", viewNum=" + viewNum +
                ", likeNum=" + likeNum +
                ", commentNum=" + commentNum +
                ", bookmarkNum=" + bookmarkNum +
                '}';
    }
}
