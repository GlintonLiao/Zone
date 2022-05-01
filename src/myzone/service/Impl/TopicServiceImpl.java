package myzone.service.Impl;

import myzone.dao.TopicDAO;
import myzone.pojo.Topic;
import myzone.pojo.UserBasic;
import myzone.service.TopicService;

import java.util.List;

public class TopicServiceImpl implements TopicService {

    private TopicDAO topicDAO = null;

    @Override
    public List<Topic> getTopicList(UserBasic userBasic) {
        return topicDAO.getTopicList(userBasic);
    }
}
