package com.example.care.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductCode code;
    private String title;
    @Lob
    private String description;
    private Integer startTime;
    private Integer endTime;

    @OneToMany(mappedBy = "product")
    private List<ProductMembership> productMembershipList = new ArrayList<>();

    @Builder
    public Product(ProductCode code, String title, String description, Integer startTime, Integer endTime) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
