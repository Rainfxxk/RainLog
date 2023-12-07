package blog.rain.com.rainlog.dao.impl;

import blog.rain.com.rainlog.dao.UserDAO;
import blog.rain.com.rainlog.frame.basedao.BaseDAO;
import blog.rain.com.rainlog.pojo.User;

import java.util.List;

public class UserDAOImpl extends BaseDAO<User> implements UserDAO {

    @Override
    public int insertUser(User user) {
        return executeUpdate("insert into user values (?, ?, ?, ?, ?, ?, ?)",
                user.getUserId(),
                user.getUserName(),
                user.getAvatarPath() == null? "/img/avatar/default_avatar.png" : user.getAvatarPath(),
                user.getTelephone(),
                user.getEmail(),
                user.getPersonalitySignature(),
                user.getPassword());
    }

    @Override
    public int deleteUser(User user) {
        return executeUpdate("delete user where id = ?", user.getUserId());
    }

    @Override
    public int updateUser(User user) {
        return executeUpdate("update user set user_name = ?, avatar_path = ?, telephone = ?, email = ?, personality_signature = ?, password = ? where user_id = ?",
                user.getUserName(),
                user.getAvatarPath(),
                user.getTelephone(),
                user.getEmail(),
                user.getPersonalitySignature(),
                user.getPassword(),
                user.getUserId());
    }

    @Override
    public List<User> queryUserById(int id) {
        return executeQuery("select * from user where user_id = ?", id);
    }

    @Override
    public List<User> queryUserByTel(String tel) {
        List<User> users = executeQuery("select * from user where telephone = ?", tel);
        return users;
    }

    @Override
    public List<User> queryUserByEmail(String email) {
        return executeQuery("select * from user where email = ?", email);
    }

    @Override
    public List<User> queryUserByName(String name) {
        List<User> users = executeQuery("select * from user where user_name = ?", name);
        return users;
    }

    @Override
    public List<User> getFollowUser(int userId) {
        return executeQuery("select user_id, user_name, avatar_path from user where user_id = (select to_id from follow where from_id = ?)", userId);
    }
}
