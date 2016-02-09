package com.toptal.entrance.alexeyz.rest;

import com.toptal.entrance.alexeyz.Application;
import com.toptal.entrance.alexeyz.db.UserRepository;
import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.rest.security.jwt.TokenUtil;
import com.toptal.entrance.alexeyz.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * REST User API
 * <br>
 *
 * @author alexey.zakharchenko@gmail.com
 */
@RestController
@RequestMapping("/rest/user/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * <i>GET /rest/user/list</i>
     * <br>
     * <br>
     * You must have Manager authority to call this method
     *
     * @return List of all users as JSON
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAuthority('manager')")
    public ResponseEntity<List<User>> list() {
        return new ResponseEntity<>(userRepository.findAll(), OK);
    }

    /**
     * <i>POST /rest/user/</i>
     * <br>
     * <br>
     * No authorization required for this method (everyone can create user and use it later for accessing API)
     *
     * @param user User object as JSON
     * @return JWT token on succes; 400 if login already exist, login or pwd are too short/long,
     * on attempt to create Manager/Admin if current user is not Manager/Admin
     */

    @RequestMapping(value = "/", method = RequestMethod.POST)
    // Anyone may create users
    public ResponseEntity create(@RequestBody User user) {
        User existing = userRepository.findByLogin(user.getLogin());
        if (existing != null) {
            return new ResponseEntity<>("User with the given login already exists: " + user.getLogin(), HttpStatus.BAD_REQUEST);
        }

        ResponseEntity error = validate(user);
        if (error != null)
            return error;


        user = userRepository.save(user);

        return new ResponseEntity<>(TokenUtil.createTokenForUser(user.getLogin()), OK);
    }

    /**
     * <i>POST /rest/user/login</i>
     * <br>
     * <br>
     * No authorization required for this method (everyone can login for accessing API)
     *
     * @param user User object as JSON (login and password fields only have sense)
     * @return JWT token on success; 403 if login failed
     */

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    // Anyone may login
    public ResponseEntity login(@RequestBody User user) {
        String pwd = Application.PWD_HASH ? UserUtil.hash(user.getPassword()) : user.getPassword();
        User existing = userRepository.findByLoginAndPassword(user.getLogin(), pwd);
        if (existing == null) {
            return new ResponseEntity<>("User with the given login and password not found", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(TokenUtil.createTokenForUser(user.getLogin()), OK);
    }

    /**
     * <i>DELETE /rest/user/{id}</i>
     * <br>
     * <br>
     * Delete user with the given id
     *
     * @param id Id of user in the system
     *
     * @return HTTP OK; 400 on attempt to delete Admin or yourself
     */
    @PreAuthorize("hasAuthority('manager')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") long id) {
        User user = userRepository.findOne(id);
        // Don't delete admins
        if (user.isAdmin())
            return new ResponseEntity<>("Its prohibited to delete Admins", BAD_REQUEST);

        // Don't delete yourself
        if (currentUser().getId().equals(id))
            return new ResponseEntity<>("You cannot delete yourself", BAD_REQUEST);


        userRepository.delete(id);

        return new ResponseEntity<>(OK);
    }

    /**
     * <i>PUT /rest/user/</i>
     * <br>
     * <br>
     * Update the given user (based on id)
     *
     * @param user User object as JSON
     *
     * @return HHTP OK; 404 if user not found, 400 if login or pwd are too short/long,
     * on attempt to create Manager/Admin if current user is not Manager/Admin
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('manager')")
    public ResponseEntity update(@RequestBody User user) {
        if (user.getId() == null || userRepository.findOne(user.getId()) == null)
            return new ResponseEntity<>("User with the given id does not exist: " + user.getId(), NOT_FOUND);

        User existing = userRepository.findByLogin(user.getLogin());
        if (existing != null && !existing.getId().equals(user.getId()))
            return new ResponseEntity<>("User with the given login already exists: " + user.getLogin(), BAD_REQUEST);


        ResponseEntity error = validate(user);
        if (error != null)
            return error;


        user = userRepository.save(user);

        return new ResponseEntity<>(user, OK) ;
    }


    private ResponseEntity validate(User user) {
        if (user.getLogin() == null || user.getLogin().length() < User.MIN_LOGIN_LENGTH)
            return new ResponseEntity<>("Login must be at least " + User.MIN_LOGIN_LENGTH + " chars long", BAD_REQUEST);

        if (user.getPassword() == null || user.getPassword().length() < User.MIN_PASSWORD_LENGTH
                || user.getPassword().length() > User.MAX_PASSWORD_LENGTH)
            return new ResponseEntity<>("Password must be at between " + User.MIN_PASSWORD_LENGTH
                    + " and " + User.MAX_PASSWORD_LENGTH +" chars long", BAD_REQUEST);

        User current = currentUser();

        if (user.isAdmin() && (current == null || !currentUser().isAdmin()) )
            return new ResponseEntity<>("Only Admin may create/update Admins", BAD_REQUEST);

        if (user.isManager() && (current == null || !currentUser().isManager()) )
            return new ResponseEntity<>("Only Manager may create/update Managers", BAD_REQUEST);

        return null;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByLogin(auth.getName());
    }

}
