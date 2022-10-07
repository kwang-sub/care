package com.example.care.membership.service;

import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.dto.KaKaoPayReadyDTO;
import com.example.care.payment.dto.MemberShipDTO;
import com.example.care.product.domain.MembershipProduct;
import com.example.care.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
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
                .productDTOList(membership.getMembershipProductList()
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

    private ProductDTO productEntityToDTO(MembershipProduct membershipProduct) {
        return ProductDTO.builder()
                .id(membershipProduct.getProduct().getId())
                .code(membershipProduct.getProduct().getCode())
                .title(membershipProduct.getProduct().getTitle())
                .description(membershipProduct.getProduct().getDescription())
                .maxNum(membershipProduct.getMaxNum())
                .build();
    }

}
