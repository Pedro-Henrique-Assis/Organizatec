package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_role_history")
public class EmployeeRoleHistory extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false, length = 100)
    private String roleTitle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public String getRoleTitle() { return roleTitle; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
