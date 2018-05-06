/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;


/**
 * This is a test class for NamedDeliverableStructure. It extends NamedDeliverableStructure and no method is overrided.
 *
 * @author singlewood
 * @version 1.1
 */
public class NamedDeliverableStructureExtends extends NamedDeliverableStructure {
    /**
     * Creates a new NamedDeliverableStructureExtends.
     */
    protected NamedDeliverableStructureExtends() {
        super();
    }

    /**
     * Creates a new NamedDeliverableStructureExtends.
     *
     * @param id The id of the structure
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    protected NamedDeliverableStructureExtends(long id) {
        super(id);
    }

    /**
     * Creates a new NamedDeliverableStructureExtends.
     *
     * @param id   The id of the structure
     * @param name The name of the structure
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    protected NamedDeliverableStructureExtends(long id, String name) {
        super(id, name);
    }
}
