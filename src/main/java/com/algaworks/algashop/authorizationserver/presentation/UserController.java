package com.algaworks.algashop.authorizationserver.presentation;

import com.algaworks.algashop.authorizationserver.application.user.management.AuthUserInput;
import com.algaworks.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.algaworks.algashop.authorizationserver.application.user.management.AuthUserUpdateInput;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserFilter;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserQueryService;
import com.algaworks.algashop.authorizationserver.application.user.query.PageModel;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.algaworks.algashop.authorizationserver.infrastructure.security.check.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthUserManagementApplicationService managementService;
    private final AuthUserQueryService queryService;

    @PostMapping
    @SecurityAnnotations.CanWriteUser
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserOutput create(@RequestBody @Valid AuthUserInput input) {
        return managementService.createUser(input);
    }

    @SecurityAnnotations.CanReadUser
    @GetMapping
    public PageModel<AuthUserOutput> findAll(AuthUserFilter filter) {
        return queryService.findAll(filter);
    }

    @SecurityAnnotations.CanReadUser
    @GetMapping("/{userId}")
    public AuthUserOutput findById(@PathVariable UUID userId) {
        return queryService.findById(userId);
    }

    @SecurityAnnotations.CanWriteUser
    @PutMapping("/{userId}")
    public AuthUserOutput update(@PathVariable UUID userId, @RequestBody AuthUserUpdateInput input) {
        return managementService.updateUser(userId, input);
    }

    @SecurityAnnotations.CanWriteUser
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId) {
        managementService.deleteUser(userId);
    }
}