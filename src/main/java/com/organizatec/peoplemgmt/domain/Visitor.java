package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;

/**
 * Representa o cadastro de uma pessoa externa (visitante).
 *
 * O objetivo desta entidade é manter um registro de pessoas que visitam a empresa,
 * para que não seja necessário redigitar os dados a cada nova visita.
 * Mapeada para a tabela "visitors".
 */
@Entity
@Table(name = "visitors")
public class Visitor extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 40)
    private String documentId;

    @Column(nullable = false, length = 200)
    private String reason;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
