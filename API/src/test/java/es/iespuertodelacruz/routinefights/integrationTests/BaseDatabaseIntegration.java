package es.iespuertodelacruz.routinefights.integrationTests;

import org.junit.jupiter.api.*;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories.IUserEntityRepository;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class BaseDatabaseIntegration {

    private static final String ROLE = "ROLE_USER";


    private static final String EMAIL = "jonaykb@gmail.com";


    protected static Neo4j embeddedDatabaseServer;


    @Autowired
    private IUserEntityRepository userRepository;

    @BeforeAll
    static void initializeNeo4j() {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }

    @AfterAll
    static void stopNeo4j() {
        embeddedDatabaseServer.close();
    }

    @BeforeEach
    @Transactional
    void setupTestData() {
        UserEntity user = new UserEntity();
        user.setUsername("JonayKB");
        user.setRole(ROLE);
        user.setVerified(true);
        user.setCreatedAt(LocalDateTime.parse("2025-04-29T17:53:39.430780697"));
        user.setUpdatedAt(LocalDateTime.parse("2025-04-29T18:36:39.651213733"));
        user.setPassword("$2a$10$.SpWMpH2w1jQzcg6S1Gb1eImmFmnoJavACHfgqCtAMQANHQ4Vah5W");
        user.setNationality("Spain");
        user.setPhoneNumber("+34648742361");
        user.setEmail(EMAIL);

            
        userRepository.save(user);
    }

    @BeforeEach
    void setupSecurityContext() {
        var authentication = new UsernamePasswordAuthenticationToken(
                EMAIL, null,
                List.of(new SimpleGrantedAuthority(ROLE)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
