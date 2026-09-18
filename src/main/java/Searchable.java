package com.tracker.interface_def;

import java.util.List;
import java.util.function.Predicate;

/**
 * Generic Interface demonstrating polymorphism and abstraction for searching entities.
 * @param <T> The model type to search
 */
public interface Searchable<T> {
    /**
     * Searches items by keyword matching.
     * @param query Search keyword
     * @return Filtered list of matching elements
     */
    List<T> searchByNameOrKeyword(String query);

    /**
     * Filters items using custom predicate logic.
     * @param predicate Filter predicate condition
     * @return Filtered list of matching elements
     */
    List<T> filter(Predicate<T> predicate);
}
