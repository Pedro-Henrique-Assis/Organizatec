package com.organizatec.peoplemgmt.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {
    @Column(nullable = false, unique = true, length = 120)
    private String name;

    public Project() {}
    public Project(String name) { this.name = name; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}