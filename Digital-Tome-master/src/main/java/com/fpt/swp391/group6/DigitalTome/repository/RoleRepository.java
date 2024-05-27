package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}

