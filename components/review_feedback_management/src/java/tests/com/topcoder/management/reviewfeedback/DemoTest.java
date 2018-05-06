/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.management.reviewfeedback.impl.JDBCReviewFeedbackManager;

/**
 * <p>
 * The demo of this component.
 * </p>
 *
 * <p>
 * <em>Changes in 2.0:</em>
 * <ol>
 * <li>Updated according to the new data model and changed API.</li>
 * </ol>
 * </p>
 *
 * @author gevak, amazingpig, sparemax
 * @version 2.0
 */
public class DemoTest extends BaseUnitTest {

    /**
     * <p>
     * Returns the test suite which aggregating all tests in this class.
     * </p>
     *
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(DemoTest.class);
    }

    /**
     * <p>
     * Demonstrates the api usage of the component. It provides CRUD operations for managing the ReviewFeedback entities
     * in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated according to the new data model and changed API.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_demo() throws Exception {
        // Create sample input.
        ReviewFeedback entity = new ReviewFeedback();
        entity.setProjectId(1);
        entity.setComment("comment text");
        ReviewFeedbackDetail detail = new ReviewFeedbackDetail();
        detail.setReviewerUserId(126);
        detail.setScore(2);
        detail.setFeedbackText("feedback text");
        List<ReviewFeedbackDetail> details = new ArrayList<ReviewFeedbackDetail>();
        details.add(detail);
        entity.setDetails(details);

        // Create DAO.
        ReviewFeedbackManager dao = new JDBCReviewFeedbackManager();

        // Perform CRUD operations
        // Create.
        entity = dao.create(entity, "operator");
        System.out.println("entity.getId() = " + entity.getId());
        System.out.println("entity.getCreateUser() = " + entity.getCreateUser());
        // Update.
        entity.getDetails().get(0).setScore(1);
        dao.update(entity, "anotherOperator");
        // Get.
        long id = entity.getId();
        entity = null;
        entity = dao.get(id);
        System.out.println("entity.getScore() = " + entity.getDetails().get(0).getScore());
        System.out.println("entity.getModifyUser() = " + entity.getModifyUser());

        // Retrieves entities with given project ID from persistence.
        List<ReviewFeedback> list = dao.getForProject(1);
        System.out.println(list.size() + " ReviewFeedback entities return.");

        // Delete.
        dao.delete(entity.getId());
        entity = dao.get(entity.getId());
        System.out.println("'entity == null' = " + (entity == null));
    }
}
