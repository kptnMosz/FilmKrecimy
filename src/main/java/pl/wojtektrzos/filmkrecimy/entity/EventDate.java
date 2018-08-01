package pl.wojtektrzos.filmkrecimy.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "event_dates")
public class EventDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    private PlanItem occupiedBy;

    public EventDate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public PlanItem getOccupiedBy() {
        return occupiedBy;
    }

    public long getOccupiedById() {
        return occupiedBy.getId();
    }

    public void setOccupiedBy(PlanItem occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    @Override
    public String toString() {
        return "EventDate{" +
                "date=" + date +
                "ocupied by=" + occupiedBy.getId() +
                '}';
    }
}
