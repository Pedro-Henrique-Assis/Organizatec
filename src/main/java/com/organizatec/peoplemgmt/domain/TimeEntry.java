package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="time_entries")
public class TimeEntry {
    public enum PunchType { IN, OUT }

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="employee_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name="punch_type", nullable=false, length=10)
    private PunchType punchType;

    @Column(name="occurred_at", nullable=false)
    private LocalDateTime occurredAt;

    // GET/SET
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Employee getEmployee() {return employee;}
    public void setEmployee(Employee employee) {this.employee = employee;}
    public PunchType getPunchType() {return punchType;}
    public void setPunchType(PunchType punchType) {this.punchType = punchType;}
    public LocalDateTime getOccurredAt() {return occurredAt;}
    public void setOccurredAt(LocalDateTime occurredAt) {this.occurredAt = occurredAt;}
}