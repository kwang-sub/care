package com.example.care.reply.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReplyRemoveDTO {
    @NotNull
    private Long boardId;
    @NotNull
    private Long userId;
}
