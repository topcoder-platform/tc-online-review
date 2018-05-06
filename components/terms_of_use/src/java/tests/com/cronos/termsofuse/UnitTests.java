/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.cronos.termsofuse.dao.EntityNotFoundExceptionUnitTests;
import com.cronos.termsofuse.dao.TermsOfUseCyclicDependencyExceptionUnitTests;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationExceptionUnitTests;
import com.cronos.termsofuse.dao.TermsOfUseDaoExceptionUnitTests;
import com.cronos.termsofuse.dao.TermsOfUseDependencyNotFoundExceptionUnitTests;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceExceptionUnitTests;
import com.cronos.termsofuse.dao.UserBannedExceptionUnitTests;
import com.cronos.termsofuse.dao.impl.BaseTermsOfUseDaoUnitTests;
import com.cronos.termsofuse.dao.impl.HelperUnitTests;
import com.cronos.termsofuse.dao.impl.ProjectTermsOfUseDaoImplUnitTests;
import com.cronos.termsofuse.dao.impl.TermsOfUseDaoImplUnitTests;
import com.cronos.termsofuse.dao.impl.UserTermsOfUseDaoImplUnitTests;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityTypeUnitTests;
import com.cronos.termsofuse.model.TermsOfUseTypeUnitTests;
import com.cronos.termsofuse.model.TermsOfUseUnitTests;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author sparemax
 * @version 1.1
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * All unit test cases.
     * </p>
     *
     * @return The test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(Demo.suite());

        suite.addTest(HelperUnitTests.suite());
        suite.addTest(BaseTermsOfUseDaoUnitTests.suite());
        suite.addTest(ProjectTermsOfUseDaoImplUnitTests.suite());
        suite.addTest(TermsOfUseDaoImplUnitTests.suite());
        suite.addTest(UserTermsOfUseDaoImplUnitTests.suite());

        suite.addTest(TermsOfUseAgreeabilityTypeUnitTests.suite());
        suite.addTest(TermsOfUseTypeUnitTests.suite());
        suite.addTest(TermsOfUseUnitTests.suite());

        // Exceptions
        suite.addTest(TermsOfUseCyclicDependencyExceptionUnitTests.suite());
        suite.addTest(TermsOfUseDependencyNotFoundExceptionUnitTests.suite());
        suite.addTest(TermsOfUseDaoConfigurationExceptionUnitTests.suite());
        suite.addTest(TermsOfUseDaoExceptionUnitTests.suite());
        suite.addTest(EntityNotFoundExceptionUnitTests.suite());
        suite.addTest(TermsOfUsePersistenceExceptionUnitTests.suite());
        suite.addTest(UserBannedExceptionUnitTests.suite());

        return suite;
    }

}
