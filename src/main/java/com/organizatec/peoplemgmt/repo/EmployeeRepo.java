// src/main/java/com/organizatec/peoplemgmt/repo/EmployeeRepo.java
package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    // mantém se já existir:
    boolean existsByCpf(String cpf);

    // novo: compara removendo . e -
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Employee e " +
            "WHERE REPLACE(REPLACE(e.cpf, '.', ''), '-', '') = " +
            "REPLACE(REPLACE(:cpf, '.', ''), '-', '')")
    boolean existsByCpfNormalized(@Param("cpf") String cpf);
}
