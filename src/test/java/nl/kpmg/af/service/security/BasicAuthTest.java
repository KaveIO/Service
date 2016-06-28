package nl.kpmg.af.service.security;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fziliotto on 27-6-16.
 */
public class BasicAuthTest {
    private String username="admin";
    private String password="admin";
    private String basicAuthString = "Basic YWRtaW46YWRtaW4=";

    @Test
    public void decodeTest() throws Exception {
        String[] credential = BasicAuth.decode(basicAuthString);

        assertEquals(credential[0],username);
        assertEquals(credential[1], password);
    }

}