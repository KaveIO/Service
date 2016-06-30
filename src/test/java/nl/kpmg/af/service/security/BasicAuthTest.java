package nl.kpmg.af.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Created by fziliotto on 27-6-16.
 */
public class BasicAuthTest {

	@Test
	public void decodeTest() throws Exception {
		String[] credential;

		String username = "admin";
		String password = "admin";
		String basicAuthString = "Basic YWRtaW46YWRtaW4="; // auth string with admin:admin
		credential = BasicAuth.decode(basicAuthString);
		assertEquals(credential[0], username);
		assertEquals(credential[1], password);

		String nullAuthString = null;
		credential = BasicAuth.decode(nullAuthString);
		assertNull(credential);

		String emptyAuthentication = "";
		credential = BasicAuth.decode(emptyAuthentication);
		assertNull(credential);

		String onlyUsernameAuth = "Basic YWRtaW4="; // admin in Base64
		credential = BasicAuth.decode(onlyUsernameAuth);
		assertEquals(credential[0], username);
		assertEquals(credential.length,1);

		String onlyPasswordAuth = "OmFkbWlu";
		credential = BasicAuth.decode(onlyPasswordAuth);
		assertEquals(credential[1], password);
		assertEquals(credential[0], "");
	}

}