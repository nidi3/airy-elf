package org.airyelf.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.airyelf.widget.WidgetDefinition;
import org.airyelf.widget.WidgetDefinitionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 */
@Controller
@RequestMapping("/widget-definition")
public class WidgetDefinitionController {
    @Inject
    private WidgetDefinitionService service;

    private static class WidgetDefinitionList extends ArrayList<WidgetDefinition> {
    }

    @RequestMapping(value = "/{url}", method = RequestMethod.POST)
    @ResponseBody
    public void register(@PathVariable String url, @RequestBody WidgetDefinitionList widgetDefinitions) {
        for (WidgetDefinition widgetDefinition : widgetDefinitions) {
            widgetDefinition.setUrl(decodeUrl(url));
            service.registerPortletDefinition(widgetDefinition);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<WidgetDefinition> getAll() {
        return service.getAllPortletDefinitions();
    }

    @RequestMapping(value = "/{url}", method = RequestMethod.DELETE)
    @ResponseBody
    public void unregister(@PathVariable String url) {
        service.unregisterPortletDefinitionsOfUrl(decodeUrl(url));
    }

    private String decodeUrl(String url) {
        return url.replace("%2F", "/");
    }
}
