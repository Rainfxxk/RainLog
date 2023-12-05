package blog.rain.com.rainlog.dao;

import blog.rain.com.rainlog.pojo.Follow;

import java.util.List;

public interface FollowDAO {
    public int addFollow(int fromId, int toId);

    public int deleteFollow(int fromId, int toId);

    public int deleteFollow(int followId);

    public List<Follow> selectByFromId(int fromId);

    public List<Follow> selectByToId(int toId);

    public List<Follow> selectByFromIdAndToId(int fromId, int toId);
}
