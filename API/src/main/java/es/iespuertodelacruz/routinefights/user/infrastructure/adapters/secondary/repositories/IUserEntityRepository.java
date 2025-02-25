package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

public interface IUserEntityRepository extends Neo4jRepository<UserEntity, Long> {
    
}
