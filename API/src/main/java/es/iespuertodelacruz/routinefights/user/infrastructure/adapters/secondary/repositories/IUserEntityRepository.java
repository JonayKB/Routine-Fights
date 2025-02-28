package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

@Repository
public interface IUserEntityRepository extends Neo4jRepository<UserEntity, String> {
    public UserEntity findByEmail(String email);

    public boolean existsByEmail(String email);
}
