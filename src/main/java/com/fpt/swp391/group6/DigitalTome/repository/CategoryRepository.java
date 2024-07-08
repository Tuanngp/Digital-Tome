package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByNameContainingIgnoreCase(String name);

<<<<<<< HEAD
}
=======
}
>>>>>>> 728ce2091d5a52ed77fa453748e001245b19c9ed
