package myzone.services.Impl;

import myzone.dao.TopicDAO;
import myzone.pojo.Topic;
import myzone.pojo.UserBasic;
import myzone.services.TopicService;

import java.util.List;

public class TopicServiceImpl implements TopicService {

    private TopicDAO topicDAO = null;

    @Override
    public List<Topic> getTopicList(UserBasic userBasic) {
        return topicDAO.getTopicList(userBasic);
    }
}
