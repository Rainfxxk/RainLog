package blog.rain.com.rainlog.pojo;

public class Like {

    private int likeId;

    private int userId;

    private int topicId;

    private String topicType;

    private int authorId;

    public Like() {
    }

    public Like(int likeId, int userId, int topicId, String topicType, int authorId) {
        this.likeId = likeId;
        this.userId = userId;
        this.topicId = topicId;
        this.topicType = topicType;
        this.authorId = authorId;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
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

    @Override
    public String toString() {
        return "Bookmark{" +
                "bookmarkId=" + likeId +
                ", userId=" + userId +
                ", topicId=" + topicId +
                ", topicType='" + topicType + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
