/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.accuracytests;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.cronos.termsofuse.dao.TermsOfUseDao;
import com.cronos.termsofuse.dao.impl.TermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.cronos.termsofuse.model.TermsOfUseType;

/**
 * <p>
 * This class contains Accuracy tests for TermsOfUseDaoImpl.
 * </p>
 *
 * <p>
 * Change in 1.1:
 * <ul>
 * <li>Add test cases for new methods managing terms of use dependencies.
 * <li>Update test cases to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
 * <li>Update test cases to support adding of TermsOfUse#agreeabilityType property.
 * </ul>
 * </p>
 *
 * @author sokol, fish_ship
 * @version 1.1
 */
public class TermsOfUseDaoImplAccuracyTest extends BaseAccuracyTest {

    /**
     * <p>
     * Represents TermsOfUseDao for testing.
     * </p>
     */
    private TermsOfUseDao dao;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void setUp() throws Exception {
        super.setUp();
        dao = new TermsOfUseDaoImpl(getConfiguration(TERMS_OF_USE_CONFIGURATION));
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void tearDown() throws Exception {
        super.tearDown();
        dao = null;
    }

    /**
     * <p>
     * Tests TermsOfUseDaoImpl constructor.
     * </p>
     * <p>
     * TermsOfUseDaoImpl instance should be created successfully. No exception is expected.
     * </p>
     */
    public void testConstructor() {
        assertNotNull("TermsOfUseDaoImpl instance should be created successfully", dao);
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#createTermsOfUse(com.cronos.termsofuse.model.TermsOfUse, String)} with valid
     * arguments passed.
     * </p>
     * <p>
     * Terms of use should be added to database successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testCreateTermsOfUse() throws Exception {
        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(1, "Non-agreeable", "Non-agreeable");
        TermsOfUse termsOfUse = createTermsOfUse(0, 1, "title1", "some url", type);
        String termsText = "terms text";
        TermsOfUse actualTermsOfUse = dao.createTermsOfUse(termsOfUse, termsText);
        assertTrue("Terms of use id should be generated.", actualTermsOfUse.getTermsOfUseId() != 0);
        assertTrue("Terms of use should be created in database.",
                compareTermsOfUse(termsOfUse, dao.getTermsOfUse(actualTermsOfUse.getTermsOfUseId())));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#createTermsOfUse(com.cronos.termsofuse.model.TermsOfUse, String)} with null terms
     * text passed.
     * </p>
     * <p>
     * Terms of use should be added to database successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testCreateTermsOfUse_Null_TermsText() throws Exception {
        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(1, "Non-agreeable", "Non-agreeable");
        TermsOfUse termsOfUse = createTermsOfUse(0, 1, "title1", "some url", type);
        String termsText = null;
        TermsOfUse actualTermsOfUse = dao.createTermsOfUse(termsOfUse, termsText);
        assertTrue("Terms of use id should be generated.", actualTermsOfUse.getTermsOfUseId() != 0);
        assertTrue("Terms of use should be created in database.",
                compareTermsOfUse(termsOfUse, dao.getTermsOfUse(actualTermsOfUse.getTermsOfUseId())));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#updateTermsOfUse(TermsOfUse)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use should be updated in database successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testUpdateTermsOfUse() throws Exception {
        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(2, "Electronically-agreeable",
                "Electronically-agreeable");
        TermsOfUse termsOfUse = createTermsOfUse(1, 1, "title1", "some url", type);
        TermsOfUse actualTermsOfUse = dao.updateTermsOfUse(termsOfUse);
        assertTrue("Terms of use should be updated in database.",
                compareTermsOfUse(termsOfUse, dao.getTermsOfUse(actualTermsOfUse.getTermsOfUseId())));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUse(long)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUse() throws Exception {
        long id = 1;
        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(2, "Electronically-agreeable",
                "Electronically-agreeable");
        TermsOfUse termsOfUse = createTermsOfUse(id, 1, "t1", "url1", type);
        assertTrue("Terms of use should be retrieved successfully.",
                compareTermsOfUse(termsOfUse, dao.getTermsOfUse(id)));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUse(long)} with id for non existing terms of use passed.
     * </p>
     * <p>
     * null should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUse_Null() throws Exception {
        long id = 0;
        assertNull("null should be retrieved successfully.", dao.getTermsOfUse(id));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteTermsOfUse(long)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use should be deleted successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testDeleteTermsOfUse() throws Exception {
        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(2, null, null);
        TermsOfUse termsOfUse = dao.createTermsOfUse(createTermsOfUse(0, 1, "title", "url", type), "termsText");
        dao.deleteTermsOfUse(termsOfUse.getTermsOfUseId());
        assertNull("Terms of use should be deleted successfully.", dao.getTermsOfUse(termsOfUse.getTermsOfUseId()));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUseByTypeId(int)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUseByTypeId() throws Exception {
        int id = 1;
        List<TermsOfUse> termsOfUses = dao.getTermsOfUseByTypeId(id);
        assertEquals("Terms of use for given type should be retrieved successfully.", 3, termsOfUses.size());
        for (TermsOfUse termsOfUse : termsOfUses) {
            assertEquals("Terms of use type should be correct.", id, termsOfUse.getTermsOfUseTypeId());
        }
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUseByTypeId(int)} with id for non existing type passed.
     * </p>
     * <p>
     * Empty collection of terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUseByTypeId_Empty() throws Exception {
        int id = 0;
        List<TermsOfUse> termsOfUses = dao.getTermsOfUseByTypeId(id);
        assertEquals("Terms of use for given type should be retrieved successfully.", 0, termsOfUses.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getAllTermsOfUse()}.
     * </p>
     * <p>
     * Terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetAllTermsOfUse() throws Exception {
        List<TermsOfUse> termsOfUses = dao.getAllTermsOfUse();
        assertEquals("All terms of use should be retrieved successfully.", 4, termsOfUses.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUseType(int)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUseType() throws Exception {
        int id = 2;
        TermsOfUseType type = dao.getTermsOfUseType(id);
        assertNotNull("Terms of use type should be retrieved successfully.", type);
        assertTrue("Terms of use type id and description should have correct value.",
                compareTermsOfUseType(type, createTermsType(id, "type2")));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUseType(int)} with non existing id for terms type.
     * </p>
     * <p>
     * null should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUseType_Null() throws Exception {
        int id = 0;
        TermsOfUseType type = dao.getTermsOfUseType(id);
        assertNull("null should be retrieved successfully.", type);
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#updateTermsOfUseType(TermsOfUseType)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use should be updated successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testUpdateTermsOfUseType() throws Exception {
        int id = 2;
        TermsOfUseType type = createTermsType(id, "description");
        dao.updateTermsOfUseType(type);
        assertTrue("Terms of use type description should be updated successfully.",
                compareTermsOfUseType(type, dao.getTermsOfUseType(id)));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUseText(long)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use text should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUseText() throws Exception {
        long id = 1;
        String text = dao.getTermsOfUseText(id);
        assertEquals("Terms of use text should be retrieved successfully.", "text1", text);
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getTermsOfUseText(long)} with valid arguments passed for null text.
     * </p>
     * <p>
     * null terms of use text should be retrieved successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUseText_Null() throws Exception {
        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(2, null, null);
        TermsOfUse termsOfUse = dao.createTermsOfUse(createTermsOfUse(0, 1, "title", "url", type), null);
        String text = dao.getTermsOfUseText(termsOfUse.getTermsOfUseId());
        assertNull("null terms of use text should be retrieved successfully.", text);
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#setTermsOfUseText(long, String)} with valid arguments passed.
     * </p>
     * <p>
     * Terms of use text should be set successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testSetTermsOfUseText() throws Exception {
        long id = 1;
        String text = "some new text";
        dao.setTermsOfUseText(id, text);
        assertEquals("Terms of use text should be set successfully.", text, dao.getTermsOfUseText(id));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#createDependencyRelationship(long, long)} with valid arguments passed.
     * </p>
     * <p>
     * Dependency relationship should be created successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     *
     * @since 1.1
     */
    public void testCreateDependencyRelationship_One() throws Exception {
        dao.createDependencyRelationship(1, 2);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'createDependencyRelationship' should be correct.", 1, res.size());

        TermsOfUseAgreeabilityType agreeabilityType = createTermsOfUseAgreeabilityType(2, "Electronically-agreeable",
                "Electronically-agreeable");
        TermsOfUseAgreeabilityType noAgreeabilityType = createTermsOfUseAgreeabilityType(1, "Non-agreeable",
                "Non-agreeable");

        assertTrue("fail to create dependency relationship",
                compareTermsOfUse(createTermsOfUse(2l, 1, "t2", "url2", noAgreeabilityType), res.get(0)));

        res = dao.getDependentTermsOfUse(2);

        assertEquals("'createDependencyRelationship' should be correct.", 1, res.size());

        assertTrue("fail to create dependency relationship",
                compareTermsOfUse(createTermsOfUse(1l, 1, "t1", "url1", agreeabilityType), res.get(0)));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#createDependencyRelationship(long, long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * The dependent with two dependencies and the dependency with two dependents. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testCreateDependencyRelationship_Two() throws Exception {
        dao.createDependencyRelationship(1, 2);
        dao.createDependencyRelationship(1, 3);
        dao.createDependencyRelationship(3, 2);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'createDependencyRelationship' should be correct.", 2, res.size());
        List<Long> ids = Arrays.asList(res.get(0).getTermsOfUseId(), res.get(1).getTermsOfUseId());
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(2L));
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(3L));

        res = dao.getDependentTermsOfUse(2);

        ids = Arrays.asList(res.get(0).getTermsOfUseId(), res.get(1).getTermsOfUseId());
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(1L));
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(3L));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getDependencyTermsOfUse(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Because there is no dependency the result should be empty. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testGetDependencyTermsOfUse_NoDependency() throws Exception {
        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'getDependencyTermsOfUse' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getDependencyTermsOfUse(long)} with valid arguments passed.
     * </p>
     *
     * <P>
     * Should get the dependency terms of use successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testGetDependencyTermsOfUse_OneDependency() throws Exception {
        dao.createDependencyRelationship(1, 2);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'getDependencyTermsOfUse' should be correct.", 1, res.size());

        assertTrue(
                "fail to get dependency terms of use",
                compareTermsOfUse(
                        createTermsOfUse(2l, 1, "t2", "url2",
                                createTermsOfUseAgreeabilityType(1, "Non-agreeable", "Non-agreeable")), res.get(0)));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getDependencyTermsOfUse(long)} with valid arguments passed.
     * </p>
     *
     * <P>
     * Should get the two dependencies terms of use successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testGetDependencyTermsOfUse_TwoDependencies() throws Exception {
        dao.createDependencyRelationship(1, 2);
        dao.createDependencyRelationship(1, 3);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'getDependencyTermsOfUse' should be correct.", 2, res.size());

        assertTrue(
                "fail to get dependency terms of use",
                compareTermsOfUse(
                        createTermsOfUse(2l, 1, "t2", "url2",
                                createTermsOfUseAgreeabilityType(1, "Non-agreeable", "Non-agreeable")), res.get(0)));
        assertTrue(
                "fail to get dependency terms of use",
                compareTermsOfUse(
                        createTermsOfUse(3l, 1, "t3", "url3",
                                createTermsOfUseAgreeabilityType(1, "Non-agreeable", "Non-agreeable")), res.get(1)));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getDependentTermsOfUse(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * The result should be empty because there is no dependent.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testGetDependentTermsOfUse_NoDependent() throws Exception {
        List<TermsOfUse> res = dao.getDependentTermsOfUse(2);

        assertEquals("'getDependentTermsOfUse' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getDependentTermsOfUse(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Get the only one dependent successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testGetDependentTermsOfUse_OneDependent() throws Exception {
        dao.createDependencyRelationship(1, 2);

        List<TermsOfUse> res = dao.getDependentTermsOfUse(2);

        assertEquals("'getDependentTermsOfUse' should be correct.", 1, res.size());

        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(2, "Electronically-agreeable",
                "Electronically-agreeable");
        assertTrue("fail to get dependent terms of use",
                compareTermsOfUse(createTermsOfUse(1l, 1, "t1", "url1", type), res.get(0)));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#getDependentTermsOfUse(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Get the two dependents successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testGetDependentTermsOfUse_TwoDependents() throws Exception {
        dao.createDependencyRelationship(1, 2);
        dao.createDependencyRelationship(3, 2);

        List<TermsOfUse> res = dao.getDependentTermsOfUse(2);

        assertEquals("'getDependentTermsOfUse' should be correct.", 2, res.size());

        TermsOfUseAgreeabilityType type1 = createTermsOfUseAgreeabilityType(2, "Electronically-agreeable",
                "Electronically-agreeable");
        assertTrue("fail to get dependent terms of use",
                compareTermsOfUse(createTermsOfUse(1l, 1, "t1", "url1", type1), res.get(0)));
        TermsOfUseAgreeabilityType type2 = createTermsOfUseAgreeabilityType(1, "Non-agreeable",
                "Non-agreeable");
        assertTrue("fail to get dependent terms of use",
                compareTermsOfUse(createTermsOfUse(3l, 1, "t3", "url3", type2), res.get(1)));
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteDependencyRelationship(long, long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Should delete the dependency relationship successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testDeleteDependencyRelationship() throws Exception {
        dao.createDependencyRelationship(1, 2);
        dao.deleteDependencyRelationship(1, 2);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'deleteDependencyRelationship' should be correct.", 0, res.size());

        res = dao.getDependentTermsOfUse(2);

        assertEquals("'deleteDependencyRelationship' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependent(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Delete the dependency relationship for dependent with no dependency successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteAllDependencyRelationshipsForDependent_Nodependency() throws Exception {
        dao.deleteAllDependencyRelationshipsForDependent(1);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependent(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Delete the only one dependency for dependent successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependent_OneDependency() throws Exception {
        dao.createDependencyRelationship(1, 2);

        dao.deleteAllDependencyRelationshipsForDependent(1);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());

        res = dao.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependent(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Delete the two dependencies for dependent successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependent_TwoDependencies() throws Exception {
        dao.createDependencyRelationship(1, 2);
        dao.createDependencyRelationship(1, 3);

        dao.deleteAllDependencyRelationshipsForDependent(1);

        List<TermsOfUse> res = dao.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());

        res = dao.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());

        res = dao.getDependentTermsOfUse(3);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependency(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Delete the dependent for dependency successfully when with no dependent. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependency_Nodependent() throws Exception {
        dao.deleteAllDependencyRelationshipsForDependency(2);

        List<TermsOfUse> res = dao.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependency(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Delete the only one dependent for dependency successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependency_OneDependent() throws Exception {
        dao.createDependencyRelationship(1, 2);

        dao.deleteAllDependencyRelationshipsForDependency(2);

        List<TermsOfUse> res = dao.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());

        res = dao.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());

    }

    /**
     * <p>
     * Tests {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependency(long)} with valid arguments passed.
     * </p>
     *
     * <p>
     * Delete the two dependents for dependency successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependency_TwoDependents() throws Exception {
        dao.createDependencyRelationship(1, 2);
        dao.createDependencyRelationship(3, 2);

        dao.deleteAllDependencyRelationshipsForDependency(2);

        List<TermsOfUse> res = dao.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());

        res = dao.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());

        res = dao.getDependencyTermsOfUse(3);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());
    }
}
