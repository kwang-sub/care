package com.example.care.board.service;

import com.example.care.board.domain.Board;
import com.example.care.board.dto.BoardDTO;
import com.example.care.board.dto.BoardEditDTO;
import com.example.care.board.dto.BoardListDTO;
import com.example.care.board.repository.BoardRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.escape.HTMLCharacterEscapes;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long registerBoard(BoardEditDTO boardEditDTO) {
        User user = userRepository.getReferenceById(boardEditDTO.getUserId());
        Board board = Board.builder()
                .title(boardEditDTO.getTitle())
                .content(boardEditDTO.getContent())
                .user(user)
                .build();
        boardRepository.save(board);
        return board.getId();
    }

    @Override
    public PageResultDTO<BoardListDTO, Board> getList(PageRequestDTO pageRequestDTO) {
        Page<Board> result = boardRepository.findBoardList(pageRequestDTO);
        Function<Board, BoardListDTO> fn = (entity -> BoardListDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .view(entity.getView())
                .replyCnt(entity.getReplyCnt())
                .regDate(entity.getRegDate())
                .userNickname(entity.getUser().getNickname())
                .build());

        return new PageResultDTO<>(result, fn);
    }

    @Override
    @Transactional
    public BoardDTO readBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        return board == null ? null : BoardEntityToDTO(board);
    }

    @Override
    public BoardEditDTO getModifyBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);

        return board == null ? null : BoardEditDTO.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .userId(board.getUser().getId())
                .build();
    }

    @Override
    @Transactional
    public void modifyBoard(BoardEditDTO boardEditDTO) {
        Board board = boardRepository.findById(boardEditDTO.getBoardId()).orElse(null);
        if (board != null) {
            board.changeBoard(boardEditDTO.getTitle(), boardEditDTO.getContent());
        }
    }

    @Override
    @Transactional
    public void removeBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    private BoardDTO BoardEntityToDTO(Board board) {
        board.read();
        BoardDTO boardDTO = BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .view(board.getView())
                .replyCnt(board.getReplyCnt())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .username(board.getUser().getUsername())
                .userNickname(board.getUser().getNickname())
                .build();
        return boardDTO;
    }
}
