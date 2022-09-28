package com.example.care.board.service;

import com.example.care.board.domain.Board;
import com.example.care.board.dto.BoardDTO;
import com.example.care.board.dto.BoardEditDTO;
import com.example.care.board.dto.BoardListDTO;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;

public interface BoardService {

    Long registerBoard(BoardEditDTO boardEditDTO);

    PageResultDTO<BoardListDTO, Board> getList(PageRequestDTO pageRequestDTO);

    BoardDTO readBoard(Long boardId);

    BoardEditDTO getModifyBoard(Long boardId);

    void modifyBoard(BoardEditDTO boardEditDTO);

    void removeBoard(Long boardId);
}
