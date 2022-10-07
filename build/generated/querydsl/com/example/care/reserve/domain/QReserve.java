package com.example.care.reserve.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReserve is a Querydsl query type for Reserve
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReserve extends EntityPathBase<Reserve> {

    private static final long serialVersionUID = 228156400L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReserve reserve = new QReserve("reserve");

    public final StringPath address = createString("address");

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath extraAddress = createString("extraAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.care.membership.domain.QMembershipHistory membershipHistory;

    public final StringPath name = createString("name");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath postcode = createString("postcode");

    public final com.example.care.product.domain.QProduct product;

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> reserveDate = createDate("reserveDate", java.time.LocalDate.class);

    public final EnumPath<ReserveStatus> reserveStatus = createEnum("reserveStatus", ReserveStatus.class);

    public final NumberPath<Integer> ReserveTime = createNumber("ReserveTime", Integer.class);

    public QReserve(String variable) {
        this(Reserve.class, forVariable(variable), INITS);
    }

    public QReserve(Path<? extends Reserve> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReserve(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReserve(PathMetadata metadata, PathInits inits) {
        this(Reserve.class, metadata, inits);
    }

    public QReserve(Class<? extends Reserve> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.membershipHistory = inits.isInitialized("membershipHistory") ? new com.example.care.membership.domain.QMembershipHistory(forProperty("membershipHistory"), inits.get("membershipHistory")) : null;
        this.product = inits.isInitialized("product") ? new com.example.care.product.domain.QProduct(forProperty("product")) : null;
    }

}

