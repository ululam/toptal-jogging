package com.toptal.entrance.alexeyz.rest;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.Week;
import com.toptal.entrance.alexeyz.repo.JoggingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@RestController
public class JoggingController {
    private static final Logger log = LoggerFactory.getLogger(JoggingController.class);

    private final JoggingRepository repo;

    @Autowired
    public JoggingController(JoggingRepository repo) {
        this.repo = repo;
    }

    @RequestMapping("/jog/list")
    public List<Jog> list() {
        return repo.findAllWithParameters(new Date(0), new Date(), 0);
    }

    @RequestMapping("/week/list")
    public List<Week> weeks() {
        return repo.getWeeks(0);
    }


    @RequestMapping(value = "/jog/", method = RequestMethod.POST)
    public Jog create(@RequestBody Jog jog) {
        return repo.save(jog);
    }

    @RequestMapping(value = "/jog/{id}", method = RequestMethod.PUT)
    public Jog update(@PathVariable("id") long id, @RequestBody Jog jog) {
        Jog existingJog = repo.findOne(id);
        if (existingJog == null) {
            throw new IllegalArgumentException("Jog with id = " + id + " not found");
        }

        repo.save( existingJog.copyFrom(jog) );

        return existingJog;
    }

    @RequestMapping(value = "/jog/{id}", method = RequestMethod.DELETE)
    public Jog delete(@PathVariable("id") long id) {
        Jog jog = repo.findOne(id);
        if (jog == null) {
            log.warn("Jog with id {} not found", id);
        }

        repo.delete(id);

        return jog;
    }



}
