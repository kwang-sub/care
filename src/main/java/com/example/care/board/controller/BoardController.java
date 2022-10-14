package com.example.care.board.controller;

import com.example.care.board.domain.Board;
import com.example.care.board.dto.BoardDTO;
import com.example.care.board.dto.BoardEditDTO;
import com.example.care.board.dto.BoardListDTO;
import com.example.care.board.service.BoardService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String boardList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model) {
        PageResultDTO<BoardListDTO, Board> result = boardService.getList(pageRequestDTO);
        model.addAttribute("result", result);

        return "board/list";
    }

    @GetMapping("/register")
    public String boardRegisterForm(@ModelAttribute("boardEditDTO") BoardEditDTO boardEditDTO) {

        return "board/register";
    }

    @PostMapping("/register")
    public String register(@AuthenticationPrincipal PrincipalDetails principalDetails,
                           @Validated BoardEditDTO boardEditDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.debug("게시글작성 validation errors = {}", bindingResult);
            return "board/register";
        }

        Long userId = principalDetails.getUser().getId();
        boardEditDTO.setUserId(userId);
        boardService.registerBoard(boardEditDTO);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "게시글 작성이 완료되었습니다.", SwalIcon.SUCCESS));
        return "redirect:/board";
    }

    @GetMapping("/{boardId}")
    public String boardReadForm(@PathVariable("boardId") Long boardId, Model model,
                                @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO) {
        BoardDTO boardDTO = boardService.readBoard(boardId);
        model.addAttribute("boardDTO", boardDTO);
        return "board/read";
    }
    @GetMapping("/{boardId}/modify")
    public String boardModifyForm(@PathVariable("boardId") Long boardId, Model model,
                                  @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO) {
        BoardEditDTO boardEditDTO = boardService.getModifyBoard(boardId);
        model.addAttribute("boardEditDTO", boardEditDTO);
        return "board/modify";
    }

    @PostMapping("/{boardId}/modify")
    public String boardModify(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal PrincipalDetails principalDetails,
                              @Validated BoardEditDTO boardEditDTO, BindingResult bindingResult,
                              @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        if (boardEditDTO.getUserId() != principalDetails.getUser().getId()) {
            log.error("로그인한 회원이 작성하지 않은 게시글 수정요청");
            return "error/400";
        }
        if (bindingResult.hasErrors()) {
            log.info("요청 파라미터 값 이상 {} ", bindingResult.getAllErrors());
            return "board/modify";
        }

        boardService.modifyBoard(boardEditDTO);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/board/" + boardId;
    }
    @PostMapping("/{boardId}/remove")
    public String boardRemove(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal PrincipalDetails principalDetails,
                              BoardEditDTO boardEditDTO, RedirectAttributes redirectAttributes) {
        if (boardEditDTO.getUserId() != principalDetails.getUser().getId()) {
            log.error("로그인한 회원이 작성하지 않은 게시글 삭제요청");
            return "error/400";
        }

        boardService.removeBoard(boardId);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "게시글이 삭제되었습니다.", SwalIcon.SUCCESS));

        return "redirect:/board/";
    }

}
