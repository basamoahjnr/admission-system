package com.yasobafinibus.nnmtc.enrollment.infra.security;

import com.yasobafinibus.nnmtc.enrollment.infra.security.model.User;
import com.yasobafinibus.nnmtc.enrollment.infra.service.UserService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.credential.RememberMeCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.RememberMeIdentityStore;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yasobafinibus.nnmtc.enrollment.infra.security.model.LoginToken.TokenType.REMEMBER_ME;
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;


@ApplicationScoped
public class CustomRememberMeIdentityStore implements RememberMeIdentityStore {

    @Inject
    private HttpServletRequest request;


    @EJB
    private UserService userService;

    @Override
    public CredentialValidationResult validate(RememberMeCredential credential) {
        User byLoginToken = getUserService().findByLoginToken(credential.getToken(), REMEMBER_ME);
        if (Objects.nonNull(byLoginToken)) {
            return new CredentialValidationResult(new CallerPrincipal(byLoginToken.getUsername()),
                    byLoginToken.getRoles().stream().map(Enum::toString).collect(Collectors.toSet()));
        } else return INVALID_RESULT;

    }

    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> groups) {
        return getUserService().generate(callerPrincipal.getName(),
                request.getRemoteAddr(),
                getDescription(),
                REMEMBER_ME);//default expiration days is 14
    }

    @Override
    public void removeLoginToken(String loginToken) {
        getUserService().remove(loginToken);
    }

    private String getDescription() {
        return "Remember me session: " + request.getHeader("User-Agent");
    }

    public UserService getUserService() {
        return this.userService;
    }
}
