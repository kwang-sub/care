package com.example.care.membership.service;

import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipDetail;
import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipDetailDTO;
import com.example.care.membership.repository.MembershipDetailRepository;
import com.example.care.membership.repository.MembershipRepository;
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
    private final MembershipDetailRepository membershipDetailRepository;

    @Override
    public List<MembershipDTO> membershipList() {
        List<Membership> membershipAll = membershipRepository.findMembershipAll();

        List<MembershipDTO> membershipDTOS = membershipAll.stream()
                .map(membership -> {
                    List<MembershipDetailDTO> membershipDetailDTOS = membership.getMembershipDetails()
                            .stream()
                            .map(membershipDetail -> detailEntityToDTO(membershipDetail))
                            .collect(Collectors.toList());

                    MembershipDTO membershipDTO = membershipEntityToDTO(membership, membershipDetailDTOS);
                    return membershipDTO;
                })
                .collect(Collectors.toList());
        return membershipDTOS;
    }

    @Override
    @Transactional
    public void membershipSave(MembershipDTO membershipDTO) {
        Membership membership = membershipDTOToEntity(membershipDTO);
        membershipRepository.save(membership);

        membershipDTO.getMembershipDetailDTOS().stream()
                .forEach(membershipDetailDTO -> {
                    MembershipDetail membershipDetail = detailDTOToEntity(membership, membershipDetailDTO);
                    membershipDetailRepository.save(membershipDetail);
                });
    }

    private Membership membershipDTOToEntity(MembershipDTO membershipDTO) {
        return Membership.builder()
                .id(membershipDTO.getId())
                .grade(membershipDTO.getGrade())
                .price(membershipDTO.getPrice())
                .build();
    }

    private MembershipDTO membershipEntityToDTO(Membership membership, List<MembershipDetailDTO> membershipDetailDTOS) {
        return MembershipDTO.builder()
                .id(membership.getId())
                .grade(membership.getGrade())
                .price(membership.getPrice())
                .membershipDetailDTOS(membershipDetailDTOS)
                .build();
    }

    private MembershipDetail detailDTOToEntity(Membership membership, MembershipDetailDTO membershipDetailDTO) {
        return MembershipDetail.builder()
                .id(membershipDetailDTO.getId())
                .title(membershipDetailDTO.getTitle())
                .comment(membershipDetailDTO.getComment())
                .number(membershipDetailDTO.getNumber())
                .membership(membership)
                .build();
    }

    private static MembershipDetailDTO detailEntityToDTO(MembershipDetail membershipDetail) {
        return MembershipDetailDTO.builder()
                .id(membershipDetail.getId())
                .title(membershipDetail.getTitle())
                .number(membershipDetail.getNumber())
                .comment(membershipDetail.getComment())
                .build();
    }
}
