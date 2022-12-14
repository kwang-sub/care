package com.example.care.reserve.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.domain.Payment;
import com.example.care.payment.repository.PaymentRepository;
import com.example.care.product.domain.MembershipProduct;
import com.example.care.product.domain.Product;
import com.example.care.product.domain.ProductCode;
import com.example.care.product.dto.ProductDTO;
import com.example.care.product.repository.MembershipProductRepository;
import com.example.care.product.repository.ProductRepository;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.domain.ReserveStatus;
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.dto.ReserveListDTO;
import com.example.care.reserve.repository.ReserveRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.ex.exception.ReserveFullException;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

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

    @Autowired
    PaymentRepository paymentRepository;


    private User user;
    private Membership membership;
    private Product product;
    private MembershipHistory membershipHistory;

    @BeforeEach
    void setup() {
        user = new User();
        userRepository.save(user);

        membership = Membership.builder()
                .grade(Grade.BRONZE)
                .build();
        membershipRepository.save(membership);

        product = Product.builder()
                .title("???????????????")
                .code(ProductCode.CLEAN)
                .build();
        productRepository.save(product);

        Payment payment = new Payment();
        paymentRepository.save(payment);
        membershipHistory = MembershipHistory.builder()
                .counselUseNum(0)
                .transportUseNum(0)
                .cleanUseNum(0)
                .user(user)
                .membership(membership)
                .payment(payment)
                .status(MembershipStatus.ORDER)
                .build();
        membershipHistoryRepository.save(membershipHistory);
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
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
    @DisplayName("?????? 3?????? ???????????? ?????? ?????????")
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
                .reserveTime(LocalDateTime.now().getHour() - 1)
                .productDTO(productDTO)
                .build();

        assertThatThrownBy(() -> reserveService.reserve(reserveDTO, user.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
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
    @DisplayName("???????????? ?????? ????????? ?????? ?????????")
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

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
    void reservePaging() {
//        given
        IntStream.rangeClosed(1, 100).forEach(
                i -> {
                    Reserve reserve = Reserve.builder()
                            .reserveStatus(ReserveStatus.RESERVE)
                            .reserveDate(LocalDate.now())
                            .reserveTime(13)
                            .address("??????")
                            .detailAddress("????????????")
                            .product(product)
                            .membershipHistory(membershipHistory)
                            .build();
                    reserveRepository.save(reserve);
                });
//        when
        PageRequestDTO pageRequestDTO = new PageRequestDTO(1,10, null, user.getId().toString());
        PageResultDTO<ReserveListDTO, Reserve> reserveList = reserveService.getReserveList(pageRequestDTO);

//        then
        assertThat(reserveList.getTotalPage()).isEqualTo(10);
        assertThat(reserveList.isPrev()).isFalse();
        assertThat(reserveList.isNext()).isFalse();
        List<ReserveListDTO> dtoList = reserveList.getDtoList();
        assertThat(dtoList.size()).isEqualTo(10);
        for (ReserveListDTO reserveListDTO : dtoList) {
            assertThat(reserveListDTO.getReserveUserId()).isEqualTo(user.getId());
        }
    }
}