package com.example.care.board.controller;

import com.example.care.board.dto.BoardDTO;
import com.example.care.board.service.BoardService;
import com.example.care.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String boardList() {

        return "/board/list";
    }

    @PostMapping
    public String sample(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         BoardDTO boardDTO) {
        Long userId = principalDetails.getUser().getId();
        boardDTO.setUserId(userId);
        boardService.registerBoard(boardDTO);
        return "redirect:/board/list";
    }
}
