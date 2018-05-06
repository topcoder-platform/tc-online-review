/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests;

import com.topcoder.file.transfer.accuracytests.message.BytesMessageTypeAccuracyTest;
import com.topcoder.file.transfer.accuracytests.message.FileSystemMessageAccuracyTest;
import com.topcoder.file.transfer.accuracytests.message.MessageTypeAccuracyTest;
import com.topcoder.file.transfer.accuracytests.message.RequestMessageAccuracyTest;
import com.topcoder.file.transfer.accuracytests.message.ResponseMessageAccuracyTest;
import com.topcoder.file.transfer.accuracytests.persistence.FileAlreadyLockedExceptionAccuracyTest;
import com.topcoder.file.transfer.accuracytests.persistence.FileNotYetLockedExceptionAccuracyTest;
import com.topcoder.file.transfer.accuracytests.persistence.FileSystemPersistenceAccuracyTest;
import com.topcoder.file.transfer.accuracytests.persistence.InputStreamBytesIteratorAccuracyTest;
import com.topcoder.file.transfer.accuracytests.persistence.SimpleLockingFileSystemPersistenceAccuracyTest;
import com.topcoder.file.transfer.accuracytests.registry.FileSystemXmlRegistryAccuracyTest;
import com.topcoder.file.transfer.accuracytests.search.FileIdGroupSearcherAccuracyTest;
import com.topcoder.file.transfer.accuracytests.search.RegexFileSearcherAccuracyTest;
import com.topcoder.file.transfer.accuracytests.search.RegexGroupSearcherAccuracyTest;
import com.topcoder.file.transfer.accuracytests.search.SearchManagerAccuracyTest;
import com.topcoder.file.transfer.accuracytests.validator.FreeDiskSpaceNativeCheckerAccuracyTest;
import com.topcoder.file.transfer.accuracytests.validator.UploadRequestValidatorAccuracyTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 * @author mayday
 * @version 1.1
 */
public class AccuracyTests extends TestCase {

    /**
     * <p>
     * Aggregates the accuracy tests to a new test suite.
     * </p>
     * @return test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(BytesMessageTypeAccuracyTest.class);
        suite.addTestSuite(FileSystemMessageAccuracyTest.class);
        suite.addTestSuite(MessageTypeAccuracyTest.class);
        suite.addTestSuite(RequestMessageAccuracyTest.class);
        suite.addTestSuite(ResponseMessageAccuracyTest.class);

        suite.addTestSuite(FileIdGroupSearcherAccuracyTest.class);
        suite.addTestSuite(RegexFileSearcherAccuracyTest.class);
        suite.addTestSuite(RegexGroupSearcherAccuracyTest.class);
        suite.addTestSuite(SearchManagerAccuracyTest.class);

        suite.addTestSuite(FileSystemPersistenceAccuracyTest.class);
        suite.addTestSuite(InputStreamBytesIteratorAccuracyTest.class);

        suite.addTestSuite(FileSystemXmlRegistryAccuracyTest.class);

        suite.addTestSuite(FileUploadCheckStatusAccuracyTest.class);
        suite.addTestSuite(FileSystemHandlerAccuracyTest.class);
        suite.addTestSuite(FileSystemClientAccuracyTest.class);

        suite.addTestSuite(UploadRequestValidatorAccuracyTest.class);
        suite.addTestSuite(FreeDiskSpaceNativeCheckerAccuracyTest.class);

        suite.addTestSuite(FileAlreadyLockedExceptionAccuracyTest.class);
        suite.addTestSuite(FileNotYetLockedExceptionAccuracyTest.class);
        suite.addTestSuite(SimpleLockingFileSystemPersistenceAccuracyTest.class);
        return suite;
    }
}
