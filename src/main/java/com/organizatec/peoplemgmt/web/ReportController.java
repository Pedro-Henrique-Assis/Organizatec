package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.domain.Visit;
import com.organizatec.peoplemgmt.repo.DepartmentRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import com.organizatec.peoplemgmt.repo.VisitRepo;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final VisitRepo visitRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public ReportController(VisitRepo visitRepo,
                            EmployeeRepo employeeRepo,
                            DepartmentRepo departmentRepo) {
        this.visitRepo = visitRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @GetMapping("/daily")
    public String daily(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(value = "empId", required = false) Long empId,
            @RequestParam(value = "deptId", required = false) Long deptId,
            Model model) {

        // Intervalo padrão = hoje
        LocalDate today = LocalDate.now();
        if (from == null) from = today;
        if (to == null)   to   = today;

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end   = to.atTime(LocalTime.of(23, 59, 59));

        // Base de dados para montar tabelas e filtros
        List<Visit> visitors = visitRepo.findByEntryTimeBetween(start, end);
        List<Employee> employees = employeeRepo.findAll(Sort.by("name").ascending());

        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("visitors", visitors);
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departmentRepo.findAll(Sort.by("name").ascending()));

        // Cards de resumo
        model.addAttribute("totalVisitors", visitors.size());
        model.addAttribute("totalEmployees", employees.size());

        return "reports/daily";
    }

    // Exportação CSV do relatório (aplica o mesmo intervalo de datas)
    @GetMapping(value = "/daily/export.csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end   = to.atTime(LocalTime.of(23, 59, 59));

        List<Visit> list = visitRepo.findByEntryTimeBetween(start, end);

        StringBuilder sb = new StringBuilder();
        sb.append("Data entrada;Hora entrada;Visitante;Documento;Anfitriao;Departamento;Empresa;Placa;Cracha;Motivo\n");
        for (Visit v : list) {
            String date = v.getEntryTime() != null ? v.getEntryTime().toLocalDate().toString() : "";
            String time = v.getEntryTime() != null ? v.getEntryTime().toLocalTime().toString() : "";
            String host = v.getVisitedEmployee() != null ? v.getVisitedEmployee().getName() : "";
            String dept = (v.getVisitedDepartment() != null) ? v.getVisitedDepartment().getName() : "";
            sb.append(String.join(";",
                            safe(date),
                            safe(time),
                            safe(v.getVisitorName()),
                            safe(v.getDocumentId()),
                            safe(host),
                            safe(dept),
                            safe(v.getCompany()),
                            safe(v.getVehiclePlate()),
                            safe(v.getBadgeCode()),
                            safe(v.getReason())))
                    .append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio-diario.csv\"")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(bytes);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace("\n", " ").replace("\r", " ");
    }
}