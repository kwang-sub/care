package com.example.care.reply.repository;

import com.example.care.reply.domain.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.care.reply.domain.QReply.reply;
import static com.example.care.user.domain.QUser.user;


@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Reply> findList(Long boardId) {
        return queryFactory.selectFrom(reply)
                .distinct()
                .leftJoin(reply.children).fetchJoin()
                .leftJoin(reply.user, user).fetchJoin()
                .where(reply.board.id.eq(boardId),
                        reply.parent.isNull())
                .orderBy(reply.id.desc())
                .fetch();
    }
}
