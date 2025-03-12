package es.iespuertodelacruz.routinefights.category.infrastructure.adapters.secondary.entities;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CategoryEntityTest {

    private static final String ID1 = "cat1";
    private static final String ID2 = "cat2";
    private static final String NAME = "Test Category";
    private static final List<ActivityEntity> ACTIVITIES = new ArrayList<>();

    @Test
    void testSettersAndGetters() {
        CategoryEntity category = new CategoryEntity();
        category.setId(ID1);
        category.setName(NAME);
        category.setActivity(ACTIVITIES);
        assertEquals(ID1, category.getId());
        assertEquals(NAME, category.getName());
        assertEquals(ACTIVITIES, category.getActivities());
    }

    @Test
    void testConstructorAndGetters() {
        CategoryEntity category = new CategoryEntity(ID1, NAME, ACTIVITIES);
        assertEquals(ID1, category.getId());
        assertEquals(NAME, category.getName());
        assertEquals(ACTIVITIES, category.getActivities());
    }

    @Test
    void testFluentMethods() {
        CategoryEntity category = new CategoryEntity();
        category.id(ID1).name(NAME).activity(ACTIVITIES);
        assertEquals(ID1, category.getId());
        assertEquals(NAME, category.getName());
        assertEquals(ACTIVITIES, category.getActivities());
    }

    @Test
    void testEqualsAndHashCode() {
        CategoryEntity category1 = new CategoryEntity();
        CategoryEntity category2 = new CategoryEntity();
        assertEquals(category1, category2);
        category1.setId(ID1);
        category2.setId(ID1);
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
        category2.setId(ID2);
        assertNotEquals(category1, category2);
        assertNotEquals(category1.hashCode(), category2.hashCode());
        assertNotEquals(category1, new Object());
        assertEquals(category1, category1);
    }

    @Test
    void testToString() {
        CategoryEntity category = new CategoryEntity(ID1, NAME, ACTIVITIES);
        String expected = "{" +
                " id='" + ID1 + "'" +
                ", name='" + NAME + "'" +
                ", activity='" + ACTIVITIES + "'" +
                "}";
        assertEquals(expected, category.toString());
    }
}
