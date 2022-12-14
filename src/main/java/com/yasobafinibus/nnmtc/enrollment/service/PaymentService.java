package com.yasobafinibus.nnmtc.enrollment.service;


import com.yasobafinibus.nnmtc.enrollment.infra.model.Filter;
import com.yasobafinibus.nnmtc.enrollment.model.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

import java.time.LocalDateTime;


@Stateless
public class PaymentService {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(@Valid Payment payment) {
        getEntityManager().persist(payment);
    }

    public void save(@Valid PaymentLedger paymentLedger) {
        getEntityManager().persist(paymentLedger);
    }

    public Double getTotalLedger(Filter<Applicant> filter) {
        return 0.00;
    }

    public Double calculateTotalExpectedPayment() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<PaymentLedger> root = cq.from(PaymentLedger.class);
        cq.select(cb.sum(root.get(PaymentLedger_.EXPECTED_PAYMENT)));
        return getEntityManager().createQuery(cq).getSingleResult();
    }


    public Double calculateTotalActualPayment() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<PaymentLedger> root = cq.from(PaymentLedger.class);
        cq.select(cb.sum(root.get(PaymentLedger_.TOTAL_PAYMENT)));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public Double calculateTotalPaymentToday() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<Payment> root = cq.from(Payment.class);
        cq.select(cb.sum(root.get(Payment_.AMOUNT_PAID)));
        cq.where(cb.greaterThanOrEqualTo(root.get(Payment_.CREATED_ON), LocalDateTime.now()));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    private EntityManager getEntityManager() {
        return this.entityManager;
    }
}
