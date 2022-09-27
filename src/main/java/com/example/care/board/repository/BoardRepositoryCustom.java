package com.example.care.board.repository;

import com.example.care.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> findBoardList(Pageable pageable);
}
