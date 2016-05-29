package com.spring.test.controller;

import com.spring.test.dao.CrudDAO;
import com.spring.test.model.Greeting;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Collection;

import static com.spring.test.util.Validate.validateGreeting;

@Log4j2
@RestController
public class GreetingController {

    @Autowired
    @Qualifier("Greeting")
    private CrudDAO dao;

    @PostConstruct()
    public void greeting() {
        Greeting g1 = new Greeting().builder().text("hello world").build();
        Greeting g2 = new Greeting().builder().text("Oi").build();
        dao.save(g1);
        dao.save(g2);

    }

    @RequestMapping(value = "/api/greetings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Greeting>> getGreetings() {
        log.info("Getting all greetings");
        return new ResponseEntity<Collection<Greeting>>(dao.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "api/greetings/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> getGreeting(@PathVariable("id") BigInteger id) {
        if (!validateGreeting((Greeting) dao.getById(id))) {
            log.info("Couldn't find greeting - id: {}", id);
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }
        log.info("Return greeting with id: {}",id);
        return new ResponseEntity<Greeting>((Greeting) dao.getById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "api/greetings", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> createGreeting(@RequestBody Greeting greeting) {
        Greeting newGreeting = (Greeting) dao.save(greeting);
        log.info("New greeting saved - id: {}", newGreeting.getId());
        return new ResponseEntity<Greeting>(newGreeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "api/greetings/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> updateGreeting(@RequestBody Greeting greeting) {
        Greeting updatedGreeting = (Greeting) dao.update(greeting);

        if (!validateGreeting(updatedGreeting)) {
            log.info("Invalid greeting - id: {}", greeting.getId());
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }
        log.info("Updating greeting - id: {}", updatedGreeting.getId());
        return new ResponseEntity<Greeting>(updatedGreeting, HttpStatus.OK);

    }

    @RequestMapping(value = "api/greetings/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") BigInteger id, @RequestBody Greeting greeting) {
        boolean deleted = dao.delete(id);
        if (!deleted) {
            log.info("Couldn't delete the following greeting - id: {}", id);
            return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Greeting successfuly deleted - id: {}", id);
        return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);

    }

}