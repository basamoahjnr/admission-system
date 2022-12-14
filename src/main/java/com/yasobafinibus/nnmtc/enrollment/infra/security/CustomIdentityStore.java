package com.yasobafinibus.nnmtc.enrollment.infra.security;

import com.yasobafinibus.nnmtc.enrollment.infra.service.UserService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.credential.CallerOnlyCredential;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.PasswordHash;

import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;


@Named
@RequestScoped
public class CustomIdentityStore implements IdentityStore {

    @EJB
    UserService userService;

    @Inject
    PasswordHash passwordHash;

    @Override
    public CredentialValidationResult validate(Credential credential) {

        if (credential instanceof UsernamePasswordCredential) {
            String username = ((UsernamePasswordCredential) credential).getCaller();
            String password = ((UsernamePasswordCredential) credential).getPasswordAsString();

            return getUserService().findByUsername(username)
                    .filter(u -> passwordHash.verify(password.toCharArray(), u.getPassword()))
                    .map(u -> new CredentialValidationResult(u.getUsername(),
                            u.getRoles().stream().map(Enum::toString).collect(Collectors.toSet())))
                    .orElse(INVALID_RESULT);
        }

        if (credential instanceof CallerOnlyCredential) {
            String username = ((CallerOnlyCredential) credential).getCaller();
            return getUserService().findByUsername(username)
                    .map(u -> new CredentialValidationResult(u.getUsername(),
                            u.getRoles().stream().map(Enum::toString).collect(Collectors.toSet())))
                    .orElse(INVALID_RESULT);
        }

        return NOT_VALIDATED_RESULT;
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return validationResult.getCallerGroups();
    }


    public UserService getUserService() {
        return this.userService;
    }
}
