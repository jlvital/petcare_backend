package com.petcare.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPetId(Long petId);
    
    List<Report> findByPetIdOrderByLastUpdateDesc(Long petId);
}