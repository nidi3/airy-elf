package org.airyelf.widget;

import java.util.List;

/**
 *
 */
public interface WidgetDefinitionRepository {
    List<WidgetDefinition> loadAll();

    WidgetDefinition load(String groupId, String artifactId, String version);

    void store(WidgetDefinition widgetDefinition);

    void remove(WidgetDefinition widgetDefinition);

    void removeUrl(String url);

}
