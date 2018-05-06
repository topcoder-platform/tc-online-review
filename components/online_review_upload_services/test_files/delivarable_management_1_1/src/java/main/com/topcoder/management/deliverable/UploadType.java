/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p> The UploadType class is a support class in the modeling classes. It is used to tag an upload as being of a
 * certain type. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is mutable because its base class is mutable.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class UploadType extends NamedDeliverableStructure {

    /**
     * <p>Creates a new UploadType.</p>
     */
    public UploadType() {
        super();
    }

    /**
     * <p>Creates a new UploadType.</p>
     *
     * @param id The id of the upload type
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    public UploadType(long id) {
        super(id);
    }

    /**
     * <p>Creates a new UploadType.</p>
     *
     * @param id   The id of the upload type
     * @param name The name of the upload type
     *
     * @throws IllegalArgumentException If id is <= 0
     * @throws IllegalArgumentException If name is null
     */
    public UploadType(long id, String name) {
        super(id, name);
    }
}
