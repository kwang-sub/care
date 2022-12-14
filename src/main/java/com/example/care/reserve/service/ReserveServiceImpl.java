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
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.dto.ReserveListDTO;
import com.example.care.reserve.dto.ReserveTimeRequestDTO;
import com.example.care.reserve.dto.ReserveTimeResponseDTO;
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

//        ?????? ?????? ?????? ???????????? ????????? ???????????? ??????
        if (!productReserve.isEmpty()) {
            productReserve.forEach(reserve ->
                    reserveTimeResponseDTO.getReserveTimeList().add(reserve.getReserveTime()));
            product = productReserve.get(0).getProduct();
        } else {
            product = productRepository.findByCode(reserveTimeRequestDTO.getProductCode());
        }

//        ???????????? ?????? ????????? ?????? 3?????? ???????????? ????????? ??? ????????? ???????????? ??????
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
//        ?????? ?????? ????????? ????????? ???????????? ??????
        ProductDTO productDTO = reserveDTO.getProductDTO();
        ProductCode productCode = productDTO.getCode();

        MembershipHistory userMembershipHistory = membershipHistoryRepository.findMembershipHistoryByUserId(userId);
        int maxNum = membershipProductRepository.findMaxNumByProductCode(productDTO.getCode(), userMembershipHistory.getMembership().getId());
        int useNum  = productUseNum(productCode, userMembershipHistory);

        if (useNum >= maxNum) {
            throw new InsufficientAuthenticationException("?????? ????????? ????????? ????????????");
        }

//        ?????? ????????? ????????? ?????? ????????? ???????????? ??????
        ReserveTimeRequestDTO reserveTimeRequestDTO = ReserveTimeRequestDTO.builder()
                .productCode(reserveDTO.getProductDTO().getCode())
                .reserveDate(reserveDTO.getReserveDate())
                .reserveTime(reserveDTO.getReserveTime())
                .build();
        List<Reserve> reserveList = reserveRepository.findProductReserve(reserveTimeRequestDTO);

        if (!reserveList.isEmpty()){
            throw new ReserveFullException();
        }

//        ???????????? ??????
        Product product = productRepository.getReferenceById(productDTO.getId());
        MembershipHistory membershipHistory = membershipHistoryRepository.findMembershipHistoryByUserId(userId);
        Reserve reserve = reserveDTOToEntity(reserveDTO, product, membershipHistory);
        reserveRepository.save(reserve);
        userMembershipHistory.reserveProduct(productCode);
    }

    @Override
    @Transactional
    public void reserveCancel(Long reserveId) {
//        ?????? ????????????
        Reserve reserve = reserveRepository.findByIdWithCancel(reserveId);
        reserve.cancel();
//        ????????? ???????????? ?????? ??????
        ProductCode productCode = reserve.getProduct().getCode();
        reserve.getMembershipHistory().cancelProduct(productCode);
    }

    @Override
    @Transactional
    public void reserveCompleteSch(LocalDateTime now) {
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
//        ????????? ??????????????? ?????? ?????? ??????????????? ????????? ????????? ????????? ??????
        if (reserveDTO.getAddress() == null && product.getCode().equals(ProductCode.COUNSEL)) {
            reserveDTO.setPostcode("04509");
            reserveDTO.setAddress("?????? ?????? ???????????? 405");
            reserveDTO.setDetailAddress("????????????");
            reserveDTO.setExtraAddress("(?????????2???)");
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
