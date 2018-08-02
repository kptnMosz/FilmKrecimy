package pl.wojtektrzos.filmkrecimy.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ownerPlanItem")
    private List<EventDate> eventDates;

    @Getter
    @Setter
    private LocalDate availibleAfter;

    @Getter
    @Setter
    private LocalDate availibleBefore;


    public void notifyMe(PlanItem who, String message){
    //todo ewentualne powiadomienia
    }

    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<PlanItemRole> getPlanItemRoles() {

        return planItemRoles;
    }
    public List<String> getPlanItemRoleNames() {
        return planItemRoles.stream()
                .map(r->r.getName())
                .collect(Collectors.toList());
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

    @Override
    public String toString() {
        return "PlanItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}\n<br />";
    }
}
