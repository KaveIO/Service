/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import nl.kpmg.af.service.data.core.Measurement;

/**
 *
 * @author mhoekstra
 */
public class MeasurementsRepresentation {

    private List<Measurement> items;

    @XmlElement(name="links")
    @XmlJavaTypeAdapter(JaxbLinksAdapter.class)
    private Map<String, Link> links;

    public MeasurementsRepresentation(List<Measurement> items, Map<String, Link> links) {
        this.items = items;
        this.links = links;
    }

    public List<Measurement> getItems() {
        return items;
    }

    public void setItems(List<Measurement> items) {
        this.items = items;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

}
