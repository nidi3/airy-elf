package org.airyelf.portlet;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class DefaultPortletDefinitionService implements PortletDefinitionService {
    @Inject
    private PortletDefinitionRepository repository;

    @Transactional
    public List<PortletDefinition> getAllPortletDefinitions() {
        return repository.loadAll();
    }

    @Transactional
    public PortletDefinition getPortletDefinition(String groupId, String artifactId, String version) {
        return repository.load(groupId, artifactId, version);
    }

    @Transactional
    public void registerPortletDefinition(PortletDefinition portletDefinition) {
        repository.store(portletDefinition);
    }

    @Transactional
    public void unregisterPortletDefinitionsOfUrl(String url) {
        repository.removeUrl(url);
    }

    @Transactional
    public void unregisterPortletDefinition(PortletDefinition portletDefinition) {
        repository.remove(portletDefinition);
    }
}
