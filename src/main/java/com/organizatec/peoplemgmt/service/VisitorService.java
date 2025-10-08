package com.organizatec.peoplemgmt.service;

import com.organizatec.peoplemgmt.domain.Department;
import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.domain.Visit;
import com.organizatec.peoplemgmt.repo.DepartmentRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import com.organizatec.peoplemgmt.repo.VisitRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class VisitorService {

    private final VisitRepo visitRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public VisitorService(VisitRepo visitRepo,
                          EmployeeRepo employeeRepo,
                          DepartmentRepo departmentRepo) {
        this.visitRepo = visitRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    /**
     * Registra a entrada de uma visita, vinculando as relações por ID.
     * @param visit objeto vindo do form (visitorName, documentId, company, reason,
     *              vehiclePlate, entryTime já preenchidos)
     * @param visitedEmployeeId id do anfitrião (obrigatório)
     * @param visitedDepartmentId id do departamento (opcional, pode ser null)
     */
    @Transactional
    public Visit register(Visit visit, Long visitedEmployeeId, Long visitedDepartmentId) {

        // Anfitrião (obrigatório)
        if (visitedEmployeeId == null) {
            throw new IllegalArgumentException("Selecione o anfitrião.");
        }
        Optional<Employee> empOpt = employeeRepo.findById(visitedEmployeeId);
        if (!empOpt.isPresent()) {
            throw new IllegalArgumentException("Funcionário não encontrado: " + visitedEmployeeId);
        }
        visit.setVisitedEmployee(empOpt.get());

        // Departamento (opcional)
        if (visitedDepartmentId != null) {
            Optional<Department> deptOpt = departmentRepo.findById(visitedDepartmentId);
            visit.setVisitedDepartment(deptOpt.isPresent() ? deptOpt.get() : null);
        } else {
            visit.setVisitedDepartment(null);
        }

        // Normalizações leves
        if (visit.getVehiclePlate() != null) {
            visit.setVehiclePlate(
                    visit.getVehiclePlate().toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9-]", "")
            );
        }

        // Gera badge se vier vazio
        if (visit.getBadgeCode() == null || visit.getBadgeCode().trim().isEmpty()) {
            visit.setBadgeCode("V-" + UUID.randomUUID().toString()
                    .substring(0, 6).toUpperCase(Locale.ROOT));
        }

        return visitRepo.save(visit);
    }

    /** Registra a saída (fecha a visita). */
    @Transactional
    public Visit registerExit(Long visitId) {
        Visit visit = visitRepo.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visita não encontrada: " + visitId));
        visit.setExitTime(java.time.LocalDateTime.now());
        return visitRepo.save(visit);
    }
}