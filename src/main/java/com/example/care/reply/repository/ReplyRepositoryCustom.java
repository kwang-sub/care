package com.example.care.reply.repository;

import com.example.care.reply.domain.Reply;

import java.util.List;

public interface ReplyRepositoryCustom {
    List<Reply> findList(Long boardId);

}
