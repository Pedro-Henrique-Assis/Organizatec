// src/main/java/com/organizatec/peoplemgmt/domain/Visit.java
package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa o registro de uma visita de uma pessoa externa à empresa.
 *
 * Contém detalhes sobre o visitante, o anfitrião, motivo e horários.
 * Mapeada para a tabela "visits".
 */
@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Coluna existente no banco, sem entidade própria por enquanto
    @Column(name = "visitor_id", nullable = true)
    private Long visitorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visited_emp_id")            // FK -> employees.id
    private Employee visitedEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visited_dept_id")           // FK -> departments.id (opcional)
    private Department visitedDepartment;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "badge_code", length = 20)
    private String badgeCode;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "company", length = 100)
    private String company;

    @Column(name = "visitor_name", length = 150)
    private String visitorName;

    @Column(name = "document_id", length = 20)
    private String documentId;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "vehicle_plate", length = 10)
    private String vehiclePlate;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // ==== GETTERS/SETTERS ====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVisitorId() { return visitorId; }
    public void setVisitorId(Long visitorId) { this.visitorId = visitorId; }

    public Employee getVisitedEmployee() { return visitedEmployee; }
    public void setVisitedEmployee(Employee visitedEmployee) { this.visitedEmployee = visitedEmployee; }

    public Department getVisitedDepartment() { return visitedDepartment; }
    public void setVisitedDepartment(Department visitedDepartment) { this.visitedDepartment = visitedDepartment; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }

    public String getBadgeCode() { return badgeCode; }
    public void setBadgeCode(String badgeCode) { this.badgeCode = badgeCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
}