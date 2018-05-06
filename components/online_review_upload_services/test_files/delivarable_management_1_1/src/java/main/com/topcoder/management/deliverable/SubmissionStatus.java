/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p> The SubmissionStatus class is a support class in the modeling classes. It is used to tag a submission as having a
 * certain status. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is mutable because its base class is mutable.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class SubmissionStatus extends NamedDeliverableStructure {

    /**
     * <p>Creates a new SubmissionStatus.</p>
     */
    public SubmissionStatus() {
        super();
    }

    /**
     * <p>Creates a new SubmissionStatus.</p>
     *
     * @param id The id of the submission status
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    public SubmissionStatus(long id) {
        super(id);
    }

    /**
     * <p>Creates a new SubmissionStatus.</p>
     *
     * @param id   The id of the submission status
     * @param name The name of the submission status
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    public SubmissionStatus(long id, String name) {
        super(id, name);
    }
}
