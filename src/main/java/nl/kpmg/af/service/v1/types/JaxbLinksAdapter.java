/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Extension of the preexisting Link.JaxbAdapter which adds support for marshaling lists of Links.
 *
 * @author mhoekstra
 */
public class JaxbLinksAdapter extends XmlAdapter<Map<String, Link.JaxbLink>, Map<String, Link>> {

    Link.JaxbAdapter JaxbAdapter = new Link.JaxbAdapter();

    @Override
    public Map<String, Link> unmarshal(Map<String, Link.JaxbLink> values) throws Exception {
        Map<String, Link> result = new HashMap();
        for (Map.Entry<String, Link.JaxbLink> value : values.entrySet()) {
            result.put(value.getKey(), JaxbAdapter.unmarshal(value.getValue()));
        }
        return result;
    }

    @Override
    public Map<String, Link.JaxbLink> marshal(Map<String, Link> values) throws Exception {
        Map<String, Link.JaxbLink> result = new HashMap();
        for (Map.Entry<String, Link> value : values.entrySet()) {
            result.put(value.getKey(), JaxbAdapter.marshal(value.getValue()));
        }
        return result;
    }
}
