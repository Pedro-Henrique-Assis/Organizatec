package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.domain.Contractor;
import com.organizatec.peoplemgmt.domain.ContractorAccess;
import com.organizatec.peoplemgmt.domain.Department;
import com.organizatec.peoplemgmt.repo.DepartmentRepo;
import com.organizatec.peoplemgmt.repo.EmployeeRepo;
import com.organizatec.peoplemgmt.service.ContractorService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequestMapping("/contractors")
public class ContractorController {

    private final ContractorService contractorService;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public ContractorController(ContractorService contractorService,
                                EmployeeRepo employeeRepo,
                                DepartmentRepo departmentRepo) {
        this.contractorService = contractorService;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @GetMapping
    public String list(Model model) {
        // Carrega a lista ordenada por nome (ajuste o service se precisar)
        List<Contractor> contractors = contractorService.findAll();
        model.addAttribute("contractors", contractors);
        return "contractors/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("contractor", new Contractor());
        model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
        model.addAttribute("departments", departmentRepo.findAll(Sort.by("name").ascending()));
        return "contractors/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("contractor") Contractor contractor,
                         BindingResult br,
                         @RequestParam(value = "departmentIds", required = false) List<Long> departmentIds,
                         Model model) {

        // Validações básicas
        if (contractor.getContractStart() == null) {
            br.rejectValue("contractStart", "required", "Informe o início do contrato.");
        }
        if (contractor.getContractEnd() != null &&
                contractor.getContractStart() != null &&
                contractor.getContractEnd().isBefore(contractor.getContractStart())) {
            br.rejectValue("contractEnd", "invalid", "Término não pode ser antes do início.");
        }

        java.time.LocalDate today = java.time.LocalDate.now();
        if (contractor.getContractEnd() != null &&
                contractor.getContractEnd().isBefore(today)) {
            br.rejectValue("contractEnd","past","Término do contrato não pode ser anterior a hoje.");
        }

        // Recarrega combos em caso de erro
        if (br.hasErrors()) {
            model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
            model.addAttribute("departments", departmentRepo.findAll(Sort.by("name").ascending()));
            return "contractors/form";
        }

        // Monta departamentos selecionados
        LinkedHashSet<Department> deps = new LinkedHashSet<Department>();
        if (departmentIds != null) {
            for (Long id : departmentIds) {
                departmentRepo.findById(id).ifPresent(deps::add);
            }
        }
        contractor.setDepartments(deps);

        try {
            contractorService.saveNew(contractor);
            return "redirect:/contractors";
        } catch (DataIntegrityViolationException ex) {
            // Ex.: CPF único violado
            br.rejectValue("cpf", "duplicate", "CPF já cadastrado.");
        } catch (IllegalArgumentException ex) {
            br.reject("error", ex.getMessage());
        }

        // Volta ao form com mensagens
        model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
        model.addAttribute("departments", departmentRepo.findAll(Sort.by("name").ascending()));
        return "contractors/form";
    }

    @PostMapping("/{id}/punch")
    public String punch(@PathVariable("id") Long id,
                        @RequestParam("type") String type) {
        ContractorAccess.PunchType t =
                "IN".equalsIgnoreCase(type) ? ContractorAccess.PunchType.IN : ContractorAccess.PunchType.OUT;
        contractorService.punch(id, t);
        return "redirect:/contractors";
    }

    @PostMapping("/{id}/renew")
    public String renew(@PathVariable("id") Long id,
                        @RequestParam("newEnd") String newEnd) {
        LocalDate d = (newEnd == null || newEnd.trim().isEmpty()) ? null : LocalDate.parse(newEnd);
        contractorService.renew(id, d);
        return "redirect:/contractors";
    }
}