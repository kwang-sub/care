package com.example.care.reserve.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.product.domain.MembershipProduct;
import com.example.care.product.domain.Product;
import com.example.care.product.domain.ProductCode;
import com.example.care.product.dto.ProductDTO;
import com.example.care.product.repository.MembershipProductRepository;
import com.example.care.product.repository.ProductRepository;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.repository.ReserveRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.exception.DuplicateUserException;
import com.example.care.util.exception.ReserveFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ReserveServiceTest {

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
    @Autowired
    MembershipProductRepository membershipProductRepository;

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
    @DisplayName("정상 저장 테스트")
    void reserveTest() {
        MembershipProduct membershipProduct = MembershipProduct.builder()
                .product(product)
                .membership(membership)
                .maxNum(2)
                .build();
        membershipProductRepository.save(membershipProduct);
//        given
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .code(ProductCode.CLEAN)
                .build();

        ReserveDTO reserveDTO = ReserveDTO.builder()
                .name("test")
                .reserveDate(LocalDate.now().plusDays(1))
                .reserveTime(10)
                .productDTO(productDTO)
                .build();
//        when
        reserveService.reserve(reserveDTO, user.getId());
//        then
        Reserve reserve = reserveRepository.findById(1L).get();
        assertThat(reserve.getName()).isEqualTo(reserveDTO.getName());
    }

    @Test
    @DisplayName("당일 3시간 이전예약 예외 테스트")
    void reserveNowTest() {
        MembershipProduct membershipProduct = MembershipProduct.builder()
                .product(product)
                .membership(membership)
                .maxNum(2)
                .build();
        membershipProductRepository.save(membershipProduct);
//        given
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .code(ProductCode.CLEAN)
                .build();

        ReserveDTO reserveDTO = ReserveDTO.builder()
                .name("test")
                .reserveDate(LocalDate.now())
                .reserveTime(10)
                .productDTO(productDTO)
                .build();
        
        assertThatThrownBy(() -> reserveService.reserve(reserveDTO, user.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("같은 시간 예약시 예외 테스트")
    void reserveFullTest() {
        MembershipProduct membershipProduct = MembershipProduct.builder()
                .product(product)
                .membership(membership)
                .maxNum(2)
                .build();
        membershipProductRepository.save(membershipProduct);
//        given
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .code(ProductCode.CLEAN)
                .build();

        ReserveDTO reserveDTO = ReserveDTO.builder()
                .name("test")
                .reserveDate(LocalDate.now().plusDays(1))
                .reserveTime(10)
                .productDTO(productDTO)
                .build();
//        when
        reserveService.reserve(reserveDTO, user.getId());
//        then
        assertThatThrownBy(() -> reserveService.reserve(reserveDTO, user.getId()))
                .isInstanceOf(ReserveFullException.class);
    }

    @Test
    @DisplayName("남은예약 횟수 없을시 예외 테스트")
    void reserveUseTest() {
        MembershipProduct membershipProduct = MembershipProduct.builder()
                .product(product)
                .membership(membership)
                .maxNum(1)
                .build();
        membershipProductRepository.save(membershipProduct);
//        given
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .code(ProductCode.CLEAN)
                .build();

        ReserveDTO reserveDTO1 = ReserveDTO.builder()
                .name("test")
                .reserveDate(LocalDate.now().plusDays(1))
                .reserveTime(10)
                .productDTO(productDTO)
                .build();

        ReserveDTO reserveDTO2 = ReserveDTO.builder()
                .name("test")
                .reserveDate(LocalDate.now().plusDays(1))
                .reserveTime(16)
                .productDTO(productDTO)
                .build();
//        when
        reserveService.reserve(reserveDTO1, user.getId());
//        then
        assertThatThrownBy(() -> reserveService.reserve(reserveDTO2, user.getId()))
                .isInstanceOf(InsufficientAuthenticationException.class);
    }
}