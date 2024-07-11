package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findByAccountEntity(AccountEntity accountEntity);
    Page<FavoriteEntity> findByAccountEntity(AccountEntity user, Pageable pageable);

    Optional<FavoriteEntity> findByBookEntityIdAndAccountEntity(Long bookId, AccountEntity user);
}
