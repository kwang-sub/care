package com.example.care.reply.controller;

import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.service.ReplyService;
import com.example.care.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/{boardId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> replyList(@PathVariable("boardId")Long boardId,
                                                    @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        Long id = principalDetails.getUser().getId();
        List<ReplyDTO> replyDTOList = replyService.getList(boardId);
        return new ResponseEntity<>(replyDTOList, HttpStatus.OK);
    }
}
