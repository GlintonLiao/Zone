package myzone.dao.impl;

import myssm.basedao.BaseDAO;
import myzone.dao.ReplyDAO;
import myzone.pojo.Reply;
import myzone.pojo.Topic;

import java.util.List;

public class ReplyDAOImpl extends BaseDAO<Reply> implements ReplyDAO {

    @Override
    public List<Reply> getReplyList(Topic topic) {
        return executeQuery("SELECT * FROM t_reply WHERE topic = ?", topic.getId());
    }

    @Override
    public void addReply(Reply reply) {
        executeQuery("INSERT INTO t_reply value(0, ?, ?, ?)", reply.getContent(), reply.getReplyDate(), reply.getAuthor().getId(), reply.getTopic().getId());
    }

    @Override
    public void delReply(Integer id) {
        executeUpdate("DELETE FROM t_reply WHERE id = ?", id);
    }

    @Override
    public Reply getReply(Integer id) {
        return load("SELECT * FROM t_reply WHERE id = ?", id);
    }
}
