package com.yasobafinibus.nnmtc.enrollment.infra.security.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.*;

@Entity
@Cacheable
@Table(name = "users", indexes = {
        @Index(name = "i_username", columnList = "username")
})
@NamedQueries({
        @NamedQuery(name = "User.findByUserLoginToken",
                query = "select u from User u join u.loginTokens l where " +
                        "l.tokenHash= :tokenHash and " +
                        "l.type= :tokenType and " +
                        "l.expiredDate > current_timestamp order by l.createdOn"),
        @NamedQuery(name = "User.findByUsername",
                query = "select u from User u join fetch u.roles where u.username= :username")
})
public class User extends AbstractAuditEntity implements Serializable {


    private static final long serialVersionUID = 3433162327572738634L;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    List<LoginToken> loginTokens = new ArrayList<>();
    @Email
    @NotEmpty
    @Column(name = "USERNAME", nullable = false, updatable = false, unique = true)
    private String username;
    @NotEmpty
    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;
    @Column(name = "PASSWORD", nullable = true)
    private String password;
    @NotEmpty
    @Enumerated(EnumType.ORDINAL)
    @CollectionTable(name = "user_roles", joinColumns = {@JoinColumn(name = "USER_ID")})
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "ROLES", nullable = false)
    private Set<UserRole> roles;


    public void addRoles(@NotNull UserRole userRole) {
        if (roles == null) {
            this.roles = new HashSet<>();
        }
        roles.add(userRole);
    }


    public User(String username) {
        this.username = username;
    }

    public User() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVersion(), getUsername(), getPassword());
    }

    public List<LoginToken> getLoginTokens() {
        return this.loginTokens;
    }

    public @Email @NotEmpty String getUsername() {
        return this.username;
    }

    public @NotEmpty String getFullName() {
        return this.fullName;
    }

    public String getPassword() {
        return this.password;
    }

    public @NotEmpty Set<UserRole> getRoles() {
        return this.roles;
    }

    public void setLoginTokens(List<LoginToken> loginTokens) {
        this.loginTokens = loginTokens;
    }

    public void setUsername(@Email @NotEmpty String username) {
        this.username = username;
    }

    public void setFullName(@NotEmpty String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(@NotEmpty Set<UserRole> roles) {
        this.roles = roles;
    }

}
