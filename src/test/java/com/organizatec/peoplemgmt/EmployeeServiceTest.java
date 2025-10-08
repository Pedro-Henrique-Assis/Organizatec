package com.organizatec.peoplemgmt;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.domain.TimeEntry;
import com.organizatec.peoplemgmt.repo.DepartmentRepo;
import com.organizatec.peoplemgmt.repo.EmployeeActivityRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import com.organizatec.peoplemgmt.repo.ProjectRepo;
import com.organizatec.peoplemgmt.repo.TimeEntryRepo;
import com.organizatec.peoplemgmt.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepo employeeRepo;
    @Mock private TimeEntryRepo timeEntryRepo;

    // Estes três mocks existem porque o seu EmployeeService espera 5 args no construtor.
    // Se algum desses repositórios NÃO existir no seu projeto, remova o @Mock correspondente
    // e ajuste o construtor do serviço conforme sua assinatura real.
    @Mock private EmployeeActivityRepo employeeActivityRepo;
    @Mock private ProjectRepo projectRepo;
    @Mock private DepartmentRepo departmentRepo;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee mockEmployee;

    @BeforeEach
    void setup() {
        mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setBaseSalary(BigDecimal.valueOf(1000));
    }

    @Test
    void updateBaseSalary_deveAtualizarESalvar() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(mockEmployee));

        BigDecimal novoSalario = BigDecimal.valueOf(2500);
        employeeService.updateBaseSalary(1L, novoSalario);

        // capturar e verificar que salvou com o valor novo
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepo).save(captor.capture());
        assertEquals(0, captor.getValue().getBaseSalary().compareTo(novoSalario));
    }
}