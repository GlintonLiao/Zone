package myzone.dao;

import myzone.pojo.HostReply;

public interface HostReplyDAO {
    //根据replyId查询关联的HostReply实体
    HostReply getHostReplyByReplyId(Integer replyId);
    //删除特定的HostReply
    void delHostReply(Integer id);
}
