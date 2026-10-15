package com.algaworks.algashop.authorizationserver.domain.model.user;

import com.algaworks.algashop.authorizationserver.domain.model.AbstractAuditableAggregateRoot;
import com.algaworks.algashop.authorizationserver.domain.model.DomainException;
import com.algaworks.algashop.authorizationserver.domain.model.IdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "auth_user")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUser extends AbstractAuditableAggregateRoot<AuthUser> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String email;
    private String password;
    private String name;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private AuthUserType type;


    public static AuthUser brandNew( String name,   String email, String passwordHash,  AuthUserType type) {
        AuthUser user = new AuthUser();
        user.setId(IdGenerator.generateTimeBasedUUID());
        user.setName(name);
        user.setEmail(email);
        user.setType(type);
        user.setPassword(passwordHash);
        user.setEnabled(true);
        return user;
    }

    public void setId(UUID id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public void setEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException();
        }
        this.password = password;
    }

    public void setName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setType(AuthUserType type) {
        Objects.requireNonNull(type);
        if (this.type == AuthUserType.CUSTOMER) {
            throw new DomainException("Cannot change type of a CUSTOMER user");
        }
        this.type = type;
    }
}
