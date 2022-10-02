package com.example.care.reply.service;

import com.example.care.board.domain.Board;
import com.example.care.board.repository.BoardRepository;
import com.example.care.reply.domain.Reply;
import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.dto.ReplyModifyDTO;
import com.example.care.reply.dto.ReplyRegisterDTO;
import com.example.care.reply.repository.ReplyRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public Long registerReply(ReplyRegisterDTO replyRegisterDTO) {
        User user = userRepository.getReferenceById(replyRegisterDTO.getUserId());
        Board board = boardRepository.findById(replyRegisterDTO.getBoardId()).orElse(null);
        board.replyPlus();
        Reply parentReply = null;
        if (replyRegisterDTO.getParentId() != null) {
            parentReply = replyRepository.getReferenceById(replyRegisterDTO.getParentId());
        }

        Reply reply = Reply.builder()
                .text(replyRegisterDTO.getText())
                .board(board)
                .user(user)
                .parent(parentReply)
                .build();
        replyRepository.save(reply);

        return reply.getId();
    }

    @Override
    @Transactional
    public void modifyReply(ReplyModifyDTO replyModifyDTO) {
        Reply reply = replyRepository.findById(replyModifyDTO.getReplyId()).orElse(null);
        if (reply != null) {
            reply.changeReply(replyModifyDTO.getText());
        }
    }

    @Override
    public List<ReplyDTO> getList(Long boardId) {
        List<Reply> replyList = replyRepository.findList(boardId);

        return replyList.stream()
                .map(this::replyEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeReply(Long replyId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        board.replyMinus();
        replyRepository.deleteById(replyId);
    }

    private ReplyDTO replyEntityToDTO(Reply reply) {
        return ReplyDTO.builder()
                .id(reply.getId())
                .text(reply.getText())
                .regDate(reply.getRegDate())
                .userId(reply.getUser().getId())
                .username(reply.getUser().getUsername())
                .userNickname(reply.getUser().getNickname())
                .childReplyList(reply.getChildren().stream()
                        .map(this:: childrenEntityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private ReplyDTO childrenEntityToDTO(Reply reply) {
        return ReplyDTO.builder()
                .id(reply.getId())
                .text(reply.getText())
                .regDate(reply.getRegDate())
                .userId(reply.getUser().getId())
                .username(reply.getUser().getUsername())
                .userNickname(reply.getUser().getNickname())
                .build();
    }
}
