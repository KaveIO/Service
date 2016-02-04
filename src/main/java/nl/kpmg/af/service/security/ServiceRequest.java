/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import nl.kpmg.af.service.data.core.Role;

/**
 *
 * @author mhoekstra
 */
public abstract class ServiceRequest {

    public abstract boolean isValid();

    public abstract String getApplication();

    public abstract String getResource();

    public abstract String getOperation();

    public abstract String getService();

    public boolean isAllowed(Role role) {
        if (this != null && this.isValid()) {

            if (role.getDeny() != null) {
                for (Role.Rule rule : role.getDeny()) {
                    if (match(rule)) {
                        return false;
                    }
                }
            }

            if (role.getAllow() != null) {
                for (Role.Rule rule : role.getAllow()) {
                    if (match(rule)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean match(Role.Rule rule) {
        char operation;
        switch (this.getOperation()) {
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

        boolean applicationMatch = rule.getService().equals(this.getService());
        boolean resourceMatch = rule.getResource().equals("*") || rule.getResource().equals(this.getResource());
        boolean rightsMatch = rule.getRights().equals("*") || rule.getRights().contains("" + operation);

        return applicationMatch && resourceMatch && rightsMatch;
    }

}
