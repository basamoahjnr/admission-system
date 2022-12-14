package com.yasobafinibus.nnmtc.enrollment;

import com.opencsv.bean.CsvToBeanBuilder;
import com.yasobafinibus.nnmtc.enrollment.model.Applicant;
import com.yasobafinibus.nnmtc.enrollment.model.ApplicantDTO;
import com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger;
import com.yasobafinibus.nnmtc.enrollment.model.Program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yasobafinibus.nnmtc.enrollment.model.Applicant.AdmissionStatus;
import static com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger.PaymentLedgerType;
import static com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger.PaymentStatus;


public class Main {

    public static void main(String[] args) {
        String fileName = "/home/koko/Projects/NNMTC/admission-system/students.csv";

        List<ApplicantDTO> beans = new ArrayList<>();
        List<Applicant> applicants;
        try {
            beans = new CsvToBeanBuilder(new FileReader(fileName))
                    .withType(ApplicantDTO.class)
                    .build()
                    .parse();
            ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        applicants = beans.stream().map(applicantDTO -> new Applicant(
                applicantDTO.getNumber(),
                applicantDTO.getSurname(),
                applicantDTO.getOtherNames(),
                applicantDTO.getGender(),
                new Program(applicantDTO.getProgram()),
                applicantDTO.getAdmissionYear(),
                Enum.valueOf(AdmissionStatus.class, applicantDTO.getAdmissionStatus()),
                new PaymentLedger(Enum.valueOf(PaymentLedgerType.class, applicantDTO.getLedgerName()),
                        Enum.valueOf(PaymentStatus.class, applicantDTO.getPaymentStatus()),
                        applicantDTO.getAmountToPay().doubleValue())


        )).collect(Collectors.toList());
        applicants.forEach(System.out::println);
    }
}
