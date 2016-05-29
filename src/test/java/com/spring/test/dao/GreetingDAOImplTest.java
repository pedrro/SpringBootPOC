package com.spring.test.dao;

import com.spring.test.model.Greeting;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GreetingDAOImplTest {
    GreetingDAOImpl greetingDAO;
    Greeting greeting;

    @Before
    public void setUp() throws Exception {
        greetingDAO = new GreetingDAOImpl();
        greeting = new Greeting().builder().id(BigInteger.ONE).text("ha").build();
        greetingDAO.save(greeting);

    }

    @Test
    public void shouldReturnAllGreetings() throws Exception {
        assertThat(greetingDAO.getAll(), contains(greeting));
    }

    @Test
    public void shouldGetASpecificGreetingSearchingById() {
        assertThat(greetingDAO.getById(BigInteger.ONE), is(greeting));

    }

    @Test
    public void shouldReturnTrueWhenDeleteAGreeting() {
        assertTrue(greetingDAO.delete(BigInteger.ONE));
    }

    @Test
    public void shouldReturnFalseWhenDeleteAGreeting() {
        assertFalse(greetingDAO.delete(BigInteger.ZERO));
    }

    @Test
    public void shouldUpdateAGreetingMessage(){
        greeting = new Greeting().builder().id(BigInteger.ONE).text("ha2").build();
        greetingDAO.update(greeting);
        assertThat(greetingDAO.getById(BigInteger.ONE).getText(), is("ha2"));
    }

    @Test
    public void shouldSaveANewGreeting() {
        Greeting greeting2 = new Greeting().builder().id(BigInteger.valueOf(2)).text("ha3").build();
        assertThat(greetingDAO.save(greeting2), is(greeting2));
    }

}