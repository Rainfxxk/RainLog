package blog.rain.com.rainlog.pojo;

public class User {
    private int userId;

    private String userName;

    private String password;

    private String avatarPath;

    private String telephone;

    private String email;

    private String personalitySignature;

    public User() {

    }

    public User(int userId, String userName, String password, String avatarPath, String telephone, String email, String personalSignature) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.avatarPath = avatarPath;
        this.telephone = telephone;
        this.email = email;
        this.personalitySignature = personalSignature;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalitySignature() {
        return personalitySignature;
    }

    public void setPersonalitySignature(String personalitySignature) {
        this.personalitySignature = personalitySignature;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", personalSignature='" + personalitySignature + '\'' +
                '}';
    }
}
