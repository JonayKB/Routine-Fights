package es.iespuertodelacruz.routinefights.category.infrastructure.adapters.secondary.entities;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;

import java.util.List;
import java.util.Objects;

@Node("Category")
/**
 * CategoryEntity
 */
public class CategoryEntity {
    @Id
    @GeneratedValue
    private String id;
    private String name;

    @Relationship(type = "Belongs_To", direction = Relationship.Direction.INCOMING)
    private List<ActivityEntity> activities;

    public CategoryEntity() {
    }

    public CategoryEntity(String id, String name, List<ActivityEntity> activities) {
        this.id = id;
        this.name = name;
        this.activities = activities;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ActivityEntity> getActivities() {
        return this.activities;
    }

    public void setActivity(List<ActivityEntity> activity) {
        this.activities = activity;
    }

    public CategoryEntity id(String id) {
        setId(id);
        return this;
    }

    public CategoryEntity name(String name) {
        setName(name);
        return this;
    }

    public CategoryEntity activity(List<ActivityEntity> activity) {
        setActivity(activity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CategoryEntity)) {
            return false;
        }
        CategoryEntity categoryEntity = (CategoryEntity) o;
        return Objects.equals(id, categoryEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", activity='" + getActivities() + "'" +
                "}";
    }

}
