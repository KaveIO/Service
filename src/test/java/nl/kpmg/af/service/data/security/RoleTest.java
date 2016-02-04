/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.security;

import java.util.LinkedList;
import nl.kpmg.af.service.data.core.Role;
import nl.kpmg.af.service.security.ServiceRequest;
import nl.kpmg.af.service.security.V0ServiceRequest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author mhoekstra
 */
public class RoleTest {

    @Test
    public void testIsAllowedWithoutRulesIsFalse() {
        Role role = new Role();
        ServiceRequest serviceRequest = new MockServiceRequest(true, "data", "x", "y", "GET");

        assertFalse(serviceRequest.isAllowed(role));
    }

    @Test
    public void testIsAllowedInvalidServiceIsFalse() {
        Role role = new Role();
        ServiceRequest serviceRequest = new MockServiceRequest(false, "data", "x", "y", "GET");

        assertFalse(serviceRequest.isAllowed(role));
    }

    @Test
    public void testIsAllowedFollowsRules() {
        Role role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "resourceA", "r"));

        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "GET")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "PUT")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceB", "GET")).isAllowed(role));
    }

    @Test
    public void testIsAllowedFollowsResourceWildcard() {
        Role role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "*", "r"));

        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "GET")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceB", "GET")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "PUT")).isAllowed(role));
    }

    @Test
    public void testIsAllowedFollowsOperationWildcard() {
        Role role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "resourceA", "*"));

        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "POST")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "GET")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "PUT")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "DELETE")).isAllowed(role));

        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceB", "POST")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceB", "GET")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceB", "PUT")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceB", "DELETE")).isAllowed(role));
    }

    @Test
    public void testIsAllowedFollowsOperations() {
        Role role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "*", "r"));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "GET")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "POST")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "PUT")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "DELETE")).isAllowed(role));

        role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "*", "c"));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "GET")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "POST")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "PUT")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "DELETE")).isAllowed(role));

        role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "*", "d"));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "GET")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "POST")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "resourceA", "PUT")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "DELETE")).isAllowed(role));

        role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "*", "crud"));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "GET")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "POST")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "PUT")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "resourceA", "DELETE")).isAllowed(role));

        role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "*", "cru"));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "x", "GET")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "x", "POST")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "applicationA", "x", "PUT")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "applicationA", "x", "DELETE")).isAllowed(role));
    }

    @Test
    public void testIsAllowedAppliesDeny() {
        Role role = new Role();
        role.setAllow(new LinkedList());
        role.getAllow().add(new Role.Rule("data", "*", "r"));

        role.setDeny(new LinkedList());
        role.getDeny().add(new Role.Rule("data", "verybad", "r"));

        assertTrue((new MockServiceRequest(true, "data", "good", "good", "GET")).isAllowed(role));
        assertTrue((new MockServiceRequest(true, "data", "good", "bad", "GET")).isAllowed(role));
        assertFalse((new MockServiceRequest(true, "data", "good", "verybad", "GET")).isAllowed(role));
    }

    private class MockServiceRequest extends ServiceRequest {

        private final boolean isValid;
        private final String service;
        private final String application;
        private final String resource;
        private final String operation;

        public MockServiceRequest(boolean isValid, String service, String application, String resource, String operation) {
            this.isValid = isValid;
            this.service = service;
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

        public String getService() {
            return service;
        }
    }
}
