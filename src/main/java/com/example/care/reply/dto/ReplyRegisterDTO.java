package com.example.care.reply.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyRegisterDTO {

    @NotNull
    private Long boardId;
    @NotBlank
    private String text;
    private Long parentId;
    private Long userId;
}
