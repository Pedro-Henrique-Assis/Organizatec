package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contractors")
public class Contractor extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, length = 100)
    private String functionTitle;

    @Column(nullable = false, length = 120)
    private String providerCompany;

    @Column(nullable = false)
    private LocalDate contractStart;

    private LocalDate contractEnd;

    @ManyToOne
    @JoinColumn(name = "internal_resp_id")
    private Employee internalResponsible;

    @ManyToMany
    @JoinTable(
            name = "contractor_departments",
            joinColumns = @JoinColumn(name = "contractor_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> departments = new HashSet<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getFunctionTitle() { return functionTitle; }
    public void setFunctionTitle(String functionTitle) { this.functionTitle = functionTitle; }

    public String getProviderCompany() { return providerCompany; }
    public void setProviderCompany(String providerCompany) { this.providerCompany = providerCompany; }

    public LocalDate getContractStart() { return contractStart; }
    public void setContractStart(LocalDate contractStart) { this.contractStart = contractStart; }

    public LocalDate getContractEnd() { return contractEnd; }
    public void setContractEnd(LocalDate contractEnd) { this.contractEnd = contractEnd; }

    public Employee getInternalResponsible() { return internalResponsible; }
    public void setInternalResponsible(Employee internalResponsible) { this.internalResponsible = internalResponsible; }

    public Set<Department> getDepartments() { return departments; }
    public void setDepartments(Set<Department> departments) { this.departments = departments; }
}
