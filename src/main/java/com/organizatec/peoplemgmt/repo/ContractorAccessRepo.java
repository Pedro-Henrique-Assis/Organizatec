// src/main/java/com/organizatec/peoplemgmt/repo/ContractorAccessRepo.java
package com.organizatec.peoplemgmt.repo;

import com.organizatec.peoplemgmt.domain.ContractorAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractorAccessRepo extends JpaRepository<ContractorAccess, Long> {

    // Conta quantos terceirizados têm o ÚLTIMO evento como IN
    @Query(value =
            "SELECT COUNT(*) " +
                    "FROM contractor_access ca " +
                    "WHERE ca.event_at = (" +
                    "   SELECT MAX(ca2.event_at) " +
                    "   FROM contractor_access ca2 " +
                    "   WHERE ca2.contractor_id = ca.contractor_id" +
                    ") " +
                    "AND ca.type = 'IN'",
            nativeQuery = true)
    long countCurrentlyInside();
}
