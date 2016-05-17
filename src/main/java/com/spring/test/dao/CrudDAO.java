package com.spring.test.dao;


import java.math.BigInteger;
import java.util.Collection;

public interface CrudDAO<Greeting> {
        Greeting save(Greeting object);
        Greeting update(Greeting object);
        boolean delete(BigInteger id);
        Collection<Greeting> getAll();
        Greeting getById(BigInteger id);


}
