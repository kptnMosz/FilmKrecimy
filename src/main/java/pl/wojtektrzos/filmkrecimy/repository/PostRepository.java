package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findAllByMovie(Movie movie);
}
