// src/main/java/com/organizatec/peoplemgmt/service/ContractorService.java
package com.organizatec.peoplemgmt.service;

import com.organizatec.peoplemgmt.domain.Contractor;
import com.organizatec.peoplemgmt.domain.ContractorAccess;
import com.organizatec.peoplemgmt.repo.ContractorAccessRepo;
import com.organizatec.peoplemgmt.repo.ContractorRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço que encapsula a lógica de negócio para a gestão de terceirizados.
 */
@Service
public class ContractorService {

    private final ContractorRepo contractorRepo;
    private final ContractorAccessRepo accessRepo;

    public ContractorService(ContractorRepo contractorRepo,
                             ContractorAccessRepo contractorAccessRepo) {
        this.contractorRepo = contractorRepo;
        this.accessRepo = contractorAccessRepo;
    }

    /**
     * Registra um evento de acesso (entrada ou saída) para um terceirizado.
     *
     * @param contractorId O ID do terceirizado.
     * @param type O tipo de acesso (IN ou OUT).
     * @throws IllegalArgumentException se o terceirizado não for encontrado.
     */
    @Transactional
    public void punch(Long contractorId, ContractorAccess.PunchType type) {
        Contractor contractor = contractorRepo.findById(contractorId)
                .orElseThrow(() -> new IllegalArgumentException("Terceirizado não encontrado"));

        ContractorAccess access = new ContractorAccess();
        access.setContractor(contractor);
        access.setType(type);
        access.setEventAt(LocalDateTime.now());
        accessRepo.save(access);
    }

    public List<Contractor> findAll() {
        return contractorRepo.findAll();
    }

    @Transactional
    public void saveNew(Contractor contractor) {
        contractorRepo.save(contractor);
    }

    @Transactional
    public void renew(Long contractorId, java.time.LocalDate newEnd) {
        Contractor contractor = contractorRepo.findById(contractorId)
                .orElseThrow(() -> new IllegalArgumentException("Terceirizado não encontrado"));
        contractor.setContractEnd(newEnd);
        contractorRepo.save(contractor);
    }
}