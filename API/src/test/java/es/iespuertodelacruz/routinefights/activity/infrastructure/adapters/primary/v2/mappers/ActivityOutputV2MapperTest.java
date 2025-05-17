package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;

@SpringBootTest()
class ActivityOutputV2MapperTest {
    private static final User USER = new User();
    private static final List<User> LIST = List.of(USER);
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final int TIMES_REQUIRED = 1;
    private static final String TIME_RATE = "timeRate";
    private static final String IMAGE = "image";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String ID = "1";
    Activity activity;
    ActivityOutputV2 activityOutputV2;
    @Autowired
    private ActivityOutputV2Mapper activityOutputV2Mapper;

    @BeforeEach
     void setUp() {
        activity = new Activity(ID, NAME, DESCRIPTION, IMAGE, TIME_RATE, TIMES_REQUIRED, NOW,
                NOW, NOW, USER, LIST);
                activityOutputV2 = new ActivityOutputV2(ID,NAME,DESCRIPTION,IMAGE,null,TIME_RATE,TIMES_REQUIRED,NOW);
    }

    @Test
    void testToActivityOutputV2() {
        ActivityOutputV2 activityOutputV2Test = activityOutputV2Mapper.toDTO(activity);
        assertEquals(activity.getId(), activityOutputV2Test.id());
        assertEquals(activity.getName(), activityOutputV2Test.name());
        assertEquals(activity.getDescription(), activityOutputV2Test.description());
        assertEquals(activity.getImage(), activityOutputV2Test.image());
        assertEquals(activity.getTimeRate(), activityOutputV2Test.timeRate());
        assertEquals(activity.getTimesRequiered(), activityOutputV2Test.timesRequiered());
        assertEquals(activity.getCreatedAt(), activityOutputV2Test.createdAt());
        assertTrue(activityOutputV2Test.creator() instanceof UserOutputDTOV2);
    }

    @Test
    void testToActivityOutputV2List() {
        List<ActivityOutputV2> activityOutputV2List = activityOutputV2Mapper.toDTO(List.of(activity));
        assertEquals(activity.getId(), activityOutputV2List.get(0).id());
        assertEquals(activity.getName(), activityOutputV2List.get(0).name());
        assertEquals(activity.getDescription(), activityOutputV2List.get(0).description());
        assertEquals(activity.getImage(), activityOutputV2List.get(0).image());
        assertEquals(activity.getTimeRate(), activityOutputV2List.get(0).timeRate());
        assertEquals(activity.getTimesRequiered(), activityOutputV2List.get(0).timesRequiered());
        assertEquals(activity.getCreatedAt(), activityOutputV2List.get(0).createdAt());
        assertTrue(activityOutputV2List.get(0).creator() instanceof UserOutputDTOV2);
    }
}