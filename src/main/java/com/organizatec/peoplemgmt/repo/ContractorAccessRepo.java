package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.ContractorAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface ContractorAccessRepo extends JpaRepository<ContractorAccess, Long> {
    long countByEntryTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByExitTimeIsNull();
}
