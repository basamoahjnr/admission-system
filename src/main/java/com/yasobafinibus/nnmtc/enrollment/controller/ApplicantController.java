package com.yasobafinibus.nnmtc.enrollment.controller;


import com.yasobafinibus.nnmtc.enrollment.infra.model.Filter;
import com.yasobafinibus.nnmtc.enrollment.infra.service.UserActivityLogger;
import com.yasobafinibus.nnmtc.enrollment.model.Applicant;
import com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger;
import com.yasobafinibus.nnmtc.enrollment.service.ApplicantService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.omnifaces.util.Messages;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventOutcome.FAILURE;
import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventOutcome.SUCCESS;
import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventType.CREATE;
import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventType.UPDATE;
import static com.yasobafinibus.nnmtc.enrollment.model.Applicant.AdmissionStatus.AWAITING;
import static com.yasobafinibus.nnmtc.enrollment.model.Applicant.AdmissionStatus.PENDING;
import static com.yasobafinibus.nnmtc.enrollment.util.Utils.format;


@Named
@ViewScoped
public class ApplicantController implements Serializable {

    Applicant selectedApplicant = new Applicant();
    Filter<Applicant> filter = new Filter<>(new Applicant());
    List<Applicant> selectedApplicants;
    List<Applicant> filteredValue;


    @Inject
    private ApplicantService applicantService;
    @Inject
    private Principal principal;
    @Inject
    private UserActivityLogger logger;
    private String applicantNumber;
    private LazyDataModel<Applicant> applicants;

    public static boolean isValidationFailed() {
        return !FacesContext.getCurrentInstance().isValidationFailed();
    }

    @PostConstruct
    public void initDataModel() {
        applicants = new ApplicantLazyDataModel(this);
    }

    public void nullSelected() {
        setSelectedApplicant(new Applicant());
    }

    public void addApplicant() {

        try {

            //find applicant in system
            Optional<Applicant> applicantByNumber = getApplicantService().findApplicantByNumber(selectedApplicant.getNumber());

            //if not existent
            if (applicantByNumber.isEmpty()) {
                getApplicantService().save(selectedApplicant);
            } else {
                //return if already in the system
                Messages.addGlobalError(null, "applicant already in the system");
                throw new FacesException();
            }

            logger.log(this.getClass(),
                    CREATE,
                    SUCCESS,
                    getPrincipal().getName(),
                    format("new applicant with {0} was created",
                            selectedApplicant.getNumber()));

        } catch (PersistenceException pe) {
            //find root cause message, if it contains constraint
            if (ExceptionUtils.getRootCauseMessage(pe).contains("constraint")) {
                Messages.addGlobalError(null, "failed to create applicant, applicant with program already exist");
            }

            logger.log(this.getClass(),
                    CREATE,
                    FAILURE,
                    getPrincipal().getName(),
                    format("failed to create new applicant with {0}",
                            selectedApplicant.getNumber()));

            Messages.addGlobalError(null, "Failed to create user please try again");
        }
    }

    public List<String> completeApplicantNumber(String query) {
        return applicantService.getApplicantNumbers(query);
    }

    public void delete() {
        getApplicantService().remove(selectedApplicant);
        Messages.addInfo(null, selectedApplicant.getFullName() + " applicant deleted successfully!");
    }

    public void changeApplicantAdmissionStatus() {
        //TODO log
        selectedApplicant.setAdmissionStatus(PENDING);
        //confirm that admission status has changed if not reset it to false and flash not updated message
        if (getApplicantService().update(selectedApplicant).getAdmissionStatus().equals(PENDING)) {
            logger.log(this.getClass(),
                    UPDATE,
                    SUCCESS,
                    getPrincipal().getName(),
                    format("{0} admission status was updated",
                            selectedApplicant.getNumber()));

            Messages.addFlashGlobalInfo(selectedApplicant.getNumber() + " admission status updated to PENDING");
        } else {
            selectedApplicant.setAdmissionStatus(AWAITING);
            Messages.addFlashGlobalError(selectedApplicant.getNumber() + " admission status update failed");
        }
        selectedApplicant = new Applicant();

    }

    public void findByApplicantId(Integer id) {
        if (id != null) {
            selectedApplicants.add(applicantService.findById(id));
        } else {
            Messages.addError(null, "Provide Car ID to load");
        }
    }

    public Long totalAdmitted() {
        return getApplicantService().totalAdmissionStatusTrue(true);
    }

    public Long totalNotAdmitted() {
        return getApplicantService().totalAdmissionStatusTrue(false);
    }

    public String enumValue(Enum<?> anEnum) {
        return anEnum.toString();
    }

    public Double totalPayment(Applicant applicant) {
        return applicant.getPaymentLedgers().stream().mapToDouble(PaymentLedger::getTotalPayment).sum();
    }

    public Double expectedPayment(Applicant applicant) {
        return applicant.getPaymentLedgers().stream().mapToDouble(PaymentLedger::getExpectedPayment).sum();
    }

    public Applicant getSelectedApplicant() {
        return selectedApplicant;
    }

    public void setSelectedApplicant(Applicant selectedApplicant) {
        this.selectedApplicant = selectedApplicant;
    }

    public Filter<Applicant> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Applicant> filter) {
        this.filter = filter;
    }

    public List<Applicant> getSelectedApplicants() {
        return selectedApplicants;
    }

    public void setSelectedApplicants(List<Applicant> selectedApplicants) {
        this.selectedApplicants = selectedApplicants;
    }

    public List<Applicant> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Applicant> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public ApplicantService getApplicantService() {
        return applicantService;
    }

    public void setApplicantService(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public UserActivityLogger getLogger() {
        return logger;
    }

    public void setLogger(UserActivityLogger logger) {
        this.logger = logger;
    }

    public String getApplicantNumber() {
        return applicantNumber;
    }

    public void setApplicantNumber(String applicantNumber) {
        this.applicantNumber = applicantNumber;
    }

    public LazyDataModel<Applicant> getApplicants() {
        return applicants;
    }

    public void setApplicants(LazyDataModel<Applicant> applicants) {
        this.applicants = applicants;
    }
}
