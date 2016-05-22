package com.spring.test.dao;


import com.spring.test.model.Greeting;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.spring.test.util.Validate.validateGreeting;

@Component
@Qualifier("Greeting")
public class GreetingDAOImpl implements CrudDAO<Greeting> {

    private static BigInteger nextId;
    private Map<BigInteger, Greeting> greetingMap;

    @Override
    public Greeting save(Greeting greeting) {
        if (greetingMap == null) {
            greetingMap = new HashMap<BigInteger, Greeting>();
            nextId = BigInteger.ONE;
        }

        greeting.setId(nextId);
        nextId = nextId.add(BigInteger.ONE);
        greetingMap.put(greeting.getId(), greeting);

        return greeting;
    }

    @Override
    public Greeting update(Greeting greeting) {
        if (validateGreeting(greeting)) {
            Greeting oldGreeting = greetingMap.get(greeting.getId());
            if (!validateGreeting(oldGreeting)) {
                return null;
            }
            greetingMap.remove(greeting.getId());
            greetingMap.put(greeting.getId(), greeting);
            return greeting;
        }
        return null;
    }

    @Override
    public boolean delete(BigInteger id) {
        Greeting deletedGreeting = greetingMap.remove(id);
        if (!validateGreeting(deletedGreeting)) {
            return false;
        }
        return true;
    }

    @Override
    public Collection<Greeting> getAll() {
        Collection<Greeting> greetings = greetingMap.values();
        return greetings;
    }

    @Override
    public Greeting getById(BigInteger id) {
        Greeting greeting = greetingMap.get(id);
        return greeting;
    }

}
