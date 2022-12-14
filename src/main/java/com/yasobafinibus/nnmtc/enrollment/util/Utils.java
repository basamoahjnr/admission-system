package com.yasobafinibus.nnmtc.enrollment.util;

import com.yasobafinibus.nnmtc.enrollment.infra.annotations.AdmissionsPersistenceUnit;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Produces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;


@Dependent
public class Utils implements Serializable {


    @PersistenceContext(unitName = "admissions_pu")
    private EntityManager admissionsEntityManager;



    public static Object getPropertyValueViaReflection(Object o, Field field) {


        //find correct Method
        for (Method method : o.getClass().getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                        return method.invoke(o);
                    } catch (IllegalAccessException | InvocationTargetException ignored) {

                    }

                }
            }
        }
        return null;
    }


    public static String format(String message, Object... params) {
        return MessageFormat.format(message, params);
    }


    @Produces
    @AdmissionsPersistenceUnit
    public EntityManager getAdmissionsEntityManager() {
        return this.admissionsEntityManager;
    }

}
