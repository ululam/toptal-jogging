package com.toptal.entrance.alexeyz.rest;

import com.toptal.entrance.alexeyz.db.JoggingRepository;
import com.toptal.entrance.alexeyz.db.UserRepository;
import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.domain.Week;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@RestController
@RequestMapping("/rest/jog/")
public class JoggingController {
    private static final Logger log = LoggerFactory.getLogger(JoggingController.class);

    @Autowired
    private JoggingRepository repo;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<List<Jog>> list() {
        List<Jog> jogs = repo.findAllWithParameters(new Date(0), new Date(), currentUser().getId());

        return new ResponseEntity<>(jogs, HttpStatus.OK);
    }

    @RequestMapping(value = "/weeks", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<List<Week>> weeks() {
        List<Week> weeks = repo.getWeeks(currentUser().getId());

        return new ResponseEntity<>(weeks, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Jog> create(@RequestBody Jog jog) {
        long currentUserId = currentUser().getId();
        if (jog.getUserId() == null)
            jog.setUserId(currentUserId);
        else if (!jog.getUserId().equals(currentUserId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        jog = repo.save(jog);

        return new ResponseEntity<>(jog, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Void> update(@RequestBody Jog jog) {
        if (jog.getId() == null || repo.findOne(jog.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        jog = repo.save(jog);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        Jog jog = repo.findOne(id);
        if (jog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!jog.getUserId().equals(currentUser().getId()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        repo.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByLogin(auth.getName());
    }
}
