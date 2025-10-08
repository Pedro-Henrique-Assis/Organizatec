package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Long> {}
