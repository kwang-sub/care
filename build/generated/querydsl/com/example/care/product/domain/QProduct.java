package com.example.care.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1827706768L;

    public static final QProduct product = new QProduct("product");

    public final EnumPath<ProductCode> code = createEnum("code", ProductCode.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> endTime = createNumber("endTime", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<MembershipProduct, QMembershipProduct> membershipProductList = this.<MembershipProduct, QMembershipProduct>createList("membershipProductList", MembershipProduct.class, QMembershipProduct.class, PathInits.DIRECT2);

    public final ListPath<com.example.care.reserve.domain.Reserve, com.example.care.reserve.domain.QReserve> reserveList = this.<com.example.care.reserve.domain.Reserve, com.example.care.reserve.domain.QReserve>createList("reserveList", com.example.care.reserve.domain.Reserve.class, com.example.care.reserve.domain.QReserve.class, PathInits.DIRECT2);

    public final NumberPath<Integer> startTime = createNumber("startTime", Integer.class);

    public final StringPath title = createString("title");

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

