package com.organizatec.peoplemgmt.service;

import com.organizatec.peoplemgmt.domain.*;
import com.organizatec.peoplemgmt.repo.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.time.LocalDate;
import java.math.BigDecimal;


import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final TimeEntryRepo timeRepo;
    private final ProjectRepo projectRepo;
    private final EmployeeRoleHistoryRepo roleHistoryRepo;
    private final EmployeeActivityRepo activityRepo;


    public EmployeeService(EmployeeRepo employeeRepo,
                           TimeEntryRepo timeRepo,
                           ProjectRepo projectRepo,
                           EmployeeRoleHistoryRepo roleHistoryRepo,
                           EmployeeActivityRepo activityRepo) {
        this.employeeRepo = employeeRepo;
        this.timeRepo = timeRepo;
        this.projectRepo = projectRepo;
        this.roleHistoryRepo = roleHistoryRepo;
        this.activityRepo = activityRepo;
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

    @Transactional
    public Employee addProject(Long employeeId, Long projectId) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));
        Project prj = projectRepo.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado: " + projectId));
        emp.getProjects().add(prj);
        return employeeRepo.save(emp);
    }

    @Transactional
    public Employee removeProject(Long employeeId, Long projectId) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));
        emp.getProjects().removeIf(p -> p.getId().equals(projectId));
        return employeeRepo.save(emp);
    }

    @Transactional
    public EmployeeRoleHistory registerRoleChange(Long employeeId,
                                                  String newRoleTitle,
                                                  Double newBaseSalary,
                                                  String reason) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));

        // encerra histórico atual (se houver) — regra simples: último sem endDate
        // (Opcional: consultar por order desc, etc. Aqui vamos só registrar novo sem fechar anterior)
        EmployeeRoleHistory hist = new EmployeeRoleHistory();
        hist.setEmployee(emp);
        hist.setRoleTitle(newRoleTitle);
        hist.setBaseSalary(newBaseSalary);
        hist.setStartDate(java.time.LocalDate.now());
        hist.setChangeReason(reason);
        roleHistoryRepo.save(hist);

        // aplica no cadastro atual
        emp.setRoleTitle(newRoleTitle);
        emp.setBaseSalary(BigDecimal.valueOf(newBaseSalary));
        employeeRepo.save(emp);

        return hist;
    }

    // ======== Registrar atividades =========
    @Transactional
    public EmployeeActivity registerActivity(Long employeeId, String title, String description,
                                             LocalDateTime startedAt, LocalDateTime finishedAt) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));
        EmployeeActivity ac = new EmployeeActivity();
        ac.setEmployee(emp);
        ac.setTitle(title);
        ac.setDescription(description);
        ac.setStartedAt(startedAt != null ? startedAt : LocalDateTime.now());
        ac.setFinishedAt(finishedAt);
        return activityRepo.save(ac);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    // Bater ponto
    @Transactional
    public TimeEntry punch(Long employeeId, TimeEntry.PunchType type) {
        Optional<Employee> employeeOpt = employeeRepo.findById(employeeId);
        if (!employeeOpt.isPresent()) {
            throw new IllegalArgumentException("Funcionário não encontrado: " + employeeId);
        }
        TimeEntry te = new TimeEntry();
        te.setEmployee(employeeOpt.get());
        te.setPunchType(type);
        te.setOccurredAt(LocalDateTime.now());
        return timeRepo.save(te);
    }

    @Transactional
    public void updateBaseSalary(Long employeeId, BigDecimal newBaseSalary) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));
        emp.setBaseSalary(newBaseSalary);
        employeeRepo.save(emp);
    }
}