package org.airyelf.portlet;

/**
 *
 */
public interface PortletDefinitionRepository {
    PortletDefinition load(String groupId, String artifactId, String version);

    void store(PortletDefinition portletDefinition);

    void remove(PortletDefinition portletDefinition);

}
