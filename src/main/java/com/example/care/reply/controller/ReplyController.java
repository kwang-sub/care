package com.example.care.reply.controller;

import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.dto.ReplyModifyDTO;
import com.example.care.reply.dto.ReplyRegisterDTO;
import com.example.care.reply.dto.ReplyRemoveDTO;
import com.example.care.reply.service.ReplyService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.util.ex.exception.RequestParamBindException;
import com.example.care.util.ex.exception.UserAccessException;
import com.example.care.util.ex.handler.SussesResult;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SussesResult> replySave(@Validated @RequestBody ReplyRegisterDTO replyRegisterDTO, BindingResult bindingResult,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (bindingResult.hasErrors()) {
            throw new RequestParamBindException("잘못된 요청 파라미터 값 입니다.");
        }
        if (principalDetails == null) {
            throw new UserAccessException("올바르지 않은 사용자 요청");
        }

        Long userId = principalDetails.getUser().getId();
        replyRegisterDTO.setUserId(userId);
        Long replyId = replyService.registerReply(replyRegisterDTO);

        SussesResult result = new SussesResult("200", replyId + "댓글 작성 성공");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/{replyId}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SussesResult> replyModify(@PathVariable("replyId")Long replyId,
                                      @Validated @RequestBody ReplyModifyDTO replyModifyDTO, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (bindingResult.hasErrors()) {
            throw new RequestParamBindException("잘못된 요청 파라미터 값 입니다.");
        }
        if (principalDetails == null || replyModifyDTO.getUserId() != principalDetails.getUser().getId()) {
            throw new UserAccessException("올바르지 않은 사용자 요청입니다.");
        }

        replyModifyDTO.setReplyId(replyId);
        replyService.modifyReply(replyModifyDTO);

        SussesResult result = new SussesResult("200", replyId + "댓글 수정 성공");
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{replyId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SussesResult> replyRemove(@PathVariable("replyId") Long replyId,
                                                    @Validated @RequestBody ReplyRemoveDTO replyRemoveDTO, BindingResult bindingResult,
                                                    @AuthenticationPrincipal PrincipalDetails principalDetails ) {
        if (bindingResult.hasErrors()) {
            throw new RequestParamBindException("잘못된 요청 파라미터 값 입니다.");
        }
        if (principalDetails == null || replyRemoveDTO.getUserId() != principalDetails.getUser().getId()) {
            throw new UserAccessException("올바르지 않은 사용자 요청입니다.");
        }

        replyService.removeReply(replyId, replyRemoveDTO.getBoardId());

        SussesResult result = new SussesResult("200", replyId + "댓글 삭제 성공");
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
