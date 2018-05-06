/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator;
import com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator;


/**
 * <p>
 * A test case that demonstrates the usage of this component.
 * </p>
 *
 * @author Schpotsky, TCSDEVELOPER
 * @version 1.0
 */
public class Demo {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(Demo.class);
    }

    /**
     * <p>
     * Sets up test environment before all tests run.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestHelper.initDB();
    }

    /**
     * <p>
     * Tears down test environment after all tests have run.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        TestHelper.cleanDB();
    }

    /**
     * <p>
     * Demonstrates the usage of this component to calculate and adjust the project payment with following records
     * in database.
     * </p>
     * <p>
     * Table <code>default_project_payment</code>
     *
     * <pre>
     * ------------------------------------------------------------------------------------------------------
     * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
     * ------------------------------------------------------------------------------------------------------
     * |          1          |         2        |       0      |       0.00       |           0.01          |
     * |          1          |         4        |       0      |       0.12       |           0.05          |
     * |          1          |         8        |      10      |       0.00       |           0.00          |
     * |          2          |         9        |       0      |       0.05       |           0.00          |
     * ------------------------------------------------------------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>project</code>
     *
     * <pre>
     * ------------------------------------
     * | project_id | project_category_id |
     * ------------------------------------
     * |    230     |          1          |
     * |    231     |          2          |
     * |    232     |          3          |
     * |    233     |          4          |
     * ------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>prize</code>
     *
     * <pre>
     * -----------------------------------------------------
     * | project_id | prize_type_id | place | prize_amount |
     * -----------------------------------------------------
     * |     230    |      15       |   1   |      500     |
     * |     231    |      15       |   1   |      400     |
     * |     232    |      15       |   1   |      650     |
     * |     233    |      15       |   1   |     1000     |
     * -----------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>submission</code>
     *
     * <pre>
     * ---------------------------------------------------------
     * | submission_type_id | upload_id | submission_status_id |
     * ---------------------------------------------------------
     * |          1         |    500    |          1           |
     * |          1         |    623    |          2           |
     * |          1         |     46    |          7           |
     * ---------------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>upload</code>
     *
     * <pre>
     * -------------------------------------------
     * | project_id | upload_type_id | upload_id |
     * -------------------------------------------
     * |    230     |       1        |    500    |
     * |    230     |       1        |    623    |
     * |    233     |       1        |     46    |
     * -------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>project_payment_adjustment</code>
     *
     * <pre>
     * -------------------------------------------------------------
     * | project_id | resource_role_id | fixed_amount | multiplier |
     * -------------------------------------------------------------
     * |    230     |         2        |    14.00     |    NULL    |
     * |    230     |         4        |    22.00     |    NULL    |
     * |    230     |         5        |    NULL      |    3.00    |
     * |    230     |         8        |    NULL      |    2.00    |
     * |    230     |         9        |    NULL      |    NULL    |
     * -------------------------------------------------------------
     * </pre>
     *
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void demoAPI() throws Exception {
        // create an instance of DefaultProjectPaymentCalculator using the default configuration file
        // found in com/topcoder/management/payment/calculator/impl/DefaultProjectPaymentCalculator.properties
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();

        // create an instance of DefaultProjectPaymentCalculator using the provided configuration file
        String configFile = "test_files" + File.separator + "config.properties";
        String namespace = "com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator";

        calculator = new DefaultProjectPaymentCalculator(configFile, namespace);

        // Get the default payments for the primary screener and reviewer for the project identified
        // by projectId = 230
        List<Long> resourceRoleIDs = new ArrayList<Long>();
        resourceRoleIDs.add(DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID);
        resourceRoleIDs.add(DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID);

        Map<Long, BigDecimal> result = calculator.getDefaultPayments(230, resourceRoleIDs);
        System.out.println("Default payment: ");
        for (Long roleId : result.keySet()) {
            System.out.println("Payment for role " + roleId + " = " + result.get(roleId));
        }

        // The returned Map will contain two entries :
        // The first element : key = 2, BigDecimal value = 10 = 0 + (0.0 + 0.01*2)*500
        // This project has two submissions :
        // (submission.upload_id = 500 , and submission.upload_id = 623)

        // The second element : key = 4, BigDecimal value = 85 = 0 + (0.12 + 0.05*1)*500
        // This project has a single submission that passed screening.
        // (submission.submission_status_id != 2)

        // Get the default payment for a Final Reviewer for a project with prize = $ 1000.00 having 3
        // submissions and project category id = 2
        BigDecimal finalReviewerPayment =
            ((DefaultProjectPaymentCalculator) calculator).getDefaultPayment(2,
                DefaultProjectPaymentCalculator.FINAL_REVIEWER_RESOURCE_ROLE_ID, new BigDecimal(1000), 3);
        System.out.println("Payment for Final Reviewer role = " + finalReviewerPayment);

        // The value of the finalReviewerPayment will be 50 = 0 + (0.05 + 0.00*0)*1000
        // in the above calculation the number of submissions is equal to zero, because it has no meaning
        // for a final reviewer.

        // create an instance of DefaultProjectPaymentCalculator using the default configuration file
        // found in com/topcoder/management/payment/calculator/impl/ProjectPaymentAdjustmentCalculator.properties
        ProjectPaymentCalculator adjuster = new ProjectPaymentAdjustmentCalculator();

        // create a new ProjectPaymentAdjustmentCalculator with to wrap the calculator created above using the same
        // configuration file.
        String namespace1 = "com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator";
        adjuster = new ProjectPaymentAdjustmentCalculator(calculator, configFile, namespace1);

        // Get the adjusted default payments for primary screener, reviewer and aggregator for the
        // project with project_id = 230.
        resourceRoleIDs.add(DefaultProjectPaymentCalculator.AGGREGATOR_RESOURCE_ROLE_ID);
        // the other two role ids were already in the list (see above).

        Map<Long, BigDecimal> adjustedPayments = adjuster.getDefaultPayments(230, resourceRoleIDs);

        // The adjustedPayments map will contain the following 3 elements
        // first element : key = 2, value = 14 (adjusted to the value of fixed_amount)
        // second element : key = 4, value = 22 (adjusted to the value of fixed_amount).
        // third element : key = 8, value = (10 + (0.00 + 0.00*0)*500 ) * 2 = 20 (multiplier == 2)
        for (Long roleId : adjustedPayments.keySet()) {
            System.out.println("Adjusted Payment for role " + roleId + " = " + adjustedPayments.get(roleId));
        }
    }
}
