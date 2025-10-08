package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.repo.VisitRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class ReportController {

    private final VisitRepo visitRepo;

    public ReportController(VisitRepo visitRepo) {
        this.visitRepo = visitRepo;
    }

    @GetMapping("/reports/daily")
    public String daily(Model model) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        model.addAttribute("visits", visitRepo.findByEntryTimeBetween(start, end));
        return "reports/daily-circulation";
    }
}
