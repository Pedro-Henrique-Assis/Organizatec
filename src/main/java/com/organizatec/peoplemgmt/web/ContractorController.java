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

/**
 * Controlador Spring MVC para gerenciar as requisições web relacionadas a terceirizados.
 */
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

    /**
     * Exibe a lista de todos os terceirizados cadastrados.
     * @param model O Model para adicionar atributos à view.
     * @return O nome da view "contractors/list".
     */
    @GetMapping
    public String list(Model model) {
        // Carrega a lista ordenada por nome (ajuste o service se precisar)
        List<Contractor> contractors = contractorService.findAll();
        model.addAttribute("contractors", contractors);
        return "contractors/list";
    }

    /**
     * Exibe o formulário para cadastrar um novo terceirizado.
     * @param model O Model para adicionar atributos à view.
     * @return O nome da view "contractors/form".
     */
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("contractor", new Contractor());
        model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
        model.addAttribute("departments", departmentRepo.findAll(Sort.by("name").ascending()));
        return "contractors/form";
    }

    /**
     * Processa a submissão do formulário para criar um novo terceirizado.
     *
     * @param contractor O objeto Contractor preenchido com os dados do formulário.
     * @param br O resultado do binding para validação.
     * @param departmentIds A lista de IDs dos departamentos selecionados.
     * @param model O Model para o caso de erro, para devolver os dados à view.
     * @return Redireciona para a lista em caso de sucesso, ou retorna ao formulário em caso de erro.
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("contractor") Contractor contractor,
                         BindingResult br,
                         @RequestParam(value = "departmentIds", required = false) List<Long> departmentIds,
                         Model model) {

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

        if (br.hasErrors()) {
            model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
            model.addAttribute("departments", departmentRepo.findAll(Sort.by("name").ascending()));
            return "contractors/form";
        }

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

        model.addAttribute("employees", employeeRepo.findAll(Sort.by("name").ascending()));
        model.addAttribute("departments", departmentRepo.findAll(Sort.by("name").ascending()));
        return "contractors/form";
    }

    /**
     * Registra uma batida de ponto (entrada/saída) para um terceirizado.
     *
     * @param id O ID do terceirizado.
     * @param type O tipo de batida ("IN" ou "OUT").
     * @return Redireciona para a lista de terceirizados.
     */
    @PostMapping("/{id}/punch")
    public String punch(@PathVariable("id") Long id,
                        @RequestParam("type") String type) {
        ContractorAccess.PunchType t =
                "IN".equalsIgnoreCase(type) ? ContractorAccess.PunchType.IN : ContractorAccess.PunchType.OUT;
        contractorService.punch(id, t);
        return "redirect:/contractors";
    }

    /**
     * Renova o contrato de um terceirizado, atualizando a data de término.
     *
     * @param id O ID do terceirizado.
     * @param newEnd A nova data de término do contrato no formato "yyyy-MM-dd".
     * @return Redireciona para a lista de terceirizados.
     */
    @PostMapping("/{id}/renew")
    public String renew(@PathVariable("id") Long id,
                        @RequestParam("newEnd") String newEnd) {
        LocalDate d = (newEnd == null || newEnd.trim().isEmpty()) ? null : LocalDate.parse(newEnd);
        contractorService.renew(id, d);
        return "redirect:/contractors";
    }
}