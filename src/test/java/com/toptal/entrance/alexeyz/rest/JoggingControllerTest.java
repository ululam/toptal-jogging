package com.toptal.entrance.alexeyz.rest;

import com.toptal.entrance.alexeyz.Application;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JoggingControllerTest {

    @BeforeClass
    public void beforeAll() {
        Application.main(null);
    }

    @Test
    public void testList() throws Exception {
        Object response = RestTestUtils.fetch("/jog/list");
        Assert.assertNotNull(response);
    }

    @Test
    public void testWeeks() throws Exception {
        Object response = RestTestUtils.fetch("/jog/weeks");
        Assert.assertNotNull(response);
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