package com.yasobafinibus.nnmtc.enrollment;

import com.github.javafaker.Faker;
import com.yasobafinibus.nnmtc.enrollment.infra.security.model.User;
import com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserRole;
import com.yasobafinibus.nnmtc.enrollment.infra.service.UserService;
import com.yasobafinibus.nnmtc.enrollment.model.Applicant;
import com.yasobafinibus.nnmtc.enrollment.model.Payment;
import com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger;
import com.yasobafinibus.nnmtc.enrollment.model.Program;
import com.yasobafinibus.nnmtc.enrollment.service.ApplicantService;
import com.yasobafinibus.nnmtc.enrollment.service.PaymentService;
import com.yasobafinibus.nnmtc.enrollment.service.ProgramService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.PasswordHash;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserRole.ACCOUNT;
import static com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger.PaymentLedgerType.HOSTEL_FEES;
import static com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger.PaymentLedgerType.TUITION;


@Singleton
@Startup
public class Boostrap {

    @Inject
    private PasswordHash passwordHash;

    @Inject
    private UserService userService;
    @Inject
    private PaymentService paymentService;
    @Inject
    private ApplicantService applicantService;
    @Inject
    private ProgramService programService;


    @PostConstruct
    public void start() {
        createUsers();
        createApplicants();
    }

    public void createUsers() {


        User user = new User();
        user.setUsername("basamoahjnr@outlook.com");
        String generate = passwordHash.generate("password".toCharArray());
        user.setPassword(generate);
        user.setFullName("Boakye Asamoah Jnr");
        user.addRoles(ACCOUNT);

        user = userService.save(user);


        user = new User();
        user.setUsername("basamoahjnr@gmail.com");
        generate = passwordHash.generate("password".toCharArray());
        user.setPassword(generate);
        user.setFullName("Kojo Amponsah");
        user.addRoles(UserRole.ENTRY);

        user = userService.save(user);


    }


    private void createApplicants() {
        Faker faker = new Faker();

        List<Applicant> applicants = new ArrayList<>();
        List<PaymentLedger> paymentLedgers = new ArrayList<>();
        List<Program> programs = Arrays.asList(new Program("NAC MIDWIFERY"), new Program("DIPLOMA MIDWIFERY"), new Program("DIPLOMA NURSING"));


        for (int i = 0; i < 1024; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String idNumber = faker.idNumber().valid();
            String gender = faker.options().option("MALE", "FEMALE");
            Program program = programs.get(new Random().nextInt(programs.size()));
            Applicant applicant = new Applicant(idNumber, lastName, firstName, gender, program, 2022);
            applicants.add(applicant);
        }

        Date now;
        Payment payment;
        double price;
        for (int i = 0; i < 200; i++) {

            Applicant applicant = applicants.get(i);

            now = new Date();
            int amountToSubtract = ThreadLocalRandom.current().nextInt(1, 14 + 1);
            Date from = Date.from(now.toInstant().minus(amountToSubtract, ChronoUnit.DAYS));
            LocalDateTime paymentDate = LocalDateTime.ofInstant(faker.date().between(from, now).toInstant(), ZoneId.systemDefault());
            String txId = faker.finance().iban().substring(0, 10);


            //ledger for tuition
            price = Double.parseDouble(faker.commerce().price(902, 1103.45));
            PaymentLedger paymentLedger = new PaymentLedger(TUITION, PaymentLedger.PaymentStatus.NO_PAYMENT, price);
            applicant.addPaymentLedger(paymentLedger);
            paymentLedgers.add(paymentLedger);

            //pay tuition fees
            payment = new Payment(Double.valueOf(faker.commerce().price(210.00, 434.22)), paymentDate, txId, TUITION);
            paymentLedger.addPayment(payment);


            //ledger for hostel fees
            price = Double.parseDouble(faker.commerce().price(1420, 1230));
            paymentLedger = new PaymentLedger(HOSTEL_FEES, PaymentLedger.PaymentStatus.NO_PAYMENT, price);
            applicant.addPaymentLedger(paymentLedger);
            paymentLedgers.add(paymentLedger);

            //pay hostel fees
            if (ThreadLocalRandom.current().nextInt(-100, 10 + 1) < 0) {
                amountToSubtract = ThreadLocalRandom.current().nextInt(10, 20 + 1);
                from = Date.from(now.toInstant().minus(amountToSubtract, ChronoUnit.DAYS));
                paymentDate = LocalDateTime.ofInstant(faker.date().between(from, now).toInstant(), ZoneId.systemDefault());
                txId = faker.finance().iban().substring(0, 10);
                payment = new Payment(Double.valueOf(faker.commerce().price(0, 1220.30)), paymentDate, txId, HOSTEL_FEES);
                paymentLedger.addPayment(payment);
            }


            //generate another tuition payment
            amountToSubtract = new Faker().number().numberBetween(14, 20);
            from = Date.from(now.toInstant().minus(amountToSubtract, ChronoUnit.DAYS));
            paymentDate = LocalDateTime.ofInstant(faker.date().between(from, now).toInstant(), ZoneId.systemDefault());
            txId = faker.finance().iban().substring(0, 10);
            payment = new Payment(Double.valueOf(faker.commerce().price(512.60, 612.17)), paymentDate, txId, TUITION);
            paymentLedger.addPayment(payment);
        }

        programs.forEach(programService::save);
        applicants.forEach(applicantService::save);
        paymentLedgers.forEach(paymentService::save);


    }
}
