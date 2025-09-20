package br.edu.ufpel.rokamoka.service;

import java.lang.reflect.Method;

/**
 * Interface for mocking repositories methods.
 *
 * @param <T> Type of entity.
 * @author MauricioMucci
 */
public interface MockRepository<T> {

    Long DEFAULT_ID = 1L;

    default T mockRepositorySave(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        this.tryToSetId(entity);

        return entity;
    }

    private void tryToSetId(T entity) {
        try {
            Method getId = entity.getClass().getMethod("getId");
            Object id = getId.invoke(entity);
            if (id == null) {
                Method setId = entity.getClass().getMethod("setId", Long.class);
                setId.invoke(entity, DEFAULT_ID);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao atribuir ID ao objeto: " + entity.getClass().getName(), e);
        }
    }
}
