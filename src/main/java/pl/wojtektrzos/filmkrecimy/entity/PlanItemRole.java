package pl.wojtektrzos.filmkrecimy.entity;

import lombok.ToString;

import javax.persistence.*;
@ToString
@Entity
@Table(name="plan_item_roles")
public class PlanItemRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String modifier; // pole modyfikujące dostęp

    public PlanItemRole() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
