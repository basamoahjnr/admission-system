package com.yasobafinibus.nnmtc.enrollment.service;

import com.yasobafinibus.nnmtc.enrollment.model.Applicant;

import java.io.InputStream;
import java.util.Set;

public interface ApplicantCSVParser {

    public Set<Applicant> csvToApplicants(String filename, InputStream inputStream);
}
