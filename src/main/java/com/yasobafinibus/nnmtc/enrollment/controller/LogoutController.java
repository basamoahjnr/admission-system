package com.yasobafinibus.nnmtc.enrollment.controller;


import com.yasobafinibus.nnmtc.enrollment.infra.service.UserActivityLogger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.omnifaces.util.Messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;

import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventOutcome.FAILURE;
import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventOutcome.SUCCESS;
import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventType.LOGOUT;
import static com.yasobafinibus.nnmtc.enrollment.util.Utils.format;


@Named
@RequestScoped
public class LogoutController implements Serializable {


    String username;

    @Inject
    private UserActivityLogger logger;
    @Inject
    private Principal principal;

    public String logOut() throws IOException {
        try {


            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            username = principal.getName();
            request.logout();


            //Deal with Browser Back Bottom
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");


            logger.log(this.getClass(),
                    LOGOUT,
                    SUCCESS,
                    username,
                    format("user {0} logged out",
                            username));


        } catch (ServletException ex) {

            logger.log(this.getClass(),
                    LOGOUT,
                    FAILURE,
                    username,
                    format("user {0} logged out failed",
                            username));


            Messages.addError(null, "logout failed try again");

            // if logout error just redirect to the page the user came from
            String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            FacesContext.getCurrentInstance().getExternalContext().redirect(viewId);
            FacesContext.getCurrentInstance().getExceptionHandler().handle();

        }

        return "/login?faces-redirect=true";
    }

}
