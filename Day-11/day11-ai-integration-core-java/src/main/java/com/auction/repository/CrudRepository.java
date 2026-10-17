package com.auction.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD Repository interface for data access.
 *
 * @param <T>  The entity type
 * @param <ID> The type of the entity's unique identifier
 */
public interface CrudRepository<T, ID> {

    /**
     * Saves a given entity. If an entity with the same ID already exists,
     * it updates (replaces) the existing entity and returns it.
     *
     * @param entity Entity to save, must not be null
     * @return The saved entity
     */
    T save(T entity);

    /**
     * Retrieves an entity by its unique ID.
     *
     * @param id The entity ID, must not be null
     * @return Optional containing the entity if found, or empty Optional if not found
     */
    Optional<T> findById(ID id);

    /**
     * Returns all entities currently managed by the repository.
     *
     * @return List of all entities
     */
    List<T> findAll();

    /**
     * Deletes the entity with the given ID.
     *
     * @param id The entity ID, must not be null
     * @return true if the entity existed and was deleted, false otherwise
     */
    boolean deleteById(ID id);
}
