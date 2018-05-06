/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableChecker;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * This demo shows the component usage. The API of this component is very simple, it contains only
 * one public method. The constructors are the same for all classes.
 *
 * @author kr00tki
 * @version 1.0
 */
public class Demo extends DbTestCase {

    /**
     * <p>
     * Demonstrates the component usage. Because all the checker work in the same way,
     * only some of them are shown.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDemo() throws Exception {
        DBConnectionFactory factory = getConnectionFactory();
        // creating the checkers is very simple and the same for all
        // it takes the DbConnectionFactory argument and uses default connection
        DeliverableChecker checker = new AggregationDeliverableChecker(factory);

        // or the factory and connection name if it need to be used.
        checker = new AggregationReviewDeliverableChecker(factory, CONNECTION_NAME);

        // alternatively the second constructor can take null connection name
        checker = new TestCasesDeliverableChecker(factory, null);

        // to check the deliverable we need to call the check method
        Deliverable deliverable = new Deliverable(1, 1, 1, null, true);

        checker.check(deliverable);

        // some checkers such as:
        // CommittedReviewDeliverableChecker
        // FinalReviewDeliverableChecker
        // SubmitterCommentDeliverableChecker
        // can only check deliverable that are per submission
        Deliverable perSubmission = new Deliverable(2, 1, 2, new Long(1), false);

        checker = new FinalReviewDeliverableChecker(factory, CONNECTION_NAME);
        checker.check(perSubmission);

        // checking the other deliverables will cause the DeliverableCheckingException
        try {
            checker.check(deliverable);
            fail("Not per submission deliverable.");
        } catch (DeliverableCheckingException ex) {
            // ok
        }
    }

}
