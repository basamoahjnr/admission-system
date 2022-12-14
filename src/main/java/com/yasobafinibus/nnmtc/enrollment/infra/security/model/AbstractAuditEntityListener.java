package com.yasobafinibus.nnmtc.enrollment.infra.security.model;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import java.util.Objects;

public class AbstractAuditEntityListener {


    @PrePersist
    public void beforeSave(Object o) {
        if (o instanceof LoginToken && Objects.isNull(((LoginToken) o).getExpiredDate())) {
            ((LoginToken) o).setExpiredDate(LocalDateTime.now().plusDays(14L));
        }
    }
}
