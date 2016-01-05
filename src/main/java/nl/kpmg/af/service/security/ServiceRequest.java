/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

/**
 *
 * @author mhoekstra
 */
public interface ServiceRequest {

    public boolean isValid();

    public String getApplication();

    public String getResource();

    public String getOperation();

}
