package com.toptal.entrance.alexeyz.rest;

import com.jayway.jsonpath.internal.JsonFormatter;
import com.toptal.entrance.alexeyz.Application;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JoggingControllerTest {

    @BeforeClass
    public void beforeAll() {
        Application.main(null);
    }

    @Test
    public void testSimple() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(RestTestUtils.createHeaders("user", "user"));

        Object response = restTemplate.exchange("http://localhost:8080/rest/jog/list", HttpMethod.GET, request, List.class).getBody();
        response = restTemplate.exchange("http://localhost:8080/rest/jog/weeks", HttpMethod.GET, request, List.class).getBody();

        System.out.println( JsonFormatter.prettyPrint(String.valueOf(response)) );
//
//        Object res = RestTestUtils.fetch("/jog/list");
//        System.out.println( JsonFormatter.prettyPrint(String.valueOf(res)) );
    }

    @Test
    public void testList() throws Exception {

    }

    @Test
    public void testWeeks() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

}