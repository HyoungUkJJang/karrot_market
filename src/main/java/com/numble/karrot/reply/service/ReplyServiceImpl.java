package com.numble.karrot.reply.service;

import com.numble.karrot.reply.domain.Reply;
import com.numble.karrot.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    @Transactional
    public Reply registerReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @Override
    public List<Reply> getReplyList(Long productId) {
        return replyRepository.findByProductId(productId);
    }

}
