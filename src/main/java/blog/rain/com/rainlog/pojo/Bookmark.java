package blog.rain.com.rainlog.pojo;

public class Bookmark {

    private int bookmarkId;

    private int userId;

    private int topicId;

    private String topicType;

    private int authorId;

    public Bookmark() {
    }

    public Bookmark(int bookmarkId, int userId, int topicId, String topicType, int authorId) {
        this.bookmarkId = bookmarkId;
        this.userId = userId;
        this.topicId = topicId;
        this.topicType = topicType;
        this.authorId = authorId;
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
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
                "bookmarkId=" + bookmarkId +
                ", userId=" + userId +
                ", topicId=" + topicId +
                ", topicType='" + topicType + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
