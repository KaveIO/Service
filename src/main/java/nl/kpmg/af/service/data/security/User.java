/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.security;

import java.util.List;
import nl.kpmg.af.service.security.ServiceRequest;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mhoekstra
 */
@Document(collection = "users")
public class User {

    private String username;

    private String password;

    private List<Rule> allow;

    private List<Rule> deny;

    public boolean isAllowed(ServiceRequest serviceRequest) {
        if (serviceRequest != null && serviceRequest.isValid()) {

            if (deny != null) {
                for (Rule rule : deny) {
                    if (rule.match(serviceRequest)) {
                        return false;
                    }
                }
            }

            if (allow != null) {
                for (Rule rule : allow) {
                    if (rule.match(serviceRequest)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Rule> getAllow() {
        return allow;
    }

    public void setAllow(List<Rule> allow) {
        this.allow = allow;
    }

    public List<Rule> getDeny() {
        return deny;
    }

    public void setDeny(List<Rule> deny) {
        this.deny = deny;
    }

    public static class Rule {
        private String application;

        private String resource;

        private String rights;

        public Rule(String application, String resource, String rights) {
            this.application = application;
            this.resource = resource;
            this.rights = rights;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getRights() {
            return rights;
        }

        public void setRights(String rights) {
            this.rights = rights;
        }


        private boolean match(ServiceRequest serviceRequest) {
            char operation;

            switch (serviceRequest.getOperation()) {
                case "POST":
                    operation = 'c';
                    break;
                case "GET":
                    operation = 'r';
                    break;
                case "PUT":
                    operation = 'u';
                    break;
                case "DELETE":
                    operation = 'd';
                    break;
                default:
                    operation = '?';
            }

            boolean applicationMatch = application.equals("*") || application.equals(serviceRequest.getApplication());
            boolean resourceMatch = resource.equals("*") || resource.equals(serviceRequest.getResource());
            boolean rightsMatch = rights.equals("*") || rights.contains(""+operation);

            return applicationMatch && resourceMatch && rightsMatch;
        }
    }
}
