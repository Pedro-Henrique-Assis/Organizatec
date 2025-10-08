package com.organizatec.peoplemgmt.web;

import com.organizatec.peoplemgmt.domain.Employee;
import com.organizatec.peoplemgmt.repo.DepartmentRepo;
import com.organizatec.peoplemgmt.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import com.organizatec.peoplemgmt.service.UnderageEmployeeException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
