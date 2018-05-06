/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import com.topcoder.file.transfer.message.BytesMessageTypeTestCase;
import com.topcoder.file.transfer.message.FileSystemMessageTestCase;
import com.topcoder.file.transfer.message.MessageTypeTestCase;
import com.topcoder.file.transfer.message.MessageTypeValidatorTestCase;
import com.topcoder.file.transfer.message.RequestMessageTestCase;
import com.topcoder.file.transfer.message.ResponseMessageTestCase;
import com.topcoder.file.transfer.persistence.FileAlreadyLockedExceptionTest;
import com.topcoder.file.transfer.persistence.FileNotYetLockedExceptionTest;
import com.topcoder.file.transfer.persistence.FilePersistenceExceptionTestCase;
import com.topcoder.file.transfer.persistence.FileSystemPersistenceTestCase;
import com.topcoder.file.transfer.persistence.InputStreamBytesIteratorTestCase;
import com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistenceTest;
import com.topcoder.file.transfer.registry.FileIdExceptionTestCase;
import com.topcoder.file.transfer.registry.FileIdExistsExceptionTestCase;
import com.topcoder.file.transfer.registry.FileIdNotFoundExceptionTestCase;
import com.topcoder.file.transfer.registry.GroupExceptionTestCase;
import com.topcoder.file.transfer.registry.GroupExistsExceptionTestCase;
import com.topcoder.file.transfer.registry.GroupNotFoundExceptionTestCase;
import com.topcoder.file.transfer.registry.RegistryConfigurationExceptionTestCase;
import com.topcoder.file.transfer.registry.RegistryExceptionTestCase;
import com.topcoder.file.transfer.registry.RegistryPersistenceExceptionTestCase;
import com.topcoder.file.transfer.search.FileSearcherNotFoundExceptionTestCase;
import com.topcoder.file.transfer.search.GroupSearcherNotFoundExceptionTestCase;
import com.topcoder.file.transfer.search.RegexFileSearcherTestCase;
import com.topcoder.file.transfer.search.SearchExceptionTestCase;
import com.topcoder.file.transfer.search.SearchManagerTestCase;
import com.topcoder.file.transfer.search.SearcherExceptionTestCase;
import com.topcoder.file.transfer.validator.FreeDiskSpaceCheckerExceptionTestCase;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNonNativeCheckerTestCase;
import com.topcoder.file.transfer.validator.UploadRequestValidatorTestCase;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * Aggregates all tests in this class.
     * </p>
     *
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // transfer package
        suite.addTest(ClientServerTestCase.suite());
        suite.addTest(FileUploadCheckStatusTestCase.suite());
        suite.addTest(FileSystemHandlerTestCase.suite());
        suite.addTest(FileSystemClientTestCase.suite());

        // registry package
        suite.addTest(registrySuite());
        // search package
        suite.addTest(searchSuite());
        // validator package
        suite.addTest(FreeDiskSpaceCheckerExceptionTestCase.suite());
        suite.addTest(UploadRequestValidatorTestCase.suite());
        suite.addTest(FreeDiskSpaceNonNativeCheckerTestCase.suite());
        // message package
        suite.addTest(messageSuite());
        // persistence package
        suite.addTest(FilePersistenceExceptionTestCase.suite());
        suite.addTest(InputStreamBytesIteratorTestCase.suite());
        suite.addTest(FileSystemPersistenceTestCase.suite());
        
        // demo
        suite.addTest(DemoTestCase.suite());

        // add new unit test for version 1.1
        suite.addTest(newSuite());

        return suite;
    }

    /**
     * <p>
     * Aggregates all tests for in classes in registry package.
     * </p>
     *
     * @return test suite aggregating all tests.
     */
    private static Test registrySuite() {
        TestSuite suite = new TestSuite();
        suite.addTest(RegistryExceptionTestCase.suite());
        suite.addTest(RegistryConfigurationExceptionTestCase.suite());
        suite.addTest(FileIdExceptionTestCase.suite());
        suite.addTest(GroupExceptionTestCase.suite());
        suite.addTest(RegistryPersistenceExceptionTestCase.suite());
        suite.addTest(FileIdExistsExceptionTestCase.suite());
        suite.addTest(FileIdNotFoundExceptionTestCase.suite());
        suite.addTest(GroupExistsExceptionTestCase.suite());
        suite.addTest(GroupNotFoundExceptionTestCase.suite());
        // suite.addTest(FileSystemXmlRegistryTestCase.suite());
        return suite;
    }

    /**
     * <p>
     * Aggregates all tests for in classes in search package.
     * </p>
     *
     * @return test suite aggregating all tests.
     */
    private static Test searchSuite() {
        TestSuite suite = new TestSuite();
        suite.addTest(FileSearcherNotFoundExceptionTestCase.suite());
        suite.addTest(GroupSearcherNotFoundExceptionTestCase.suite());
        suite.addTest(SearcherExceptionTestCase.suite());
        suite.addTest(SearchExceptionTestCase.suite());
        suite.addTest(RegexFileSearcherTestCase.suite());
        // suite.addTest(FileIdGroupSearcherTestCase.suite());
        // suite.addTest(RegexGroupSearcherTestCase.suite());
        suite.addTest(SearchManagerTestCase.suite());
        return suite;
    }

    /**
     * <p>
     * Aggregates all tests for in classes in message package.
     * </p>
     *
     * @return test suite aggregating all tests.
     */
    private static Test messageSuite() {
        TestSuite suite = new TestSuite();
        suite.addTest(MessageTypeTestCase.suite());
        suite.addTest(BytesMessageTypeTestCase.suite());
        suite.addTest(FileSystemMessageTestCase.suite());
        suite.addTest(RequestMessageTestCase.suite());
        suite.addTest(ResponseMessageTestCase.suite());
        suite.addTest(MessageTypeValidatorTestCase.suite());
        return suite;
    }

    /**
     * <p>
     * Aggregates all tests for version 1.1.
     * </p>
     *
     * @return test suite for version 1.1.
     */
    private static Test newSuite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(FileAlreadyLockedExceptionTest.class);
        suite.addTestSuite(FileNotYetLockedExceptionTest.class);
        suite.addTestSuite(SimpleLockingFileSystemPersistenceTest.class);

        suite.addTestSuite(FileSystemClient2ExpTest.class);
        suite.addTestSuite(FileSystemClient2AccTest.class);

        return suite;
    }

}
