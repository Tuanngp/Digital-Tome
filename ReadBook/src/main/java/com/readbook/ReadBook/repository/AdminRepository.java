package com.readbook.ReadBook.repository;

import com.readbook.ReadBook.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<AccountEntity, Long> {
}
