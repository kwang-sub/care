package com.example.care.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRegisterDTO {

    @NotNull
    private Long boardId;
    @NotBlank
    private String text;
    private Long parentId;
    private Long userId;
}
