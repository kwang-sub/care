package com.example.care.reply.service;

import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.dto.ReplyModifyDTO;
import com.example.care.reply.dto.ReplyRegisterDTO;

import java.util.List;

public interface ReplyService {

    Long registerReply(ReplyRegisterDTO replyRegisterDTO);

    void modifyReply(ReplyModifyDTO replyModifyDTO);

    List<ReplyDTO> getList(Long boardId);

    void removeReply(Long replyId, Long boardId);
}
