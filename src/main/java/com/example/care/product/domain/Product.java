package com.example.care.product.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
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

    @OneToMany(mappedBy = "product")
    private List<ProductMembership> productMembershipList = new ArrayList<>();
}
