package myzone.dao.impl;

import myssm.basedao.BaseDAO;
import myzone.dao.TopicDAO;
import myzone.pojo.Topic;
import myzone.pojo.UserBasic;

import java.util.List;

public class TopicDAOImpl extends BaseDAO<Topic> implements TopicDAO {
    @Override
    public List<Topic> getTopicList(UserBasic UserBasic) {
        return super.executeQuery("SELECT * FROM t_topic WHERE author = ?", UserBasic.getId());
    }

    @Override
    public void addTopic(Topic topic) {

    }

    @Override
    public void delTopic(Topic topic) {

    }

    @Override
    public Topic getTopic(Integer id) {
        return null;
    }
}
