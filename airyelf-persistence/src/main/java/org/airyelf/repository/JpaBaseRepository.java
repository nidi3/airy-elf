package org.airyelf.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 */
public abstract class JpaBaseRepository<T, K extends Serializable> implements BaseRepository<T, K> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityClass;

    public JpaBaseRepository() {
        this(null);
    }

    public JpaBaseRepository(Class<T> entityClass) {
        this.entityClass = (entityClass != null) ? entityClass : determineEntityClass();
    }

    @Override
    public T findById(K id) {
        return getCurrentEntityManager().find(this.entityClass, id);
    }

    @Override
    public T getById(K id) {
        T result = findById(id);
        if (result == null) {
            throw new EntityNotFoundException("No Entity found for id:'" + id
                    + "'");
        }
        return result;
    }

    @Override
    public void remove(T entity) {
        getCurrentEntityManager().remove(entity);
    }

    @Override
    public void store(T entity) {
        getCurrentEntityManager().persist(entity);
    }

    protected EntityManager getCurrentEntityManager() {
        return entityManager;
    }

    protected T getSingleResult(TypedQuery<T> query) {
        T result;

        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    protected String createSearchPattern(String searchParam) {
        if (searchParam != null) {
            return "%" + searchParam.replace('*', '%') + "%";
        }
        return "%";
    }

    protected TypedQuery<T> createTypedQuery(String query) {
        return getCurrentEntityManager().createQuery(query, entityClass);
    }

    private Class<T> determineEntityClass() {
        if (!(getClass().getGenericSuperclass() instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Class: "
                            + getClass().getName()
                            + " must be Parameterized. Please "
                            + "define TypeArgument while extending JpaBaseRepository (eg. MyRepo extends "
                            + "JpaBaseRepository<EntityClass>)");
        }

        ParameterizedType type = (ParameterizedType) getClass()
                .getGenericSuperclass();
        if (type.getActualTypeArguments().length == 0) {
            throw new IllegalArgumentException("No Type Parameter define for: "
                    + type);
        }

        @SuppressWarnings("unchecked")
        final Class<T> result = (Class<T>) type.getActualTypeArguments()[0];
        return result;
    }
}

