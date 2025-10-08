package com.organizatec.peoplemgmt;

import com.organizatec.peoplemgmt.domain.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeServiceTest {

    @Test
    void totalSalaryAddsAllowance() {
        Employee e = new Employee();
        e.setName("Ana");
        e.setCpf("000.000.000-00");
        e.setBirthDate(LocalDate.of(1990, 1, 1));
        e.setRoleTitle("Analyst");
        e.setBaseSalary(new BigDecimal("5000.00"));
        e.setHiredAt(LocalDate.now());

        BigDecimal total = e.computeTotalSalary();

        assertEquals(new BigDecimal("5600.00"), total);
    }
}
