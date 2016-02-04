/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import nl.kpmg.af.service.data.DatabaseInitialiser;
import org.apache.catalina.Realm;
import org.apache.catalina.realm.GenericPrincipal;
import org.jboss.as.web.deployment.mock.MemoryRealm;
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

    private final Realm realm = new MemoryRealm();

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
    public void testV0UserWithRoleIsAllowed() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/test/layer/visit");
        req.setAuthType("BASIC");
        req.setUserPrincipal(new GenericPrincipal(realm, "", "", Arrays.asList(new String[]{"user"})));

        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(200, rsp.getStatus());
    }

    @Test
    public void testV0UserWithApplicationRoleIsAllowed() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/test/layer/visit");
        req.setAuthType("BASIC");
        req.setUserPrincipal(new GenericPrincipal(realm, "", "", Arrays.asList(new String[]{"test.user"})));

        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(200, rsp.getStatus());
    }

    @Test
    public void testV0UserWithRoleIsDeniedForSpecificResource() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/test/layer/trilaterationFitter");
        req.setAuthType("BASIC");
        req.setUserPrincipal(new GenericPrincipal(realm, "", "", Arrays.asList(new String[]{"user"})));

        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(403, rsp.getStatus());
    }

    @Test
    public void testV0UserWithApplicationRoleIsDeniedForSpecificResource() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/test/layer/trilaterationFitter");
        req.setAuthType("BASIC");
        req.setUserPrincipal(new GenericPrincipal(realm, "", "", Arrays.asList(new String[]{"test.user"})));

        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(403, rsp.getStatus());
    }

    @Test
    public void testV0UserWithAdminRoleIsAllowedForSpecificResource() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/test/layer/trilaterationFitter");
        req.setAuthType("BASIC");
        req.setUserPrincipal(new GenericPrincipal(realm, "", "", Arrays.asList(new String[]{"admin"})));

        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(200, rsp.getStatus());
    }

    @Test
    public void testV0UserWithApplicationAdminRoleIsAllowedForSpecificResource() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/test/layer/trilaterationFitter");
        req.setAuthType("BASIC");
        req.setUserPrincipal(new GenericPrincipal(realm, "", "", Arrays.asList(new String[]{"test.admin"})));

        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(200, rsp.getStatus());
    }

    @Test
    public void testV0UserWithoutRoleIsDenied() throws IOException, ServletException {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "");
        req.setPathInfo("/test/layer/visit");
        req.setAuthType("BASIC");
        req.setUserPrincipal(new GenericPrincipal(realm, "", "", Arrays.asList(new String[]{})));

        MockHttpServletResponse rsp = new MockHttpServletResponse();

        permissionCheckerFilter.doFilter(req, rsp, mockFilterChain);

        assertEquals(403, rsp.getStatus());
    }
}
