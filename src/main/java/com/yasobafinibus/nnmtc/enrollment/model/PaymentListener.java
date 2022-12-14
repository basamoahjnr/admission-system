package com.yasobafinibus.nnmtc.enrollment.model;

import jakarta.persistence.PrePersist;

public class PaymentListener {

    //on save of every payment
    //sum up payment for that ledger
    //save the sum into the totalPayment column
    //update the paymentStatus
    @PrePersist
    public void prePersist(Object o) {
        if (o instanceof Payment) {
            Payment payment = (Payment) o;
            Double totalPayment = payment.getPaymentLedger().getTotalPayment();
            payment.getPaymentLedger().setTotalPayment(totalPayment + payment.getAmountPaid());

            //if totalPayment is greater than zero but less than expeced payment sent type to PART_PAYMENT
            if (payment.getPaymentLedger().getTotalPayment() > 0 && payment.getPaymentLedger().getTotalPayment() < payment.getPaymentLedger().getExpectedPayment()) {
                payment.getPaymentLedger().setPaymentStatus(PaymentLedger.PaymentStatus.PART_PAYMENT);
            } else {
                payment.getPaymentLedger().setPaymentStatus(PaymentLedger.PaymentStatus.FULL_PAYMENT);
            }
        }
    }
}
