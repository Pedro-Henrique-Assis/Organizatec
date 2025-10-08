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

@Controller
@RequestMapping("/visitors")
public class VisitorController {

    private final VisitRepo visitRepo;
    private final EmployeeRepo employeeRepo;

    public VisitorController(VisitRepo visitRepo, EmployeeRepo employeeRepo) {
        this.visitRepo = visitRepo;
        this.employeeRepo = employeeRepo;
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("employees", employeeRepo.findAll(Sort.by("name")));
        return "visitors/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("visit") Visit visit,
                         BindingResult br,
                         Model model) {
        if (visit.getVisitedEmployee() == null || visit.getVisitedEmployee().getId() == null) {
            br.rejectValue("employee", "required", "Selecione o anfitrião.");
        }
        if (visit.getEntryTime() == null) {
            br.rejectValue("entryTime", "required", "Informe data/hora de entrada.");
        }

        if (br.hasErrors()) {
            model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
            model.addAttribute("errorMessage", "Corrija os campos obrigatórios.");
            return "visitors/form";
        }

        visitRepo.save(visit);
        return "redirect:/"; // ajuste se desejar outra página
    }
}