package com.toptal.entrance.alexeyz.rest;

import com.toptal.entrance.alexeyz.db.UserRepository;
import com.toptal.entrance.alexeyz.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author alexey.zakharchenko@gmail.com
 *
 */
@RestController
@RequestMapping("/rest/user/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/list")
    @PreAuthorize("hasAuthority('manager')")
    public ResponseEntity<List<User>> list() {
        return new ResponseEntity<>(userRepository.findAll(), OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    // Anyone may create users
    public ResponseEntity<User> create(@RequestBody User user) {
        User existing = userRepository.findByLogin(user.getLogin());
        if (existing != null)
            throw new IllegalArgumentException("User with the given login already exists: " + user.getLogin());

        user = userRepository.save(user);

        return new ResponseEntity<>(user, OK);
    }

    @PreAuthorize("hasAuthority('manager')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        User user = userRepository.findOne(id);
        // Don't delete admins
        if (user.isAdmin())
            throw new IllegalArgumentException("Its prohibited to delete Admins");

        // Don't delete yourself
        if (currentUser().getId().equals(id))
            throw new IllegalArgumentException("You cannot delete yourself");


        userRepository.delete(id);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('manager')")
    public ResponseEntity<User> update(@RequestBody User user) {
        User existing = userRepository.findByLogin(user.getLogin());
        if (existing != null)
            throw new IllegalArgumentException("User with the given login already exists: " + user.getLogin());

        User current = currentUser();
        if (!current.isManager() && current.getId().equals(user.getId()))
            // For simple users, its prohibited to update others
            throw new IllegalAccessError("You should be at least Manager to update others");

        user = userRepository.save(user);

        return new ResponseEntity<>(user, OK) ;
    }


    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByLogin(auth.getName());
    }

}
