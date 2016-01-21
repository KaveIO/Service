/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.security;

import java.util.LinkedList;
import nl.kpmg.af.service.security.ServiceRequest;
import nl.kpmg.af.service.security.V0ServiceRequest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author mhoekstra
 */
public class UserTest {

    @Test
    public void testIsAllowedWithoutRulesIsFalse() {
        User user = new User();
        ServiceRequest serviceRequest = new MockServiceRequest(true, "x", "y", "GET");

        assertFalse(user.isAllowed(serviceRequest));
    }

    @Test
    public void testIsAllowedInvalidServiceIsFalse() {
        User user = new User();
        ServiceRequest serviceRequest = new MockServiceRequest(false, "x", "y", "GET");

        assertFalse(user.isAllowed(serviceRequest));
    }

    @Test
    public void testIsAllowedNullServiceIsFalse() {
        User user = new User();

        assertFalse(user.isAllowed(null));
    }

    @Test
    public void testIsAllowedFollowsRules() {
        User user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("good", "good", "r"));

        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "good", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "good", "good", "PUT")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "bad", "good", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "good", "bad", "GET")));
    }

    @Test
    public void testIsAllowedFollowsApplicationWildcard() {
        User user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("*", "good", "r"));

        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "good", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "good", "good", "PUT")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "bad", "good", "GET")));  // this is the interesting one
        assertFalse(user.isAllowed(new MockServiceRequest(true, "good", "bad", "GET")));
    }

    @Test
    public void testIsAllowedFollowsResourceWildcard() {
        User user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("good", "*", "r"));

        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "good", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "good", "good", "PUT")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "bad", "good", "GET")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "bad", "GET"))); // this is the interesting one
    }

    @Test
    public void testIsAllowedFollowsOperationWildcard() {
        User user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("good", "good", "*"));

        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "good", "GET")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "good", "PUT"))); // this is the interesting one
        assertFalse(user.isAllowed(new MockServiceRequest(true, "bad", "good", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "good", "bad", "GET")));
    }

    @Test
    public void testIsAllowedFollowsOperations() {
        User user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("*", "*", "r"));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "POST")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "PUT")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "DELETE")));

        user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("*", "*", "c"));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "GET")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "POST")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "PUT")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "DELETE")));

        user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("*", "*", "d"));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "POST")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "PUT")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "DELETE")));

        user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("*", "*", "crd"));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "GET")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "POST")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "PUT")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "DELETE")));

        user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("*", "*", "cr"));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "GET")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "x", "x", "POST")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "PUT")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "x", "x", "DELETE")));
    }

    @Test
    public void testIsAllowedAppliesDeny() {
        User user = new User();
        user.setAllow(new LinkedList());
        user.getAllow().add(new User.Rule("good", "*", "r"));

        user.setDeny(new LinkedList());
        user.getDeny().add(new User.Rule("good", "verybad", "r"));

        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "good", "GET")));
        assertTrue(user.isAllowed(new MockServiceRequest(true, "good", "bad", "GET")));
        assertFalse(user.isAllowed(new MockServiceRequest(true, "good", "verybad", "GET")));
    }

    private class MockServiceRequest implements ServiceRequest {

        private final boolean isValid;
        private final String application;
        private final String resource;
        private final String operation;

        public MockServiceRequest(boolean isValid, String application, String resource, String operation) {
            this.isValid = isValid;
            this.application = application;
            this.resource = resource;
            this.operation = operation;
        }

        @Override
        public boolean isValid() {
            return isValid;
        }

        @Override
        public String getApplication() {
            return application;
        }

        @Override
        public String getResource() {
            return resource;
        }

        @Override
        public String getOperation() {
            return operation;
        }
    }

}
