package com.organizatec.peoplemgmt.repo;
import com.organizatec.peoplemgmt.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DepartmentRepo extends JpaRepository<Department, Long> {}
