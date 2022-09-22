package com.example.care.membership.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.product.domain.ProductMembership;
import com.example.care.product.dto.ProductDTO;
import com.example.care.util.exception.DuplicateMembershipException;
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

    private MembershipDTO membershipEntityToDTO(Membership membership) {
        return MembershipDTO.builder()
                .id(membership.getId())
                .price(membership.getPrice())
                .grade(membership.getGrade())
                .productDTOList(membership.getProductMembershipList()
                        .stream()
                        .map(this::productEntityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public MembershipHistoryDTO findValidMembership(String username) {
        MembershipHistory membership = membershipHistoryRepository.findValidMembership(username);

        return membership != null ? MembershipHistoryDTO.builder()
                .membership(membership.getMembership())
                .build() : null;
    }

    private ProductDTO productEntityToDTO(ProductMembership productMembership) {
        return ProductDTO.builder()
                .id(productMembership.getProduct().getId())
                .code(productMembership.getProduct().getCode())
                .title(productMembership.getProduct().getTitle())
                .description(productMembership.getProduct().getDescription())
                .maxNum(productMembership.getMaxNum())
                .build();
    }
}
