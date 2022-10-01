package com.example.care.reply.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class ReplyRemoveDTO {
    @NotNull
    private Long boardId;
    @NotNull
    private Long userId;
}
