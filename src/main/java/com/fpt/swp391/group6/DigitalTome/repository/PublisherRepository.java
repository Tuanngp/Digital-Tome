package com.fpt.swp391.group6.DigitalTome.repository;


import com.fpt.swp391.group6.DigitalTome.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    boolean existsByUserId(Long userId);

    @Query("SELECT p FROM PublisherEntity p JOIN FETCH p.user")
    List<PublisherEntity> findAllWithUserEager();
}


