/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p> The NamedDeliverableStructure class extends the AuditedDeliverableStructure class to hold a name and description.
 * Like AuditedDeliverableStructure, it is an abstract class. The NamedDeliverableStructure class is simply a container
 * for the name and description. Both these data fields have the getters and setters. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is highly mutable. All fields can be changed.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public abstract class NamedDeliverableStructure extends AuditedDeliverableStructure {

    /**
     * <p> The name of the structure. This field is set in the constructor and is mutable. Any value is allowed for this
     * field. It can be null or non-null, and any value is allowed when non-null (empty string, all whitespace, etc).
     * The default value of this field is null, which indicates that the name has not yet been set (or has been unset).
     * </p>
     */
    private String name;

    /**
     * <p> The description of the structure. This field is set in the constructor and is mutable. Any value is allowed
     * for this field. It can be null or non-null, and any value is allowed when non-null (empty string, all whitespace,
     * etc). The default value of this field is null, which indicates that the name has not yet been set (or has been
     * unset). </p>
     */
    private String description = null;

    /**
     * <p>Creates a new NamedDeliverableStructure.</p>
     */
    protected NamedDeliverableStructure() {
        super();
        name = null;
    }

    /**
     * <p>Creates a new NamedDeliverableStructure.</p>
     *
     * @param id The id of the structure
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    protected NamedDeliverableStructure(long id) {
        super(id);
        name = null;
    }

    /**
     * <p>Creates a new NamedDeliverableStructure.</p>
     *
     * @param id   The id of the structure
     * @param name The name of the structure
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    protected NamedDeliverableStructure(long id, String name) {
        super(id);
        this.name = name;
    }

    /**
     * <p>Sets the name of the structure. Any value, null or non-null is allowed.</p>
     *
     * @param name The name for the structure
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Gets the name of the structure. This method may return null or any other string value.</p>
     *
     * @return The name of the structure
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Sets the description of the structure. Any value, null or non-null is allowed.</p>
     *
     * @param description The description for the structure
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Gets the description of the structure. This method may return null or any other string value.</p>
     *
     * @return The description of the structure
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Tells whether all the required fields of this structure have values set.</p>
     *
     * @return True if all fields required for persistence are present
     */
    public boolean isValidToPersist() {
        // This method returns true only all of the following are true: id is not UNSET_ID, name is
        // not null, description is not null, super.isValidToPersist is true.
        return ((getId() != UNSET_ID)
                && (name != null)
                && (description != null)
                && (super.isValidToPersist()));
    }
}
