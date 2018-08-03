package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtektrzos.filmkrecimy.entity.PlanItemRole;

import java.util.List;

public interface PlanItemRoleRepository extends JpaRepository<PlanItemRole, Long> {
    List<PlanItemRole> findAllByModifier(String modifier);
    PlanItemRole findPlanItemRoleByName(String name);
}
