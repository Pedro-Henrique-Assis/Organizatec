package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale;

@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @NotNull
    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true, length = 32)
    private String enrollment;

    @NotBlank
    @Column(name = "role_title", length = 100)
    private String roleTitle;

    @NotNull
    @Column(name = "base_salary", precision = 15, scale = 2)
    private BigDecimal baseSalary;

    @NotNull
    @Column(nullable = false)
    private LocalDate hiredAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "employee_projects",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    // ===== Business Logic =====
    public BigDecimal computeTotalSalary() {
        String role = roleTitle == null ? "" : roleTitle.toLowerCase(Locale.ROOT);
        BigDecimal allowance;
        switch (role) {
            case "manager":
            case "gerente":
                allowance = new BigDecimal("1500.00");
                break;
            case "analyst":
            case "analista":
                allowance = new BigDecimal("600.00");
                break;
            default:
                allowance = BigDecimal.ZERO;
        }
        return baseSalary.add(allowance);
    }

    @Transient
    public double getTotalSalary() {
        double base = getBaseSalary() == null ? 0.0 : getBaseSalary().doubleValue();
        return base + allowanceForRole(this.getRoleTitle());
    }

    private static double allowanceForRole(String role){
        if(role == null) return 0.0;
        String r = role.trim().toLowerCase();
        if ("gerente".equals(r) || "manager".equals(r)) return 1500.0;
        if ("analista".equals(r) || "analyst".equals(r)) return 600.0;
        return 0.0;
    }

    // ===== Getters and Setters =====

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getRoleTitle() { return roleTitle; }

    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }

    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }

    public LocalDate getHiredAt() {
        return hiredAt;
    }

    public void setHiredAt(LocalDate hiredAt) {
        this.hiredAt = hiredAt;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Project> getProjects() { return projects; }
    public void setProjects(Set<Project> projects) { this.projects = projects; }
}