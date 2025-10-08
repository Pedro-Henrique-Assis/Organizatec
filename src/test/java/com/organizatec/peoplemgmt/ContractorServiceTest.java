package com.organizatec.peoplemgmt;

import com.organizatec.peoplemgmt.domain.Contractor;
import com.organizatec.peoplemgmt.domain.ContractorAccess;
import com.organizatec.peoplemgmt.repo.ContractorAccessRepo;
import com.organizatec.peoplemgmt.repo.ContractorRepo;
import com.organizatec.peoplemgmt.service.ContractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractorServiceTest {

    private ContractorRepo contractorRepo;
    private ContractorAccessRepo accessRepo;
    private ContractorService contractorService;

    @BeforeEach
    void setup() {
        contractorRepo = Mockito.mock(ContractorRepo.class);
        accessRepo = Mockito.mock(ContractorAccessRepo.class);
        contractorService = new ContractorService(contractorRepo, accessRepo);
    }

    @Test
    void testRenewContract() {
        Contractor c = new Contractor();
        c.setId(1L);
        c.setContractEnd(LocalDate.of(2025, 5, 1));

        when(contractorRepo.findById(1L)).thenReturn(Optional.of(c));
        contractorService.renew(1L, LocalDate.of(2026, 5, 1));

        assertEquals(LocalDate.of(2026, 5, 1), c.getContractEnd());
        verify(contractorRepo, times(1)).save(c);
    }

    @Test
    void testPunchInCreatesNewAccess() {
        Contractor c = new Contractor();
        c.setId(1L);

        when(contractorRepo.findById(1L)).thenReturn(Optional.of(c));
        contractorService.punch(1L, ContractorAccess.PunchType.IN);

        verify(accessRepo, times(1)).save(any(ContractorAccess.class));
    }
}