package com.example.care.board.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private long view;
    private int replyCnt;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private String username;
    private String userNickname;
}

