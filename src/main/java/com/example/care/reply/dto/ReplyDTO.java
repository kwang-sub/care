package com.example.care.reply.dto;

import com.example.care.board.domain.Board;
import com.example.care.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReplyDTO {

    private Long id;
    private String text;
    private LocalDateTime regDate;
    private Long userId;
    private String userNickname;
}
