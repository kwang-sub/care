package com.example.care.board.service;

import com.example.care.board.domain.Board;
import com.example.care.board.dto.BoardDTO;
import com.example.care.board.repository.BoardRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
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
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .username("테스트")
                .build();
        userRepository.save(user);


    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void createBoardTest() {
        BoardDTO boardDTO = BoardDTO.builder()
                .title("제목")
                .content("내용")
                .userId(user.getId())
                .build();

        Long boardId = boardService.registerBoard(boardDTO);
        Board board = boardRepository.findById(boardId).orElse(null);
        assertThat(board.getTitle()).isEqualTo(boardDTO.getTitle());
        assertThat(board.getView()).isEqualTo(0);
    }

    @Test
    @DisplayName("페이징 목록 테스트")
    void paginationTest1() {

        IntStream.rangeClosed(1, 30)
                .forEach( i -> {
                    Board board = Board.builder()
                            .title("제목" + i)
                            .content("내용" + i)
                            .user(user)
                            .build();
                    boardRepository.save(board);
                });

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultDTO<BoardDTO, Board> result = boardService.getList(pageRequestDTO);
        List<BoardDTO> dtoList = result.getDtoList();

        assertThat(dtoList.size()).isEqualTo(10);
        assertThat(dtoList.get(0).getId()).isEqualTo(30L);
    }

    @Test
    @DisplayName("페이지네이션 데이터 한개 테스트")
    void paginationTest2() {
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        boardRepository.save(board);

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultDTO<BoardDTO, Board> result = boardService.getList(pageRequestDTO);

        assertThat(result.getEnd()).isEqualTo(1);
        assertThat(result.getPageList().size()).isEqualTo(1);
        assertThat(result.isNext()).isFalse();
        assertThat(result.isPrev()).isFalse();
    }
}