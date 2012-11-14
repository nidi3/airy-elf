package org.airyelf.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.airyelf.portlet.PortletDefinition;
import org.airyelf.portlet.PortletDefinitionService;
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
@RequestMapping("/portlet-definition")
public class PortletDefinitionController {
    @Inject
    private PortletDefinitionService service;

    private static class PortletDefinitionList extends ArrayList<PortletDefinition> {
    }

    @RequestMapping(value = "/{url}", method = RequestMethod.POST)
    @ResponseBody
    public void register(@PathVariable String url, @RequestBody PortletDefinitionList portletDefinitions) {
        for (PortletDefinition portletDefinition : portletDefinitions) {
            portletDefinition.setUrl(decodeUrl(url));
            service.registerPortletDefinition(portletDefinition);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<PortletDefinition> getAll() {
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
