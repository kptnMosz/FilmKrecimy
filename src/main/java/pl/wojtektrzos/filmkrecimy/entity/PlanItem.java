package pl.wojtektrzos.filmkrecimy.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="plan_items")
public class PlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany
    private List<Prerequisite> prerequisites;
    @ManyToMany
    private List<PlanItem> observers;
    private String name;
    @ManyToMany
    private List<PlanItemRole> planItemRoles;
    @OneToMany(fetch = FetchType.EAGER)
    private List<EventDate> eventDates;

    private LocalDate doneBy;

    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<PlanItemRole> getPlanItemRoles() {
        return planItemRoles;
    }

    public void setPlanItemRoles(List<PlanItemRole> planItemRoles) {
        this.planItemRoles = planItemRoles;
    }

    public List<EventDate> getEventDates() {
        return eventDates;
    }

    public void setEventDates(List<EventDate> eventDates) {
        this.eventDates = eventDates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlanItem> getObservers() {
        return observers;
    }

    public void setObservers(List<PlanItem> observers) {
        this.observers = observers;
    }


    public PlanItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
