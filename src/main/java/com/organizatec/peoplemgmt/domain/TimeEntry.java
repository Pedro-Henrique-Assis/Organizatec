package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_entries")
public class TimeEntry extends BaseEntity {

    public enum PunchType { IN, OUT }

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime punchTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private PunchType punchType;

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LocalDateTime getPunchTime() { return punchTime; }
    public void setPunchTime(LocalDateTime punchTime) { this.punchTime = punchTime; }

    public PunchType getPunchType() { return punchType; }
    public void setPunchType(PunchType punchType) { this.punchType = punchType; }
}
