package com.yasobafinibus.nnmtc.enrollment.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;


/**
 * holds payments applicant makes for example
 * distinctive payments like TUITION,HOSTEL FEES, FEEDING FEES etc
 * for each distinctive payment there will be a leger to check if the applicant has finished paying for that ledger
 */
@EntityListeners(PaymentLedgerListener.class)
@Table(name = "payment_ledgers", indexes = {
        @Index(name = "idx_paymentLedger_applicant", columnList = "APPLICANT")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_paymentledger_applicant", columnNames = {"APPLICANT", "NAME"})
})
@Entity
public class PaymentLedger extends AbstractEntity {


    @OneToMany(mappedBy = "paymentLedger", cascade = {PERSIST, MERGE, REFRESH}, fetch = FetchType.LAZY)
    private final List<Payment> payments = new ArrayList<>();
    //bears similarity with purpose of payment
    @Enumerated(EnumType.STRING)
    @Column(name = "NAME", nullable = false, updatable = false)
    public PaymentLedgerType ledgerType = PaymentLedgerType.TUITION;//type of payment being mage
    @ManyToOne(optional = false,
            fetch = FetchType.EAGER,
            cascade = {MERGE, REFRESH})
    @JoinColumn(name = "APPLICANT", nullable = false)
    private Applicant applicant;
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.NO_PAYMENT;//set payment status of payment
    @Column(name = "TOTAL_PAYMENT", nullable = false)
    private Double totalPayment = 0.00;
    @Column(name = "EXPECTED_PAYMENT", nullable = false)
    private Double expectedPayment = 0.00;//total expected payment for current payment type

    public PaymentLedger(PaymentLedgerType ledgerType) {
        this.ledgerType = ledgerType;
    }

    public PaymentLedger(PaymentLedgerType ledgerType, PaymentStatus paymentStatus) {
        this.ledgerType = ledgerType;
        this.paymentStatus = paymentStatus;
    }

    public PaymentLedger(PaymentLedgerType ledgerType, PaymentStatus paymentStatus, double expectedPayment) {
        this(ledgerType, paymentStatus);
        this.expectedPayment = expectedPayment;
    }

    public PaymentLedger(PaymentLedgerType paymentLedgerType,
                         PaymentStatus paymentStatus,
                         Double expectedPayment) {
        this(paymentLedgerType, paymentStatus);
        this.expectedPayment = expectedPayment;
    }

    public PaymentLedger() {
    }

    public void addPayment(Payment payment) {
        if (payment.getPaymentLedger() == null) payment.setPaymentLedger(this);
        this.payments.add(payment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentLedger that = (PaymentLedger) o;

        if (getLedgerType() != that.getLedgerType()) return false;
        if (!getApplicant().equals(that.getApplicant())) return false;
        return getExpectedPayment().equals(that.getExpectedPayment());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return "PaymentLedger{" +
                "id=" + id +
                ", applicant=" + applicant +
                ", ledgerType=" + ledgerType +
                ", paymentStatus=" + paymentStatus +
                ", totalPayment=" + totalPayment +
                ", expectedPayment=" + expectedPayment +
                ", payments=" + payments +
                '}';
    }

    public List<Payment> getPayments() {
        return this.payments;
    }

    public PaymentLedgerType getLedgerType() {
        return this.ledgerType;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Double getTotalPayment() {
        return this.totalPayment;
    }

    public Double getExpectedPayment() {
        return this.expectedPayment;
    }

    public void setLedgerType(PaymentLedgerType ledgerType) {
        this.ledgerType = ledgerType;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public void setExpectedPayment(Double expectedPayment) {
        this.expectedPayment = expectedPayment;
    }

    public enum PaymentStatus {
        NO_PAYMENT("NO PAYMENT"),
        PART_PAYMENT("PART PAYMENT"),
        FULL_PAYMENT("FULL PAYMENT");


        String label;

        PaymentStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public enum PaymentLedgerType {
        TUITION("TUITION"),
        HOSTEL_FEES("HOSTEL FEES"),
        FEEDING("FEEDING FEES");

        String label;

        PaymentLedgerType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}