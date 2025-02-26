package es.iespuertodelacruz.routinefights.shared.config;

import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableNeo4jRepositories(basePackages = "es.iespuertodelacruz.routinefights")
public class Neo4jRepositoryConfig {
    
}
