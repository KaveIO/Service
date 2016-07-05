/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author mhoekstra
 */
@Document(collection="proxy")
public class Proxy {
    private String name;
    private String target;

    @Field("methods_allowed")
    private List<String> methodsAllowed;

    @Field("disable_ssl")
    private boolean disableSSL;

    @Field("path_extension")
    private boolean pathExtension;

    private String username;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getMethodsAllowed() {
        return methodsAllowed;
    }

    public void setMethodsAllowed(List<String> methods_allowed) {
        this.methodsAllowed = methods_allowed;
    }

    public boolean isDisableSSL() {
        return disableSSL;
    }

    public void setDisableSSL(boolean disableSSL) {
        this.disableSSL = disableSSL;
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

    public boolean isPathExtension() {
        return pathExtension;
    }

    public void setPathExtension(boolean pathExtension) {
        this.pathExtension = pathExtension;
    }
}
