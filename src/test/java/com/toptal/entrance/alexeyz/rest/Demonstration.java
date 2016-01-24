package com.toptal.entrance.alexeyz.rest;

import com.jayway.jsonpath.internal.JsonFormatter;
import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.util.Utils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Date;

import static com.toptal.entrance.alexeyz.rest.RestTestUtils.*;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class Demonstration {

    @Test(enabled = false)
    public void showList() {
        Object res = fetch("/jog/list");
        System.out.println(JsonFormatter.prettyPrint(String.valueOf(res)));
    }

    @Test(enabled = false)
    public void addJog() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(createHeaders("admin", "admin"));

        String url = SERVER_URL + "/jog/";

        ResponseEntity<Jog> jog = restTemplate.postForEntity(url, request, Jog.class, new Jog(1, new Date(), 2.2f, 20 * Utils.M));

        System.out.println(jog);
    }

    @Test(enabled = false)
    public void deleteJog() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(createHeaders("admin", "admin"));

        String url = SERVER_URL + "/jog/1";

        restTemplate.delete(url, Collections.singletonMap("id", 1));
    }

    @Test(enabled = false)
    public void updateJog() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(createHeaders("admin", "admin"));

        String url = SERVER_URL + "/jog/";

        restTemplate.put(url, request, 12, new Jog(1, new Date(), 2.2f, 20 * Utils.M));

        restTemplate.delete(url, Collections.singletonMap("id", 1));
    }

}