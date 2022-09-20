package com.example.care.membership.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMembershipHistory is a Querydsl query type for MembershipHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembershipHistory extends EntityPathBase<MembershipHistory> {

    private static final long serialVersionUID = 1341243552L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMembershipHistory membershipHistory = new QMembershipHistory("membershipHistory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMembership membership;

    public final com.example.care.pay.domain.QPayment payment;

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final EnumPath<MembershipStatus> status = createEnum("status", MembershipStatus.class);

    public final com.example.care.user.domain.QUser user;

    public QMembershipHistory(String variable) {
        this(MembershipHistory.class, forVariable(variable), INITS);
    }

    public QMembershipHistory(Path<? extends MembershipHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMembershipHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMembershipHistory(PathMetadata metadata, PathInits inits) {
        this(MembershipHistory.class, metadata, inits);
    }

    public QMembershipHistory(Class<? extends MembershipHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.membership = inits.isInitialized("membership") ? new QMembership(forProperty("membership")) : null;
        this.payment = inits.isInitialized("payment") ? new com.example.care.pay.domain.QPayment(forProperty("payment")) : null;
        this.user = inits.isInitialized("user") ? new com.example.care.user.domain.QUser(forProperty("user")) : null;
    }

}
