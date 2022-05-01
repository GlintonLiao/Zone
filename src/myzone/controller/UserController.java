package myzone.controller;

import myzone.pojo.Topic;
import myzone.pojo.UserBasic;
import myzone.service.TopicService;
import myzone.service.UserBasicService;

import javax.servlet.http.HttpSession;
import java.util.List;

public class UserController {

    private UserBasicService userBasicService;
    private TopicService topicService;

    public String login(String loginId, String pwd, HttpSession session) {
        // 1. 登录验证
        UserBasic userBasic = userBasicService.login(loginId, pwd);
        if (userBasic != null) {
            // 1.1 获取相关的好友信息
            List<UserBasic> friendList = userBasicService.getFriendList(userBasic);
            // 1.2 获取相关的日志列表信息（但是日志只有 id，没有其他信息）
            List<Topic> topicList = topicService.getTopicList(userBasic);

            userBasic.setFriendList(friendList);
            userBasic.setTopicList(topicList);

            session.setAttribute("userBasic", userBasic);
            return "index";
        } else {
            return "login";
        }
    }
}
