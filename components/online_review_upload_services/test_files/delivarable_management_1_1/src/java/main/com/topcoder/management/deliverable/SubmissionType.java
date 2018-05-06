/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p>The SubmissionType class is a support class in the modeling classes.  It is used to tag a submission as having a
 * certain type. For development, this class will be very simple to implement, as has no fields of its own and simply
 * delegates to the constructors of the base class.</p>
 *
 * <p><strong>Thread Safety:</strong> This class is mutable because its base class is mutable.</p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.1
 * @since 1.1
 */
public class SubmissionType extends NamedDeliverableStructure {

    /**
     * <p>Creates new instance of <strong>SubmissionType</strong> class.</p>
     */
    public SubmissionType() {
        // empty constructor
    }

    /**
     * <p>Creates new instance of <strong>SubmissionType</strong> class.</p>
     *
     * @param id The id of the submission status
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    public SubmissionType(long id) {
        super(id);
    }

    /**
     * <p>Creates new instance of <strong>SubmissionType</strong> class.</p>
     *
     * @param id   The id of the submission status
     * @param name The name of the submission status
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    public SubmissionType(long id, String name) {
        super(id, name);
    }
}
