package com.numble.karrot.reply.repository;

import com.numble.karrot.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByProductId(Long productId);
}
