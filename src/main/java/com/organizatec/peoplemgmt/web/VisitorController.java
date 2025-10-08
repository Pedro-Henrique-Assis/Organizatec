// src/main/java/com/organizatec/peoplemgmt/web/VisitorController.java
package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.domain.Visit;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import com.organizatec.peoplemgmt.repo.VisitRepo;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Controlador para o registro de novos visitantes.
 */
@Controller
@RequestMapping("/visitors")
public class VisitorController {

    private final VisitRepo visitRepo;
    private final EmployeeRepo employeeRepo;

    public VisitorController(VisitRepo visitRepo, EmployeeRepo employeeRepo) {
        this.visitRepo = visitRepo;
        this.employeeRepo = employeeRepo;
    }

    /**
     * Exibe o formulário para registrar um novo visitante.
     *
     * @param model O Model para adicionar a lista de funcionários (anfitriões) à view.
     * @return O nome da view "visitors/form".
     */
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
        return "visitors/form";
    }

    /**
     * Processa o envio do formulário de registro de visitante.
     *
     * @param visit O objeto Visit com os dados do formulário.
     * @param br O resultado da validação.
     * @param entryDate A data de entrada (string).
     * @param entryTimeClock A hora de entrada (string).
     * @param model O Model para o caso de erro.
     * @return Redireciona para a página inicial em caso de sucesso, ou volta ao formulário em caso de erro.
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("visit") Visit visit,
                         BindingResult br,
                         @RequestParam(name = "entryDate", required = false) String entryDate,
                         @RequestParam(name = "entryTimeClock", required = false) String entryTimeClock,
                         Model model) {

        if (visit.getVisitedEmployee() == null || visit.getVisitedEmployee().getId() == null) {
            br.rejectValue("visitedEmployee", "required", "Selecione o anfitrião.");
        }

        if (isBlank(entryDate) || isBlank(entryTimeClock)) {
            br.rejectValue("entryTime", "required", "Informe data/hora de entrada.");
        } else {
            try {
                LocalDate d = LocalDate.parse(entryDate);       // yyyy-MM-dd
                LocalTime t = LocalTime.parse(entryTimeClock);  // HH:mm
                visit.setEntryTime(LocalDateTime.of(d, t));
            } catch (Exception ex) {
                br.rejectValue("entryTime", "invalid", "Data/hora de entrada inválida.");
            }
        }

        if (br.hasErrors()) {
            model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
            model.addAttribute("errorMessage", "Corrija os campos obrigatórios.");
            return "visitors/form";
        }

        visitRepo.save(visit);
        return "redirect:/";
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}