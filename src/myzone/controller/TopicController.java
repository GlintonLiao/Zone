package myzone.controller;

import myzone.pojo.Topic;
import myzone.pojo.UserBasic;
import myzone.service.TopicService;

import javax.servlet.http.HttpSession;
import java.util.List;

public class TopicController {
    private TopicService topicService;

    public String topicDetail(Integer id, HttpSession session) {
        Topic topic = topicService.getTopicById(id);
        session.setAttribute("topic", topic);
        return "frames/detail";
    }

    public String delTopic(Integer topicId){
        topicService.delTopic(topicId);
        return "redirect:topic.do?operate=getTopicList" ;
    }

    public String getTopicList(HttpSession session){
        //从 session 中获取当前用户信息
        UserBasic userBasic = (UserBasic)session.getAttribute("userBasic");
        //再次查询当前用户关联的所有的日志
        List<Topic> topicList = topicService.getTopicList(userBasic);
        //设置一下关联的日志列表(因为之前 session 中关联的 friend 的 topicList 和此刻数据库中不一致）
        userBasic.setTopicList(topicList);
        //重新覆盖一下 friend 中的信息(为什么不覆盖 userbasic 中？因为 main.html 页面迭代的是 friend 这个 key 中的数据)
        session.setAttribute("friend",userBasic);
        return "frames/main";
    }
}
