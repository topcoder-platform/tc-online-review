/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests;

import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationResourceRole;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.project.phases.Phase;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>A helper class providing the utility methods for accuracy tests.</p>
 * 
 * @author isv
 * @version 1.0.1
 */
public class AccuracyTestsHelper {

    /**
     * <p>Gets the value of specified field of specified object using Java Reflection API.</p>
     *
     * @param object an <code>Object</code> to get field value for. 
     * @param fieldName a <code>String</code> providing the name of the field.
     * @return an <code>Object</code> providing the value of specified field. 
     * @throws Exception if an unexpected error occurs.
     */
    public static Object getField(Object object, String fieldName) throws Exception {
        Class clazz = object.getClass();
        Field field = null;
        while (field == null && clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(object);
        field.setAccessible(accessible);
        return value;
    }

    /**
     * <p>Removes the records which might have been added to database tables by the tests.</p>
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public static void cleanupDatabase() throws Exception {
        Connection connection = TestDataFactory.getDatabaseConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM resource_info WHERE resource_id >= 125200 ");
            stmt.executeUpdate("DELETE FROM resource WHERE resource_id >= 125200");
            stmt.executeUpdate("UPDATE review_application SET review_application_status_id = 1 " +
                    "WHERE review_application_id BETWEEN 29901 AND 29951");
//            stmt.executeUpdate("UPDATE project_phase " +
//                               "SET scheduled_end_time = scheduled_start_time + 172800 UNITS SECOND, " +
//                               "duration = 172800000 " +
//                               "WHERE phase_type_id = 4 AND phase_status_id = 2");
        } finally {
            connection.close();
        }
    }

    /**
     * <p>Verifies that specified phases are equal.</p>
     *
     * @param before a <code>Project</code> providing the phases before test execution.
     * @param after a <code>Project</code> providing the phases after test execution.
     */
    public static void testPhasesForEquality(com.topcoder.project.phases.Project before,
                                             com.topcoder.project.phases.Project after) {
        Phase[] phasesBefore = before.getAllPhases();
        Phase[] phasesAfter = after.getAllPhases();

        Assert.assertEquals("The phases number has changed", phasesBefore.length, phasesAfter.length);
        int n = phasesBefore.length;

        for (int i = 0; i < n; i++) {
            Phase phaseBefore = phasesBefore[i];
            Phase phaseAfter = phasesAfter[i];
            String phaseType = phaseBefore.getPhaseType().getName();
            Assert.assertEquals("Phase type has changed",
                    phaseBefore.getPhaseType().getName(), phaseAfter.getPhaseType().getName());
            Assert.assertEquals("Phase status has changed",
                    phaseBefore.getPhaseStatus().getName(), phaseAfter.getPhaseStatus().getName());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") scheduled start date has changed",
                    phaseBefore.getScheduledStartDate(), phaseAfter.getScheduledStartDate());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") scheduled end date has changed",
                    phaseBefore.getScheduledEndDate(), phaseAfter.getScheduledEndDate());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") actual start date has changed",
                    phaseBefore.getActualStartDate(), phaseAfter.getActualStartDate());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") actual end date has changed",
                    phaseBefore.getActualEndDate(), phaseAfter.getActualEndDate());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") fixed start date has changed",
                    phaseBefore.getFixedStartDate(), phaseAfter.getFixedStartDate());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") duration has changed",
                    phaseBefore.getLength(), phaseAfter.getLength());
        }
    }

    /**
     * <p>Verifies that specified phases are equal.</p>
     *
     * @param before a <code>Project</code> providing the phases before test execution.
     * @param after a <code>Project</code> providing the phases after test execution.
     */
    public static  void testPhasesForExtension(com.topcoder.project.phases.Project before,
                                               com.topcoder.project.phases.Project after) {
        Phase[] phasesBefore = before.getAllPhases();
        Phase[] phasesAfter = after.getAllPhases();

        Assert.assertEquals("The phases number has changed", phasesBefore.length, phasesAfter.length);
        int n = phasesBefore.length;

        for (int i = 0; i < n; i++) {
            Phase phaseBefore = phasesBefore[i];
            Phase phaseAfter = phasesAfter[i];
            String phaseType = phaseBefore.getPhaseType().getName();
            long phaseTypeId = phaseBefore.getPhaseType().getId();
            boolean phaseTypeRequiresExtension = ((phaseTypeId == 3) || (phaseTypeId == 4))
                    && phaseBefore.getPhaseStatus().getId() == 2;
            boolean isPhaseClosed = phaseBefore.getPhaseStatus().getId() == 3;

            Assert.assertEquals("Phase type has changed",
                    phaseBefore.getPhaseType().getName(), phaseAfter.getPhaseType().getName());
            Assert.assertEquals("Phase status has changed",
                    phaseBefore.getPhaseStatus().getName(), phaseAfter.getPhaseStatus().getName());
            if (isPhaseClosed) {
                Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") scheduled start date has changed",
                        phaseBefore.getScheduledStartDate(), phaseAfter.getScheduledStartDate());
            }
            if (phaseTypeRequiresExtension) {
                Assert.assertTrue(phaseType + " phase (" + phaseBefore.getId() + ") scheduled end date has not " +
                        "changed correctly: before: " + phaseBefore.getScheduledEndDate() + ", after: " +
                        phaseAfter.getScheduledEndDate(),
                        phaseBefore.getScheduledEndDate().before(phaseAfter.getScheduledEndDate()));
            } else {
                if (isPhaseClosed) {
                    Assert.assertEquals(
                            phaseType + " phase (" + phaseBefore.getId() + ") scheduled end date has changed",
                            phaseBefore.getScheduledEndDate(), phaseAfter.getScheduledEndDate());
                }
            }
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") actual start date has changed",
                    phaseBefore.getActualStartDate(), phaseAfter.getActualStartDate());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") actual end date has changed",
                    phaseBefore.getActualEndDate(), phaseAfter.getActualEndDate());
            Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") fixed start date has changed",
                    phaseBefore.getFixedStartDate(), phaseAfter.getFixedStartDate());
            if (phaseTypeRequiresExtension) {
                Assert.assertTrue(phaseType + " phase (" + phaseBefore.getId() + ") duration has not changed",
                        phaseBefore.getLength() < phaseAfter.getLength());
            } else {
                Assert.assertEquals(phaseType + " phase (" + phaseBefore.getId() + ") duration has changed",
                        phaseBefore.getLength(), phaseAfter.getLength());
            }
        }
    }

    /**
     * <p>Verifies that resources for project have been modified according to specified assignment.</p>
     *
     * @param before a <code>Resource[]</code> providing the phases before test execution.
     * @param after a <code>Resource[]</code> providing the phases after test execution.
     * @param assignment a <code>Map</code> providing the
     * @param testName a <code>String</code> providing the name of the test.
     */
    public static void testResources(Resource[] before, Resource[] after,
                                     Map<ReviewApplication, ReviewApplicationRole> assignment, String testName) {
        int expectedNewResourcesCount = 0;
        for (ReviewApplicationRole role : assignment.values()) {
            List<ReviewApplicationResourceRole> resourceRoles = role.getResourceRoles();
            expectedNewResourcesCount += resourceRoles.size();
        }

        System.out.println("ACCURACY TEST - " + testName + ". Checking resources.");
        System.out.println("Resources Before: ");
        for (int i = 0; i < before.length; i++) {
            Resource resource = before[i];
            System.out.println("  " + resource.getId() + " (" + resource.getResourceRole().getName() + ") (" 
                    + resource.getProject() + ") -> " + resource.getAllProperties());
        }
        System.out.println("Resources After: ");
        for (int i = 0; i < after.length; i++) {
            Resource resource = after[i];
            System.out.println("  " + resource.getId() + " (" + resource.getResourceRole().getName() + ") ("
                    + resource.getProject() + ") -> " + resource.getAllProperties());
        }

        Assert.assertEquals("Incorrect number of resources", before.length + expectedNewResourcesCount, after.length);

        // Test that resources existing before still exist and collect newly added resources
        List<Resource> resourcesAfter = new ArrayList<Resource>(Arrays.asList(after));
        for (Resource resource : before) {
            boolean found = false;
            for (Resource resource2 : resourcesAfter) {
                if (resource.getId() == resource2.getId()) {
                    found = true;
                    resourcesAfter.remove(resource2);
                    break;
                }
            }
            Assert.assertTrue("Resource " + resource.getId() + " existing before is not found anymore", found);
        }

        // Now test that appropriate resources have been added according to assignment
        for (Map.Entry<ReviewApplication, ReviewApplicationRole> entry : assignment.entrySet()) {
            ReviewApplicationRole role = entry.getValue();
            ReviewApplication reviewApplication = entry.getKey();
            List<ReviewApplicationResourceRole> expectedResourceRoles = role.getResourceRoles();
            for (ReviewApplicationResourceRole expectedResourceRole : expectedResourceRoles) {
                boolean found = false;
                for (Resource resource : resourcesAfter) {
                    long resourceRoleId = resource.getResourceRole().getId();
                    if (resourceRoleId == expectedResourceRole.getResourceRoleId()) {
                        long addedResourceUserId 
                                = Long.parseLong(String.valueOf(resource.getProperty("External Reference ID")));
                        if (addedResourceUserId == reviewApplication.getUserId()) {
                            found = true;
                            resourcesAfter.remove(resource);

                            // Test resource properties
                            Assert.assertEquals("Wrong resource handle",
                                    User.getByUserId(reviewApplication.getUserId()).getName(),
                                    resource.getProperty("Handle"));
                            Assert.assertNotNull("Resource registration date is null",
                                    resource.getProperty("Registration Date"));
                            break;
                        }
                    }
                }
                Assert.assertTrue("No resource added for review application: " + reviewApplication
                        + " and role :" + expectedResourceRole, found);
            }
        }

        // Just a safety check
        Assert.assertTrue("Some strange resources have been added", resourcesAfter.isEmpty());
    }
}
