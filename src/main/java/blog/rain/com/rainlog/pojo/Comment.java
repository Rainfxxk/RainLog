package blog.rain.com.rainlog.pojo;


import java.time.LocalDateTime;

public class Comment {
    private int commentId;

    private String commentContent;

    private int userId;

    private int topicId;

    private String topicType;

    private int authorId;

    private LocalDateTime commentTime;

    public Comment() {
    }

    public Comment(int commentId, String commentContent, int userId, int topicId, String topicType, int authorId, LocalDateTime commentTime) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.userId = userId;
        this.topicId = topicId;
        this.topicType = topicType;
        this.authorId = authorId;
        this.commentTime = commentTime;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public LocalDateTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(LocalDateTime commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", commentContent='" + commentContent + '\'' +
                ", userId=" + userId +
                ", topicId=" + topicId +
                ", topicType='" + topicType + '\'' +
                ", authorId=" + authorId +
                ", commentTime=" + commentTime +
                '}';
    }
}
