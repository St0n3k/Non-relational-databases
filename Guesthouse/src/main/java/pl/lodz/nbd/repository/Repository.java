package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;

import java.util.List;

public interface Repository<T> {

    void add(T item, EntityManager em);

    void remove(T item, EntityManager em);

    T getById(Long id, EntityManager em);

    List<T> getAll(EntityManager em);
}
