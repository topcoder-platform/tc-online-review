/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.accuracytests;

import java.util.List;

import com.cronos.termsofuse.dao.impl.UserTermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;

/**
 * <p>
 * This class contains Accuracy tests for UserTermsOfUseDaoImpl.
 * </p>
 *
 * <p>
 * Change in 1.1:
 * <ul>
 * <li>Update test cases to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
 * <li>Update test cases to support adding of TermsOfUse#agreeabilityType property.
 * </ul>
 * </p>
 *
 * @author sokol, fish_ship
 * @version 1.1
 */
public class UserTermsOfUseDaoImplAccuracyTest extends BaseAccuracyTest {
    /**
     * <p>
     * Represents UserTermsOfUseDaoImpl for testing.
     * </p>
     */
    private UserTermsOfUseDaoImpl dao;

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
        dao = new UserTermsOfUseDaoImpl(getConfiguration(USER_TERMS_OF_USE_CONFIGURATION));
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
     * Tests UserTermsOfUseDaoImpl constructor.
     * </p>
     * <p>
     * UserTermsOfUseDaoImpl instance should be created successfully. No exception is expected.
     * </p>
     */
    public void testConstructor() {
        assertNotNull("UserTermsOfUseDaoImpl instance should be created successfully", dao);
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#createUserTermsOfUse(long, long)} with valid arguments passed.
     * </p>
     * <p>
     * User's terms of use should be created successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testCreateUserTermsOfUse() throws Exception {
        long userId = 3;
        long termsOfUseId = 1;
        dao.createUserTermsOfUse(userId, termsOfUseId);
        assertEquals("User's terms of use should be created successfully", 2, dao.getTermsOfUseByUserId(userId).size());
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#removeUserTermsOfUse(long, long)} with valid arguments passed.
     * </p>
     * <p>
     * User's terms of use should be removed successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testRemoveUserTermsOfUse() throws Exception {
        long userId = 3;
        long termsOfUseId = 3;
        dao.removeUserTermsOfUse(userId, termsOfUseId);
        assertEquals("User's terms of use should be removed successfully", 0, dao.getTermsOfUseByUserId(userId).size());
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#getTermsOfUseByUserId(long)} with valid arguments passed.
     * </p>
     * <p>
     * User's terms of use should be retrieved successfully. No exception is expected.
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
    public void testGetTermsOfUseByUserId() throws Exception {
        long userId = 2;
        List<TermsOfUse> termsOfUses = dao.getTermsOfUseByUserId(userId);
        assertEquals("User's terms of use should be retrieved successfully", 1, termsOfUses.size());
        TermsOfUseAgreeabilityType type = createTermsOfUseAgreeabilityType(2, "Electronically-agreeable",
                "Electronically-agreeable");
        assertTrue("User's terms of use should be retrieved successfully",
                compareTermsOfUse(createTermsOfUse(1, 1, "t1", "url1", type), termsOfUses.get(0)));
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#getTermsOfUseByUserId(long)} with invalid user id passed.
     * </p>
     * <p>
     * Empty user's terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUseByUserId_Empty() throws Exception {
        long userId = 0;
        List<TermsOfUse> termsOfUses = dao.getTermsOfUseByUserId(userId);
        assertEquals("Empty user's terms of use should be retrieved successfully", 0, termsOfUses.size());
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#getUsersByTermsOfUseId(long)} with valid arguments passed.
     * </p>
     * <p>
     * User's ids should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetUsersByTermsOfUseId() throws Exception {
        long termsId = 3;
        List<Long> usersId = dao.getUsersByTermsOfUseId(termsId);
        assertEquals("User's terms of use should be retrieved successfully", 1, usersId.size());
        assertEquals("User's terms of use should be retrieved successfully", new Long(3L), usersId.get(0));
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#getUsersByTermsOfUseId(long)} with termsId for non existing users passed.
     * </p>
     * <p>
     * Empty user's ids should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetUsersByTermsOfUseId_Empty() throws Exception {
        long termsId = 4;
        List<Long> usersId = dao.getUsersByTermsOfUseId(termsId);
        assertEquals("User's terms of use should be retrieved successfully", 0, usersId.size());
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#hasTermsOfUse(long, long)} with valid arguments passed.
     * </p>
     * <p>
     * User should has term of use. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testHasTermsOfUse() throws Exception {
        long userId = 3;
        long termsOfUseId = 3;
        assertTrue("User should has term of use.", dao.hasTermsOfUse(userId, termsOfUseId));
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#hasTermsOfUse(long, long)} with valid arguments passed.
     * </p>
     * <p>
     * User should not has term of use. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testHasTermsOfUse_Not() throws Exception {
        long userId = 3;
        long termsOfUseId = 1;
        assertFalse("User should not has term of use.", dao.hasTermsOfUse(userId, termsOfUseId));
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#hasTermsOfUseBan(long, long)} with valid arguments passed.
     * </p>
     * <p>
     * User should has ban for term of use. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testHasTermsOfUseBan() throws Exception {
        long userId = 3;
        long termsOfUseId = 4;
        assertTrue("User should not has term of use.", dao.hasTermsOfUseBan(userId, termsOfUseId));
    }

    /**
     * <p>
     * Tests {@link UserTermsOfUseDaoImpl#hasTermsOfUseBan(long, long)} with valid arguments passed.
     * </p>
     * <p>
     * User should not has ban for term of use. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testHasTermsOfUseBan_NoBan() throws Exception {
        long userId = 3;
        long termsOfUseId = 1;
        assertFalse("User should not has term of use.", dao.hasTermsOfUseBan(userId, termsOfUseId));
    }
}
