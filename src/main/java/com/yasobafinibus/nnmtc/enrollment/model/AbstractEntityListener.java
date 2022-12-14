package com.yasobafinibus.nnmtc.enrollment.model;


import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AbstractEntityListener {

    @PreUpdate
    public void preUpdate(Object object) {
        if (object instanceof AbstractEntity) {
            AbstractEntity ae = (AbstractEntity) object;
            ae.setUpdatedOn(LocalDateTime.now());
        }
    }
}
