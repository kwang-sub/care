package com.example.care.reply.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyEditDTO {

    private Long replyId;
    private Long userId;
    private Long boardId;
    private String text;
}
