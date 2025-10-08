package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.EmployeeRoleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleHistoryRepo extends JpaRepository<EmployeeRoleHistory, Long> {}
