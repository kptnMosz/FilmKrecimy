package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.Activity;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByMovie(Movie movie);
}
