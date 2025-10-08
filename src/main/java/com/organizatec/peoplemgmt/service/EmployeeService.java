package com.organizatec.peoplemgmt.service;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import com.organizatec.peoplemgmt.repo.TimeEntryRepo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import com.organizatec.peoplemgmt.domain.TimeEntry;
import java.time.LocalDateTime;
import java.util.Optional;
import java.time.LocalDate;


import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final TimeEntryRepo timeRepo; // mantido para futuras operações de ponto

    public EmployeeService(EmployeeRepo employeeRepo, TimeEntryRepo timeRepo) {
        this.employeeRepo = employeeRepo;
        this.timeRepo = timeRepo;
    }

    public static String normalizeCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }

    public String generateEnrollment() {
        return "EMP-" + Year.now().getValue() + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    @Transactional
    public Employee saveNew(Employee e) {
        // valida idade mínima
        LocalDate birth = e.getBirthDate();
        if (birth == null || birth.isAfter(LocalDate.now().minusYears(18))) {
            throw new UnderageEmployeeException("Funcionários com menos de 18 anos não podem ser cadastrados no sistema.");
        }

        String rawCpf = e.getCpf();
        if (rawCpf != null && employeeRepo.existsByCpfNormalized(rawCpf)) {
            throw new org.springframework.dao.DuplicateKeyException("CPF já cadastrado: " + rawCpf);
        }
        if (e.getEnrollment() == null || e.getEnrollment().trim().isEmpty()) {
            e.setEnrollment(generateEnrollment());
        }
        try {
            return employeeRepo.save(e);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new org.springframework.dao.DuplicateKeyException("CPF já cadastrado: " + rawCpf, ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    @Transactional
    public void punch(Long employeeId, TimeEntry.PunchType type) {
        Optional<Employee> employeeOpt = employeeRepo.findById(employeeId);
        if (!employeeOpt.isPresent()) {
            throw new IllegalArgumentException("Funcionário não encontrado: ID " + employeeId);
        }

        Employee employee = employeeOpt.get();

        TimeEntry entry = new TimeEntry();
        entry.setEmployee(employee);
        entry.setPunchType(type);
        entry.setPunchTime(LocalDateTime.now());

        timeRepo.save(entry);
    }
}