package es.iespuertodelacruz.routinefights.integrationTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.controllers.ActivityControllerV2;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityInputV2;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2;

class ActivityV2ControllerIntegrationTest extends BaseDatabaseIntegration {

    private static final String CATEGORY_ID = "0";
    private static final int TIMES_REQUIERED = 3;
    private static final String TIME_RATE = "daily";
    private static final String IMAGE = "image";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";

    
    @Autowired
    private ActivityControllerV2 activityControllerV2;

    @Test
    void createActivity() {
        ActivityInputV2 input = new ActivityInputV2(NAME, DESCRIPTION, IMAGE, TIME_RATE, TIMES_REQUIERED, CATEGORY_ID);
        ActivityOutputV2 output = activityControllerV2.createActivity(input);

        assertNotNull(output);
        assertEquals(NAME, output.name());
        assertEquals(DESCRIPTION, output.description());
        assertEquals(IMAGE, output.image());
        assertEquals(TIME_RATE, output.timeRate());
        assertEquals(TIMES_REQUIERED, output.timesRequiered());
    }
}
