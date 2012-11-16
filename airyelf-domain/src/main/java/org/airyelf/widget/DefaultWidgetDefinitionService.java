package org.airyelf.widget;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class DefaultWidgetDefinitionService implements WidgetDefinitionService {
    @Inject
    private WidgetDefinitionRepository repository;

    @Transactional
    public List<WidgetDefinition> getAllPortletDefinitions() {
        return repository.loadAll();
    }

    @Transactional
    public WidgetDefinition getPortletDefinition(String groupId, String artifactId, String version) {
        return repository.load(groupId, artifactId, version);
    }

    @Transactional
    public void registerPortletDefinition(WidgetDefinition widgetDefinition) {
        repository.store(widgetDefinition);
    }

    @Transactional
    public void unregisterPortletDefinitionsOfUrl(String url) {
        repository.removeUrl(url);
    }

    @Transactional
    public void unregisterPortletDefinition(WidgetDefinition widgetDefinition) {
        repository.remove(widgetDefinition);
    }
}
