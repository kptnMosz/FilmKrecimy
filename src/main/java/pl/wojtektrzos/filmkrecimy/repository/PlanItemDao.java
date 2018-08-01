package pl.wojtektrzos.filmkrecimy.repository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Transactional
@Repository
public class PlanItemDao {
    @Autowired
    public PlanItemRepository planItemRepository;
    @PersistenceContext
    EntityManager entityManager;

    public PlanItem getByIdWithAvailibleDates(long id) {
        PlanItem planItem= entityManager.find(PlanItem.class, id);
        Hibernate.initialize(planItem.getEventDates());
        return planItem;
    }

    public PlanItem getByIdWithAvailibleDatesPrerequisitesAndObservers(long id) {
        PlanItem planItem= entityManager.find(PlanItem.class, id);
        Hibernate.initialize(planItem.getObservers());
        Hibernate.initialize(planItem.getEventDates());
        Hibernate.initialize(planItem.getPrerequisites());
        return planItem;
    }



}
