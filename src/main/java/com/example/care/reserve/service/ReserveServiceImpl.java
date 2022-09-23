package com.example.care.reserve.service;

import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.product.domain.Product;
import com.example.care.product.domain.ProductCode;
import com.example.care.product.domain.QProductMembership;
import com.example.care.product.dto.ProductDTO;
import com.example.care.product.repository.ProductRepository;
import com.example.care.reserve.domain.QReserve;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveConfirmDTO;
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.repository.ReserveRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.exception.ReserveFullException;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.care.product.domain.QProductMembership.productMembership;
import static com.example.care.reserve.domain.QReserve.reserve;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService{

    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public void reserve(ReserveDTO reserveDTO, Long userId) {
//        회원 남은 예약이 있는지 확인하는 로직
        ProductDTO productDTO = reserveDTO.getProductDTO();
        Tuple useProductNum = reserveRepository.findUseProductNum(productDTO.getCode(), userId);
        if (useProductNum != null) {
            long use = useProductNum.get(reserve.count());
            int max = useProductNum.get(productMembership.maxNum.max());

            if (use >= max) {
                throw new RuntimeException();
            }
        }

//        같은 시간대 서비스 예약 있는지 확인하는 로직
        ReserveConfirmDTO reserveConfirmDTO = ReserveConfirmDTO.builder()
                .productCode(reserveDTO.getProductDTO().getCode())
                .reserveDate(reserveDTO.getReserveDate())
                .reserveTime(reserveDTO.getReserveTime())
                .build();
        List<Reserve> reserveList = reserveRepository.findProductReserve(reserveConfirmDTO);

        if (!reserveList.isEmpty()){
            throw new ReserveFullException();
        };

//        예약하는 로직
        Product product = productRepository.getReferenceById(productDTO.getId());
        User user = userRepository.getReferenceById(userId);
        Reserve reserve = reserveDTOToEntity(reserveDTO, product, user);
        reserveRepository.save(reserve);
    }

    private Reserve reserveDTOToEntity(ReserveDTO reserveDTO, Product product, User user) {
        return Reserve.builder()
                .name(reserveDTO.getName())
                .phoneNumber(reserveDTO.getPhoneNumber())
                .reserveDate(reserveDTO.getReserveDate())
                .reserveTime(reserveDTO.getReserveTime())
                .address(reserveDTO.getAddress())
                .detailAddress(reserveDTO.getDetailAddress())
                .extraAddress(reserveDTO.getExtraAddress())
                .user(user)
                .product(product)
                .build();
    }
}
