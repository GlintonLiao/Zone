package myzone.dao.impl;

import myssm.basedao.BaseDAO;
import myzone.dao.HostReplyDAO;
import myzone.pojo.HostReply;

public class HostReplyDAOImpl extends BaseDAO<HostReply> implements HostReplyDAO {
    @Override
    public HostReply getHostReplyByReplyId(Integer replyId) {
        return load("SELECT * FROM t_host_reply WHERE reply = ?", replyId);
    }

    @Override
    public void delHostReply(Integer id) {
        super.executeUpdate("DELETE FROM t_host_reply WHERE id = ? " , id) ;
    }
}
