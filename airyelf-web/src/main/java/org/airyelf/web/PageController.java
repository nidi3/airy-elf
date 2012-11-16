package org.airyelf.web;

import org.airyelf.widget.Page;
import org.airyelf.widget.Widget;
import org.airyelf.widget.WidgetDefinition;
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
@RequestMapping("/page")
public class PageController {

    @RequestMapping(value = "/{pageId}", method = RequestMethod.POST)
    @ResponseBody
    public void update(@PathVariable String pageId, @RequestBody Page page) {
    }

    @RequestMapping(value = "/{pageId}", method = RequestMethod.GET)
    @ResponseBody
    public Page getPage(@PathVariable String pageId) {
        final Page page = new Page();
        page.addWidget(new Widget(
                new WidgetDefinition("org.airyelf.widget.core", "hello-airy-elf", "0.0.1", "http://localhost:8080/wc", "h"), 1),
                "column1");

        page.addWidget(new Widget(
                new WidgetDefinition("org.airyelf.widget.core", "time", "0.0.1", "http://localhost:8080/wc", "h"), 2),
                "column1");
        return page;
    }

}
