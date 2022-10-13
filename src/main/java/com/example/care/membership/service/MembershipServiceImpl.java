package com.example.care.membership.service;

import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.api.PayAPI;
import com.example.care.product.domain.MembershipProduct;
import com.example.care.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService{

    private final MembershipRepository membershipRepository;
    private final MembershipHistoryRepository membershipHistoryRepository;
    private final PayAPI payAPI;

    @Override
    public List<MembershipDTO> membershipList() {
        List<Membership> membershipList = membershipRepository.findMembershipList();

        return membershipList.stream()
                .map(this::membershipEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void membershipSave(MembershipDTO membershipDTO) {
        Membership membership = Membership.builder()
                .id(membershipDTO.getId())
                .price(membershipDTO.getPrice())
                .grade(membershipDTO.getGrade())
                .build();

        membershipRepository.save(membership);
    }

    @Override
    public MembershipHistoryDTO findValidMembership(Long userId) {
        MembershipHistory membership = membershipHistoryRepository.findValidMembership(userId);

        return membership != null ? MembershipHistoryDTO.builder()
                .membership(membership.getMembership())
                .build() : null;
    }

    @Override
    @Transactional
    public void userMembershipCancel(Long userId) {
        MembershipHistory membershipHistory = membershipHistoryRepository.findValidMembership(userId);
        String sid = membershipHistory.getPayment().getSid();
        payAPI.paymentDisabledAPI(sid);
        membershipHistory.membershipCancel();
    }

    private ProductDTO productEntityToDTO(MembershipProduct membershipProduct) {
        return ProductDTO.builder()
                .id(membershipProduct.getProduct().getId())
                .code(membershipProduct.getProduct().getCode())
                .title(membershipProduct.getProduct().getTitle())
                .description(membershipProduct.getProduct().getDescription())
                .maxNum(membershipProduct.getMaxNum())
                .build();
    }

    private MembershipDTO membershipEntityToDTO(Membership membership) {
        return MembershipDTO.builder()
                .id(membership.getId())
                .price(membership.getPrice())
                .grade(membership.getGrade())
                .productDTOList(membership.getMembershipProductList()
                        .stream()
                        .map(this::productEntityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
