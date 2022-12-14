package com.yasobafinibus.nnmtc.enrollment.infra.service;


import com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventOutcome;
import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventType;


@Stateless
public class UserActivityLogger implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public void log(UserActivity activity) {
        getEntityManager().persist(activity);

    }

    public void log(Class<?> ip,
                    @NotNull EventType eventType,
                    @NotNull EventOutcome eventOutcome,
                    @NotEmpty String username,
                    @NotEmpty String message) {
        log(UserActivity.UserActivityBuilder.anUserActivity()
                .withLogger(ip.getName())
                .withEventType(eventType)
                .withEventOutcome(eventOutcome)
                .withUserName(username)
                .withMessage(message)
                .build());
    }

    private EntityManager getEntityManager() {
        return this.entityManager;
    }
}
