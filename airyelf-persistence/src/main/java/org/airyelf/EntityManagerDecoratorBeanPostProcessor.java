package org.airyelf;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Represents a bean post processor that decorates the entity manager factory /
 * the entity manager with a proxy that flushes automatically after a persist,
 * merge, and remove.
 * <p/>
 * <b>Note:</b> this class must not be configured in production environments,
 * but in testing only.
 */
public class EntityManagerDecoratorBeanPostProcessor implements
        BeanPostProcessor {

    private List<String> flushAfterMethods;

    /**
     *
     */
    public EntityManagerDecoratorBeanPostProcessor() {
        flushAfterMethods = Arrays.asList("persist", "merge", "remove");
    }

    /**
     * @param flushAfterMethods (mandatory)
     */
    public void setFlushAfterMethodNames(List<String> flushAfterMethods) {
        if (flushAfterMethods == null) {
            throw new IllegalArgumentException("Property 'flushAfterMethods' must not be null");
        }
        this.flushAfterMethods = flushAfterMethods;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof EntityManagerFactory) {
            return createEntityManagerFactoryProxy((EntityManagerFactory) bean);
        }

        return bean;
    }

    private EntityManagerFactory createEntityManagerFactoryProxy(
            final EntityManagerFactory delegate) {
        return new EntityManagerFactory() {
            @Override
            public EntityManager createEntityManager() {
                return createEntityManagerProxy(delegate.createEntityManager());
            }

            @Override
            public EntityManager createEntityManager(
                    @SuppressWarnings("rawtypes") Map map) {
                return createEntityManagerProxy(delegate
                        .createEntityManager(map));
            }

            @Override
            public void close() {
                delegate.close();
            }

            @Override
            public boolean isOpen() {
                return delegate.isOpen();
            }

            @Override
            public Cache getCache() {
                return delegate.getCache();
            }

            @Override
            public CriteriaBuilder getCriteriaBuilder() {
                return delegate.getCriteriaBuilder();
            }

            @Override
            public Metamodel getMetamodel() {
                return delegate.getMetamodel();
            }

            @Override
            public PersistenceUnitUtil getPersistenceUnitUtil() {
                return delegate.getPersistenceUnitUtil();
            }

            @Override
            public Map<String, Object> getProperties() {
                return delegate.getProperties();
            }
        };
    }

    private EntityManager createEntityManagerProxy(final EntityManager delegate) {
        return (EntityManager) Proxy.newProxyInstance(delegate.getClass()
                .getClassLoader(), new Class[]{EntityManager.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method,
                                         Object[] args) throws Throwable {
                        try {
                            Object result = method.invoke(delegate, args);

                            if (shouldFlushAfterMethodInvocation(method)) {
                                delegate.flush();
                            }

                            return result;
                        } catch (Exception e) {
                            if (e instanceof InvocationTargetException) {
                                throw ((InvocationTargetException) e)
                                        .getTargetException();
                            }

                            throw e;
                        }
                    }
                });
    }

    private boolean shouldFlushAfterMethodInvocation(Method method) {
        return flushAfterMethods.contains(method.getName());
    }

}
