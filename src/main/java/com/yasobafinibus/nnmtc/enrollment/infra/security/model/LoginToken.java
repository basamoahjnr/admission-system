package com.yasobafinibus.nnmtc.enrollment.infra.security.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

import static jakarta.persistence.EnumType.STRING;


@Entity
@Cacheable
@Table(name = "login_tokens")
@NamedQuery(name = "LoginToken.remove", query = "delete from LoginToken lt  where lt.tokenHash=:tokenHash")
@NamedQuery(name = "LoginToken.removeExpired", query = "delete from LoginToken lt where lt.expiredDate < current_timestamp")
public class LoginToken extends AbstractAuditEntity implements Serializable {

    private static final long serialVersionUID = -1271518840679154154L;
    @Column(unique = true, name = "TOKEN_HASH", length = 800)
    private byte[] tokenHash;
    @Column(name = "EXPIRED_DATE")
    private LocalDateTime expiredDate;
    @Column(length = 45, name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;
    @Column(name = "TOKEN_TYPE")
    @Enumerated(STRING)
    private TokenType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginToken that = (LoginToken) o;

        if (!getId().equals(that.getId())) return false;
        return Arrays.equals(this.getTokenHash(), that.getTokenHash());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + Arrays.hashCode(getTokenHash());
        return result;
    }

    public byte[] getTokenHash() {
        return this.tokenHash;
    }

    public LocalDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getDescription() {
        return this.description;
    }

    public User getUser() {
        return this.user;
    }

    public TokenType getType() {
        return this.type;
    }

    public void setTokenHash(byte[] tokenHash) {
        this.tokenHash = tokenHash;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public enum TokenType {
        REMEMBER_ME,
        API,
        RESET_PASSWORD
    }

}
