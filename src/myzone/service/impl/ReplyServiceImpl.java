package myzone.service.impl;

import myzone.dao.ReplyDAO;
import myzone.pojo.HostReply;
import myzone.pojo.Reply;
import myzone.pojo.Topic;
import myzone.pojo.UserBasic;
import myzone.service.HostReplyService;
import myzone.service.ReplyService;
import myzone.service.UserBasicService;

import java.util.List;

public class ReplyServiceImpl implements ReplyService {

    private ReplyDAO replyDAO;
    private HostReplyService hostReplyService;
    private UserBasicService userBasicService;


    @Override
    public List<Reply> getReplyListByTopicId(Integer topicId) {
        List<Reply> replyList = replyDAO.getReplyList(new Topic(topicId));
        for (int i = 0; i < replyList.size(); i++) {
            Reply reply = replyList.get(i);
            // 1. 将关联的作者设置进去
            UserBasic author = userBasicService.getUserBasicById(reply.getAuthor().getId());
            reply.setAuthor(author);
            // 2. 将关联的 HostReply 设置进去
            HostReply hostReply = hostReplyService.getHostReplyByReplyId(reply.getId());
            reply.setHostReply(hostReply);
        }
        return replyList;
    }

    @Override
    public void addReply(Reply reply) {
        replyDAO.addReply(reply);
    }

    @Override
    public void delReplyList(Topic topic) {
        List<Reply> replyList = replyDAO.getReplyList(topic);
        if (replyList != null) {
            for (Reply reply : replyList) {
                delReply(reply.getId());
            }
        }
    }

    @Override
    public void delReply(Integer id) {
        // 1. 根据 id 获取到 reply
        Reply reply = replyDAO.getReply(id);
        if (reply != null) {
            // 2. 如果 reply 有关联的 hostReply，则先删除 hostReply
            HostReply hostReply = hostReplyService.getHostReplyByReplyId(reply.getId());
            if (hostReply != null) {
                hostReplyService.delHostReply(hostReply.getId());
            }
            // 3. 删除 reply
            replyDAO.delReply(id);
        }
    }
}
