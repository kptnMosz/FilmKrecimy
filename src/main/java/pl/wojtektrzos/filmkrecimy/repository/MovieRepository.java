package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.UserDetails;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOwner(UserDetails owner);

}
