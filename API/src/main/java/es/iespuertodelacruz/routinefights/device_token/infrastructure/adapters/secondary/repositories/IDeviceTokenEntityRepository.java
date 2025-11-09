package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;

@Repository
public interface IDeviceTokenEntityRepository extends Neo4jRepository<DeviceTokenEntity, String> {

}
