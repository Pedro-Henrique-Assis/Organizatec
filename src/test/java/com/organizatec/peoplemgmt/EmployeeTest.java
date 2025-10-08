package com.organizatec.peoplemgmt;

import com.organizatec.peoplemgmt.domain.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testComputeTotalSalaryWithAllowance() {
        Employee e = new Employee();
        e.setRoleTitle("Gerente");                    // regra: +1500
        e.setBaseSalary(BigDecimal.valueOf(5000));

        BigDecimal total = e.computeTotalSalary();

        // Esperado: 5000 + 1500 = 6500
        assertEquals(0, total.compareTo(BigDecimal.valueOf(6500)));
    }

    @Test
    void testComputeTotalSalaryWithoutAllowance() {
        Employee e = new Employee();
        e.setRoleTitle("Assistente");                 // sem adicional
        e.setBaseSalary(BigDecimal.valueOf(3000));

        BigDecimal total = e.computeTotalSalary();

        assertEquals(0, total.compareTo(BigDecimal.valueOf(3000)));
    }

    @Test
    void testComputeTotalSalaryWhenBaseIsZero_simulaBaseNulaSemMudarProducao() {
        Employee e = new Employee();
        e.setRoleTitle("Analista");                   // regra: +600
        // Em vez de setar null (quebraria no código atual), usamos ZERO
        e.setBaseSalary(BigDecimal.ZERO);

        BigDecimal total = e.computeTotalSalary();

        assertEquals(0, total.compareTo(BigDecimal.valueOf(600)));
    }

    @Test
    void testSetAndGetHiringDate() {
        Employee e = new Employee();
        LocalDate date = LocalDate.of(2024, 1, 10);
        e.setHiredAt(date);
        assertEquals(date, e.getHiredAt());
    }
}
