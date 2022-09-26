package com.example.care.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMembershipProduct is a Querydsl query type for MembershipProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembershipProduct extends EntityPathBase<MembershipProduct> {

    private static final long serialVersionUID = -1867434598L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMembershipProduct membershipProduct = new QMembershipProduct("membershipProduct");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxNum = createNumber("maxNum", Integer.class);

    public final com.example.care.membership.domain.QMembership membership;

    public final QProduct product;

    public QMembershipProduct(String variable) {
        this(MembershipProduct.class, forVariable(variable), INITS);
    }

    public QMembershipProduct(Path<? extends MembershipProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMembershipProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMembershipProduct(PathMetadata metadata, PathInits inits) {
        this(MembershipProduct.class, metadata, inits);
    }

    public QMembershipProduct(Class<? extends MembershipProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.membership = inits.isInitialized("membership") ? new com.example.care.membership.domain.QMembership(forProperty("membership")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

