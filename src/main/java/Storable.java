package com.tracker.interface_def;

/**
 * Interface representing entities that can be serialized to and deserialized from
 * text files for file persistence.
 */
public interface Storable {
    /**
     * Converts object state into a delimited string format suitable for text storage.
     * @return Formatted data line string.
     */
    String toDataString();

    /**
     * Returns unique identifier for entity storage lookup.
     * @return Entity string ID.
     */
    String getId();
}
