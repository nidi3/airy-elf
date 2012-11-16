package org.airyelf.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nidi
 * Date: 16.11.12
 * Time: 02:21
 * To change this template use File | Settings | File Templates.
 */
public class Page {
    private String name;
    private String url;
    private Map<String, List<Widget>> widgets = new HashMap<>();

    public void addWidget(Widget widget, String container) {
        List<Widget> list = widgets.get(container);
        if (list == null) {
            list = new ArrayList<>();
            widgets.put(container, list);
        }
        list.add(widget);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, List<Widget>> getWidgets() {
        return widgets;
    }
}
