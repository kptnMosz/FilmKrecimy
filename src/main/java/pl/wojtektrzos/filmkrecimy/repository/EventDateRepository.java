package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.EventDate;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public interface EventDateRepository extends JpaRepository<EventDate, Long> {
    List<EventDate> findAllByOccupiedBy(PlanItem ocupied);
    EventDate findEventDateByOwnerPlanItemAndDate(PlanItem owner, LocalDate date);
    List<EventDate> findAllByOwnerPlanItem(PlanItem owner);
    List<EventDate> findAllByOwnerPlanItemAndOccupiedByIsNotNull(PlanItem owner);
}
