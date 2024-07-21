package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.util.Date;
import java.util.List;

@Repository
public interface ContributionRepository extends JpaRepository<ContributionEntity, Long> {

    @Query("SELECT c FROM ContributionEntity c " +
            "join fetch c.accountEntity " +
            "join fetch c.bookEntity b " +
            "where b.status = :status")
    List<ContributionEntity> findByBookEntity_Status(@Param("status") int status, Pageable pageable);

    int countByBookEntity_Status(int status);
    boolean existsByAccountEntityIdAndBookEntityId(Long accountId, Long bookId);


    @Transactional
    @Modifying
    @Query("UPDATE ContributionEntity c " +
            "SET c.modifiedDate = CURRENT_TIMESTAMP  " +
            "where c.bookEntity.isbn = :isbn")
            void updateModifiedDateByBookEntity_ISBN(@Param("isbn") String isbn);

    @Query("SELECT c.bookEntity.id FROM ContributionEntity c WHERE c.accountEntity.id = :accountId")
    List<Long> findBookIdsByAccountId(@Param("accountId") Long accountId);
}
