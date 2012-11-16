package org.airyelf.repository;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.airyelf.widget.WidgetDefinition;
import org.airyelf.widget.WidgetDefinitionRepository;
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
public class JpaWidgetDefinitionRepositoryTest {
    @Inject
    private WidgetDefinitionRepository repository;

    @Test
    @Transactional
    public void testStoreAndLoad() throws Exception {
        final WidgetDefinition def = new WidgetDefinition("gi", "ai", "v", "http://", "widget");
        repository.store(def);
        final WidgetDefinition load = repository.load("gi", "ai", "v");
        assertEquals(def, load);
    }

    @Test
    @Transactional
    public void testStoreAndLoadAll() throws Exception {
        repository.store(new WidgetDefinition("gi", "ai", "v", "http://", "widget"));
        repository.store(new WidgetDefinition("gi2", "ai2", "v2", "http://2", "widget2"));
        final List<WidgetDefinition> load = repository.loadAll();
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
        repository.store(new WidgetDefinition("gi", "ai", "v", "http://", "widget"));
        repository.store(new WidgetDefinition("gi", "ai", "v", "", ""));
        repository.load("gi", "ai", "v");
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Transactional
    public void testRemove() throws Exception {
        repository.store(new WidgetDefinition("gi", "ai", "v", "http://", "widget"));
        repository.remove(new WidgetDefinition("gi", "ai", "v", "http://", "widget"));
        repository.load("gi", "ai", "v");
    }
}
