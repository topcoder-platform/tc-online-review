/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;

import com.cronos.onlinereview.external.impl.MockDBProjectRetrieval;
import com.cronos.onlinereview.external.impl.MockDBUserRetrieval;

/**
 * <p>
 * This class demonstrates the common usage of this <b>User Project Data Store</b> component.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class Demo extends TestCase {

    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "SampleConfig.xml";

    /**
     * <p>
     * The name of the namespace that the calling program can populate which contains DBConnectionFactory and other
     * configuration values.
     * </p>
     */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>
     * An DBUserRetrieval instance for testing.
     * </p>
     */
    private UserRetrieval defaultDBUserRetrieval = null;

    /**
     * <p>
     * An DBProjectRetrieval instance for testing.
     * </p>
     */
    private ProjectRetrieval defaultDBProjectRetrieval = null;

    /**
     * <p>
     * The default connection used for db operations.
     * </p>
     */
    private Connection defaultConnection = null;

    /**
     * <p>
     * Setup the demonstration environment, prepare data for retrieval.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        UnitTestHelper.addConfig(CONFIG_FILE);

        defaultDBUserRetrieval = new MockDBUserRetrieval(NAMESPACE);
        defaultDBProjectRetrieval = new MockDBProjectRetrieval(NAMESPACE);

        // Retrieves DBConnectionFactory.
        defaultConnection = ((MockDBUserRetrieval) defaultDBUserRetrieval).getConnection();

        cleanupDatabase();

        // Inserts.
        UnitTestHelper.insertIntoComponentCataLog(defaultConnection);
        UnitTestHelper.insertIntoComponentVersions(defaultConnection);
        UnitTestHelper.insertIntoCompForumXref(defaultConnection);
        UnitTestHelper.insertIntoTechnologyTypes(defaultConnection);
        UnitTestHelper.associateComponentTechnologies(defaultConnection);

        UnitTestHelper.insertIntoEmail(defaultConnection);
        UnitTestHelper.insertIntoUser(defaultConnection);
        UnitTestHelper.insertIntoUserRating(defaultConnection);
        UnitTestHelper.insertIntoUserReliability(defaultConnection);
    }

    /**
     * <p>
     * Tears down the demonstration environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        cleanupDatabase();

        defaultDBUserRetrieval = null;

        UnitTestHelper.clearConfig();
        defaultConnection.close();
        super.tearDown();
    }

    /**
     * <p>
     * Cleanup the database.
     * </p>
     *
     * @throws SQLException
     *             If any unexpected exception occurs.
     */
    private void cleanupDatabase() throws SQLException {
        // Cleans up.
        UnitTestHelper.cleanupTable(defaultConnection, "comp_technology");
        UnitTestHelper.cleanupTable(defaultConnection, "technology_types");
        UnitTestHelper.cleanupTable(defaultConnection, "comp_forum_xref");
        UnitTestHelper.cleanupTable(defaultConnection, "comp_versions");
        UnitTestHelper.cleanupTable(defaultConnection, "comp_catalog");

        UnitTestHelper.cleanupTable(defaultConnection, "user_reliability");
        UnitTestHelper.cleanupTable(defaultConnection, "user_rating");
        UnitTestHelper.cleanupTable(defaultConnection, "user");
        UnitTestHelper.cleanupTable(defaultConnection, "email");
    }

    /**
     * <p>
     * Retrieves the users by handle.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByHandle() throws RetrievalException {
        System.out.println("Demo 1: Retrieves the users by handle.");

        // Should be non-null.
        ExternalUser user = defaultDBUserRetrieval.retrieveUser("Handle B");

        // Should be 1002.
        System.out.println(user.getId());
        // Should all be non-null.
        System.out.println(user.getHandle());
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());

        // Should be N/A or zero.
        System.out.println(user.getDesignRating());
        System.out.println(user.getDesignReliability());
        System.out.println(user.getDesignNumRatings());
        System.out.println(user.getDesignVolatility());

        // There are two alt-emails.
        System.out.println(user.getAlternativeEmails().length);
        System.out.println(user.getAlternativeEmails()[0]);
        System.out.println(user.getAlternativeEmails()[1]);

        // Should be not N/A and not zero.
        System.out.println(user.getDevRating());
        System.out.println(user.getDevVolatility());
        System.out.println(user.getDevReliability());
        System.out.println(user.getDevNumRatings());

        // Should be null if user is not found.
        ExternalUser shouldBeNull = defaultDBUserRetrieval.retrieveUser("Not Exist");
        System.out.println(shouldBeNull);

        // Find all users, case insensitively. This demo assumes there is only 1 user
        // with this handle as a lower-case string.
        ExternalUser[] userA = defaultDBUserRetrieval.retrieveUsersIgnoreCase(new String[] {"hAnDle A"});
        // Should only have one.
        System.out.println(userA.length);
        ExternalUser sameAsUser = userA[0];
        // Outputs his info.
        System.out.println(sameAsUser.getHandle());

        // Should have a maximum of 2 users, but the order is indeterminate
        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsers(new String[] {"Handle A", "Handle C", "Handle Z"});
        System.out.println(users.length);

        System.out.println();
    }

    /**
     * <p>
     * Retrieves the users by id.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersById() throws RetrievalException {
        System.out.println("Demo 2: Retrieves the users by id.");

        // The record exists.
        ExternalUser user = defaultDBUserRetrieval.retrieveUser(1002);
        System.out.println(user.getId());

        // This record does not exist; should be null if user is not found.
        ExternalUser shouldBeNull = defaultDBUserRetrieval.retrieveUser(Long.MAX_VALUE);
        // Should be null
        System.out.println(shouldBeNull);

        // Should have a maximum of 2 users even though there were 3 values given.
        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsers(new long[] {1001, 1002, Long.MAX_VALUE});
        // The length should be 2.
        System.out.println(users.length);

        System.out.println();
    }

    /**
     * <p>
     * Retrieves the users by last name and first name.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByNames() throws RetrievalException {
        System.out.println("Demo 3: Retrieves the users by names.");

        // Should retrieve all users whose first name starts with 'First' and last name starts with 'Last'.
        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsersByName("First A", "Last A");
        // There should be 1 user got.
        System.out.println(users.length);

        ExternalUser user = users[0];
        // There is only one alternative email.
        System.out.println(user.getAlternativeEmails().length);
        // The email address should be the same to User1@163.com.
        System.out.println(user.getAlternativeEmails()[0]);
        // The design number ratings should be the same as 10.
        System.out.println(user.getDesignNumRatings());
        // The design rating should be the same as 1563.
        System.out.println(user.getDesignRating());
        // The design reliability should be the same 1.00 %.
        System.out.println(user.getDesignReliability());
        // The design volatility should be the same as 431.
        System.out.println(user.getDesignVolatility());
        // There is no dev rating of the user.
        System.out.println(user.getDevNumRatings());
        // There is no dev rating of the user.
        System.out.println(user.getDevRating());
        // There is no dev rating of the user.
        System.out.println(user.getDevReliability());
        // There is no dev rating of the user.
        System.out.println(user.getDevVolatility());
        // The email address should be the same as User1@gmail.com.
        System.out.println(user.getEmail());
        // The first name should be the same as First A.
        System.out.println(user.getFirstName());
        // The last name should be the same as Last A.
        System.out.println(user.getLastName());
        // The handle should be the same as Handle A.
        System.out.println(user.getHandle());
        // The user id should be the same as 1001.
        System.out.println(user.getId());

        // Should retrieve all users whose last name starts with Last (ignores first name)
        ExternalUser[] tcsUsers = defaultDBUserRetrieval.retrieveUsersByName(" ", "Last");
        // There should be 3 users got.
        System.out.println(tcsUsers.length);

        // must have at least first or last name be not empty.
        try {
            defaultDBUserRetrieval.retrieveUsersByName("", "");
        } catch (IllegalArgumentException e) {
            // Ignore.
        }

        System.out.println();
    }

    /**
     * <p>
     * Retrieves the projects by id.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectsById() throws RetrievalException {
        System.out.println("Demo 4: Retrieves the projects by id.");

        // Retrieve a known project
        ExternalProject project = defaultDBProjectRetrieval.retrieveProject(2);
        System.out.println(project.getId());

        // Outputs its info.
        System.out.println(project.getComponentId());
        System.out.println(project.getForumId());
        System.out.println(project.getName());
        System.out.println(project.getVersion());
        System.out.println(project.getVersionId());
        System.out.println(project.getDescription());
        System.out.println(project.getComments());
        System.out.println(project.getShortDescription());
        System.out.println(project.getFunctionalDescription());
        String[] technologies = project.getTechnologies(); // should not be null
        for (int t = 0; t < technologies.length; t++) {
            System.out.println("Uses technology: " + technologies[t]);
        }

        // Not found ¨C should be null which is acceptable
        ExternalProject shouldBeNull = defaultDBProjectRetrieval.retrieveProject(Long.MAX_VALUE);
        System.out.println(shouldBeNull);

        // Should only have a maximum of 1 entry.
        ExternalProject[] projects = defaultDBProjectRetrieval.retrieveProjects(new long[] {1, 100});
        System.out.println(projects.length);
        System.out.println(projects[0].getName());

        System.out.println();
    }

    /**
     * <p>
     * Retrieves the projects by name and version.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectsByNameAndVersion() throws RetrievalException {
        System.out.println("Demo 5: Retrieves the projects by name and version.");

        // There might be more than one with this name and version (e.g., different catalog)
        ExternalProject[] projects = defaultDBProjectRetrieval.retrieveProject("Project A", "Version 1");

        ExternalProject project = null;
        String[] technologies = null;
        for (int i = 0; i < projects.length; ++i) {
            // Outputs the info of each project.
            project = projects[i];
            System.out.println(project.getId());
            System.out.println(project.getName());
            System.out.println(project.getVersion());
            System.out.println(project.getCatalogId());
            System.out.println(project.getForumId());
            System.out.println(project.getShortDescription());
            System.out.println(project.getFunctionalDescription());
            technologies = project.getTechnologies(); // should not be null
            for (int t = 0; t < technologies.length; t++) {
                System.out.println("Uses technology: " + technologies[t]);
            }
        }

        // Should only have a maximum of 2 entries but the order may vary.
        ExternalProject[] projects2 = defaultDBProjectRetrieval.retrieveProjects(
                new String[] {"Project A", "Project C"}, new String[] {"Version 1", "Version 2"});
        // Should get only one.
        System.out.println(projects2.length);
        System.out.println(projects2[0].getName());

        System.out.println();
    }
}
