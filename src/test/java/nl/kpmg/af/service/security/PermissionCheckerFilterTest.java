/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.ServletException;
import nl.kpmg.af.service.data.DatabaseInitialiser;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author mhoekstra
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@DirtiesContext
public class PermissionCheckerFilterTest {

    private static DatabaseInitialiser databaseInitialiser;

    @Autowired
    private PermissionCheckerFilter permissionCheckerFilter;

    @BeforeClass
    public static void setUpClass() throws Exception {
        databaseInitialiser = new DatabaseInitialiser();
        databaseInitialiser.start();
    }


    @AfterClass
    public static void tearDownClass() {
        databaseInitialiser.stop();
    }

    @Test
    public void testAnonymousIs401() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/");
        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(401, rsp.getStatus());
    }

    @Test
    public void testV0UserWithRulesIsAllowed() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/userWithRules/layer/visit");
        req.setUserPrincipal(new MockPrincipal("userWithRules"));
        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(200, rsp.getStatus());
    }

    @Test
    public void testV0UserWithRulesIsDeniedForSpecificResource() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/userWithRules/layer/trilaterationFitter");
        req.setUserPrincipal(new MockPrincipal("userWithRules"));
        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(403, rsp.getStatus());
    }

    @Test
    public void testV0UserWithoutRulesIsDenied() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/userWithoutRules/layer/visit");
        req.setUserPrincipal(new MockPrincipal("userWithoutRules"));
        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(403, rsp.getStatus());
    }

    private class MockPrincipal implements Principal {

        private final String name;

        public MockPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
