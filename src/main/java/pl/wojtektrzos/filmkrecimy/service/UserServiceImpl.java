package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.Authority;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.User;
import pl.wojtektrzos.filmkrecimy.entity.UserDetails;
import pl.wojtektrzos.filmkrecimy.repository.AuthorityRepository;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRepository;
import pl.wojtektrzos.filmkrecimy.repository.UserDetailsRepository;
import pl.wojtektrzos.filmkrecimy.repository.UserRepository;
import pl.wojtektrzos.filmkrecimy.util.EnterLog;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PlanItemRepository planItemRepository;

    public UserServiceImpl(UserRepository userRepository,
                           AuthorityRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           UserDetailsRepository userDetailsRepository,
                           PlanItemRepository planItemRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.planItemRepository = planItemRepository;
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Authority userRole = roleRepository.findByName("ROLE_USER");
        user.setAuthorities(new HashSet<Authority>(Arrays.asList(userRole)));
        UserDetails userDetails = new UserDetails();
        userDetailsRepository.save(userDetails);
        user.setDetails(userDetails);
        PlanItem mySelf = new PlanItem();
        planItemRepository.save(mySelf);
        userDetails.setPlanMyself(mySelf);
        userRepository.save(user);
        userDetails.setLogInfo(user);
        userDetailsRepository.save(userDetails);
    }
}
