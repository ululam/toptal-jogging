package com.toptal.entrance.alexeyz.util;

import org.testng.annotations.Test;

import static com.toptal.entrance.alexeyz.util.UserUtil.hash;
import static org.testng.Assert.assertEquals;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class UserUtilTest {

    @Test
    public void testHash() throws Exception {
        check("");
        check("a");
        check("0");
        check(null);
        check("asdasdasdas");
        check("asdasdasdasasdfdsgdf56534590t9re0g9--0464564566");
    }

    private void check(String password) {
        String hash = hash(password);

        assertEquals(hash.length(), 32);
        assertEquals(hash, hash(password));
    }
}