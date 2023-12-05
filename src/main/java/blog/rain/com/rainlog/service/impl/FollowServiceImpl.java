package blog.rain.com.rainlog.service.impl;

import blog.rain.com.rainlog.dao.FollowDAO;
import blog.rain.com.rainlog.service.FollowService;

public class FollowServiceImpl implements FollowService {
    private FollowDAO followDAO = null;

    @Override
    public int getFanNum(int userId) {
        return followDAO.selectByToId(userId).size();
    }

    @Override
    public int getFollowNum(int userId) {
        return followDAO.selectByFromId(userId).size();
    }

    @Override
    public void followUser(int fromId, int toId) {
        followDAO.addFollow(fromId, toId);
    }

    @Override
    public void cancelFollowUser(int fromId, int toId) {
        followDAO.deleteFollow(fromId, toId);
    }

    @Override
    public boolean isFollow(int fromId, int toId) {
        return followDAO.selectByFromIdAndToId(fromId, toId).size() > 0;
    }
}
