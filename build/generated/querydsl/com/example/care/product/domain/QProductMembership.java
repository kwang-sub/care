package com.example.care.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductMembership is a Querydsl query type for ProductMembership
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductMembership extends EntityPathBase<ProductMembership> {

    private static final long serialVersionUID = -839661690L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductMembership productMembership = new QProductMembership("productMembership");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxNum = createNumber("maxNum", Integer.class);

    public final com.example.care.membership.domain.QMembership membership;

    public final QProduct product;

    public QProductMembership(String variable) {
        this(ProductMembership.class, forVariable(variable), INITS);
    }

    public QProductMembership(Path<? extends ProductMembership> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductMembership(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductMembership(PathMetadata metadata, PathInits inits) {
        this(ProductMembership.class, metadata, inits);
    }

    public QProductMembership(Class<? extends ProductMembership> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.membership = inits.isInitialized("membership") ? new com.example.care.membership.domain.QMembership(forProperty("membership")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

