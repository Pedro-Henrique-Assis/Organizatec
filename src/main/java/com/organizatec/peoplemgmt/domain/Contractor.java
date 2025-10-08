package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "contractors",
        uniqueConstraints = @UniqueConstraint(columnNames = "cpf"))
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 150)
    private String name;

    @NotBlank @Size(max = 14)         // com máscara
    private String cpf;

    @NotBlank @Size(max = 100)
    @Column(name = "role_title")
    private String roleTitle;          // função

    @NotBlank @Size(max = 120)
    @Column(name = "vendor_company")
    private String vendorCompany;      // empresa prestadora

    @NotNull
    @Column(name = "contract_start")
    private LocalDate contractStart;

    @Column(name = "contract_end")
    private LocalDate contractEnd;     // pode ser nulo (contrato em aberto/renovações)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internal_resp_id")
    private Employee internalResponsible; // responsável interno

    // Associação a departamentos específicos (N:N)
    @ManyToMany
    @JoinTable(name = "contractor_departments",
            joinColumns = @JoinColumn(name = "contractor_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    private Set<Department> departments = new LinkedHashSet<>();

    @Column(name = "badge_code", length = 20)
    private String badgeCode;

    @Column(name = "active_flag")
    private Boolean active = Boolean.TRUE;

    // --- Getters/Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRoleTitle() { return roleTitle; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }

    public String getVendorCompany() { return vendorCompany; }
    public void setVendorCompany(String vendorCompany) { this.vendorCompany = vendorCompany; }

    public LocalDate getContractStart() { return contractStart; }
    public void setContractStart(LocalDate contractStart) { this.contractStart = contractStart; }

    public LocalDate getContractEnd() { return contractEnd; }
    public void setContractEnd(LocalDate contractEnd) { this.contractEnd = contractEnd; }

    public Employee getInternalResponsible() { return internalResponsible; }
    public void setInternalResponsible(Employee internalResponsible) { this.internalResponsible = internalResponsible; }

    public Set<Department> getDepartments() { return departments; }
    public void setDepartments(Set<Department> departments) { this.departments = departments; }

    public String getBadgeCode() { return badgeCode; }
    public void setBadgeCode(String badgeCode) { this.badgeCode = badgeCode; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}