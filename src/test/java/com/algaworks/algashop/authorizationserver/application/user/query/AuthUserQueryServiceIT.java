package com.algaworks.algashop.authorizationserver.application.user.query;

import com.algaworks.algashop.authorizationserver.application.security.SecurityCheckApplicationService;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUser;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.algaworks.algashop.authorizationserver.infrastructure.persistence.AuthUserQueryServiceImpl;
import com.algaworks.algashop.authorizationserver.infrastructure.persistence.SpringDataAuditingConfig;
import com.algaworks.algashop.authorizationserver.utils.TestContainerPostgresSqlConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        TestContainerPostgresSqlConfig.class,
        SpringDataAuditingConfig.class,
        AuthUserQueryServiceImpl.class
})
class AuthUserQueryServiceIT {

    private static final String DEFAULT_PASSWORD = "{noop}123456";

    @Autowired
    private AuthUserQueryService queryService;

    @Autowired
    private AuthUserRepository authUserRepository;

    // Needed by the auditing configuration; no authenticated user in these tests
    @MockitoBean
    private SecurityCheckApplicationService securityCheckApplicationService;

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
    }

    @Test
    void shouldFindUserById() {
        AuthUser persisted = saveUser("John Doe", "john.doe@email.com", AuthUserType.CUSTOMER);

        AuthUserOutput output = queryService.findById(persisted.getId());

        assertThat(output.getId()).isEqualTo(persisted.getId());
        assertThat(output.getName()).isEqualTo("John Doe");
        assertThat(output.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(output.getType()).isEqualTo(AuthUserType.CUSTOMER);
        assertThat(output.isEnabled()).isTrue();
    }

    @Test
    void shouldThrowNotFoundWhenUserIdDoesNotExist() {
        UUID unknownId = UUID.randomUUID();

        assertThatThrownBy(() -> queryService.findById(unknownId))
                .isInstanceOf(AuthUserNotFoundException.class)
                .hasMessageContaining("User not found with id: " + unknownId);
    }

    @Test
    void shouldReturnOnlyUsersMatchingEmailFilter() {
        saveUser("John Doe", "john.doe@email.com", AuthUserType.CUSTOMER);
        saveUser("Victoria Garcia", "victoria.garcia@algashop.com", AuthUserType.MANAGER);
        saveUser("Jefferson Roman", "jeff.roman@algashop.com", AuthUserType.OPERATOR);

        AuthUserFilter filter = new AuthUserFilter();
        filter.setEmail("ALGASHOP");

        PageModel<AuthUserOutput> page = queryService.findAll(filter);

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(AuthUserOutput::getEmail)
                .containsExactlyInAnyOrder("victoria.garcia@algashop.com", "jeff.roman@algashop.com");
    }

    @Test
    void shouldPaginateUsersFilteredByType() {
        for (int i = 1; i <= 5; i++) {
            saveUser("Customer " + i, "customer" + i + "@email.com", AuthUserType.CUSTOMER);
        }
        saveUser("Manager", "manager@algashop.com", AuthUserType.MANAGER);

        AuthUserFilter filter = new AuthUserFilter();
        filter.setType(AuthUserType.CUSTOMER);
        filter.setSize(2);

        filter.setPage(0);
        PageModel<AuthUserOutput> firstPage = queryService.findAll(filter);

        assertThat(firstPage.getNumber()).isZero();
        assertThat(firstPage.getSize()).isEqualTo(2);
        assertThat(firstPage.getTotalElements()).isEqualTo(5);
        assertThat(firstPage.getTotalPages()).isEqualTo(3);
        assertThat(firstPage.getContent()).hasSize(2);

        filter.setPage(1);
        PageModel<AuthUserOutput> secondPage = queryService.findAll(filter);
        assertThat(secondPage.getNumber()).isEqualTo(1);
        assertThat(secondPage.getContent()).hasSize(2);

        filter.setPage(2);
        PageModel<AuthUserOutput> lastPage = queryService.findAll(filter);
        assertThat(lastPage.getNumber()).isEqualTo(2);
        assertThat(lastPage.getContent()).hasSize(1);

        List<AuthUserOutput> allContent = List.of(firstPage, secondPage, lastPage).stream()
                .flatMap(p -> p.getContent().stream())
                .toList();

        assertThat(allContent)
                .hasSize(5)
                .allMatch(user -> user.getType() == AuthUserType.CUSTOMER)
                .extracting(AuthUserOutput::getName)
                .doesNotHaveDuplicates()
                .containsExactlyInAnyOrder("Customer 1", "Customer 2", "Customer 3", "Customer 4", "Customer 5");
    }

    private AuthUser saveUser(String name, String email, AuthUserType type) {
        return authUserRepository.saveAndFlush(AuthUser.brandNew(name, email, DEFAULT_PASSWORD, type));
    }
}

