/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUseCyclicDependencyException;
import com.cronos.termsofuse.dao.TermsOfUseDao;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.cronos.termsofuse.dao.impl.ProjectTermsOfUseDaoImpl;
import com.cronos.termsofuse.dao.impl.TermsOfUseDaoImpl;
import com.cronos.termsofuse.dao.impl.UserTermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Shows usage for the component.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse , and
 * support adding of TermsOfUse#agreeabilityType property.</li>
 * <li>Added code for managing terms of use dependencies.</li>
 * </ol>
 * </p>
 *
 * @author faeton, sparemax, saarixx
 * @version 1.1
 */
public class Demo {
    /**
     * <p>
     * Represents the connection used in tests.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(Demo.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        connection = TestsHelper.getConnection();
        TestsHelper.clearDB(connection);
        TestsHelper.loadDB(connection);
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        TestsHelper.clearDB(connection);
        TestsHelper.closeConnection(connection);
        connection = null;
    }

    /**
     * <p>
     * Demo API usage of this component.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse ,
     * and support adding of TermsOfUse#agreeabilityType property.</li>
     * <li>Added code for managing terms of use dependencies.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testDemo() throws Exception {
        // Create the configuration object
        ConfigurationObject configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_TERMS);
        // Instantiate the dao implementation from configuration defined above
        TermsOfUseDao termsOfUseDao = new TermsOfUseDaoImpl(configurationObject);

        // Create simple TermsOfUse to persist
        TermsOfUse terms = new TermsOfUse();

        terms.setTermsOfUseTypeId(3);
        terms.setTitle("t5");
        terms.setUrl("url5");
        TermsOfUseAgreeabilityType nonAgreeableType = new TermsOfUseAgreeabilityType();
        nonAgreeableType.setTermsOfUseAgreeabilityTypeId(1);
        terms.setAgreeabilityType(nonAgreeableType);

        // Persist the TermsOfUse
        terms = termsOfUseDao.createTermsOfUse(terms, "");

        // Set terms of use text
        termsOfUseDao.setTermsOfUseText(terms.getTermsOfUseId(), "text5");

        // Get terms of use text. This will return "text5".
        String termsOfUseText = termsOfUseDao.getTermsOfUseText(terms.getTermsOfUseId());

        // Update some information for TermsOfUse
        TermsOfUseAgreeabilityType electronicallyAgreeableType = new TermsOfUseAgreeabilityType();
        electronicallyAgreeableType.setTermsOfUseAgreeabilityTypeId(3);
        terms.setAgreeabilityType(electronicallyAgreeableType);

        // And update the TermsOfUse
        terms = termsOfUseDao.updateTermsOfUse(terms);

        // Retrieve some terms of use. The third row will be returned
        terms = termsOfUseDao.getTermsOfUse(3);
        // terms.getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 1
        // terms.getAgreeabilityType().getName() must be "Non-agreeable"

        // Delete terms of use
        termsOfUseDao.deleteTermsOfUse(5);

        // Retrieve all terms of use. All rows will be returned
        List<TermsOfUse> allTerms = termsOfUseDao.getAllTermsOfUse();

        // Create the following dependency relationships between terms of use with the specified IDs:
        // (1) depends on (2)
        // (2) depends on (3,4)
        // (3) depends on (4)
        termsOfUseDao.createDependencyRelationship(1, 2);
        termsOfUseDao.createDependencyRelationship(2, 3);
        termsOfUseDao.createDependencyRelationship(2, 4);
        termsOfUseDao.createDependencyRelationship(3, 4);

        try {
            // Try to make a loop; TermsOfUseCyclicDependencyException must be thrown
            termsOfUseDao.createDependencyRelationship(4, 1);
        } catch (TermsOfUseCyclicDependencyException e) {
            // Good
        }

        // Retrieve the dependencies of terms of use with ID=2
        List<TermsOfUse> termsOfUseList = termsOfUseDao.getDependencyTermsOfUse(2);
        // termsOfUseList.size() must be 2
        // termsOfUseList.get(0).getTermsOfUseId() must be 3
        // termsOfUseList.get(0).getTermsOfUseTypeId() must be 1
        // termsOfUseList.get(0).getTitle() must be "t3"
        // termsOfUseList.get(0).getUrl() must be "url3"
        // termsOfUseList.get(0).getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 1
        // termsOfUseList.get(0).getAgreeabilityType().getName() must be "Non-agreeable"
        // termsOfUseList.get(0).getAgreeabilityType().getDescription() must be "Non-agreeable"
        // termsOfUseList.get(1).getTermsOfUseId() must be 4
        // termsOfUseList.get(1).getTermsOfUseTypeId() must be 2
        // termsOfUseList.get(1).getTitle() must be "t4"
        // termsOfUseList.get(1).getUrl() must be "url4"
        // termsOfUseList.get(1).getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 3
        // termsOfUseList.get(1).getAgreeabilityType().getName() must be "Electronically-agreeable"
        // termsOfUseList.get(1).getAgreeabilityType().getDescription() must be "Electronically-agreeable"
        // Note: the order of elements in termsOfUseList can vary

        // Retrieve the dependents of terms of use with ID=2
        termsOfUseList = termsOfUseDao.getDependentTermsOfUse(2);
        // termsOfUseList.size() must be 1
        // termsOfUseList.get(0).getTermsOfUseId() must be 1
        // termsOfUseList.get(0).getTermsOfUseTypeId() must be 1
        // termsOfUseList.get(0).getTitle() must be "t1"
        // termsOfUseList.get(0).getUrl() must be "url1"
        // termsOfUseList.get(0).getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 2
        // termsOfUseList.get(0).getAgreeabilityType().getName() must be "Non-electronically-agreeable"
        // termsOfUseList.get(0).getAgreeabilityType().getDescription() must be "Non-electronically-agreeable"

        // Delete the dependency relationship between terms of use with IDs=2,4
        termsOfUseDao.deleteDependencyRelationship(2, 4);

        // Delete all dependency relationships where terms of use with ID=2 is a dependent
        termsOfUseDao.deleteAllDependencyRelationshipsForDependent(2);

        // Delete all dependency relationships where terms of use with ID=4 is a dependency
        termsOfUseDao.deleteAllDependencyRelationshipsForDependency(4);

        // Create the configuration object
        configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_USER_TERMS);
        // Instantiate the dao implementation from configuration defined above
        UserTermsOfUseDao userTermsOfUseDao = new UserTermsOfUseDaoImpl(configurationObject);

        // Create user terms of use to user link
        userTermsOfUseDao.createUserTermsOfUse(3, 2);

        // Remove user terms of use to user link
        userTermsOfUseDao.removeUserTermsOfUse(3, 3);

        // Retrieve terms of use. This will return user terms with ids 1 and 2.
        List<TermsOfUse> termsList = userTermsOfUseDao.getTermsOfUseByUserId(1);

        // Retrieve users by terms of use. This will return ids 1 and 3.
        List<Long> userIdsList = userTermsOfUseDao.getUsersByTermsOfUseId(2);

        // Check whether user has terms of use. Will return false
        boolean hasTerms = userTermsOfUseDao.hasTermsOfUse(3, 3);

        // Check whether user has terms of use ban. Will return true
        boolean hasTermsBan = userTermsOfUseDao.hasTermsOfUseBan(1, 3);

        // Create the configuration object
        configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_PROJECT_TERMS);
        // Instantiate the dao implementation from configuration defined above
        ProjectTermsOfUseDao projectTermsOfUseDao = new ProjectTermsOfUseDaoImpl(configurationObject);

        // Create user terms of use to project link
        projectTermsOfUseDao.createProjectRoleTermsOfUse(2, 2, 3, 2, 0);

        // Remove user terms of use to project link
        projectTermsOfUseDao.removeProjectRoleTermsOfUse(2, 2, 3, 0);

        // Get terms of use with non-member-agreeable terms
        // Will return two lists:
        // 1st with ids: 1,2,3
        // 2nd with ids: 1
        Map<Integer, List<TermsOfUse>> termsGroupMap = projectTermsOfUseDao.getTermsOfUse(1, 1, null);

        // Get terms of use without non-member-agreeable terms
        // Will return one list:
        // 1st with ids: 1
        termsGroupMap = projectTermsOfUseDao.getTermsOfUse(1, 1, new int[] {2, 3});
    }
}
