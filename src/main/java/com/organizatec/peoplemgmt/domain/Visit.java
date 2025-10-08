package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String visitorName;

    @Column(nullable = false, length = 20)
    private String documentId; // CPF/RG do visitante

    @Column(length = 100)
    private String company;

    @Column(name = "visited_department", length = 100)
    private String visitedDepartment;

    // 🔹 Funcionário anfitrião (visitado)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visited_employee_id")
    private Employee visitedEmployee;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(length = 500)
    private String reason; // motivo da visita

    @Column(length = 8)
    private String vehiclePlate;

    // 🔹 Código do crachá gerado automaticamente
    @Column(length = 12, unique = true)
    private String badgeCode;

    // ==== GETTERS/SETTERS ====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getVisitedDepartment() { return visitedDepartment; }
    public void setVisitedDepartment(String visitedDepartment) { this.visitedDepartment = visitedDepartment; }

    public Employee getVisitedEmployee() { return visitedEmployee; }
    public void setVisitedEmployee(Employee visitedEmployee) { this.visitedEmployee = visitedEmployee; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }

    public String getBadgeCode() { return badgeCode; }
    public void setBadgeCode(String badgeCode) { this.badgeCode = badgeCode; }

    // ==== CALLBACK ====
    @PrePersist
    public void prePersist() {
        if (entryTime == null) {
            entryTime = LocalDateTime.now();
        }
    }
}