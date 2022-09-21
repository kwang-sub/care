package com.example.care.membership.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMembership is a Querydsl query type for Membership
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembership extends EntityPathBase<Membership> {

    private static final long serialVersionUID = -1310730924L;

    public static final QMembership membership = new QMembership("membership");

    public final NumberPath<Integer> CleanNum = createNumber("CleanNum", Integer.class);

    public final NumberPath<Integer> CounselNum = createNumber("CounselNum", Integer.class);

    public final EnumPath<Grade> grade = createEnum("grade", Grade.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> TransportNum = createNumber("TransportNum", Integer.class);

    public QMembership(String variable) {
        super(Membership.class, forVariable(variable));
    }

    public QMembership(Path<? extends Membership> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMembership(PathMetadata metadata) {
        super(Membership.class, metadata);
    }

}

