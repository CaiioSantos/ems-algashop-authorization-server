package com.algaworks.algashop.authorizationserver.infrastructure.persistence;

import com.algaworks.algashop.authorizationserver.infrastructure.security.query.OAuth2AuthorizationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcOAuth2AuthorizationQueryService implements OAuth2AuthorizationQueryService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> findAuthorizationsIds(String principalName) {
        String sql = "SELECT id FROM oauth2_authorization WHERE principal_name = ?";
        return jdbcTemplate.queryForList(sql, String.class, principalName);
    }
}
