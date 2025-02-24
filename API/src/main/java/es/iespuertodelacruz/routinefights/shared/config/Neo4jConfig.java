package es.iespuertodelacruz.routinefights.shared.config;

import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.context.annotation.Bean;

/**
 * Neo4jConfig
 */
@org.springframework.context.annotation.Configuration
public class Neo4jConfig {
    /**
     * Method to configure the cypher dsl
     * 
     * @return Configuration
     */
    @Bean
    Configuration cypherDslConfiguration() {
        return Configuration.newConfig()
                .withDialect(Dialect.NEO4J_5)
                .build();
    }
}
