package com.example.care.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 2005206334L;

    public static final QUser user = new QUser("user");

    public final ListPath<com.example.care.board.domain.Board, com.example.care.board.domain.QBoard> boardList = this.<com.example.care.board.domain.Board, com.example.care.board.domain.QBoard>createList("boardList", com.example.care.board.domain.Board.class, com.example.care.board.domain.QBoard.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.care.membership.domain.MembershipHistory, com.example.care.membership.domain.QMembershipHistory> membershipHistoryList = this.<com.example.care.membership.domain.MembershipHistory, com.example.care.membership.domain.QMembershipHistory>createList("membershipHistoryList", com.example.care.membership.domain.MembershipHistory.class, com.example.care.membership.domain.QMembershipHistory.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath provider = createString("provider");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

