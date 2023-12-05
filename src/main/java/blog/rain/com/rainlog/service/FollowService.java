package blog.rain.com.rainlog.service;

public interface FollowService {

    public int getFanNum(int userId);

    public int getFollowNum(int userId);

    public void followUser(int fromId, int toId);

    public void cancelFollowUser(int fromId, int toId);

    public boolean isFollow(int fromId, int toId);
}
