/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request;

import java.util.Map;

/**
 * @author Hoekstra.Maarten
 */
public class InputRequest {
    private Map value;

    public Map getValue() {
        return this.value;
    }

    public void setValue(final Map value) {
        this.value = value;
    }
}
