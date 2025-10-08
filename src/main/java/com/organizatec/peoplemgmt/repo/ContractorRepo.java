package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para a entidade {@link Contractor}.
 *
 * Fornece métodos de CRUD (Create, Read, Update, Delete) para a gestão
 * de terceirizados sem a necessidade de implementação manual.
 */
public interface ContractorRepo extends JpaRepository<Contractor, Long> {
    boolean existsByCpf(String cpf);
}
