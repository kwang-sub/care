package com.example.care.membership.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMembershipDetail is a Querydsl query type for MembershipDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembershipDetail extends EntityPathBase<MembershipDetail> {

    private static final long serialVersionUID = -213480763L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMembershipDetail membershipDetail = new QMembershipDetail("membershipDetail");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMembership membership;

    public final NumberPath<Integer> number = createNumber("number", Integer.class);

    public final StringPath title = createString("title");

    public QMembershipDetail(String variable) {
        this(MembershipDetail.class, forVariable(variable), INITS);
    }

    public QMembershipDetail(Path<? extends MembershipDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMembershipDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMembershipDetail(PathMetadata metadata, PathInits inits) {
        this(MembershipDetail.class, metadata, inits);
    }

    public QMembershipDetail(Class<? extends MembershipDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.membership = inits.isInitialized("membership") ? new QMembership(forProperty("membership")) : null;
    }

}

