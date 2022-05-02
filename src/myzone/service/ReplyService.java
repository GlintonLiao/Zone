package myzone.service;

import myzone.pojo.Reply;
import myzone.pojo.Topic;

import java.util.List;

public interface ReplyService {
    // 根据 topic 的 id 获取关联的所有回复
    List<Reply> getReplyListByTopicId(Integer id);
    // 添加回复
    void addReply(Reply reply);
    // 删除指定的日志关联的所有回复
    void delReplyList(Topic topic);
    //删除指定的回复
    void delReply(Integer replyId);
}
