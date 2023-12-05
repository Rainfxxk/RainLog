package blog.rain.com.rainlog.service.impl;

import blog.rain.com.rainlog.dao.UserDAO;
import blog.rain.com.rainlog.dao.impl.UserDAOImpl;
import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO = null;

    @Override
    public User login(String account, String password) {
        List<User> users = userDAO.queryUserByName(account);

        if (!users.isEmpty()) {
            User user = users.get(0);
            return user;
        }

        users = userDAO.queryUserByTel(account);

        if (!users.isEmpty()) {
            User user = users.get(0);
            return user;
        }

        users = userDAO.queryUserByEmail(account);
        if (!users.isEmpty()) {
            User user = users.get(0);
            return user;
        }

        return null;
    }

    @Override
    public User loginByCaptcha(String type, String address) {
        List<User> users = null;
        User user = new User();

        if (type.equals("sms")) {
            users = userDAO.queryUserByTel(address);
            user.setTelephone(address);
        }
        else {
            users = userDAO.queryUserByEmail(address);
            user.setEmail(address);
        }

        if (!users.isEmpty()) {
            user = users.get(0);
            return user;
        }

        int userId = userDAO.insertUser(user);
        user.setUserId(userId);
        user.setUserName("USER-" + userId);
        user.setAvatarPath("/img/avatar/default_avatar.png");
        user.setPersonalitySignature("编辑你的个性签名");
        userDAO.updateUser(user);

        return user;
    }

    @Override
    public boolean isUserNameExist(String name) {
        List<User> users = userDAO.queryUserByName(name);

        return users.isEmpty();
    }

    public boolean isTelephoneExist(String telephone) {
        List<User> users = userDAO.queryUserByTel(telephone);

        return users.isEmpty();
    }

    public boolean isEmailExist(String email) {
        List<User> users = userDAO.queryUserByEmail(email);

        return users.isEmpty();
    }

    @Override
    public boolean checkLoginIsCorrect(User user) {

        List<User> users = userDAO.queryUserByName(user.getUserName());

        if (!users.isEmpty()) return false;

        if (users.get(0).equals(user.getPassword())) {
            user.setUserId(users.get(0).getUserId());

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean changeUserInfo(User user) {
        int i = userDAO.updateUser(user);

        return i != 1;
    }

    @Override
    public User getUserInfo(int userId) {
        return userDAO.queryUserById(userId).get(0);
    }

    @Override
    public int addNewUser(User user) {

        int userid = userDAO.insertUser(user);

        user.setUserId(userid);

        return userid;
    }
}
