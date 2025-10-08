package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa um registro de evento de acesso (entrada ou saída) de um terceirizado.
 * Mapeada para a tabela "contractor_access".
 */
@Entity
@Table(name = "contractor_access")
public class ContractorAccess {

    /**
     * Enumeração para os tipos de batida de ponto: Entrada (IN) ou Saída (OUT).
     */
    public enum PunchType { IN, OUT }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contractor_id", nullable = false) // FK -> contractors.id
    private Contractor contractor;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10) // "IN" / "OUT"
    private PunchType type;

    @Column(name = "event_at", nullable = false)
    private LocalDateTime eventAt;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Contractor getContractor() { return contractor; }
    public void setContractor(Contractor contractor) { this.contractor = contractor; }

    public PunchType getType() { return type; }
    public void setType(PunchType type) { this.type = type; }

    public LocalDateTime getEventAt() { return eventAt; }
    public void setEventAt(LocalDateTime eventAt) { this.eventAt = eventAt; }
}