package com.spring.test.util;

import com.spring.test.model.Greeting;
import org.junit.Test;

import static org.junit.Assert.*;


public class ValidateTest {

    @Test
    public void shouldReturnFalseWhenGreetingIsNull() throws Exception {
        Greeting nullGreeting = null;
        assertFalse(Validate.validateGreeting(nullGreeting));
    }

    @Test
    public void shouldReturnTrueWhenGreetingIsnotNull() throws Exception {
        Greeting nullGreeting = new Greeting().builder().text("lala").build();
        assertTrue(Validate.validateGreeting(nullGreeting));
    }

}