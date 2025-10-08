package com.organizatec.peoplemgmt.service;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.domain.EmployeeActivity;
import com.organizatec.peoplemgmt.repo.EmployeeActivityRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeActivityService {

    private final EmployeeActivityRepo activityRepo;
    private final EmployeeRepo employeeRepo;

    public EmployeeActivityService(EmployeeActivityRepo activityRepo, EmployeeRepo employeeRepo) {
        this.activityRepo = activityRepo;
        this.employeeRepo = employeeRepo;
    }

    @Transactional(readOnly = true)
    public List<EmployeeActivity> listByEmployee(Long employeeId) {
        return activityRepo.findByEmployeeIdOrderByStartedAtDesc(employeeId);
    }

    @Transactional
    public EmployeeActivity startActivity(Long employeeId, String title, String description, LocalDateTime startedAt) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));
        EmployeeActivity a = new EmployeeActivity();
        a.setEmployee(emp);
        a.setTitle(title);
        a.setDescription(description);
        a.setStartedAt(startedAt != null ? startedAt : LocalDateTime.now());
        return activityRepo.save(a);
    }

    @Transactional
    public EmployeeActivity finishActivity(Long activityId, LocalDateTime finishedAt) {
        EmployeeActivity a = activityRepo.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Atividade não encontrada: " + activityId));
        a.setFinishedAt(finishedAt != null ? finishedAt : LocalDateTime.now());
        return activityRepo.save(a);
    }

    @Transactional
    public void deleteActivity(Long activityId) {
        activityRepo.deleteById(activityId);
    }
}