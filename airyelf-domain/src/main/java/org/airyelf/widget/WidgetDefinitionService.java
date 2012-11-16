package org.airyelf.widget;

import java.util.List;

/**
 *
 */
public interface WidgetDefinitionService {

    List<WidgetDefinition> getAllPortletDefinitions();

    WidgetDefinition getPortletDefinition(String groupId, String artifactId, String version);

    void registerPortletDefinition(WidgetDefinition widgetDefinition);

    void unregisterPortletDefinition(WidgetDefinition widgetDefinition);

    void unregisterPortletDefinitionsOfUrl(String url);
}
