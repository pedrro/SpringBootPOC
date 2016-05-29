package com.spring.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.test.SpringApplication;
import com.spring.test.dao.CrudDAO;
import com.spring.test.model.Greeting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplication.class)
@WebIntegrationTest(randomPort=true)
@ContextConfiguration(classes = SpringApplication.class)
public class GreetingControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Mock
    private CrudDAO crudDAO;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

    }

    @Test
    public void shouldGetAllGreetings() throws Exception {
        mockMvc.perform(get("/api/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    public void shouldGetASpecificGreeting() throws Exception {
        mockMvc.perform(get("/api/greetings/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    public void shouldReturn404WhenGreetingIsNotFound() throws Exception {
        mockMvc.perform(get("/api/greetings/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSaveAGreeting() throws Exception {
        Greeting greeting = new Greeting().builder().id(BigInteger.valueOf(3)).text("ha2").build();
        when(crudDAO.save(greeting)).thenReturn(greeting);

        mockMvc.perform(post("/api/greetings/")
                .content(asJsonString(greeting))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.text", is("ha2")));
    }

    @Test
    public void shouldUpdateAGreeting() throws Exception {
        Greeting greeting = new Greeting().builder().id(BigInteger.valueOf(2)).text("ha2").build();
        when(crudDAO.update(greeting)).thenReturn(greeting);

        mockMvc.perform(put("/api/greetings/{id}", BigInteger.valueOf(1))
                .content(asJsonString(greeting))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.text", is("ha2")));

    }

    @Test
    public void shouldReturn404WhenAGreetingIsInvalidOnUpdate() throws Exception {
        Greeting greeting = new Greeting().builder().id(BigInteger.valueOf(5)).text("ha2").build();
        when(crudDAO.update(greeting)).thenReturn(null);

        mockMvc.perform(put("/api/greetings/{id}", BigInteger.valueOf(5))
                .content(asJsonString(greeting))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldDeleteAGreeting() throws Exception {
        when(crudDAO.delete(BigInteger.valueOf(1))).thenReturn(true);
        Greeting greeting = new Greeting().builder().id(BigInteger.ONE).text("ha2").build();

        mockMvc.perform(delete("/api/greetings/{id}", BigInteger.valueOf(1))
                .content(asJsonString(greeting))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn500WhenTryToDeleteAnInvalidGreeting() throws Exception {
        when(crudDAO.delete(BigInteger.valueOf(3))).thenReturn(false);
        Greeting greeting = new Greeting().builder().id(BigInteger.valueOf(3)).text("ha2").build();

        mockMvc.perform(delete("/api/greetings/{id}", BigInteger.valueOf(3))
                .content(asJsonString(greeting))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}