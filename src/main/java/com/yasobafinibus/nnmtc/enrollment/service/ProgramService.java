package com.yasobafinibus.nnmtc.enrollment.service;

import com.yasobafinibus.nnmtc.enrollment.model.Program;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;

@Stateless
public class ProgramService {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(@Valid Program program) {
        getEntityManager().persist(program);
    }

    private EntityManager getEntityManager() {
        return this.entityManager;
    }
}
