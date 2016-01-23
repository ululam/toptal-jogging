package com.toptal.entrance.alexeyz.repo;

import com.toptal.entrance.alexeyz.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);
}
