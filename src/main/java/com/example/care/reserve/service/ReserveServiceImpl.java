package com.example.care.reserve.service;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.product.domain.Product;
import com.example.care.product.domain.ProductCode;
import com.example.care.product.dto.ProductDTO;
import com.example.care.product.repository.MembershipProductRepository;
import com.example.care.product.repository.ProductRepository;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.domain.ReserveStatus;
import com.example.care.reserve.dto.*;
import com.example.care.reserve.repository.ReserveRepository;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.ex.exception.ReserveFullException;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReserveServiceImpl implements ReserveService{

    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MembershipHistoryRepository membershipHistoryRepository;
    private final MembershipProductRepository membershipProductRepository;

    @Override
    public PageResultDTO<ReserveListDTO, Reserve> getReserveList(PageRequestDTO pageRequestDTO) {
        Page<Reserve> result = reserveRepository.findUserReserveList(pageRequestDTO);
        Function<Reserve, ReserveListDTO> fn = (entity ->ReserveListDTO.builder()
                .reserveId(entity.getId())
                .title(entity.getProduct().getTitle())
                .address(entity.getAddress())
                .detailAddress(entity.getDetailAddress())
                .reserveDate(entity.getReserveDate())
                .reserveTime(entity.getReserveTime())
                .reserveStatus(entity.getReserveStatus())
                .reserveUserId(entity.getMembershipHistory().getUser().getId())
                .cancel(LocalDate.now().isBefore(entity.getReserveDate()) && entity.getReserveStatus() == ReserveStatus.RESERVE)
                .build());
        return new PageResultDTO<>(result,fn);
    }

    @Override
    public ReserveTimeResponseDTO confirmReserveTime(ReserveTimeRequestDTO reserveTimeRequestDTO) {
        ReserveTimeResponseDTO reserveTimeResponseDTO = new ReserveTimeResponseDTO();
        Product product;

        List<Reserve> productReserve = reserveRepository.findProductReserve(reserveTimeRequestDTO);

//        예약 있을 경우 예약시간 목록에 추가하는 로직
        if (!productReserve.isEmpty()) {
            productReserve.forEach(reserve ->
                    reserveTimeResponseDTO.getReserveTimeList().add(reserve.getReserveTime()));
            product = productReserve.get(0).getProduct();
        } else {
            product = productRepository.findByCode(reserveTimeRequestDTO.getProductCode());
        }

//        예약일이 오늘 날짜일 경우 3시간 이전부터 예약할 수 있도록 추가하는 로직
        if (reserveTimeRequestDTO.getReserveDate().equals(LocalDate.now())) {
            int nowHour = LocalDateTime.now().getHour();
            IntStream.range(product.getStartTime(), nowHour + 3)
                    .forEach(hour -> reserveTimeResponseDTO.getReserveTimeList().add(hour));
        }

        reserveTimeResponseDTO.setStartTime(product.getStartTime());
        reserveTimeResponseDTO.setEndTime(product.getEndTime());

        return reserveTimeResponseDTO;
    }

    @Override
    @Transactional
    public void reserve(ReserveDTO reserveDTO, Long userId) {
        if (reserveDTO.getReserveDate().equals(LocalDate.now()) && LocalDateTime.now().getHour() + 3 > reserveDTO.getReserveTime()) {
            throw new IllegalStateException();
        }
//        회원 남은 예약이 있는지 확인하는 로직
        ProductDTO productDTO = reserveDTO.getProductDTO();
        ProductCode productCode = productDTO.getCode();

        MembershipHistory userMembershipHistory = membershipHistoryRepository.findMembershipHistoryByUserId(userId);
        int maxNum = membershipProductRepository.findMaxNumByProductCode(productDTO.getCode(), userMembershipHistory.getMembership().getId());
        int useNum  = productUseNum(productCode, userMembershipHistory);

        if (useNum >= maxNum) {
            throw new InsufficientAuthenticationException("이달 멤버쉽 서비스 이용완료");
        }

//        같은 시간대 서비스 예약 있는지 확인하는 로직
        ReserveTimeRequestDTO reserveTimeRequestDTO = ReserveTimeRequestDTO.builder()
                .productCode(reserveDTO.getProductDTO().getCode())
                .reserveDate(reserveDTO.getReserveDate())
                .reserveTime(reserveDTO.getReserveTime())
                .build();
        List<Reserve> reserveList = reserveRepository.findProductReserve(reserveTimeRequestDTO);

        if (!reserveList.isEmpty()){
            throw new ReserveFullException();
        }

//        예약하는 로직
        Product product = productRepository.getReferenceById(productDTO.getId());
        MembershipHistory membershipHistory = membershipHistoryRepository.findMembershipHistoryByUserId(userId);
        Reserve reserve = reserveDTOToEntity(reserveDTO, product, membershipHistory);
        reserveRepository.save(reserve);
        userMembershipHistory.reserveProduct(productCode);
    }

    @Override
    @Transactional
    public void reserveCancel(Long reserveId) {
//        예약 상태변경
        Reserve reserve = reserveRepository.findByIdWithCancel(reserveId);
        reserve.cancel();
//        멤버쉽 이용내역 횟수 변경
        ProductCode productCode = reserve.getProduct().getCode();
        reserve.getMembershipHistory().cancelProduct(productCode);
    }

    @Override
    @Transactional
    public void reserveComplete(LocalDateTime now) {
        reserveRepository.updateStatusComplete(LocalDate.from(now), now.getHour(),
                ReserveStatus.COMPLETE, ReserveStatus.RESERVE);
    }

    private int productUseNum(ProductCode productCode, MembershipHistory membershipHistory) {
        int useNum = 0;
        if (productCode.equals(ProductCode.COUNSEL)) {
            useNum = membershipHistory.getCounselUseNum();
        } else if (productCode.equals(ProductCode.CLEAN)) {
            useNum = membershipHistory.getCleanUseNum();
        } else if (productCode.equals(ProductCode.TRANSPORT)) {
            useNum = membershipHistory.getTransportUseNum();
        }
        return useNum;
    }

    private Reserve reserveDTOToEntity(ReserveDTO reserveDTO, Product product, MembershipHistory membershipHistory) {
//        전문가 방문서비스 아닌 고객 방문서비스 이용시 임의의 장소로 저장
        if (reserveDTO.getAddress() == null && product.getCode().equals(ProductCode.COUNSEL)) {
            reserveDTO.setPostcode("04509");
            reserveDTO.setAddress("서울 중구 한강대로 405");
            reserveDTO.setDetailAddress("한강공원");
            reserveDTO.setExtraAddress("(봉래동2가)");
        }

        return Reserve.builder()
                .name(reserveDTO.getName())
                .phoneNumber(reserveDTO.getPhoneNumber())
                .reserveDate(reserveDTO.getReserveDate())
                .reserveTime(reserveDTO.getReserveTime())
                .postcode(reserveDTO.getPostcode())
                .address(reserveDTO.getAddress())
                .detailAddress(reserveDTO.getDetailAddress())
                .extraAddress(reserveDTO.getExtraAddress())
                .membershipHistory(membershipHistory)
                .product(product)
                .reserveStatus(ReserveStatus.RESERVE)
                .build();
    }
}
