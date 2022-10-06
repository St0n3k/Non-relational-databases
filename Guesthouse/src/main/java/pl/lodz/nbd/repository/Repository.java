package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;

public interface Repository<T> {

    void add(T item, EntityManager em);


}
