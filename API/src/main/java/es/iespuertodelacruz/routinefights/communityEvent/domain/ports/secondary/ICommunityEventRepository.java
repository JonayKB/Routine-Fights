package es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;

public interface ICommunityEventRepository {

    CommunityEvent save(CommunityEvent entity);

    
}
