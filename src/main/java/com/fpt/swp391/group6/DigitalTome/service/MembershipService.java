package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MembershipEntity;
import com.fpt.swp391.group6.DigitalTome.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final UserService userService;
    private final MembershipRepository membershipRepository;

    @Transactional
    public void upgradeUserToMembership(String username, String membershipType) {
        AccountEntity account = userService.findByUsername(username);
        if (account != null) {
            MembershipEntity membership = findMembershipByName(membershipType);
            if (membership == null) {
                membership = new MembershipEntity();
                membership.setName(membershipType);
                membership = membershipRepository.save(membership);
            }
            account.setMembershipEntity(membership);
            userService.save(account);
        }
    }

    public MembershipEntity findMembershipByName(String name) {
        return membershipRepository.findByName(name);
    }
}
