package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name="employee_activities")
public class EmployeeActivity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable = false)
    private Employee employee;

    @Column(nullable=false, length=150)
    private String title;

    @Column(length=1000)
    private String description;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (startedAt == null) startedAt = LocalDateTime.now();
    }

    // --- helpers de domínio ---
    @Transient
    public Long getDurationMinutes() {
        if (startedAt == null) return null;
        LocalDateTime end = (finishedAt != null) ? finishedAt : LocalDateTime.now();
        return Duration.between(startedAt, end).toMinutes();
    }

    public boolean isOpen() {
        return finishedAt == null;
    }

    // GET/SET
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Employee getEmployee() {return employee;}
    public void setEmployee(Employee employee) {this.employee = employee;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}