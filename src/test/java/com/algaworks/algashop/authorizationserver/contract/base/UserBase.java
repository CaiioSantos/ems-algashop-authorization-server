package com.algaworks.algashop.authorizationserver.contract.base;

import com.algaworks.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserFilter;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserNotFoundException;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserQueryService;
import com.algaworks.algashop.authorizationserver.application.user.query.PageModel;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.algaworks.algashop.authorizationserver.presentation.UserController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = UserController.class)
public class UserBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private AuthUserQueryService authUserQueryService;

    @MockitoBean
    private AuthUserManagementApplicationService authUserManagementApplicationService;

    public static final UUID validUserId = UUID.fromString("0c9b6f3e-5a2d-4b8e-9f1a-7d3c2e1b4a50");

    public static final UUID otherUserId = UUID.fromString("7a1b2c3d-4e5f-4a6b-8c7d-9e0f1a2b3c4d");

    public static final UUID invalidUserId = UUID.fromString("5f8e2a1c-9b3d-4e7f-8a6c-1d2e3f4a5b6c");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("SCOPE_users:read"),
                                new SimpleGrantedAuthority("SCOPE_users:write")))
                        .with(csrf()))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
        );

        mockFindAllUsers();
        mockFindUserById();
        mockFindUserByIdNotFound();
        mockDeleteUser();
        mockDeleteUserNotFound();
    }

    private void mockFindAllUsers() {
        Mockito.when(authUserQueryService.findAll(any(AuthUserFilter.class)))
                .thenReturn(PageModel.<AuthUserOutput>builder()
                        .number(0)
                        .size(2)
                        .totalPages(1)
                        .totalElements(2)
                        .content(List.of(
                                aUser(validUserId, "Joao Silva", "joao@algashop.com", AuthUserType.CUSTOMER),
                                aUser(otherUserId, "Maria Souza", "maria@algashop.com", AuthUserType.OPERATOR)
                        ))
                        .build());
    }

    private void mockFindUserById() {
        Mockito.when(authUserQueryService.findById(validUserId))
                .thenReturn(aUser(validUserId, "Joao Silva", "joao@algashop.com", AuthUserType.CUSTOMER));
    }

    private void mockFindUserByIdNotFound() {
        Mockito.when(authUserQueryService.findById(invalidUserId))
                .thenThrow(new AuthUserNotFoundException("User not found with id: " + invalidUserId));
    }

    private void mockDeleteUser() {
        Mockito.doNothing().when(authUserManagementApplicationService).deleteUser(validUserId);
    }

    private void mockDeleteUserNotFound() {
        Mockito.doThrow(new AuthUserNotFoundException("User not found with id: " + invalidUserId))
                .when(authUserManagementApplicationService).deleteUser(invalidUserId);
    }

    private AuthUserOutput aUser(UUID id, String name, String email, AuthUserType type) {
        return AuthUserOutput.builder()
                .id(id)
                .name(name)
                .email(email)
                .type(type)
                .enabled(true)
                .build();
    }
}

