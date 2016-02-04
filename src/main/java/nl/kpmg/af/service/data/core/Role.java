/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mhoekstra
 */
@Document(collection = "roles")
public class Role {

    private String name;

    private List<Role.Rule> allow;

    private List<Role.Rule> deny;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        private String service;

        private String resource;

        private String rights;

        public Rule(String service, String resource, String rights) {
            this.service = service;
            this.resource = resource;
            this.rights = rights;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
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
    }
}
