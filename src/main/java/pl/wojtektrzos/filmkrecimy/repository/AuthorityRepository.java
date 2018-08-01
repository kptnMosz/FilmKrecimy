package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojtektrzos.filmkrecimy.entity.Authority;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

        Authority findByName(String name);

}
