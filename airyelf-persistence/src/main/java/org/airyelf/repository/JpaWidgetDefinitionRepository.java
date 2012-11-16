package org.airyelf.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.airyelf.entity.WidgetDefinitionEntity;
import org.airyelf.widget.WidgetDefinition;
import org.airyelf.widget.WidgetDefinitionRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public class JpaWidgetDefinitionRepository extends JpaBaseRepository<WidgetDefinitionEntity, Integer> implements WidgetDefinitionRepository {

    @Override
    public List<WidgetDefinition> loadAll() {
        List<WidgetDefinition> result = new ArrayList<>();
        for (WidgetDefinitionEntity entity : createTypedQuery("SELECT def FROM WidgetDefinitionEntity def").getResultList()) {
            result.add(mapEntity(entity));
        }
        return result;
    }

    @Override
    public WidgetDefinition load(String groupId, String artifactId, String version) {
        final List<WidgetDefinitionEntity> entities = findByGav(groupId, artifactId, version);
        if (entities.isEmpty()) {
            throw new NoResultException();
        }
        if (entities.size() > 1) {
            throw new NonUniqueResultException();
        }
        return mapEntity(entities.get(0));
    }

    private List<WidgetDefinitionEntity> findByGav(String groupId, String artifactId, String version) {
        TypedQuery<WidgetDefinitionEntity> query = createTypedQuery(
                "SELECT def FROM WidgetDefinitionEntity def " +
                        "WHERE def.groupId=:groupId AND def.artifactId=:artifactId AND def.version=:version");

        query.setParameter("groupId", groupId);
        query.setParameter("artifactId", artifactId);
        query.setParameter("version", version);
        return query.getResultList();
    }

    private List<WidgetDefinitionEntity> findByUrl(String url) {
        TypedQuery<WidgetDefinitionEntity> query = createTypedQuery(
                "SELECT def FROM WidgetDefinitionEntity def " +
                        "WHERE def.url=:url");

        query.setParameter("url", url);
        return query.getResultList();
    }

    @Override
    public void store(WidgetDefinition widgetDefinition) {
        if (findByGav(widgetDefinition.getGroupId(), widgetDefinition.getArtifactId(), widgetDefinition.getVersion()).isEmpty()) {
            super.store(mapDomain(widgetDefinition));
        }
    }

    @Override
    public void remove(WidgetDefinition widgetDefinition) {
        for (WidgetDefinitionEntity entity : findByGav(widgetDefinition.getGroupId(), widgetDefinition.getArtifactId(), widgetDefinition.getVersion())) {
            super.remove(entity);
        }
    }

    @Override
    public void removeUrl(String url) {
        for (WidgetDefinitionEntity entity : findByUrl(url)) {
            super.remove(entity);
        }
    }

    private WidgetDefinition mapEntity(WidgetDefinitionEntity entity) {
        return new WidgetDefinition(entity.getGroupId(), entity.getArtifactId(), entity.getVersion(), entity.getUrl(), entity.getName());
    }

    private WidgetDefinitionEntity mapDomain(WidgetDefinition domain) {
        return new WidgetDefinitionEntity(domain.getGroupId(), domain.getArtifactId(), domain.getVersion(), domain.getUrl(), domain.getName());
    }
}