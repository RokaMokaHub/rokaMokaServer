package br.edu.ufpel.rokamoka.service;

/**
 *
 * @author MauricioMucci
 */
public interface MockRepository<T> {

    T mockRepositorySave(T entity);
}
