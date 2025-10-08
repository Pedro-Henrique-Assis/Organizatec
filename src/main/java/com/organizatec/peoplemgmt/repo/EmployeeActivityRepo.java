package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.EmployeeActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeActivityRepo extends JpaRepository<EmployeeActivity, Long> {
    List<EmployeeActivity> findByEmployeeIdOrderByStartedAtDesc(Long employeeId);
}
