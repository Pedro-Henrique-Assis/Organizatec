package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.repo.ContractorAccessRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import com.organizatec.peoplemgmt.repo.VisitRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Controlador para a página inicial da aplicação.
 *
 * Responsável por carregar dados do dashboard, como contadores de funcionários,
 * visitantes e terceirizados.
 */
@Controller
public class HomeController {

    private final EmployeeRepo employeeRepo;
    private final VisitRepo visitRepo;
    private final ContractorAccessRepo contractorAccessRepo;

    public HomeController(EmployeeRepo employeeRepo,
                          VisitRepo visitRepo,
                          ContractorAccessRepo contractorAccessRepo) {
        this.employeeRepo = employeeRepo;
        this.visitRepo = visitRepo;
        this.contractorAccessRepo = contractorAccessRepo;
    }

    /**
     * Exibe a página inicial (dashboard).
     *
     * @param model O Model para adicionar atributos à view.
     * @return O nome da view "index".
     */
    @GetMapping("/")
    public String index(Model model) {
        long employeesCount = employeeRepo.count();

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        long visitorsTodayCount = visitRepo.countByEntryTimeBetween(start, end);
        long contractorsInside = contractorAccessRepo.countCurrentlyInside();

        model.addAttribute("contractorsInside", contractorsInside);
        model.addAttribute("employeesCount", employeesCount);
        model.addAttribute("visitorsTodayCount", visitorsTodayCount);

        return "index";
    }
}