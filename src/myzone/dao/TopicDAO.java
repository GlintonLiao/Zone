package myzone.dao;

import myzone.pojo.Topic;
import myzone.pojo.UserBasic;

import java.util.List;

public interface TopicDAO {
    // 获取指定用户的日志列表
    public List<Topic> getTopicList(UserBasic UserBasic);
    // 添加日志
    public void addTopic(Topic topic);
    // 删除日志
    void delTopic(Topic topic);
    // 获取特定日志信息
    Topic getTopic(Integer id);
}
