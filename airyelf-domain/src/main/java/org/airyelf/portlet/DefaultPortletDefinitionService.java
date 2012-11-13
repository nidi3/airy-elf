package org.airyelf.portlet;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class DefaultPortletDefinitionService implements PortletDefinitionService {
    @Inject
    private PortletDefinitionRepository repository;

    @Transactional
    public void getPortletDefinition(String groupId, String artifactId, String version) {
        repository.load(groupId, artifactId, version);
    }

    @Transactional
    public void registerPortletDefinition(PortletDefinition portletDefinition) {
        repository.store(portletDefinition);
    }

    @Transactional
    public void unregisterPortletDefinition(PortletDefinition portletDefinition) {
        repository.remove(portletDefinition);
    }
}
