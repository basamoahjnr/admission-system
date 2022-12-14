package com.yasobafinibus.nnmtc.enrollment.infra.security;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface Authenticated {
    static Literal INSTANCE = new Literal();

    static class Literal extends AnnotationLiteral<Authenticated> implements Authenticated {
    }
}
