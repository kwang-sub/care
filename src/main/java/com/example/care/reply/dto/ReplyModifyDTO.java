package com.example.care.reply.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ReplyModifyDTO {

    @NotNull
    private Long replyId;
    @NotNull
    private Long boardId;
    @NotBlank
    private String text;
    private Long userId;
}
