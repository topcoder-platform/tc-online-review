/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.cronos.onlinereview.external.UnitTestHelper;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the ExternalProjectImpl class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class ExternalProjectImplUnitTest extends TestCase {

    /**
     * <p>
     * The default id of the projectImpl.
     * </p>
     */
    private static final long DEFAULT_ID = 123;

    /**
     * <p>
     * The default version id of the projectImpl.
     * </p>
     */
    private static final long DEFAULT_VERSION_ID = 100;

    /**
     * <p>
     * The default version string of the projectImpl.
     * </p>
     */
    private static final String DEFAULT_VERSION_STRING = "version 1.0";

    /**
     * <p>
     * The default catalog id of the projectImpl.
     * </p>
     */
    private static final long DEFAULT_CATALOG_ID = 50;

    /**
     * <p>
     * The default comment string of the projectImpl.
     * </p>
     */
    private static final String DEFAULT_COMMMENT_STRING = "Comment about User Project Data Store 1.0";

    /**
     * <p>
     * The default component id of the projectImpl.
     * </p>
     */
    private static final long DEFAULT_COMPONENT_ID = 22826649;

    /**
     * <p>
     * The default forum id of the projectImpl.
     * </p>
     */
    private static final long DEFAULT_FORUM_ID = 12345;

    /**
     * <p>
     * The default description string of the projectImpl.
     * </p>
     */
    private static final String DEFAULT_DESCRIPTION_STRING = "This component named User Project Data Store "
            + "and version 2.0";

    /**
     * <p>
     * The default short description string of the projectImpl.
     * </p>
     */
    private static final String DEFAULT_SHORT_DESCRIPTION_STRING = "User Project Data Store version 2.0";

    /**
     * <p>
     * The default functional description string of the projectImpl.
     * </p>
     */
    private static final String DEFAULT_FUNCTIONAL_DESCRIPTION_STRING = "manipulate user project data with database.";

    /**
     * <p>
     * The default name string of the projectImpl.
     * </p>
     */
    private static final String DEFAULT_NAME_STRING = "User Project Data Store";

    /**
     * <p>
     * An ExternalProjectImpl instance for testing.
     * </p>
     */
    private ExternalProjectImpl projectImpl = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        projectImpl = new ExternalProjectImpl(DEFAULT_ID, DEFAULT_VERSION_ID, DEFAULT_VERSION_STRING);
    }

    /**
     * <p>
     * Set objectImpl to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        projectImpl = null;

        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(long, long, String).
     * </p>
     * <p>
     * The ExternalProjectImpl instance should be created successfully.
     * </p>
     */
    public void testCtor_LongLongString() {

        assertNotNull("ExternalProjectImpl should be accurately created.", projectImpl);
        assertTrue("projectImpl should be ExternalProjectImpl type.", projectImpl instanceof ExternalProjectImpl);
    }

    /**
     * <p>
     * Tests the accuracy of the getter getId().
     * </p>
     */
    public void testGetter_GetId() {

        assertEquals("The id should be got correctly.", DEFAULT_ID, projectImpl.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getVersionId()().
     * </p>
     */
    public void testGetter_GetVersionId() {

        assertEquals("The version id should be got correctly.", DEFAULT_VERSION_ID, projectImpl.getVersionId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getVersion()().
     * </p>
     */
    public void testGetter_GetVersion() {

        assertEquals("The version string should be got correctly.", DEFAULT_VERSION_STRING, projectImpl.getVersion());
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, long, String).
     * </p>
     * <p>
     * If the given id is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongLongString_NegativeId() {

        try {
            new ExternalProjectImpl(-1, DEFAULT_VERSION_ID, DEFAULT_VERSION_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, long, String).
     * </p>
     * <p>
     * If the given version id is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongLongString_NegativeVersionId() {

        try {
            new ExternalProjectImpl(DEFAULT_ID, -2, DEFAULT_VERSION_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, long, String).
     * </p>
     * <p>
     * If the given version is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongLongString_NullVersion() {

        try {
            new ExternalProjectImpl(DEFAULT_ID, DEFAULT_VERSION_ID, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the setter setCatalogId(long).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetCatalogId() {

        projectImpl.setCatalogId(DEFAULT_CATALOG_ID);

        // Gets catalogID by reflection.
        Object catalogId = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl, "catalogId");

        assertEquals("The catalog id should be set correctly.", new Long(DEFAULT_CATALOG_ID), catalogId);
    }

    /**
     * <p>
     * Tests the accuracy of the setter setComments(String).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetComments() {

        projectImpl.setComments(DEFAULT_COMMMENT_STRING);

        // Gets comments by reflection.
        Object comments = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl, "comments");

        assertEquals("The comments should be set correctly.", DEFAULT_COMMMENT_STRING, comments);
    }

    /**
     * <p>
     * Tests the accuracy of the setter setComponentId(long).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetComponentId() {

        projectImpl.setComponentId(DEFAULT_COMPONENT_ID);

        // Gets componentId by reflection.
        Object componentId = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl, "componentId");

        assertEquals("The componentId should be set correctly.", new Long(DEFAULT_COMPONENT_ID), componentId);
    }

    /**
     * <p>
     * Tests the accuracy of the setter setDescription(String).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetDescription() {

        projectImpl.setDescription(DEFAULT_DESCRIPTION_STRING);

        // Gets description by reflection.
        Object description = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl, "description");

        assertEquals("The description should be set correctly.", DEFAULT_DESCRIPTION_STRING, description);
    }

    /**
     * <p>
     * Tests the accuracy of the setter setShortDescription(String).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetShortDescription() {

        projectImpl.setShortDescription(DEFAULT_SHORT_DESCRIPTION_STRING);

        // Gets short description by reflection.
        Object shortDescription = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl,
                "shortDescription");

        assertEquals("The short description should be set correctly.", DEFAULT_SHORT_DESCRIPTION_STRING,
                shortDescription);
    }

    /**
     * <p>
     * Tests the accuracy of the setter setFunctionalDescription(String).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetFunctionalDescription() {

        projectImpl.setFunctionalDescription(DEFAULT_FUNCTIONAL_DESCRIPTION_STRING);

        // Gets functional description by reflection.
        Object functionalDescription = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl,
                "functionalDescription");

        assertEquals("The functional description should be set correctly.", DEFAULT_FUNCTIONAL_DESCRIPTION_STRING,
                functionalDescription);
    }

    /**
     * <p>
     * Tests the accuracy of the setter setForumId(long).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetForumId() {

        projectImpl.setForumId(DEFAULT_FORUM_ID);

        // Gets forumId by reflection.
        Object forumId = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl, "forumId");

        assertEquals("The forumId should be set correctly.", new Long(DEFAULT_FORUM_ID), forumId);
    }

    /**
     * <p>
     * Tests the accuracy of the setter setName(String).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testSetter_SetName() {

        projectImpl.setName(DEFAULT_NAME_STRING);

        // Gets name by reflection.
        Object name = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl, "name");

        assertEquals("The name should be set correctly.", DEFAULT_NAME_STRING, name);
    }

    /**
     * <p>
     * Tests the accuracy of the addTechnology(String).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testAddTechnologyAccuracy() {
        projectImpl.addTechnology("J2EE");

        // Gets name by reflection.
        Object name = UnitTestHelper.getPrivateField(ExternalProjectImpl.class, projectImpl, "technologies");

        assertTrue("the technology should be contained.", ((Set) name).contains("J2EE"));
    }

    /**
     * <p>
     * Tests the failure of the setter setCatalogId(long).
     * </p>
     * <p>
     * If the given catalogId is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetCatalogId_NegativeCatalogId() {

        try {
            projectImpl.setCatalogId(-1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setCatalogId(long).
     * </p>
     * <p>
     * If the catalogId field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetCatalogId_CatalogIdAlreadySet() {

        projectImpl.setCatalogId(DEFAULT_CATALOG_ID);

        try {
            projectImpl.setCatalogId(DEFAULT_CATALOG_ID);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setComments(String).
     * </p>
     * <p>
     * If the given comments is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetComments_NullComments() {

        try {
            projectImpl.setComments(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setComments(String).
     * </p>
     * <p>
     * If the comments field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetComments_CommentsAlreadySet() {

        projectImpl.setComments(DEFAULT_COMMMENT_STRING);

        try {
            projectImpl.setComments(DEFAULT_COMMMENT_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setComponentId(long).
     * </p>
     * <p>
     * If the given componentId is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetComponentId_NegativeComponentId() {

        try {
            projectImpl.setComponentId(-1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setComponentId(long).
     * </p>
     * <p>
     * If the componentId field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetComponentId_ComponentIdAlreadySet() {

        projectImpl.setComponentId(DEFAULT_COMPONENT_ID);

        try {
            projectImpl.setComponentId(DEFAULT_COMPONENT_ID);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setDescription(String).
     * </p>
     * <p>
     * If the given description is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetDescriptions_NullDescriptions() {

        try {
            projectImpl.setDescription(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setDescription(String).
     * </p>
     * <p>
     * If the description field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetDescription_DescriptionsAlreadySet() {

        projectImpl.setDescription(DEFAULT_DESCRIPTION_STRING);

        try {
            projectImpl.setDescription(DEFAULT_DESCRIPTION_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setShortDescription(String).
     * </p>
     * <p>
     * If the given short description is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetShortDescription_NullDescription() {

        try {
            projectImpl.setShortDescription(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setShortDescription(String).
     * </p>
     * <p>
     * If the shortDescription field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetShortDescription_ShortDescriptionAlreadySet() {

        projectImpl.setShortDescription(DEFAULT_SHORT_DESCRIPTION_STRING);

        try {
            projectImpl.setShortDescription(DEFAULT_SHORT_DESCRIPTION_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setFunctionalDescription(String).
     * </p>
     * <p>
     * If the given functional description is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetFunctionalDescription_NullDescription() {

        try {
            projectImpl.setFunctionalDescription(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setFunctionalDescription(String).
     * </p>
     * <p>
     * If the functionalDescription field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetFunctionalDescription_FunctionalDescriptionAlreadySet() {

        projectImpl.setFunctionalDescription(DEFAULT_FUNCTIONAL_DESCRIPTION_STRING);

        try {
            projectImpl.setFunctionalDescription(DEFAULT_FUNCTIONAL_DESCRIPTION_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the addTechnology(String).
     * </p>
     * <p>
     * If the given functional description is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testAddTechnology_NullTechnology() {
        try {
            projectImpl.addTechnology(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setForumId(long).
     * </p>
     * <p>
     * If the given forumId is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetForumId_NegativeForumId() {

        try {
            projectImpl.setForumId(-1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setForumId(long).
     * </p>
     * <p>
     * If the forumId field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetForumId_ForumIdAlreadySet() {

        projectImpl.setForumId(DEFAULT_FORUM_ID);

        try {
            projectImpl.setForumId(DEFAULT_FORUM_ID);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setName(String).
     * </p>
     * <p>
     * If the given name is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetName_NullDescriptions() {

        try {
            projectImpl.setName(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the setter setName(String).
     * </p>
     * <p>
     * If the name field is already set, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetter_SetName_DescriptionsAlreadySet() {

        projectImpl.setName(DEFAULT_NAME_STRING);

        try {
            projectImpl.setName(DEFAULT_NAME_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the getter getCatalogId().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetCatalogId() {

        projectImpl.setCatalogId(DEFAULT_CATALOG_ID);

        assertEquals("The catalog id should be got correctly.", DEFAULT_CATALOG_ID, projectImpl.getCatalogId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getCatalogId().
     * </p>
     * <p>
     * If is no component, -1 would be returned.
     * </p>
     */
    public void testGetter_GetCatalogId_NoCatalogIdSet() {

        assertEquals("The catalog id should be got correctly.", -1, projectImpl.getCatalogId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getComments().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetComments() {

        projectImpl.setComments(DEFAULT_COMMMENT_STRING);

        assertEquals("The comments should be got correctly.", DEFAULT_COMMMENT_STRING, projectImpl.getComments());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getComments().
     * </p>
     * <p>
     * If is no comments, null would be returned.
     * </p>
     */
    public void testGetter_GetComments_NoCommentsSet() {

        assertNull("The comments got should be null.", projectImpl.getComments());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getComponentId().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetComponentId() {

        projectImpl.setComponentId(DEFAULT_COMPONENT_ID);

        assertEquals("The componentId should be got correctly.", DEFAULT_COMPONENT_ID, projectImpl.getComponentId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getComponentId().
     * </p>
     * <p>
     * If is no component id, -1 would be returned.
     * </p>
     */
    public void testGetter_GetComponentId_NoComponentIdSet() {

        assertEquals("The component id should be got correctly.", -1, projectImpl.getComponentId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getDescription().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetDescription() {

        projectImpl.setDescription(DEFAULT_DESCRIPTION_STRING);

        assertEquals("The description should be got correctly.", DEFAULT_DESCRIPTION_STRING, projectImpl
                .getDescription());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getDescription().
     * </p>
     * <p>
     * If is no description, null would be returned.
     * </p>
     */
    public void testGetter_GetDescription_NoDescriptionSet() {

        assertNull("The description got should be null.", projectImpl.getDescription());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getShortDescription().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetShortDescription() {

        projectImpl.setShortDescription(DEFAULT_SHORT_DESCRIPTION_STRING);

        assertEquals("The description should be got correctly.", DEFAULT_SHORT_DESCRIPTION_STRING, projectImpl
                .getShortDescription());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getShortDescription().
     * </p>
     * <p>
     * If is no description, null would be returned.
     * </p>
     */
    public void testGetter_GetShortDescription_NoShortDescriptionSet() {

        assertEquals("The short description got should be empty string.", "", projectImpl.getShortDescription());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getFunctionalDescription().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetFunctionalDescription() {

        projectImpl.setFunctionalDescription(DEFAULT_FUNCTIONAL_DESCRIPTION_STRING);

        assertEquals("The description should be got correctly.", DEFAULT_FUNCTIONAL_DESCRIPTION_STRING, projectImpl
                .getFunctionalDescription());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getFunctionalDescription().
     * </p>
     * <p>
     * If is no description, null would be returned.
     * </p>
     */
    public void testGetter_GetFunctionalDescription_NoFunctionalDescriptionSet() {

        assertEquals("The functional description got should be empty string.", "", projectImpl
                .getFunctionalDescription());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getForumId().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetForumId() {

        projectImpl.setForumId(DEFAULT_FORUM_ID);

        assertEquals("The forumId should be got correctly.", DEFAULT_FORUM_ID, projectImpl.getForumId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getForumId().
     * </p>
     * <p>
     * If is no forumId id, -1 would be returned.
     * </p>
     */
    public void testGetter_GetForumId_NoForumIdSet() {

        assertEquals("The forum id should be got correctly.", -1, projectImpl.getForumId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getName().
     * </p>
     * <p>
     * Using validated setter to test.
     * </p>
     */
    public void testGetter_GetName() {

        projectImpl.setName(DEFAULT_NAME_STRING);

        assertEquals("The name should be got correctly.", DEFAULT_NAME_STRING, projectImpl.getName());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getName().
     * </p>
     * <p>
     * If is no name, null would be returned.
     * </p>
     */
    public void testGetter_GetName_NoNameSet() {

        assertNull("The name got should be null.", projectImpl.getName());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getTechnologies().
     * </p>
     * <p>
     * Using validated addTechnology to test.
     * </p>
     */
    public void testGetter_GetTechnologies() {
        String[] expectedTechnologies = new String[] {"Java", "J2EE", "XML"};

        // Case 1
        for (int i = 0; i < expectedTechnologies.length; i++) {
            projectImpl.addTechnology(expectedTechnologies[i]);
        }

        String[] technologies = projectImpl.getTechnologies();

        assertNotNull("should never return null.", technologies);

        List techList = Arrays.asList(technologies);
        assertEquals("the array size is incorrect.", 3, techList.size());

        for (int i = 0; i < expectedTechnologies.length; i++) {
            assertTrue(expectedTechnologies[i] + " is not added.", techList.contains(expectedTechnologies[i]));
        }

        // Case 2 for duplication addition
        for (int i = 0; i < expectedTechnologies.length; i++) {
            projectImpl.addTechnology(expectedTechnologies[i]);
        }

        technologies = projectImpl.getTechnologies();

        assertNotNull("should never return null.", technologies);

        techList = Arrays.asList(technologies);
        assertEquals("the array size is incorrect.", 3, techList.size());

        for (int i = 0; i < expectedTechnologies.length; i++) {
            assertTrue(expectedTechnologies[i] + " is not added.", techList.contains(expectedTechnologies[i]));
        }
    }

    /**
     * <p>
     * Tests the accuracy of the getter getTechnologies().
     * </p>
     * <p>
     * If none technology is added, should return an empty string array.
     * </p>
     */
    public void testGetter_GetTechnologies_NoTechnologiesAdded() {
        String[] technologies = projectImpl.getTechnologies();

        assertNotNull("should never return null.", technologies);
        assertTrue("should be empty array.", technologies.length == 0);
    }
}
