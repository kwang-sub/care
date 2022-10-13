package com.example.care.board.repository;

import com.example.care.board.domain.Board;
import com.example.care.util.pagin.PageRequestDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
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
    public Page<Board> findBoardList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();

        List<Board> content = queryFactory.selectFrom(board)
                .join(board.user, user).fetchJoin()
                .where(searchBoard(pageRequestDTO))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        Long count = queryFactory.select(board.count())
                .from(board)
                .where(searchBoard(pageRequestDTO))
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }

    private Predicate searchBoard(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getType();
        if (type == null || type.isBlank()) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        String keyword = pageRequestDTO.getKeyword();

        if (type.contains("t")) {
            booleanBuilder.or(board.title.contains(keyword));
        }
        if (type.contains("c")) {
            booleanBuilder.or(board.content.contains(keyword));
        }
        if (type.contains("w")) {
            booleanBuilder.or(board.user.nickname.contains(keyword));
        }

        return booleanBuilder;
    }
}
