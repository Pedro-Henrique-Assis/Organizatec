package com.organizatec.peoplemgmt;

import com.organizatec.peoplemgmt.domain.Contractor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractorTest {

    @Test
    void testValidContractPeriod() {
        Contractor c = new Contractor();
        c.setContractStart(LocalDate.of(2025, 1, 1));
        c.setContractEnd(LocalDate.of(2025, 12, 31));
        assertTrue(c.getContractEnd().isAfter(c.getContractStart()));
    }

    @Test
    void testInvalidContractPeriod() {
        Contractor c = new Contractor();
        c.setContractStart(LocalDate.of(2025, 5, 1));
        c.setContractEnd(LocalDate.of(2025, 4, 30));
        assertTrue(c.getContractEnd().isBefore(c.getContractStart()));
    }
}