package com.example.care.pay.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTid is a Querydsl query type for Tid
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTid extends EntityPathBase<Tid> {

    private static final long serialVersionUID = 1193551959L;

    public static final QTid tid1 = new QTid("tid1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath orderId = createString("orderId");

    public final StringPath tid = createString("tid");

    public QTid(String variable) {
        super(Tid.class, forVariable(variable));
    }

    public QTid(Path<? extends Tid> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTid(PathMetadata metadata) {
        super(Tid.class, metadata);
    }

}

