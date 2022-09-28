package com.example.care.board.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class BoardEditDTO {
    private Long boardId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private Long userId;
}

