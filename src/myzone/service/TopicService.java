package myzone.service;

import myzone.pojo.Topic;
import myzone.pojo.UserBasic;

import java.util.List;

public interface TopicService {
    // 查询特定用户的日志列表
    List<Topic> getTopicList(UserBasic userBasic);
    // 根据 id 获取特定 topic
    Topic getTopicById(Integer id);
    // 根据 id 获取指定的 topic 信息，包含这个 topic 关联的作者信息
    public Topic getTopic(Integer id);
    // 删除特定的 topic
    void delTopic(Integer id);
}
