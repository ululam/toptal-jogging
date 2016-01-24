package com.toptal.entrance.alexeyz.rest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
class RestTestUtils {
    private static final Logger log = LoggerFactory.getLogger(RestTestUtils.class);

    public static final String SERVER_URL = "http://localhost:8080/rest";

    static HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                set("Content-Type", "application/json");
            }
        };
    }

    static Object fetch(String path) {
        return fetch(path, "user", "user");
    }

    static Object fetch(String path, String login, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(createHeaders(login, password));

        System.out.println("Fetching url " + SERVER_URL + path);

        Object response = restTemplate.exchange(SERVER_URL + path, HttpMethod.GET, request, List.class).getBody();

        return response;
    }
}
