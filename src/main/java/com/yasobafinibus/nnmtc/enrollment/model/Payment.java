package com.yasobafinibus.nnmtc.enrollment.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.CascadeType.MERGE;

@EntityListeners(PaymentListener.class)
@Table(name = "payments", indexes = {
        @Index(
                name = "idx_payment_transaction_id_payment_ledger",
                columnList = "TRANSACTION_ID, PAYMENT_LEDGER")
}, uniqueConstraints = {
        @UniqueConstraint(
                name = "uc_payment_transaction_id",
                columnNames = {"TRANSACTION_ID", "PAYMENT_LEDGER", "CREATED_ON"})
})
@NamedQueries({
        @NamedQuery(name = "Payment.findTotalPaymentPaymentLedger",
                query = "select sum(p.amountPaid) from Payment p where p.paymentLedger.id =:paymentLedger")
})
@Entity
public class Payment extends AbstractEntity implements Serializable {


    private static final long serialVersionUID = -7145606594739319604L;


    @Column(name = "PURPOSE", nullable = false)
    private String purpose;


    @Column(name = "TRANSACTION_ID", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "PAYMENT_DATE", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "AMOUNT_PAID", nullable = false)
    private Double amountPaid;

    @ManyToOne(optional = false, cascade = {MERGE})
    @JoinColumn(name = "PAYMENT_LEDGER", nullable = false)
    private PaymentLedger paymentLedger;

    public Payment() {
    }

    public Payment(Double amountPaid,
                   LocalDateTime paymentDate,
                   String transactionId) {
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.transactionId = transactionId;
    }

    public Payment(Double amountPaid, LocalDateTime paymentDate, String txId, PaymentLedger.PaymentLedgerType paymentFor) {
        this(amountPaid, paymentDate, txId);
        this.purpose = paymentFor.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        return getTransactionId().equals(payment.getTransactionId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getPurpose() {
        return this.purpose;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public LocalDateTime getPaymentDate() {
        return this.paymentDate;
    }

    public Double getAmountPaid() {
        return this.amountPaid;
    }

    public PaymentLedger getPaymentLedger() {
        return this.paymentLedger;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setPaymentLedger(PaymentLedger paymentLedger) {
        this.paymentLedger = paymentLedger;
    }
}