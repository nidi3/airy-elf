package org.airyelf.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.airyelf.entity.PortletDefinitionEntity;
import org.airyelf.portlet.PortletDefinition;
import org.airyelf.portlet.PortletDefinitionRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public class JpaPortletDefinitionRepository extends JpaBaseRepository<PortletDefinitionEntity, Integer> implements PortletDefinitionRepository {

    @Override
    public List<PortletDefinition> loadAll() {
        List<PortletDefinition> result = new ArrayList<>();
        for (PortletDefinitionEntity entity : createTypedQuery("SELECT def FROM PortletDefinitionEntity def").getResultList()) {
            result.add(mapEntity(entity));
        }
        return result;
    }

    @Override
    public PortletDefinition load(String groupId, String artifactId, String version) {
        final List<PortletDefinitionEntity> entities = findByGav(groupId, artifactId, version);
        if (entities.isEmpty()) {
            throw new NoResultException();
        }
        if (entities.size() > 1) {
            throw new NonUniqueResultException();
        }
        return mapEntity(entities.get(0));
    }

    private List<PortletDefinitionEntity> findByGav(String groupId, String artifactId, String version) {
        TypedQuery<PortletDefinitionEntity> query = createTypedQuery(
                "SELECT def FROM PortletDefinitionEntity def " +
                        "WHERE def.groupId=:groupId AND def.artifactId=:artifactId AND def.version=:version");

        query.setParameter("groupId", groupId);
        query.setParameter("artifactId", artifactId);
        query.setParameter("version", version);
        return query.getResultList();
    }

    private List<PortletDefinitionEntity> findByUrl(String url) {
        TypedQuery<PortletDefinitionEntity> query = createTypedQuery(
                "SELECT def FROM PortletDefinitionEntity def " +
                        "WHERE def.url=:url");

        query.setParameter("url", url);
        return query.getResultList();
    }

    @Override
    public void store(PortletDefinition portletDefinition) {
        if (findByGav(portletDefinition.getGroupId(), portletDefinition.getArtifactId(), portletDefinition.getVersion()).isEmpty()) {
            super.store(mapDomain(portletDefinition));
        }
    }

    @Override
    public void remove(PortletDefinition portletDefinition) {
        for (PortletDefinitionEntity entity : findByGav(portletDefinition.getGroupId(), portletDefinition.getArtifactId(), portletDefinition.getVersion())) {
            super.remove(entity);
        }
    }

    @Override
    public void removeUrl(String url) {
        for (PortletDefinitionEntity entity : findByUrl(url)) {
            super.remove(entity);
        }
    }

    private PortletDefinition mapEntity(PortletDefinitionEntity entity) {
        return new PortletDefinition(entity.getGroupId(), entity.getArtifactId(), entity.getVersion(), entity.getUrl(), entity.getName());
    }

    private PortletDefinitionEntity mapDomain(PortletDefinition domain) {
        return new PortletDefinitionEntity(domain.getGroupId(), domain.getArtifactId(), domain.getVersion(), domain.getUrl(), domain.getName());
    }
}