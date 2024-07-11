package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT p FROM PaymentEntity p WHERE p.bookEntity.id IN :bookIds AND p.createdDate >= :startDate AND p.createdDate <= :endDate AND p.success = true")
    List<PaymentEntity> findPaymentsByBookIdsAndDateRange(@Param("bookIds") List<Long> bookIds, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

