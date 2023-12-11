package blog.rain.com.rainlog.dao.impl;

import blog.rain.com.rainlog.dao.FollowDAO;
import blog.rain.com.rainlog.frame.basedao.BaseDAO;
import blog.rain.com.rainlog.pojo.Follow;

import java.util.List;

public class FollowDAOImpl extends BaseDAO<Follow> implements FollowDAO {
    @Override
    public int addFollow(int fromId, int toId) {
        return executeUpdate("insert into follow values(null, ?, ?)", fromId, toId);
    }

    @Override
    public int deleteFollow(int fromId, int toId) {
        return executeUpdate("delete from follow where from_id = ? and to_id = ?", fromId, toId);
    }

    @Override
    public int deleteFollow(int followId) {
        return executeUpdate("delete from follow where follow_id = ?", followId);
    }

    @Override
    public List<Follow> selectByFromId(int fromId) {
        return executeQuery("select * from follow where from_id = ?", fromId);
    }

    @Override
    public List<Follow> selectByToId(int toId) {
        return executeQuery("select * from follow where to_id = ?", toId);
    }

    public List<Follow> selectByFollowId(int followId) {
        return executeQuery("select * from follow where follow_id = ?", followId);
    }

    @Override
    public List<Follow> selectByFromIdAndToId(int fromId, int toId) {
        return executeQuery("select * from follow where from_id = ? and to_id = ?", fromId, toId);
    }
}
