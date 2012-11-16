package org.airyelf.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Entity
@Table(name = "WIDGET_DEFINITION")
public class WidgetDefinitionEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;


    @NotNull
    @Column(nullable = false)
    private String groupId;

    @NotNull
    @Column(nullable = false)
    private String artifactId;

    @NotNull
    @Column(nullable = false)
    private String version;

    @NotNull
    @Column(nullable = false)
    private String url;

    @NotNull
    @Column(nullable = false)
    private String name;

    protected WidgetDefinitionEntity() {
        // For JPA
    }

    public WidgetDefinitionEntity(String groupId, String artifactId, String version, String url, String name) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.url = url;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}