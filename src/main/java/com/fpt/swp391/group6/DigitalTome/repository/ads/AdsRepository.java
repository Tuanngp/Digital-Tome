package com.fpt.swp391.group6.DigitalTome.repository.ads;

import com.fpt.swp391.group6.DigitalTome.entity.AdsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<AdsEntity, Long> {

}
