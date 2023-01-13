package pl.lodz.nbd.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {
    Optional<T> add(T item);

    void remove(T item);

    Optional<T> getById(UUID id);

    boolean update(T item);

    List<T> getAll();
}
