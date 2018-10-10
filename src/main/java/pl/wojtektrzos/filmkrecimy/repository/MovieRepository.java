package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.UserDetails;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Transactional
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOwner(UserDetails owner);
    Set<Movie> findDistinctByObserversContaining(PlanItem observer);

}
