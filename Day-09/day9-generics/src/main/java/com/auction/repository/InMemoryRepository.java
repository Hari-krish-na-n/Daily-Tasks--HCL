package com.auction.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * In-memory implementation of CrudRepository<T, ID> using an internal LinkedHashMap
 * to preserve insertion order and provide O(1) primary key lookups.
 *
 * Uses a type-safe Function<T, ID> idExtractor to obtain the entity's ID without
 * resorting to raw types, unchecked casts, or reflection.
 *
 * Save Semantics (Duplicate ID Handling):
 * - If an entity with the same ID already exists, map.put(id, entity) overwrites
 *   the previous value (upsert semantics).
 * - Null entities or entities producing null IDs throw IllegalArgumentException.
 *
 * @param <T>  The entity type
 * @param <ID> The primary key type
 */
public class InMemoryRepository<T, ID> implements CrudRepository<T, ID> {

    private final Map<ID, T> storage = new LinkedHashMap<>();
    private final Function<T, ID> idExtractor;

    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = Objects.requireNonNull(idExtractor, "idExtractor function cannot be null");
    }

    @Override
    public synchronized T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save null entity");
        }
        ID id = idExtractor.apply(entity);
        if (id == null) {
            throw new IllegalArgumentException("Entity ID extracted cannot be null");
        }
        // Upsert behavior: overwrites if duplicate ID, preserving map semantics
        storage.put(id, entity);
        return entity;
    }

    @Override
    public synchronized Optional<T> findById(ID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public synchronized List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public synchronized boolean deleteById(ID id) {
        if (id == null) {
            return false;
        }
        return storage.remove(id) != null;
    }

    public synchronized int count() {
        return storage.size();
    }

    public synchronized void clear() {
        storage.clear();
    }
}
