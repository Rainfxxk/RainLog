package blog.rain.com.rainlog.dao;

import blog.rain.com.rainlog.pojo.User;

import java.util.List;

public interface UserDAO {
    public int insertUser(User user);

    public int deleteUser(User user);

    public int updateUser(User user);

    public List<User> queryUserById(int id);

    public List<User> queryUserByTel(String tel);

    public List<User> queryUserByEmail(String email);

    public List<User> queryUserByName(String name);
}
