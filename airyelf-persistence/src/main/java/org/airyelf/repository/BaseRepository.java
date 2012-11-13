package org.airyelf.repository;

import java.io.Serializable;

/**
 *
 */
public interface BaseRepository<T, K extends Serializable> {

	T findById(K id);

	T getById(K id);

	void remove(T entity);

	void store(T entity);
}