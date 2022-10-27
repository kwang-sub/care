package com.example.care.board.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardListDTO {

    private Long id;
    private String title;
    private long view;
    private int replyCnt;
    private LocalDateTime regDate;
    private String userNickname;
}

