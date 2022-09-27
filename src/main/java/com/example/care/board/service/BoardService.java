package com.example.care.board.service;

import com.example.care.board.domain.Board;
import com.example.care.board.dto.BoardDTO;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    Long registerBoard(BoardDTO boardDTO);

    PageResultDTO<BoardDTO, Board> getList(PageRequestDTO pageRequestDTO);

}
