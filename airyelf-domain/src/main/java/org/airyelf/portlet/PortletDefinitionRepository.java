package org.airyelf.portlet;

import java.util.List;

/**
 *
 */
public interface PortletDefinitionRepository {
    List<PortletDefinition> loadAll();

    PortletDefinition load(String groupId, String artifactId, String version);

    void store(PortletDefinition portletDefinition);

    void remove(PortletDefinition portletDefinition);

    void removeUrl(String url);

}
