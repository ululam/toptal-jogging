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
 * REST API for Jog records
 * <br>
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

    /**
     * <i>GET /rest/jog/list?from=2015-01-01&amp;to=2016-02-01</i>
     * <br><br>
     *
     * Lists current user's jog records
     *
     * @param from From date, yyyy-MM-dd (optional)
     * @param to To date, yyyy-MM-dd (optional)
     *
     * @return List of Jog entities as JSON
     */
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

    /**
     * <i>GET /rest/jog/listall?from=2015-01-01&amp;to=2016-02-01</i>
     * <br><br>
     * Lists all users' jog records. You must have Admin authorities to access this method
     *
     * @param from From date, yyyy-MM-dd (optional)
     * @param to To date, yyyy-MM-dd (optional)
     *
     * @return List of Jog entities as JSON
     */
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


    /**
     * <i>GET /rest/jog/weeks</i>
     * <br>
     * <br>
     * Returns current user's week reports for all recorded Jogs
     *
     * @return List of Week reports as JSON
     */
    @RequestMapping(value = "/weeks", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<List<Week>> weeks() {
        List<Week> weeks = repo.getWeeks(currentUser().getId());

        return new ResponseEntity<>(weeks, OK);
    }

    /**
     * <i>POST /rest/jog/</i>
     * <br>
     * <br>
     * @param jog JSON representing Jog object
     *
     * @return Saved Jog object as JSON
     */
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


    /**
     * <i>PUT /rest/jog/</i>
     * <br>
     * <br>
     * @param jog JSON representing Jog object
     *
     * @return Saved Jog object as JSON; 404 if the given Jog is not found in DB
     */
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

    /**
     * <i>DELETE /rest/jog/{id}</i>
     * <br>
     * <br>
     * @param id Jog id to delete
     *
     * @return HTTP OK; 404 if the given Jog is not found in DB
     */
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
