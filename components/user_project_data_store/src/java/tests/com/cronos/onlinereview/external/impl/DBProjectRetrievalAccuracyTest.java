/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalProject;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.accuracytests.AccuracyHelper;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

/**
 * <p>
 * Tests the DBProjectRetrieval class.
 * </p>
 *
 * @author lyt, restarter
 * @version 1.1
 */
public class DBProjectRetrievalAccuracyTest extends BaseDBRetrievalAccuracyTest {
    /**
     * <P>
     * The namespace for testing.
     * </p>
     */
    private static final String NAMESPACEL_NO_FORUM_TYPE = "com.cronos.onlinereview.external.NoForumType";

    /**
     * <p>
     * The name of the namespace that the calling program can populate which contains DBConnectionFactory and
     * other configuration values.
     * </p>
     */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>
     * The default forum id.
     * </p>
     */
    private static final int defaultForumId = 10;

    /**
     * <p>
     * An DBProjectRetrieval instance for testing.
     * </p>
     */
    private DBProjectRetrieval projectRetrieval = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        defaultConnFactory = new DBConnectionFactoryImpl(NAMESPACE);
        defaultConnection = defaultConnFactory.createConnection(DEFAULT_CONNECTION_NAME);

        projectRetrieval = new DBProjectRetrieval(defaultConnFactory, DEFAULT_CONN_NAME, defaultForumId);

        baseDBRetrievalByString = new DBProjectRetrieval(NAMESPACE);
        baseDBRetrieval = projectRetrieval;

        // Inserts.
        AccuracyHelper.insertIntoComponentCataLog(defaultConnection);
        AccuracyHelper.insertIntoComponentVersions(defaultConnection);
        AccuracyHelper.insertIntoCompForumXref(defaultConnection);
        AccuracyHelper.insertIntoTechnologyTypes(defaultConnection);
        AccuracyHelper.insertIntoCompTechnology(defaultConnection);
    }

    /**
     * <p>
     * Set defaultDBProjectRetrieval to null.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        // Cleans up.
        AccuracyHelper.cleanupTable(defaultConnection, "comp_technology");
        AccuracyHelper.cleanupTable(defaultConnection, "comp_forum_xref");
        AccuracyHelper.cleanupTable(defaultConnection, "comp_versions");
        AccuracyHelper.cleanupTable(defaultConnection, "comp_catalog");
        AccuracyHelper.cleanupTable(defaultConnection, "technology_types");

        // Sets connection and defaultDBProjectRetrieval to initial values.
        defaultConnection.close();
        projectRetrieval = null;
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(String).
     * </p>
     *
     * <p>
     * The defaultDBProjectRetrieval instance should be created successfully.
     * </p>
     *
     * @throws ConfigException to junit
     */
    public void testConstructor1_Accuracy() throws ConfigException {
        projectRetrieval = new DBProjectRetrieval(NAMESPACE);

        assertTrue("defaultDBProjectRetrieval should be instance of DBProjectRetrieval.",
            projectRetrieval instanceof DBProjectRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory =
            AccuracyHelper.getPrivateField(BaseDBRetrieval.class, projectRetrieval, "connFactory");
        Object connName = AccuracyHelper.getPrivateField(BaseDBRetrieval.class, projectRetrieval, "connName");
        Object forumType =
            AccuracyHelper.getPrivateField(DBProjectRetrieval.class, projectRetrieval, "forumType");

        // Asserts the set.
        assertNotNull("connFactory should be set correctly.", connFactory);
        assertEquals("connName should be set correctly.", DEFAULT_CONN_NAME, connName);
        assertEquals("forumType should be set correctly.", new Integer(5), forumType);
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(String).
     * </p>
     *
     * <p>
     * The defaultDBProjectRetrieval instance should be created successfully.
     * </p>
     *
     * @throws ConfigException to junit
     */
    public void testConstructor1_NoForumType() throws ConfigException {
        projectRetrieval = new DBProjectRetrieval(NAMESPACEL_NO_FORUM_TYPE);
        assertTrue("defaultDBProjectRetrieval should be instance of DBProjectRetrieval.",
            projectRetrieval instanceof DBProjectRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory =
            AccuracyHelper.getPrivateField(BaseDBRetrieval.class, projectRetrieval, "connFactory");
        Object connName = AccuracyHelper.getPrivateField(BaseDBRetrieval.class, projectRetrieval, "connName");
        Object forumType =
            AccuracyHelper.getPrivateField(DBProjectRetrieval.class, projectRetrieval, "forumType");

        // Asserts the set.
        assertNotNull("connFactory should be set correctly.", connFactory);
        assertEquals("connName should be set correctly.", DEFAULT_CONN_NAME, connName);
        assertEquals("forumType should be set correctly.", new Integer(2), forumType);
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(DBConnectionFactory, String, int).
     * </p>
     *
     * <p>
     * The instance should be created successfully.
     * </p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testConstructor2_Accuracy() throws ConfigException {
        assertTrue("defaultDBProjectRetrieval should be instance of DBProjectRetrieval.",
            projectRetrieval instanceof DBProjectRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory =
            AccuracyHelper.getPrivateField(BaseDBRetrieval.class, projectRetrieval, "connFactory");
        Object connName = AccuracyHelper.getPrivateField(BaseDBRetrieval.class, projectRetrieval, "connName");
        Object forumType =
            AccuracyHelper.getPrivateField(DBProjectRetrieval.class, projectRetrieval, "forumType");

        // Asserts the set.
        assertEquals("connFactory should be set correctly.", defaultConnFactory, connFactory);
        assertEquals("connName should be set correctly.", DEFAULT_CONN_NAME, connName);
        assertEquals("forumType should be set correctly.", new Integer(defaultForumId), forumType);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(long).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectLong_Accuracy1() throws RetrievalException {
        ExternalProject project = projectRetrieval.retrieveProject(2);

        assertEquals("The catalog id should be the same.", 2, project.getCatalogId());
        assertEquals("The comment should be the same.", "Average", project.getComments());
        assertEquals("The component id should be the same.", 1002, project.getComponentId());
        assertEquals("The description should be the same.", "Second Project B", project.getDescription()
            .trim());

        assertEquals("The short description should be the same.", "Second Project", project.getShortDescription());
        assertEquals("The functional description should be the same.", "Sleeping", project.getFunctionalDescription());
        List techs = Arrays.asList(project.getTechnologies());
        assertEquals("The technologies should be empty", 2, techs.size());
        assertTrue("The technologies is not correctly retrieved", techs.contains("jdbc"));
        assertTrue("The technologies is not correctly retrieved", techs.contains("ejb"));

        assertEquals("The id should be the same.", 2, project.getId());
        assertEquals("The name should be the same.", "Project B", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 2", project.getVersion().trim());
        assertEquals("The version id should be the same.", 2, project.getVersionId());
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(long).
     * </p>
     *
     * <p>
     * Due to the outer join, some value will got null here, but the null value would be set into integer, so
     * it turns to zero.
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
/*  public void testRetrieveProjectLong_Accuracy2() throws RetrievalException {
        ExternalProject project = projectRetrieval.retrieveProject(4);

        // Asserts.
        assertEquals("The catalog id should be the same.", 4, project.getCatalogId());
        assertEquals("The comment should be the same.", "Bad", project.getComments());
        assertEquals("The component id should be the same.", 1004, project.getComponentId());
        assertEquals("The description should be the same.", "Fourth Project D", project.getDescription()
            .trim());


        assertEquals("The short description should be the same.", "Fourth Project", project.getShortDescription());
        assertEquals("The functional description should be the same.", "Working", project.getFunctionalDescription());
        List techs = Arrays.asList(project.getTechnologies());
        assertEquals("The technologies should be empty", 2, techs.size());
        assertTrue("The technologies is not correctly retrieved", techs.contains("java"));
        assertTrue("The technologies is not correctly retrieved", techs.contains("jms"));

        assertEquals("The id should be the same.", 4, project.getId());
        assertEquals("The name should be the same.", "Project D", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 3", project.getVersion().trim());
        assertEquals("The version id should be the same.", 3, project.getVersionId());

        // Notice here, as the outer join.
        assertEquals("The forum id should be the same.", -1, project.getForumId());
    }
*/
    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(long).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectLong_Accuracy3() throws RetrievalException {
        ExternalProject project = projectRetrieval.retrieveProject(111);
        assertNull("Tests the accuracy of the retrieveProjects(long) failed.", project);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(long[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_LongArray() throws RetrievalException {
        ExternalProject[] projects = projectRetrieval.retrieveProjects(new long[] {1, 2, 4});

        // Asserts.
        assertEquals("Three records would be got.", 3, projects.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(long[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_Duplicate() throws RetrievalException {
        // fail("not implemented");
        ExternalProject[] projects = projectRetrieval.retrieveProjects(new long[] {1, 2, 2, 4});

        // Asserts.
        assertEquals("Three records would be got.", 3, projects.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(long[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_NotFound() throws RetrievalException {
        ExternalProject[] projects = projectRetrieval.retrieveProjects(new long[] {889, 900, 901});

        // Asserts.
        assertEquals("Three records would be got.", 0, projects.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(String[], String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectsStrings_Accuracy1() throws RetrievalException {
        ExternalProject[] projects =
            projectRetrieval.retrieveProjects(new String[] {"Project A", "Project C"}, new String[] {
                "Version 1", "Version 2"});

        // Asserts.
        assertEquals("One records would be got.", 1, projects.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(String[], String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectsStrings_Accuracy4() throws RetrievalException {
        ExternalProject[] projects =
            projectRetrieval.retrieveProjects(new String[] {"Project A", "Project A"}, new String[] {
                "Version 1", "Version 1"});

        // Asserts.
        assertEquals("One records would be got.", 1, projects.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(String[], String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectsStrings_Accuracy5() throws RetrievalException {
        ExternalProject[] projects =
            projectRetrieval.retrieveProjects(new String[] {"Project A", "Project A", "Project C"},
                new String[] {"Version 1", "Version 1", "Version 4"});

        // Asserts.
        assertEquals("One records would be got.", 1, projects.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(String, String).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectsStrings_Accuracy2() throws RetrievalException {
        ExternalProject[] projects = projectRetrieval.retrieveProject("Project B", "Version 2");

        // Asserts.
        assertEquals("One records would be got.", 1, projects.length);

        ExternalProject project = projects[0];
        assertEquals("The catalog id should be the same.", 2, project.getCatalogId());
        assertEquals("The comment should be the same.", "Average", project.getComments());
        assertEquals("The component id should be the same.", 1002, project.getComponentId());
        assertEquals("The description should be the same.", "Second Project B", project.getDescription()
            .trim());

        assertEquals("The short description should be the same.", "Second Project", project.getShortDescription());
        assertEquals("The functional description should be the same.", "Sleeping", project.getFunctionalDescription());
        List techs = Arrays.asList(project.getTechnologies());
        assertEquals("The technologies should be empty", 2, techs.size());
        assertTrue("The technologies is not correctly retrieved", techs.contains("jdbc"));
        assertTrue("The technologies is not correctly retrieved", techs.contains("ejb"));

        assertEquals("The id should be the same.", 2, project.getId());
        assertEquals("The name should be the same.", "Project B", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 2", project.getVersion().trim());
        assertEquals("The version id should be the same.", 2, project.getVersionId());
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveProjects(String, String).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjectsStrings_Accuracy3() throws RetrievalException {
        ExternalProject[] projects = projectRetrieval.retrieveProject("Project A", "Version 2");

        // Asserts.
        assertEquals("One records would be got.", 0, projects.length);
    }

    /**
     * <p>
     * Tests the accuracy of the createObject(ResultSet).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     * @throws SQLException this exception would never be thrown in this test case.
     */
     public void testCreateObject_ResultSet() throws RetrievalException, SQLException {
        // Prepares the ps and rs.
        PreparedStatement ps =
            defaultConnection.prepareStatement("SELECT cv.comp_vers_id, cv.component_id, "
                + "version, version_text, comments, component_name, description, "
                + "cc.root_category_id category_id, forum_id, cc.short_desc short_desc, "
                + "cc.function_desc function_desc "
                + "FROM comp_versions cv, comp_catalog cc, OUTER comp_forum_xref f "
                + "WHERE cv.component_id = cc.component_id and cv.comp_vers_id = f.comp_vers_id "
                + "and f.forum_type = 5 and cv.comp_vers_id = 2");

        ResultSet rs = ps.executeQuery();

        // Creates object.
        ExternalProject project = null;

        while (rs.next()) {
            project = (ExternalProject) projectRetrieval.createObject(rs);
        }

        // Asserts.
        assertEquals("The catalog id should be the same.", 2, project.getCatalogId());
        assertEquals("The comment should be the same.", "Average", project.getComments());
        assertEquals("The component id should be the same.", 1002, project.getComponentId());
        assertEquals("The description should be the same.", "Second Project B", project.getDescription()
            .trim());
        assertEquals("The short description should be the same.", "Second Project", project.getShortDescription());
        assertEquals("The functional description should be the same.", "Sleeping", project.getFunctionalDescription());
        assertEquals("The technologies should be empty", 0, project.getTechnologies().length);

        assertEquals("The forum id should be the same.", 100002, project.getForumId());
        assertEquals("The id should be the same.", 2, project.getId());
        assertEquals("The name should be the same.", "Project B", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 2", project.getVersion().trim());
        assertEquals("The version id should be the same.", 2, project.getVersionId());
    }

}
