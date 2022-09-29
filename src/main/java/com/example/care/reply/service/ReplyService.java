package com.example.care.reply.service;

import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.dto.ReplyEditDTO;

import java.util.List;

public interface ReplyService {

    Long registerReply(ReplyEditDTO replyEditDTO);

    void modifyReply(ReplyEditDTO replyEditDTO);

    List<ReplyDTO> getList(Long boardId);

    void removeReply(Long replyId);
}
