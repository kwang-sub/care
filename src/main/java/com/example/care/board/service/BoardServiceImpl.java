package com.example.care.board.service;

import com.example.care.board.domain.Board;
import com.example.care.board.dto.BoardDTO;
import com.example.care.board.repository.BoardRepository;
import com.example.care.user.domain.User;
import com.example.care.user.dto.UserDTO;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public Long registerBoard(BoardDTO boardDTO) {
        User user = userRepository.getReferenceById(boardDTO.getUserId());
        Board board = Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .user(user)
                .build();
        boardRepository.save(board);
        return board.getId();
    }

    @Override
    public PageResultDTO<BoardDTO, Board> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Board> result = boardRepository.findBoardList(pageable);
        Function<Board, BoardDTO> fn = (entity -> BoardDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .view(entity.getView())
                .regDate(entity.getRegDate())
                .userId(entity.getUser().getId())
                .userNickname(entity.getUser().getNickname())
                .build());

        return new PageResultDTO<>(result, fn);
    }

}
