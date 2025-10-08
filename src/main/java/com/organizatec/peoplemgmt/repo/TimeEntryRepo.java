package com.organizatec.peoplemgmt.repo;
import com.organizatec.peoplemgmt.domain.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TimeEntryRepo extends JpaRepository<TimeEntry, Long> {}
