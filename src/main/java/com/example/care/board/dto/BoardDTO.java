package com.example.care.board.dto;

import com.example.care.user.dto.UserDTO;
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
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private Long userId;
    private String userNickname;
}

