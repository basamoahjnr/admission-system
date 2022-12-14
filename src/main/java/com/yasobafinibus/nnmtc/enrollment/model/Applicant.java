package com.yasobafinibus.nnmtc.enrollment.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;

@Entity
@Cacheable
@DynamicUpdate
@SelectBeforeUpdate
@EntityListeners(ApplicantListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "applicants",
        indexes = {@Index(name = "idx_number_full_name", columnList = "NUMBER, FULL_NAME")
        },
        uniqueConstraints = {@
                UniqueConstraint(name = "u_number_program", columnNames = {"NUMBER", "PROGRAM"})
        })
@NamedQueries({
        @NamedQuery(name = "Applicant.findApplicantNumbers",
                query = "select s.number from Applicant s where s.number like :nmcNumber"),
        @NamedQuery(name = "Applicant.findByApplicantNumber",
                query = "select s from Applicant s where s.number like :nmcNumber"),
        @NamedQuery(name = "Applicant.findTotalAdmissionStatusTrue",
                query = "select count(a.id) from Applicant a where a.admissionStatus=:admissionStatus")
})
public class Applicant extends AbstractEntity implements Serializable, Comparable<Applicant> {


    private static final long serialVersionUID = -8144451855994681523L;

    @NotNull
    @Basic
    @Column(name = "NUMBER", unique = true)
    private String number;

    @NotNull
    @Basic
    @Column(name = "SURNAME")
    private String surname;

    @NotNull
    @Basic
    @Column(name = "OTHER_NAMES")
    private String otherNames;

    @NotNull
    @Basic
    @Column(name = "FULL_NAME")
    private String fullName;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRAM", nullable = false)
    private Program program;


    @Column(name = "ADMISSION_YEAR", nullable = false)
    private Integer admissionYear;


    @OneToMany(mappedBy = "applicant", cascade = {MERGE, REFRESH}, fetch = FetchType.EAGER)
    private List<PaymentLedger> paymentLedgers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "ADMISSION_STATUS", nullable = false)
    private AdmissionStatus admissionStatus = AdmissionStatus.AWAITING;

    @NotNull
    @Basic
    @Column(name = "GENDER")
    private String gender;


    public Applicant() {
    }

    public Applicant(Integer id) {
        this.id = id;
    }

    public Applicant(String number,
                     String surname,
                     String otherNames,
                     String gender,
                     Program program,
                     Integer admissionYear,
                     AdmissionStatus admissionStatus,
                     PaymentLedger paymentLedger) {
        this(number, surname, otherNames, gender, program, admissionYear, admissionStatus);
        this.paymentLedgers.add(paymentLedger);
    }


    public Applicant(String number,
                     String surname,
                     String otherNames,
                     String gender,
                     Program program,
                     Integer admissionYear,
                     AdmissionStatus admissionStatus) {
        this(number, surname, otherNames, gender, program, admissionYear);
        this.admissionStatus = admissionStatus;
    }


    public Applicant(String number,
                     String surname,
                     String otherNames,
                     String gender,
                     Program program,
                     Integer admissionYear) {
        this(number, surname, otherNames, gender, program);
        this.admissionYear = admissionYear;
    }


    public Applicant(String number,
                     String surname,
                     String otherNames,
                     String gender,
                     Program program) {
        this.number = number;
        this.surname = surname;
        this.otherNames = otherNames;
        this.program = program;
        this.gender = gender;
        if (Objects.nonNull(surname) || Objects.nonNull(otherNames))
            this.fullName = StringUtils.capitalize(surname) + " " + StringUtils.capitalize(otherNames);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Applicant applicant = (Applicant) o;
        return id != null && Objects.equals(id, applicant.id);
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public int compareTo(Applicant o) {
        return this.id.compareTo(o.getId());
    }

    public void addPaymentLedger(PaymentLedger paymentLedger) {
        if (paymentLedger.getApplicant() == null) paymentLedger.setApplicant(this);
        this.paymentLedgers.add(paymentLedger);
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", surname='" + surname + '\'' +
                ", otherNames='" + otherNames + '\'' +
                ", fullName='" + fullName + '\'' +
                ", program=" + program +
                ", admissionYear=" + admissionYear +
                ", paymentLedgers=" + paymentLedgers +
                ", admissionStatus=" + admissionStatus +
                ", gender='" + gender + '\'' +
                '}';
    }

    public @NotNull String getNumber() {
        return this.number;
    }

    public @NotNull String getSurname() {
        return this.surname;
    }

    public @NotNull String getOtherNames() {
        return this.otherNames;
    }

    public @NotNull String getFullName() {
        return this.fullName;
    }

    public Program getProgram() {
        return this.program;
    }

    public Integer getAdmissionYear() {
        return this.admissionYear;
    }

    public List<PaymentLedger> getPaymentLedgers() {
        return this.paymentLedgers;
    }

    public AdmissionStatus getAdmissionStatus() {
        return this.admissionStatus;
    }

    public @NotNull String getGender() {
        return this.gender;
    }

    public void setNumber(@NotNull String number) {
        this.number = number;
    }

    public void setSurname(@NotNull String surname) {
        this.surname = surname;
    }

    public void setOtherNames(@NotNull String otherNames) {
        this.otherNames = otherNames;
    }

    public void setFullName(@NotNull String fullName) {
        this.fullName = fullName;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setAdmissionYear(Integer admissionYear) {
        this.admissionYear = admissionYear;
    }

    public void setAdmissionStatus(AdmissionStatus admissionStatus) {
        this.admissionStatus = admissionStatus;
    }

    public void setGender(@NotNull String gender) {
        this.gender = gender;
    }

    private void setPaymentLedgers(List<PaymentLedger> paymentLedgers) {
        this.paymentLedgers = paymentLedgers;
    }

    public enum AdmissionStatus {
        AWAITING("AWAITING"),
        PENDING("PENDING"),
        ADMITTED("ADMITTED");

        String label;

        AdmissionStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }
}
