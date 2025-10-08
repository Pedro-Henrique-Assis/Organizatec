package com.organizatec.peoplemgmt.service;

import com.organizatec.peoplemgmt.domain.Department;
import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.domain.Visit;
import com.organizatec.peoplemgmt.domain.Visitor;
import com.organizatec.peoplemgmt.repo.VisitRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VisitorService {

    private final VisitRepo visitRepo;

    public VisitorService(VisitRepo visitRepo) {
        this.visitRepo = visitRepo;
    }

    public Visit registerEntry(Visitor v, Department dept, Employee emp) {
        Visit visit = new Visit();
        visit.setVisitorName(v.getName());
        visit.setVisitedDepartment(dept.getName());
        visit.setVisitedEmployee(emp);
        visit.setBadgeCode("V-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        return visitRepo.save(visit);
    }

    public Visit registerExit(Long visitId) {
        Visit visit = visitRepo.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + visitId));
        visit.setExitTime(LocalDateTime.now());
        return visitRepo.save(visit);
    }
}
