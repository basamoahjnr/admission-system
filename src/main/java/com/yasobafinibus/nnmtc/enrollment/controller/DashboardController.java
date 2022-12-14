package com.yasobafinibus.nnmtc.enrollment.controller;


import com.yasobafinibus.nnmtc.enrollment.infra.model.Filter;
import com.yasobafinibus.nnmtc.enrollment.model.Applicant;
import com.yasobafinibus.nnmtc.enrollment.service.ApplicantService;
import com.yasobafinibus.nnmtc.enrollment.service.PaymentService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalTime;

@Named
@RequestScoped
public class DashboardController {


    private final Filter<Applicant> filter = new Filter<>(new Applicant());
    @Inject
    private ApplicantService applicantService;
    @Inject
    private PaymentService paymentService;

    public Integer countApplicants(String admissionStatus) {
        if (admissionStatus.isEmpty() && admissionStatus.isBlank()) {
            return applicantService.count(filter);
        } else {
            return applicantService.countApplicants(Enum.valueOf(Applicant.AdmissionStatus.class, admissionStatus));
        }
    }


    public Double totalActualPayment() {
        return paymentService.calculateTotalActualPayment();
    }

    public Double totalExpectedPayment() {
        return paymentService.calculateTotalExpectedPayment();
    }

    public String lastFetch() {
        return "today @ " + LocalTime.now().toString();
    }

    public Double totalPaymentPeriodToday() {
        return paymentService.calculateTotalPaymentToday();
    }
}
