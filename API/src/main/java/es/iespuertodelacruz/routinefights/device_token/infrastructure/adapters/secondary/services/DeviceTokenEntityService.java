package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.secondary.IDeviceTokenRepository;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.mappers.IDeviceTokenEntityMapper;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.repositories.IDeviceTokenEntityRepository;

@Service
public class DeviceTokenEntityService implements IDeviceTokenRepository {
    private IDeviceTokenEntityRepository deviceTokenEntityRepository;
    private IDeviceTokenEntityMapper deviceTokenEntityMapper;
    private Neo4jClient neo4jClient;

    public DeviceTokenEntityService(IDeviceTokenEntityRepository deviceTokenEntityRepository,
            IDeviceTokenEntityMapper deviceTokenEntityMapper, Neo4jClient neo4jClient) {
        this.deviceTokenEntityRepository = deviceTokenEntityRepository;
        this.deviceTokenEntityMapper = deviceTokenEntityMapper;
        this.neo4jClient = neo4jClient;
    }

    @Override
    public DeviceToken save(DeviceToken deviceToken) {
        if (deviceToken.getUser() == null || deviceToken.getUser().getEmail() == null) {
            DeviceTokenEntity entity = deviceTokenEntityMapper.toEntity(deviceToken);
            return deviceTokenEntityMapper.toDomain(deviceTokenEntityRepository.save(entity));
        }
        // Only the token is returned: mapping the linked node back would walk the
        // user <-> device cycle.
        deviceTokenEntityRepository.saveForUser(deviceToken.getUser().getEmail(), deviceToken.getToken(),
                deviceToken.getLanguage());
        return deviceToken;
    }

    @Override
    public List<DeviceToken> findByEmail(String email) {
        return deviceTokenEntityRepository.findTokensByEmail(email).stream()
                .map(row -> new DeviceToken(row.getToken(), null, row.getLanguage()))
                .toList();
    }

    @Override
    public void delete(String deviceToken) {
        deviceTokenEntityRepository.deleteToken(deviceToken);
    }

    @Override
    public Map<String, Long> countByLanguage() {
        Map<String, Long> counts = new LinkedHashMap<>();
        neo4jClient.query("""
                MATCH (d:DeviceToken)
                RETURN coalesce(d.language, 'unknown') AS language, count(d) AS total
                ORDER BY total DESC
                """)
                .fetch().all()
                .forEach(row -> counts.put(String.valueOf(row.get("language")), ((Number) row.get("total")).longValue()));
        return counts;
    }

}
