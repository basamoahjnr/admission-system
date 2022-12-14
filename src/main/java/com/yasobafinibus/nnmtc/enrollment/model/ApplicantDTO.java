package com.yasobafinibus.nnmtc.enrollment.model;

import com.opencsv.bean.CsvBindByName;

import java.io.Serializable;


public class ApplicantDTO implements Serializable {


    private static final long serialVersionUID = 7363936806837482793L;

    @CsvBindByName(column = "NUMBER", required = true)
    private String number;
    @CsvBindByName(column = "SURNAME", required = true)
    private String surname;
    @CsvBindByName(column = "OTHER_NAMES", required = true)
    private String otherNames;
    @CsvBindByName(column = "GENDER", required = true)
    private String gender;
    @CsvBindByName(column = "ADMISSION_YEAR", required = true)
    private Integer admissionYear;
    @CsvBindByName(column = "ADMISSION_STATUS")
    private String admissionStatus;
    @CsvBindByName(column = "PROGRAM")
    private String program;


    @CsvBindByName(column = "LEDGER_TYPE", required = true)//sample ( TUITION, HOSTEL_FEES etc)
    private String ledgerName;//name (translates to paymentLedger name)
    @CsvBindByName(column = "PAYMENT_STATUS", required = true)//sample ( FULL_PAYMENT,PART_PAYMENT,NO_PAYMENT)
    private String paymentStatus;//name (translates to paymentLedger name)
    @CsvBindByName(column = "EXPECTED_PAYMENT", required = true)
    private Double amountToPay;//paymentLedger amount to pay


    @CsvBindByName(column = "LEDGER_TYPE1")//sample ( TUITION, HOSTEL_FEES etc)
    private String ledgerName1;//name (translates to paymentLedger name)
    @CsvBindByName(column = "PAYMENT_NAME1")//sample ( FULL_PAYMENT,PART_PAYMENT,NO_PAYMENT)
    private String paymentStatus1;//name (translates to paymentLedger name)
    @CsvBindByName(column = "EXPECTED_PAYMENT1")
    private Double amountToPay1;//paymentLedger amount to pay


    @Override
    public String toString() {
        return "ApplicantDTO{" +
                "number='" + number + '\'' +
                ", surname='" + surname + '\'' +
                ", otherNames='" + otherNames + '\'' +
                ", gender='" + gender + '\'' +
                ", admissionYear=" + admissionYear +
                ", admissionStatus='" + admissionStatus + '\'' +
                ", paymentName='" + ledgerName + '\'' +
                ", amountToPay=" + amountToPay +
                ", paymentName1='" + paymentStatus1 + '\'' +
                ", amountToPay1=" + amountToPay1 +
                '}';
    }

    public String getNumber() {
        return this.number;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getOtherNames() {
        return this.otherNames;
    }

    public String getGender() {
        return this.gender;
    }

    public Integer getAdmissionYear() {
        return this.admissionYear;
    }

    public String getAdmissionStatus() {
        return this.admissionStatus;
    }

    public String getProgram() {
        return this.program;
    }

    public String getLedgerName() {
        return this.ledgerName;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public Double getAmountToPay() {
        return this.amountToPay;
    }

    public String getLedgerName1() {
        return this.ledgerName1;
    }

    public String getPaymentStatus1() {
        return this.paymentStatus1;
    }

    public Double getAmountToPay1() {
        return this.amountToPay1;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAdmissionYear(Integer admissionYear) {
        this.admissionYear = admissionYear;
    }

    public void setAdmissionStatus(String admissionStatus) {
        this.admissionStatus = admissionStatus;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public void setLedgerName1(String ledgerName1) {
        this.ledgerName1 = ledgerName1;
    }

    public void setPaymentStatus1(String paymentStatus1) {
        this.paymentStatus1 = paymentStatus1;
    }

    public void setAmountToPay1(Double amountToPay1) {
        this.amountToPay1 = amountToPay1;
    }
}
