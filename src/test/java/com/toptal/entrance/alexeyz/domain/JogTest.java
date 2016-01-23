package com.toptal.entrance.alexeyz.domain;

import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JogTest {

    @Test
    public void testIdNull() {
        assertEquals(new Jog().getId(), null);
    }

    @Test
    public void testGetAverageSpeed() throws Exception {
        doTestAvgSpeed(0,0,0);
        doTestAvgSpeed(0,1,0);
        doTestAvgSpeed(1,0,0);

        doTestAvgSpeed(1, 1_000_000, 3.6f);
        doTestAvgSpeed(1, 1_000, 3600);
        doTestAvgSpeed(1, 3_600_000, 1f);
        doTestAvgSpeed(5, 3600*1000, 5f);
        doTestAvgSpeed(5, (int)(1.5*3_600_000), 5/1.5f);

    }

    private void doTestAvgSpeed(int distance, int time, float avgSpeed) {
        Jog jog = new Jog(0, new Date(), distance, time);
        assertEquals(jog.getAverageSpeed(), avgSpeed);
    }
}