package org.airyelf.portlet;

import java.util.List;

/**
 *
 */
public interface PortletDefinitionService {

    List<PortletDefinition> getAllPortletDefinitions();

    PortletDefinition getPortletDefinition(String groupId, String artifactId, String version);

    void registerPortletDefinition(PortletDefinition portletDefinition);

    void unregisterPortletDefinition(PortletDefinition portletDefinition);

    void unregisterPortletDefinitionsOfUrl(String url);
}
