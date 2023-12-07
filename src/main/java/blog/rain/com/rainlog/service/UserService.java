package blog.rain.com.rainlog.service;

import blog.rain.com.rainlog.pojo.User;

import java.util.List;

public interface UserService {

    /*
    * The Function below is used to login
     */

    public User login(String account, String password);

    public User loginByCaptcha(String type, String address);

    public boolean isUserNameExist(String name);

    public boolean isTelephoneExist(String telephone);

    public boolean isEmailExist(String email);

    public boolean checkLoginIsCorrect(User user);

    public boolean changeUserInfo(User user);

    public User getUserInfo(int userId);

    public int addNewUser(User user);

    public List<User> getFollowUser(int userId);
}
