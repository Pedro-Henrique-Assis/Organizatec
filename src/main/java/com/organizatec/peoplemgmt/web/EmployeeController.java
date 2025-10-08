package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.repo.DepartmentRepo;
import com.organizatec.peoplemgmt.service.EmployeeService;

import jakarta.validation.Valid;
import com.organizatec.peoplemgmt.domain.TimeEntry;
import org.springframework.web.bind.annotation.RequestParam;
import com.organizatec.peoplemgmt.service.UnderageEmployeeException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador Spring MVC para gerenciar as requisições web relacionadas a funcionários.
 *
 * Mapeia as URLs sob o caminho "/employees" para as operações de listar,
 * criar e interagir com os registros de funcionários.
 */
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;     // (Erro 1 resolvido)
    private final DepartmentRepo departmentRepo;       // (Erro 3 resolvido)

    public EmployeeController(EmployeeService employeeService,
                              DepartmentRepo departmentRepo) {
        this.employeeService = employeeService;
        this.departmentRepo = departmentRepo;
    }

    /**
     * Manipula requisições GET para "/employees" e exibe a lista de todos os funcionários.
     *
     * @param model O objeto Model para adicionar atributos que serão usados na view.
     * @return O nome da view Thymeleaf "employees/list" para ser renderizada.
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepo.findAll());
        return "employees/form";
    }

    /**
     * Manipula requisições POST para "/employees/{id}/punch" para registrar uma batida de ponto.
     *
     * @return Uma string de redirecionamento para a lista de funcionários.
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("employee") Employee employee,
                         BindingResult br,
                         Model model) {
        try {
            employeeService.saveNew(employee);
            return "redirect:/employees";
        } catch (org.springframework.dao.DuplicateKeyException ex) {
            br.rejectValue("cpf", "duplicate", "Já existe um funcionário com este CPF.");
            model.addAttribute("departments", departmentRepo.findAll());
            model.addAttribute("dupCpf", employee.getCpf());
            model.addAttribute("errorMessage", ex.getMessage());
            return "employees/form";
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            br.rejectValue("cpf", "duplicate", "Já existe um funcionário com este CPF.");
            model.addAttribute("departments", departmentRepo.findAll());
            model.addAttribute("dupCpf", employee.getCpf());
            model.addAttribute("errorMessage", "Não foi possível salvar: violação de integridade.");
            return "employees/form";
        } catch (UnderageEmployeeException ex) {
            br.rejectValue("birthDate", "underage", "Funcionários com menos de 18 anos não podem ser cadastrados.");
            model.addAttribute("departments", departmentRepo.findAll());
            model.addAttribute("errorMessage", ex.getMessage()); // SweetAlert no form.html
            return "employees/form";
        }
    }

    @PostMapping("/{id}/punch")
    public String punch(@PathVariable("id") Long id,
                        @RequestParam("type") String type) {
        TimeEntry.PunchType t = "IN".equalsIgnoreCase(type) ? TimeEntry.PunchType.IN : TimeEntry.PunchType.OUT;
        employeeService.punch(id, t);
        return "redirect:/employees";
    }

    @PostMapping("/{id}/projects/{projectId}/add")
    public String addProject(@PathVariable Long id, @PathVariable Long projectId){
        employeeService.addProject(id, projectId);
        return "redirect:/employees";
    }

    @PostMapping("/{id}/roles/change")
    public String changeRole(@PathVariable Long id,
                             @RequestParam("roleTitle") String roleTitle,
                             @RequestParam("baseSalary") Double baseSalary,
                             @RequestParam(value="reason", required=false) String reason){
        employeeService.registerRoleChange(id, roleTitle, baseSalary, reason);
        return "redirect:/employees";
    }
}
