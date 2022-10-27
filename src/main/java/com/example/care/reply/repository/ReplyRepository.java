package com.example.care.reply.repository;

import com.example.care.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {

}
