package org.airyelf.widget;

/**
 *
 */
public class Widget {
    private WidgetDefinition definition;
    private int id;

    public Widget() {
    }

    public Widget(WidgetDefinition definition, int id) {
        this.definition = definition;
        this.id = id;
    }

    public WidgetDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WidgetDefinition definition) {
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
