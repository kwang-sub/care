package com.example.care.reply.service;

import com.example.care.board.domain.Board;
import com.example.care.board.repository.BoardRepository;
import com.example.care.reply.domain.Reply;
import com.example.care.reply.dto.ReplyDTO;
import com.example.care.reply.dto.ReplyRegisterDTO;
import com.example.care.reply.dto.ReplyModifyDTO;
import com.example.care.reply.repository.ReplyRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private ReplyRepository replyRepository;

    User user;
    Board board;

    @BeforeEach
    void setup() {
        user = User.builder()
                .username("test")
                .nickname("kwang")
                .build();
        userRepository.save(user);

        board = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(user)
                .build();
        boardRepository.save(board);
    }
    
    @Test
    @DisplayName("댓글 저장 테스트")
    void registerReplyTest() {
        ReplyRegisterDTO replyRegisterDTO = ReplyRegisterDTO.builder()
                .text("첫 댓글 작성")
                .boardId(board.getId())
                .userId(user.getId())
                .build();

        Long replyId = replyService.registerReply(replyRegisterDTO);

        Reply reply = replyRepository.findById(replyId).orElse(null);

        assertThat(reply).isNotNull();
        assertThat(reply.getBoard().getId()).isEqualTo(board.getId());
        assertThat(reply.getUser().getId()).isEqualTo(user.getId());
        assertThat(reply.getText()).isEqualTo(replyRegisterDTO.getText());
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void modifyReplyTest() {
        Reply reply = Reply.builder()
                .text("댓글입니다.")
                .board(board)
                .user(user)
                .build();
        replyRepository.save(reply);

        ReplyModifyDTO replyModifyDTO = ReplyModifyDTO.builder()
                .replyId(reply.getId())
                .text("수정 했습니다.")
                .build();
        replyService.modifyReply(replyModifyDTO);

        Reply findReply = replyRepository.findById(reply.getId()).orElse(null);
        assertThat(findReply.getId()).isEqualTo(reply.getId());
        assertThat(findReply.getText()).isEqualTo(replyModifyDTO.getText());
    }

    @Test
    @DisplayName("댓글 목록 테스트")
    void replyListTest() {
        IntStream.rangeClosed(1, 10)
                .forEach( i -> {
                    Reply reply = Reply
                            .builder()
                            .text("댓글" + i)
                            .board(board)
                            .user(user)
                            .build();
                    replyRepository.save(reply);
                });

        List<ReplyDTO> list = replyService.getList(board.getId());
        assertThat(list.size()).isEqualTo(10);
        assertThat(list.get(0).getText()).isEqualTo("댓글10");
    }
}