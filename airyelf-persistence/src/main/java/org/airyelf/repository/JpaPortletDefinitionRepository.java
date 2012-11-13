package org.airyelf.repository;

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
    public PortletDefinition load(String groupId, String artifactId, String version) {
        final List<PortletDefinitionEntity> entities = find(groupId, artifactId, version);
        if (entities.isEmpty()) {
            throw new NoResultException();
        }
        if (entities.size() > 1) {
            throw new NonUniqueResultException();
        }
        return mapEntity(entities.get(0));
    }

    private List<PortletDefinitionEntity> find(String groupId, String artifactId, String version) {
        TypedQuery<PortletDefinitionEntity> query = createTypedQuery(
                "SELECT def FROM PortletDefinitionEntity def " +
                        "WHERE def.groupId=:groupId AND def.artifactId=:artifactId AND def.version=:version");

        query.setParameter("groupId", groupId);
        query.setParameter("artifactId", artifactId);
        query.setParameter("version", version);
        return query.getResultList();
    }

    @Override
    public void store(PortletDefinition portletDefinition) {
        super.store(mapDomain(portletDefinition));
    }

    @Override
    public void remove(PortletDefinition portletDefinition) {
        for (PortletDefinitionEntity entity : find(portletDefinition.getGroupId(), portletDefinition.getArtifactId(), portletDefinition.getVersion())) {
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