package org.airyelf.repository;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.airyelf.portlet.PortletDefinition;
import org.airyelf.portlet.PortletDefinitionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JpaPortletDefinitionRepositoryTest {
    @Inject
    private PortletDefinitionRepository repository;

    @Test
    @Transactional
    public void testStoreAndLoad() throws Exception {
        final PortletDefinition def = new PortletDefinition("gi", "ai", "v", "http://", "portlet");
        repository.store(def);
        final PortletDefinition load = repository.load("gi", "ai", "v");
        assertEquals(def, load);
    }

    @Test
    @Transactional
    public void testStoreAndLoadAll() throws Exception {
        repository.store(new PortletDefinition("gi", "ai", "v", "http://", "portlet"));
        repository.store(new PortletDefinition("gi2", "ai2", "v2", "http://2", "portlet2"));
        final List<PortletDefinition> load = repository.loadAll();
        assertEquals(2,load.size());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Transactional
    public void testNoResult() throws Exception {
        repository.load("gi", "ai", "v");
    }

    @Test(expected = IncorrectResultSizeDataAccessException.class)
    @Transactional
    public void testMultipleResult() throws Exception {
        repository.store(new PortletDefinition("gi", "ai", "v", "http://", "portlet"));
        repository.store(new PortletDefinition("gi", "ai", "v", "", ""));
        repository.load("gi", "ai", "v");
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Transactional
    public void testRemove() throws Exception {
        repository.store(new PortletDefinition("gi", "ai", "v", "http://", "portlet"));
        repository.remove(new PortletDefinition("gi", "ai", "v", "http://", "portlet"));
        repository.load("gi", "ai", "v");
    }
}
