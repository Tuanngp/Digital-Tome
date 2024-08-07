package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.dto.UserDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.RoleEntity;
import com.fpt.swp391.group6.DigitalTome.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByEmail(String email);

    AccountEntity findByToken(String token);

    AccountEntity findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM AccountEntity u WHERE u.roleEntity.name = :roleName")
    List<AccountEntity> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT u.password FROM AccountEntity u WHERE u.username = :username")
    String findPasswordByUsername(@Param("username") String username);

    @Query("SELECT new com.fpt.swp391.group6.DigitalTome.dto.UserDto(u.id, u.email,u.username, u.address, u.fullname, u.phone, u.avatarPath, u.description, u.point, u.gender, u.dateOfBirth) FROM AccountEntity u WHERE u.username = :username")
    UserDto findByUser(@Param("username") String username);

    @Query("SELECT u FROM AccountEntity u WHERE u.isApproved =false ")
    List<AccountEntity> findAllByNamePublisherNotNull();
    List<AccountEntity> findAllByRoleEntity(RoleEntity role);

    @Query("SELECT a FROM AccountEntity a WHERE a.membershipExpiryDate <= :currentDate")
    List<AccountEntity> findAccountsByExpiredMembership(@Param("currentDate") Date currentDate);

    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.gender = :gender")
    long countByGender(Gender gender);

    @Query("SELECT COUNT(a) FROM AccountEntity a")
    long countTotalUsers();

    AccountEntity findByPhone(String keyword);
}

