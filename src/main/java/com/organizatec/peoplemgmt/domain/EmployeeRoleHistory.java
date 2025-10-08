package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_role_history")
public class EmployeeRoleHistory {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="employee_id")
    private Employee employee;

    @Column(name="role_title", nullable=false, length=100)
    private String roleTitle;

    @Column(name="base_salary", nullable=false)
    private Double baseSalary;

    @Column(name="start_date", nullable=false)
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="change_reason", length=200)
    private String changeReason;

    // GET/SET
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Employee getEmployee() {return employee;}
    public void setEmployee(Employee employee) {this.employee = employee;}
    public String getRoleTitle() {return roleTitle;}
    public void setRoleTitle(String roleTitle) {this.roleTitle = roleTitle;}
    public Double getBaseSalary() {return baseSalary;}
    public void setBaseSalary(Double baseSalary) {this.baseSalary = baseSalary;}
    public LocalDate getStartDate() {return startDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public LocalDate getEndDate() {return endDate;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}
    public String getChangeReason() {return changeReason;}
    public void setChangeReason(String changeReason) {this.changeReason = changeReason;}
}