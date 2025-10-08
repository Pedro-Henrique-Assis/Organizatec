package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepo extends JpaRepository<Visit, Long> {
    List<Visit> findByEntryTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByEntryTimeBetween(LocalDateTime start, LocalDateTime end); // <—
}
