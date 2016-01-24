package com.toptal.entrance.alexeyz.rest;

import com.toptal.entrance.alexeyz.db.JoggingRepository;
import com.toptal.entrance.alexeyz.db.UserRepository;
import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.domain.Week;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * REST Jog API
 *
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
    public ResponseEntity<List<Jog>> list(
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(value = "to", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date to)
    {
        if (from == null) from = new Date(0);
        if (to == null) to = new Date();

        List<Jog> jogs = repo.findAllWithParameters(from, to, currentUser().getId());

        return new ResponseEntity<>(jogs, OK);
    }

    @RequestMapping(value = "/listall", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<Jog>> listAll(
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(value = "to", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date to)
    {
        if (from == null) from = new Date(0);
        if (to == null) to = new Date();

        List<Jog> jogs = repo.findAllWithParameters(from, to);

        return new ResponseEntity<>(jogs, OK);
    }


    @RequestMapping(value = "/weeks", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<List<Week>> weeks() {
        List<Week> weeks = repo.getWeeks(currentUser().getId());

        return new ResponseEntity<>(weeks, OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity create(@RequestBody Jog jog) {
        long currentUserId = currentUser().getId();
        if (jog.getUserId() == null)
            jog.setUserId(currentUserId);
        else if (!jog.getUserId().equals(currentUserId))
            return new ResponseEntity<>("You cannot create jogs for others", FORBIDDEN);

        jog = repo.save(jog);

        return new ResponseEntity<>(jog, OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity update(@RequestBody Jog jog) {
        if (jog.getId() == null || repo.findOne(jog.getId()) == null) {
            return new ResponseEntity<>("Jog with the given id does not exist: " + jog.getId(), NOT_FOUND);
        }

        repo.save(jog);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity delete(@PathVariable("id") long id) {
        Jog jog = repo.findOne(id);
        if (jog == null) {
            return new ResponseEntity<>("Jog with the given id does not exist: " + id, NOT_FOUND);
        }

        if (!currentUser().isAdmin() && !jog.getUserId().equals(currentUser().getId()))
            return new ResponseEntity<>("You cannot delete others' jogs", FORBIDDEN);

        repo.delete(id);

        return new ResponseEntity<>(OK);
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByLogin(auth.getName());
    }
}
