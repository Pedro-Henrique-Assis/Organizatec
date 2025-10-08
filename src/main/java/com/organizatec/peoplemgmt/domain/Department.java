package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;

/**
 * Representa um departamento da empresa.
 * Mapeada para a tabela "departments".
 */
@Entity
@Table(name = "departments")
public class Department extends BaseEntity {
    @Column(nullable = false, unique = true, length = 120)
    private String name;

    public Department() {}
    public Department(String name) { this.name = name; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}