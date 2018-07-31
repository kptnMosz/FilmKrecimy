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
    @ManyToOne
    private PlanItem planItem;
    @ManyToOne
    private PlanItem occupiedBy;




}
