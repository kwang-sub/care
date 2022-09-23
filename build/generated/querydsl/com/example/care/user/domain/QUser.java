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

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.care.membership.domain.MembershipHistory, com.example.care.membership.domain.QMembershipHistory> membershipHistoryList = this.<com.example.care.membership.domain.MembershipHistory, com.example.care.membership.domain.QMembershipHistory>createList("membershipHistoryList", com.example.care.membership.domain.MembershipHistory.class, com.example.care.membership.domain.QMembershipHistory.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath provider = createString("provider");

    public final ListPath<com.example.care.reserve.domain.Reserve, com.example.care.reserve.domain.QReserve> reserveList = this.<com.example.care.reserve.domain.Reserve, com.example.care.reserve.domain.QReserve>createList("reserveList", com.example.care.reserve.domain.Reserve.class, com.example.care.reserve.domain.QReserve.class, PathInits.DIRECT2);

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

