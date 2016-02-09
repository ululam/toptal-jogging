package com.toptal.entrance.alexeyz.rest;

import com.toptal.entrance.alexeyz.Application;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class UserControllerTest {
    @BeforeClass
    public void beforeAll() {
        Application.main(null);
    }


    @Test
    public void testUnathAccess() {
        try {
            RestTestUtils.fetch("/user/list");
            Assert.fail("Should not work with user:user credintials");
        } catch (HttpClientErrorException e) {
            // ok
        }
    }

    @Test
    public void testList() throws Exception {
        Object response = RestTestUtils.fetch("/user/list", "admin", "admin");
        Assert.assertNotNull(response);
    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }
}