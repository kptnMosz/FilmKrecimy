package pl.wojtektrzos.filmkrecimy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.wojtektrzos.filmkrecimy.entity.User;
import pl.wojtektrzos.filmkrecimy.entity.UserDetails;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
UserDetails findUserDetailsByLogInfo(User user);
UserDetails findUserDetailsById(long id);
}
