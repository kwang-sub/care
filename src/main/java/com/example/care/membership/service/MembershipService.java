package com.example.care.membership.service;

import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipHistoryDTO;

import java.util.List;

public interface MembershipService {

    List<MembershipDTO> membershipList();

    void membershipSave(MembershipDTO membershipDTO);

    MembershipHistoryDTO findValidMembership(String username);

}
