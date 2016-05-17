package com.spring.test.util;


import com.spring.test.model.Greeting;

public class Validate {

    public static boolean validateGreeting(Greeting greeting) {
        if (greeting == null) {
            return true;
        } else if (greeting.getId() != null) {
            return true;
        } else {
            return false;
        }
    }
}
