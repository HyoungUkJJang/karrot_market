package com.numble.karrot.reply.service;

import com.numble.karrot.reply.domain.Reply;

import java.util.List;

public interface ReplyService {

    Reply registerReply(Reply reply);

    List<Reply> getReplyList(Long productId);

}
