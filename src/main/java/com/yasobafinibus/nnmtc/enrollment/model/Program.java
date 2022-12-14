package com.yasobafinibus.nnmtc.enrollment.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "programs")
public class Program extends AbstractEntity {

    //bears similarity with purpose of payment
    @Column(name = "NAME", nullable = false, updatable = false)
    private String name;

    public Program(String programName) {
        this.name = programName;
    }

    public Program() {
    }

    @Override
    public String toString() {
        return this.name;
    }
}
