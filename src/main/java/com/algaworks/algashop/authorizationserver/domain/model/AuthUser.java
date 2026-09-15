package com.algaworks.algashop.authorizationserver.domain.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "auth_user")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
public class AuthUser extends AbstractAuditableAggregateRoot<AuthUser> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String email;
    private String password;
    private String name;
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private AuthUserType type;


}
