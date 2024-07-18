package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MembershipEntity;
import com.fpt.swp391.group6.DigitalTome.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final UserService userService;
    private final MembershipRepository membershipRepository;

    private static final int MILLIS_PER_MINUTE = 60 * 1000;

    private static final long MILLIS_IN_30_DAYS = 30L * 24 * 60 * 60 * 1000;
    private static final long MILLIS_IN_90_DAYS = 90L * 24 * 60 * 60 * 1000;
    private static final long MILLIS_IN_150_DAYS = 150L * 24 * 60 * 60 * 1000;

    @Transactional
    public void processMembershipUpgrade(AccountEntity accountEntity, String membershipType) {

        MembershipEntity membership = findOrCreateMembership(membershipType);

        accountEntity.setMembershipEntity(membership);
        accountEntity.setStartUpdate(new Date());

        Date membershipExpiryDate = calculateExpiryDate(membershipType, accountEntity.getStartUpdate());
        accountEntity.setMembershipExpiryDate(membershipExpiryDate);

        userService.save(accountEntity);
    }

    private Date calculateExpiryDate(String membershipType, Date startDate) {
        long duration = switch (membershipType) {
            case "basic" -> MILLIS_IN_30_DAYS;
            case "standard" -> MILLIS_IN_90_DAYS;
            case "premium" -> MILLIS_IN_150_DAYS;
            default -> throw new IllegalArgumentException("Unknown membership type: " + membershipType);
        };
        return new Date(startDate.getTime() + duration);
    }

    public MembershipEntity findMembershipByName(String name) {
        return membershipRepository.findByName(name);
    }

    private MembershipEntity findOrCreateMembership(String membershipType) {
        MembershipEntity membership = findMembershipByName(membershipType);
        if (membership == null) {
            membership = new MembershipEntity();
            membership.setName(membershipType);
            membership = membershipRepository.save(membership);
        }
        return membership;
    }

    @Scheduled(fixedRate = 20000)
  //  @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkAndProcessExpiredMemberships() {
        Date currentDate = new Date();
        List<AccountEntity> expiredAccounts = userService.findAccountsByExpiredMembership(currentDate);

        for (AccountEntity account : expiredAccounts) {
            if (account.getMembershipExpiryDate().before(currentDate)) {
                account.setMembershipEntity(null);
                account.setStartUpdate(null);
                account.setMembershipExpiryDate(null);
                userService.save(account);
                System.out.println("Account expiration has been processed: " + account.getUsername());
            }
        }
    }
}
