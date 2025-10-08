// src/main/java/com/organizatec/peoplemgmt/web/EmployeeActivityController.java
package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.domain.EmployeeActivity;
import com.organizatec.peoplemgmt.repo.EmployeeActivityRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/employees/{id}/activities")
public class EmployeeActivityController {

    private final EmployeeActivityRepo activityRepo;
    private final EmployeeRepo employeeRepo;

    public EmployeeActivityController(EmployeeActivityRepo activityRepo, EmployeeRepo employeeRepo) {
        this.activityRepo = activityRepo;
        this.employeeRepo = employeeRepo;
    }

    // GET /employees/{id}/activities  -> lista atividades do funcionário
    @GetMapping
    public String list(@PathVariable("id") Long employeeId, Model model) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));

        List<EmployeeActivity> activities =
                activityRepo.findByEmployeeIdOrderByStartedAtDesc(employeeId);

        model.addAttribute("employee", emp);
        model.addAttribute("activities", activities);
        return "employees/activities";
    }

    // POST /employees/{id}/activities -> cria uma nova atividade
    @PostMapping
    public String create(@PathVariable("id") Long employeeId,
                         @RequestParam("title") String title,
                         @RequestParam(value = "description", required = false) String description,
                         @RequestParam(value = "startedAt", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startedAt) {

        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }

        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: " + employeeId));

        EmployeeActivity activity = new EmployeeActivity();
        activity.setEmployee(emp);
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setStartedAt(startedAt);

        activityRepo.save(activity);
        return "redirect:/employees/" + employeeId + "/activities";
    }
}