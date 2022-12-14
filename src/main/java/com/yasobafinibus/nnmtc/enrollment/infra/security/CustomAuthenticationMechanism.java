package com.yasobafinibus.nnmtc.enrollment.infra.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.*;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@AutoApplySession
@RememberMe(
        isRememberMeExpression = "httpMessageContext.authParameters.rememberMe",
        cookieMaxAgeSeconds = 60 * 60 * 24 * 14
)
@LoginToContinue(
        loginPage = "/login.xhtml?continued=true",
        errorPage = "")
@RequestScoped
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private IdentityStoreHandler identityStore;

    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMessageContext context) {

        Credential credential = context.getAuthParameters().getCredential();

        if (credential != null) {
            return context.notifyContainerAboutLogin(identityStore.validate(credential));
        } else {
            return context.doNothing();
        }
    }


}
