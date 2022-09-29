package com.example.care.reply.service;

import com.example.care.board.domain.Board;
import com.example.care.board.repository.BoardRepository;
import com.example.care.reply.domain.Reply;
import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.dto.ReplyEditDTO;
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
    public Long registerReply(ReplyEditDTO replyEditDTO) {
        User user = userRepository.getReferenceById(replyEditDTO.getUserId());
        Board board = boardRepository.getReferenceById(replyEditDTO.getBoardId());

        Reply reply = Reply.builder()
                .text(replyEditDTO.getText())
                .board(board)
                .user(user)
                .build();
        replyRepository.save(reply);

        return reply.getId();
    }

    @Override
    @Transactional
    public void modifyReply(ReplyEditDTO replyEditDTO) {
        Reply reply = replyRepository.findById(replyEditDTO.getReplyId()).orElse(null);
        if (reply != null) {
            reply.changeReply(replyEditDTO.getText());
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
    public void removeReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    private ReplyDTO replyEntityToDTO(Reply reply) {
        return ReplyDTO.builder()
                .id(reply.getId())
                .text(reply.getText())
                .regDate(reply.getRegDate())
                .userId(reply.getUser().getId())
                .userNickname(reply.getUser().getNickname())
                .build();
    }
}
