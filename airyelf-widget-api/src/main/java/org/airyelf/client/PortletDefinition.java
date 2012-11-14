package org.airyelf.client;

/**
 *
 */
public class PortletDefinition {
    private String groupId;
    private String artifactId;
    private String version;
    private String name;

    public PortletDefinition(String groupId, String artifactId, String version, String name) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortletDefinition that = (PortletDefinition) o;

        if (!artifactId.equals(that.artifactId)) return false;
        if (!groupId.equals(that.groupId)) return false;
        if (!version.equals(that.version)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + artifactId.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PortletDefinition{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
