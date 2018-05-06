/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableChecker;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;


/**
 * A mock class implements <code>DeliverableChecker</code> interface.
 *
 * @author myxgyy
 * @version 1.0
 */
public class MockDeliverableChecker implements DeliverableChecker {
    /**
     * Checks whether the phase of deliverable is 1000L or not.
     *
     * @param deliverable the deliverable to be checked.
     * @throws DeliverableCheckingException if phase of deliverable is 1000L.
     */
    public void check(Deliverable deliverable) throws DeliverableCheckingException {
        if (deliverable.getPhase() == 1000L) {
            throw new DeliverableCheckingException("phase of deliverable should not be 1000L");
        }
    }
}
