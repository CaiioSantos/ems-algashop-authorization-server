package com.algaworks.algashop.authorizationserver.application.user.management;

import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUser;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUserManagementApplicationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserOutput createUser(AuthUserInput input) {
        if (authUserRepository.existsByEmail(input.getEmail())) {
            throw new AuthUserEmailAlreadyInUseException("User with email " + input.getEmail() + " already exists");
        }
        String tempPassword = RandomStringUtils.secure().nextAlphanumeric(12);
        System.out.println("Temporary password for user " + input.getEmail() + ": " + tempPassword);
        String passwordHash = passwordEncoder.encode(tempPassword);
        AuthUser user = AuthUser.brandNew(
                input.getName(), input.getEmail(), passwordHash, input.getType()
        );
        return AuthUserOutput.from(authUserRepository.save(user));
    }
}
