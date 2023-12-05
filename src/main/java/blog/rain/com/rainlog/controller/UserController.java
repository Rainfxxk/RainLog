package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.service.FollowService;
import blog.rain.com.rainlog.service.UserService;
import blog.rain.com.rainlog.util.ImageUtil;
import blog.rain.com.rainlog.util.captcha.factory.CaptchaSenderFactory;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import javassist.tools.rmi.AppletServer;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserController {
    private UserService userService = null;
    private FollowService followService = null;

    public String index() {
        System.out.println("------------------index---------------");
        return "page:index";
    }

    public String page() {
        return "page:page";
    }

    public String getLoginState(HttpSession session) {
        User user = (User) session.getAttribute("user");

        Gson json = new Gson();
        HashMap<String, Object> jsonMap = new HashMap<>();
        if (user == null) {
            jsonMap.put("loginState", false);
        }
        else {
            jsonMap.put("loginState", true);
            jsonMap.put("user", user);
        }

        return "json:" + json.toJson(jsonMap);
    }

    public String login(String account, String password, HttpSession session) {
        User user = userService.login(account, password);

        if (user == null || !password.equals(user.getPassword())) {
            return "json:{" +
                    "\"loginResult\": false" +
                    "}";
        }

        if (password.equals(user.getPassword())) {
            Gson gson = new Gson();
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("loginResult", true);
            jsonMap.put("user", user);
            String json = gson.toJson(jsonMap);

            session.setAttribute("user", user);

            return "json:" + json;
        }

        return "json:{" +
                "\"loginResult\": false" +
                "}";
    }

    public String sendCaptcha(String type, String address, HttpSession session) {
        System.out.println(type + address);

        CaptchaSenderFactory captchaSenderFactory = CaptchaSenderFactory.newInstance(type);

        String captcha = captchaSenderFactory.createCaptchaSender().sendCaptcha(address);
        captcha = "123456";

        session.setAttribute(address , captcha);

        return "json:" +
                "\"sendCaptchaResult\": true" +
                "}";
    }

    public String loginByCaptcha(String type, String address, String captcha, HttpSession session) {
        String sessionCaptcha = (String) session.getAttribute(address);

        //if (sessionCaptcha.isEmpty() || sessionCaptcha.isBlank()) {
        if (sessionCaptcha == null) {
            return "json:{" +
                    "}";
        }

        if (!sessionCaptcha.equals(captcha)) {
            return "json:{" +
                    "\"loginResult\": false" +
                    "}";
        }

        session.removeAttribute("address");
        User user = userService.loginByCaptcha(type, address);

        Gson gson = new Gson();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("loginResult", true);
        jsonMap.put("user", user);
        String json = gson.toJson(jsonMap);


        session.setAttribute("user", user);

        return "json:" + json;
    }

    public String getUserInfo(int userId, HttpSession session) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        User loginUser = (User) session.getAttribute("user");
        User user = userService.getUserInfo(userId);
        if (loginUser == null) {
            jsonMap.put("isSelf", false);
            jsonMap.put("isFollow", false);
        }
        else {
            jsonMap.put("isSelf", user.getUserId() == loginUser.getUserId());
            jsonMap.put("isFollow", followService.isFollow(loginUser.getUserId(), user.getUserId()));
        }

        jsonMap.put("userId", user.getUserId());
        jsonMap.put("userName", user.getUserName());
        jsonMap.put("personalitySignature", user.getPersonalitySignature());
        jsonMap.put("avatarPath", user.getAvatarPath());
        jsonMap.put("fanNum", followService.getFanNum(user.getUserId()));
        jsonMap.put("followNum", followService.getFollowNum(user.getUserId()));

        if ((boolean) jsonMap.get("isSelf")){
            jsonMap.put("email", user.getEmail());
            jsonMap.put("phone", user.getTelephone());
        }

        Gson gson = new Gson();
        return "json:" + gson.toJson(jsonMap);
    }

    public String changeInfo(String userName, String personalitySignature, String avatar, HttpSession session) {
        User user = (User) session.getAttribute("user");
        user.setUserName(userName);
        user.setPersonalitySignature(personalitySignature);

        String imageType = ImageUtil.getImageType(avatar);
        String base64 = ImageUtil.getBase64(avatar);
        byte[] decode = Base64.getDecoder().decode(base64);
        String avatarPath = "/img/avatar/" + user.getUserId() + "." + imageType;
        try {
            ImageUtil.saveImage(decode, session.getServletContext().getRealPath("/") + avatarPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setAvatarPath(avatarPath);

        userService.changeUserInfo(user);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("changeResult", true);

        return "json:" + new Gson().toJson(jsonMap);
    }
}
