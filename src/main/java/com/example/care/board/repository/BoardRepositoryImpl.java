package com.example.care.board.repository;

import com.example.care.board.domain.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.care.board.domain.QBoard.board;
import static com.example.care.user.domain.QUser.user;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> findBoardList(Pageable pageable) {
        List<Board> content = queryFactory.selectFrom(board)
                .join(board.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        Long count = queryFactory.select(board.count())
                .from(board)
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }
}
