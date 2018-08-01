package pl.wojtektrzos.filmkrecimy.service;

import pl.wojtektrzos.filmkrecimy.entity.User;

public interface UserService {
    User findByUserName(String name);
    void saveUser(User user);
}
