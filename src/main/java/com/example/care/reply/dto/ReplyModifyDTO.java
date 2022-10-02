package com.example.care.reply.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyModifyDTO {

    private Long replyId;
    @NotBlank
    private String text;
    private Long userId;
}
