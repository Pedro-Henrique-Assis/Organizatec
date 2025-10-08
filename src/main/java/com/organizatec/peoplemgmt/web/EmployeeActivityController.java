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

/**
 * Controlador para gerenciar as atividades de um funcionário específico.
 * Mapeado para a rota aninhada "/employees/{id}/activities".
 */
@Controller
@RequestMapping("/employees/{id}/activities")
public class EmployeeActivityController {

    private final EmployeeActivityRepo activityRepo;
    private final EmployeeRepo employeeRepo;

    public EmployeeActivityController(EmployeeActivityRepo activityRepo, EmployeeRepo employeeRepo) {
        this.activityRepo = activityRepo;
        this.employeeRepo = employeeRepo;
    }

    /**
     * Lista todas as atividades de um funcionário.
     *
     * @param employeeId O ID do funcionário (da URL).
     * @param model O Model para adicionar dados à view.
     * @return O nome da view "employees/activities".
     * @throws IllegalArgumentException se o funcionário não for encontrado.
     */
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

    /**
     * Cria uma nova atividade para o funcionário.
     *
     * @param employeeId O ID do funcionário (da URL).
     * @param title O título da atividade.
     * @param description A descrição da atividade (opcional).
     * @param startedAt A data/hora de início (opcional, usa o momento atual se nulo).
     * @return Redireciona para a lista de atividades do funcionário.
     */
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