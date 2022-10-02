package com.example.care.board.service;

import com.example.care.board.domain.Board;
import com.example.care.board.dto.BoardDTO;
import com.example.care.board.dto.BoardEditDTO;
import com.example.care.board.dto.BoardListDTO;
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
    @DisplayName("페이징 목록 테스트")
    void paginationTest1() {

        IntStream.rangeClosed(1, 101)
                .forEach( i -> {
                    Board board = Board.builder()
                            .title("제목" + i)
                            .content("내용" + i)
                            .user(user)
                            .build();
                    boardRepository.save(board);
                });

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResultDTO<BoardListDTO, Board> result = boardService.getList(pageRequestDTO);
        List<BoardListDTO> dtoList = result.getDtoList();

        assertThat(result.isNext()).isTrue();
        assertThat(result.isPrev()).isFalse();
        assertThat(result.getEnd()).isEqualTo(10);
        assertThat(result.getTotalPage()).isEqualTo(11);

        assertThat(dtoList.size()).isEqualTo(10);
        assertThat(dtoList.get(0).getTitle()).isEqualTo("제목101");
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void createBoardTest() {
        BoardEditDTO boardEditDTO = BoardEditDTO.builder()
                .title("제목")
                .content("내용")
                .userId(user.getId())
                .build();

        Long boardId = boardService.registerBoard(boardEditDTO);
        Board board = boardRepository.findById(boardId).orElse(null);
        assertThat(board.getTitle()).isEqualTo(boardEditDTO.getTitle());
        assertThat(board.getView()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void readTest() {
        Board board = Board.builder()
                .title("제목 test")
                .content("내용 test")
                .user(user)
                .build();
        boardRepository.save(board);

        BoardDTO boardDTO = boardService.readBoard(board.getId());

        assertThat(boardDTO.getTitle()).isEqualTo(board.getTitle());
        assertThat(boardDTO.getUserNickname()).isEqualTo(board.getUser().getNickname());
        assertThat(boardDTO.getView()).isEqualTo(1);

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
        PageResultDTO<BoardListDTO, Board> result = boardService.getList(pageRequestDTO);

        assertThat(result.getEnd()).isEqualTo(1);
        assertThat(result.getPageList().size()).isEqualTo(1);
        assertThat(result.isNext()).isFalse();
        assertThat(result.isPrev()).isFalse();
    }

    @Test
    @DisplayName("페이징 검색 테스트")
    void paginationTest3() {

        IntStream.rangeClosed(1, 101)
                .forEach( i -> {
                    Board board = Board.builder()
                            .title("제목" + i)
                            .content("내용" + i)
                            .user(user)
                            .build();
                    boardRepository.save(board);
                });

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("t")
                .keyword("2")
                .build();
        PageResultDTO<BoardListDTO, Board> result = boardService.getList(pageRequestDTO);
        List<BoardListDTO> dtoList = result.getDtoList();
        for (BoardListDTO boardListDTO : dtoList) {
            assertThat(boardListDTO.getTitle()).contains("2");
        }
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void modifyBoardTest() {
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        boardRepository.save(board);

        BoardEditDTO boardEditDTO = BoardEditDTO.builder()
                .boardId(board.getId())
                .title("수정")
                .content("수정")
                .build();
        boardService.modifyBoard(boardEditDTO);

        Board findBoard = boardRepository.findById(board.getId()).orElse(null);
        assertThat(findBoard.getId()).isEqualTo(board.getId());
        assertThat(findBoard.getTitle()).isEqualTo(boardEditDTO.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(boardEditDTO.getContent());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void removeBoardTest() {
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        boardRepository.save(board);

        boardService.removeBoard(board.getId());
        BoardDTO boardDTO = boardService.readBoard(board.getId());
        assertThat(boardDTO).isNull();
    }
}