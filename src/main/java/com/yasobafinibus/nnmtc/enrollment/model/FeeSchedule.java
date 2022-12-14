package com.yasobafinibus.nnmtc.enrollment.model;

import jakarta.persistence.*;

import java.io.Serializable;


/**
 * Total bills the applicant has to pay
 */

@Table(name = "fee_schedule", indexes = {
        @Index(name = "idx_feeschedule_total_bill",
                columnList = "TOTAL_BILL")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_feeschedule_academicyear_total_bill",
                columnNames = {"APPLICANT", "ADMISSION_YEAR", "TOTAL_BILL"})
})
@Entity
public class FeeSchedule extends AbstractEntity implements Serializable {


    private static final long serialVersionUID = -8160174077575778699L;

    @JoinColumn(name = "APPLICANT")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Applicant applicant;

    //total bill one is expected to pay, this is generated from summing all applicant expected payment from each ledger associated with applicant
    @Column(name = "TOTAL_BILL", nullable = false)
    private Double totalBill = 0.00;

    @Column(name = "ADMISSION_YEAR", nullable = false)
    private Integer admissionYear;

    public FeeSchedule(Applicant applicant, Integer admissionYear, Double totalBill) {
        this.totalBill = totalBill;
        this.admissionYear = admissionYear;
        this.applicant = applicant;
    }

    public FeeSchedule() {
    }


    public Applicant getApplicant() {
        return this.applicant;
    }

    public Double getTotalBill() {
        return this.totalBill;
    }

    public Integer getAdmissionYear() {
        return this.admissionYear;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }

    public void setAdmissionYear(Integer admissionYear) {
        this.admissionYear = admissionYear;
    }
}