package com.example.care.membership.service;

import com.example.care.membership.domain.Membership;
import com.example.care.membership.dto.MembershipDTO;

import java.util.List;

public interface MembershipService {

    List<MembershipDTO> membershipList();

    void membershipSave(MembershipDTO membershipDTO);
}
