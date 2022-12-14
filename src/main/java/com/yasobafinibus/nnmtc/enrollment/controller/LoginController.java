package com.yasobafinibus.nnmtc.enrollment.controller;

import com.yasobafinibus.nnmtc.enrollment.infra.security.Authenticated;
import com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserInfo;
import com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserRole;
import com.yasobafinibus.nnmtc.enrollment.infra.service.UserActivityLogger;
import com.yasobafinibus.nnmtc.enrollment.util.Utils;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import org.omnifaces.util.Messages;

import java.io.IOException;
import java.io.Serializable;

import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventOutcome.*;
import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.UserActivity.EventType.LOGIN;
import static com.yasobafinibus.nnmtc.enrollment.util.Utils.format;
import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@Named
@SessionScoped
public class LoginController implements Serializable {

    @Inject
    private SecurityContext securityContext;

    @EJB
    private UserActivityLogger logger;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private boolean rememberMe = false;

    private boolean continued = true;

    private static HttpServletResponse getResponse(FacesContext context) {
        return (HttpServletResponse) context
                .getExternalContext()
                .getResponse();
    }

    private static HttpServletRequest getRequest(FacesContext context) {
        return (HttpServletRequest) context
                .getExternalContext()
                .getRequest();
    }

    public void login() {

        FacesContext context = FacesContext.getCurrentInstance();
        Credential credential = new UsernamePasswordCredential(username, new Password(password));
        try {

            AuthenticationStatus status = securityContext.authenticate(
                    getRequest(context),
                    getResponse(context),
                    withParams()
                            .credential(credential)
                            .newAuthentication(!continued)
                            .rememberMe(rememberMe)
            );

            switch (status) {
                case SEND_CONTINUE:
                    // Authentication mechanism has sent a redirect, should not
                    // send anything to response from JSF now.
                    context.responseComplete();
                    break;
                case SEND_FAILURE:
                    logger.log(this.getClass(),
                            LOGIN,
                            FAILURE,
                            getUsername(),
                            format("user {0} failed to logged in",
                                    getUsername()));

                    Messages.addError(null, "Authentication failed");

                    break;
                case SUCCESS:

                    logger.log(this.getClass(),
                            LOGIN,
                            SUCCESS,
                            getUsername(),
                            format("user {0} logged in",
                                    getUsername()));

                    context.getExternalContext()
                            .redirect(context
                                    .getExternalContext()
                                    .getRequestContextPath() + redirectByUserRole() + "?faces-redirect=true");
                    break;
                case NOT_DONE:

                    logger.log(this.getClass(),
                            LOGIN,
                            NONE,
                            securityContext.getCallerPrincipal().getName(),
                            Utils.format("user {0} log in process not done",
                                    securityContext.getCallerPrincipal().getName()));

                    Messages.addError(null, "Login failed");

                default:
                    break;
            }
        } catch (IOException ex) {
            context.getExceptionHandler().handle();
        }

    }

    //redirect user based on the role the user has
    private String redirectByUserRole() {

        String dashboardUrl = "/dashboard";
        String accountRoleUrl = "/accounts/dashboard";
        String enterRoleUrl = "/admissions/dashboard";

        String path = dashboardUrl;

        if (securityContext.isCallerInRole(UserRole.ACCOUNT.toString())) {
            path = accountRoleUrl;
        }
        if (securityContext.isCallerInRole(UserRole.ENTRY.toString())) {
            path = enterRoleUrl;
        }

        return path;
    }

    @Authenticated
    public UserInfo currentUser() {
        return new UserInfo(this.username);
    }

    public @NotEmpty String getUsername() {
        return this.username;
    }

    public @NotEmpty String getPassword() {
        return this.password;
    }

    public boolean isRememberMe() {
        return this.rememberMe;
    }

    public boolean isContinued() {
        return this.continued;
    }

    public void setUsername(@NotEmpty String username) {
        this.username = username;
    }

    public void setPassword(@NotEmpty String password) {
        this.password = password;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setContinued(boolean continued) {
        this.continued = continued;
    }
}
