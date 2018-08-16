package pl.wojtektrzos.filmkrecimy.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "plan_items")
public class PlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany
    private List<Prerequisite> prerequisites;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "plan_items_observers", joinColumns = @JoinColumn(name = "plan_item_id"),
            inverseJoinColumns = @JoinColumn(name = "observers_id"))
    private Set<PlanItem> observers;
    private String name;
    @ManyToMany
    private Set<Movie> movie;
    @ManyToMany
    private List<PlanItemRole> planItemRoles;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ownerPlanItem")
    private List<EventDate> eventDates;
    @ManyToOne
    private UserDetails owner;
    @ManyToOne
    private Activity activity;
    private LocalDate availibleAfter;
    private LocalDate availibleBefore;


    public PlanItem(String name, List<PlanItemRole> planItemRoles, List<EventDate> eventDates, UserDetails owner, Activity activity, LocalDate availibleAfter, LocalDate availibleBefore) {
        this.name = name;
        this.planItemRoles = planItemRoles;
        this.eventDates = eventDates;
        this.owner = owner;
        this.activity = activity;
        this.availibleAfter = availibleAfter;
        this.availibleBefore = availibleBefore;
    }

    public PlanItem() {
    }

    public void notifyMe(PlanItem who, String message) {
        //todo ewentualne powiadomienia
    }

    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
    }
    public void addPrerequisites(Prerequisite prerequisite){
        if(this.prerequisites==null){
            this.prerequisites = new ArrayList<>();
        }
        prerequisites.add(prerequisite);
    }

    public List<PlanItemRole> getPlanItemRoles() {

        return planItemRoles;
    }

    public List<String> getPlanItemRoleNames() {
        return planItemRoles.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());
    }

    public void setPlanItemRoles(List<PlanItemRole> planItemRoles) {
        this.planItemRoles = planItemRoles;
    }

    public void setPlanItemRoles(PlanItemRole planItemRole) {
        this.planItemRoles = new ArrayList<>();
        planItemRoles.add(planItemRole);
    }

    public List<EventDate> getEventDates() {
        return eventDates;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlanItemRole(PlanItemRole role) {
        this.planItemRoles.add(role);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addObserver(PlanItem observer) {
        if (this.observers == null) {
            this.observers = new HashSet<>();
        }
        this.observers.add(observer);
    }

    @Override
    public String toString() {
        return "PlanItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}\n<br />";
    }
}
