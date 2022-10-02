package com.example.care.reply.dto;

import com.example.care.board.domain.Board;
import com.example.care.user.domain.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReplyDTO {

    private Long id;
    private String text;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;
    private Long userId;
    private String username;
    private String userNickname;
    private Long boardId;

    private List<ReplyDTO> childReplyList;
}
