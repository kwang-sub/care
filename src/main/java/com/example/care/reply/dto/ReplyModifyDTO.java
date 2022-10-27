package com.example.care.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyModifyDTO {

    private Long replyId;
    @NotBlank
    private String text;
    private Long userId;
}
