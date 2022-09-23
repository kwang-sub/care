package com.example.care.reserve.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.product.domain.Product;
import com.example.care.product.domain.ProductCode;
import com.example.care.product.domain.ProductMembership;
import com.example.care.product.dto.ProductDTO;
import com.example.care.product.repository.ProductRepository;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.repository.ReserveRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ReserveServiceImplTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MembershipHistoryRepository membershipHistoryRepository;
    @Autowired
    ReserveService reserveService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MembershipRepository membershipRepository;
    @Autowired
    ReserveRepository reserveRepository;

    private User user;
    private Membership membership;
    private Product product;

    @BeforeEach
    void setup() {
        user = new User();
        userRepository.save(user);

        membership = Membership.builder()
                .grade(Grade.BRONZE)
                .build();
        membershipRepository.save(membership);

        product = Product.builder()
                .code(ProductCode.CLEAN)
                .build();
        productRepository.save(product);
    }

    @Test
    void reserveTest() {
//        given
        ProductMembership productMembership = ProductMembership.builder()
                .product(product)
                .membership(membership)
                .maxNum(10)
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .code(ProductCode.CLEAN)
                .build();

        ReserveDTO reserveDTO = ReserveDTO.builder()
                .name("test")
                .reserveDate(LocalDate.now())
                .reserveTime(10)
                .productDTO(productDTO)
                .build();
//        when
        reserveService.reserve(reserveDTO, user.getId());
        Reserve reserve = reserveRepository.findById(1L).get();
//        then
        assertThat(reserve.getName()).isEqualTo(reserveDTO.getName());
    }
}