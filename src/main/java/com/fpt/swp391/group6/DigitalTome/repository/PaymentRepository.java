package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT u FROM PaymentEntity u WHERE u.accountEntity.id = :id ORDER BY u.createdDate DESC")
    Page<PaymentEntity> transactionHistory(@Param("id") Long id, Pageable pageable);

    @Query("SELECT u FROM PaymentEntity u WHERE u.accountEntity.id = :id AND u.createdDate BETWEEN :startDate AND :endDate ORDER BY u.createdDate DESC")
    Page<PaymentEntity> findPaymentsByAccountIdAndDateRange(@Param("id") Long id, @Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate,  Pageable pageable);

    @Query("SELECT p FROM PaymentEntity p WHERE p.bookEntity.id IN :bookIds AND p.createdDate >= :startDate AND p.createdDate <= :endDate AND p.success = true")
    List<PaymentEntity> findPaymentsByBookIdsAndDateRange(@Param("bookIds") List<Long> bookIds, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    boolean existsByAccountEntityAndBookEntity(AccountEntity user, BookEntity book);
}

