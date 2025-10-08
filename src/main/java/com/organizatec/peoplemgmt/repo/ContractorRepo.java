package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepo extends JpaRepository<Contractor, Long> {
    boolean existsByCpf(String cpf);
}
