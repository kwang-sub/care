package com.example.care.reply.controller;

import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.dto.ReplyRegisterDTO;
import com.example.care.reply.dto.ReplyRemoveDTO;
import com.example.care.reply.service.ReplyService;
import com.example.care.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> replyList(@PathVariable("boardId") Long boardId) {
        List<ReplyDTO> replyDTOList = replyService.getList(boardId);
        return new ResponseEntity<>(replyDTOList, HttpStatus.OK);
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Long> replySave(@Validated @RequestBody ReplyRegisterDTO replyRegisterDTO, BindingResult bindingResult,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            throw new IllegalStateException("로그인 안됨");
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long userId = principalDetails.getUser().getId();
        replyRegisterDTO.setUserId(userId);
        Long replyId = replyService.registerReply(replyRegisterDTO);

        return new ResponseEntity<>(replyId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{replyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity replyRemove(@PathVariable("replyId") Long replyId,
                                      @Validated @RequestBody ReplyRemoveDTO replyRemoveDTO, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails ) {
        if (principalDetails == null || replyRemoveDTO.getUserId() != principalDetails.getUser().getId()) {
            throw new IllegalStateException("권한 없음");
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        System.out.println(replyRemoveDTO);
        replyService.removeReply(replyId, replyRemoveDTO.getBoardId());

        return new ResponseEntity(HttpStatus.OK);
    }

}
