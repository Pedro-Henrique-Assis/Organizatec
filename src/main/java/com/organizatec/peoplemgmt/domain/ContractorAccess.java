package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contractor_access")
public class ContractorAccess extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;

    @Column(nullable = false)
    private LocalDateTime entryTime = LocalDateTime.now();

    private LocalDateTime exitTime;

    public Contractor getContractor() { return contractor; }
    public void setContractor(Contractor contractor) { this.contractor = contractor; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
}
