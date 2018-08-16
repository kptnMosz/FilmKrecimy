package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.Prerequisite;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface PrerequisiteRepository extends JpaRepository<Prerequisite, Long> {
}
