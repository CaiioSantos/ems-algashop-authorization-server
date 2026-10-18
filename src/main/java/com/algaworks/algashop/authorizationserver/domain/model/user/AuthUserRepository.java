package com.algaworks.algashop.authorizationserver.domain.model.user;

import aj.org.objectweb.asm.commons.Remapper;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<AuthUser> findAllByEmailAndType(String email, AuthUserType type, Pageable pageable);
}
