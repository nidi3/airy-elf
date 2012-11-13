package org.airyelf.portlet;

/**
 *
 */
public interface PortletDefinitionService {

    void getPortletDefinition(String groupId,String artifactId,String version);

    void registerPortletDefinition(PortletDefinition portletDefinition);

    void unregisterPortletDefinition(PortletDefinition portletDefinition);
}
