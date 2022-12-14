package com.yasobafinibus.nnmtc.enrollment.infra.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserInfo implements Serializable {

    private String name;
    private Set<String> roles;

    public UserInfo(String name) {
        this.name = name;
    }

    public UserInfo(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public void addUserInfoRoles(String role) {
        if (Objects.isNull(roles)) {
            roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public UserInfo() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
