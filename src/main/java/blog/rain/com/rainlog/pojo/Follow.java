package blog.rain.com.rainlog.pojo;

public class Follow {
    private int followId;

    private int fromId;

    private int toId;

    public Follow() {
    }

    public Follow(int followId, int fromId, int toId) {
        this.followId = followId;
        this.fromId = fromId;
        this.toId = toId;
    }

    public int getFollowId() {
        return followId;
    }

    public void setFollowId(int followId) {
        this.followId = followId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "followId=" + followId +
                ", fromId=" + fromId +
                ", toId=" + toId +
                '}';
    }
}
