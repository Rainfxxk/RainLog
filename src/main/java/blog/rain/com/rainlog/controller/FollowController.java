package blog.rain.com.rainlog.controller;

import blog.rain.com.rainlog.pojo.User;
import blog.rain.com.rainlog.service.FollowService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;

public class FollowController {
    private FollowService followService = null;

    public String followUser(int toId, HttpSession session) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            jsonMap.put("followResult", "noLogin");
            return "json:" + new Gson().toJson(jsonMap);
        }

        int fromId = user.getUserId();

        followService.followUser(fromId, toId);

        jsonMap.put("followResult", true);
        return "json:" + new Gson().toJson(jsonMap);
    }

    public String cancelFollowUser(int toId, HttpSession session) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        User user = (User) session.getAttribute("user");
        int fromId = user.getUserId();

        followService.cancelFollowUser(fromId, toId);

        jsonMap.put("followResult", true);
        return "json:" + new Gson().toJson(jsonMap);
    }
}
