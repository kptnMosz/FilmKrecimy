package pl.wojtektrzos.filmkrecimy.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import javax.transaction.Transactional;


@Repository
@Transactional
public interface PlanItemRepository extends JpaRepository<PlanItem, Long> {

}
