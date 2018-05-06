/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.file.transfer.FileSystemHandler;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.FileIdGroupSearcher;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNonNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.HttpRequestParser;
import com.topcoder.servlet.request.PersistenceException;
import com.topcoder.servlet.request.RemoteFileUpload;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * The class <code>RemoteFileUploadFailureTests</code> contains tests for the class
 * {@link <code>RemoteFileUpload</code>}.
 * @author FireIce
 * @version 2.0
 */
public class RemoteFileUploadFailureTests extends TestCase {
    /**
     * Represents the <code>RemoteFileUpload</code> instance.
     */
    private RemoteFileUpload remoteFileUpload;

    /** the IPServer instance used in tests. */
    private IPServer ipServer;

    /** The handler Id used for test. */
    private final String handlerId = "HanderId";

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadIllegalArgumentExceptionNullNamespace() throws Exception {
        try {
            new RemoteFileUpload(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadIllegalArgumentExceptionEmptyNamespace() throws Exception {
        try {
            new RemoteFileUpload("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new RemoteFileUpload(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionSingleFileLimitPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionSingleFileLimitPropertyNotNumber() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionSingleFileLimitPropertyInvalidLongFormat() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionSingleFileLimitPropertyTooBig() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionSingleFileLimitPropertyTooSmall() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionTotalFileLimitPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionTotalFileLimitPropertyNotNumber() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionTotalFileLimitPropertyInvalidLongFormat() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionTotalFileLimitPropertyTooBig() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionTotalFileLimitPropertyTooSmall() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionTempDirPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.TempDirPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionTempDirPropertyInvalid() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.TempDirPropertyInvalid");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionIPADDRESSPropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.IPADDRESSPropertyMissing");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionIPADDRESSPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.IPADDRESSPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionIPADDRESSPropertyInvalid() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.IPADDRESSPropertyInvalid");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionPortPropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.PortPropertyMissing");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionPortPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.PortPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionPortPropertyInvalid() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.PortPropertyInvalid");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionMessageNamespacePropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.MessageNamespacePropertyMissing");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionMessageNamespacePropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.MessageNamespacePropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionMessageNamespacePropertyInvalid() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.MessageNamespacePropertyInvalid");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionHandleIDPropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.HandleIDPropertyMissing");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadConfigurationExceptionHandleIDPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.HandleIDPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }
    
    /**
     * Run the public RemoteFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoteFileUploadPersistenceException() throws Exception {
        ConfigHelper.stopServer(ipServer);
        try {
            new RemoteFileUpload("com.topcoder.servlet.request.failuretests.valid");
            fail("expect throw PersistenceException.");
        } catch (PersistenceException e) {
            // good
        }
    }
    
    /**
     * Run the FileUploadResult uploadFiles(ServletRequest, RequestParser) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFilesIllegalArgumentExcdeptionNullServletRequest() throws Exception {
        try {
            remoteFileUpload.uploadFiles(null, new HttpRequestParser());
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult uploadFiles(ServletRequest, RequestParser) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFilesIllegalArgumentExcdeptionNullRequestParser() throws Exception {
        try {
            remoteFileUpload.uploadFiles(new MockServletRequest(), null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile getUploadedFile(String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testGetUploadedFileIllegalArgumentExceptionNullFileId() throws Exception {
        try {
            remoteFileUpload.getUploadedFile(null, true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile getUploadedFile(String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testGetUploadedFileIllegalArgumentExceptionEmptyFileId() throws Exception {
        try {
            remoteFileUpload.getUploadedFile("", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            remoteFileUpload.getUploadedFile(" ", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile getUploadedFile(String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testGetUploadedFileIllegalArgumentExceptionFileNotFound() throws Exception {
        try {
            remoteFileUpload.getUploadedFile("NotFound", true);
            fail("expect throw FileDoesNotExistException.");
        } catch (FileDoesNotExistException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile removeUploadedFile(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoveUploadedFileIllegalArgumentExceptionNullFileId() throws Exception {
        try {
            remoteFileUpload.removeUploadedFile(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile removeUploadedFile(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoveUploadedFileIllegalArgumentExceptionEmptyFileId() throws Exception {
        try {
            remoteFileUpload.removeUploadedFile("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            remoteFileUpload.removeUploadedFile(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile removeUploadedFile(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoveUploadedFileIllegalArgumentExceptionFileNotFound() throws Exception {
        try {
            remoteFileUpload.removeUploadedFile("NotFound");
            fail("expect throw FileDoesNotExistException.");
        } catch (FileDoesNotExistException e) {
            // good
        }
    }
    /**
     * init the IPServer with FileSystemHandler added.
     * @throws Exception
     *             if any exception occur.
     */
    public void initServer() throws Exception {
        ConfigHelper configHelper = ConfigHelper.getInstance();
        // create the IPServer
        ipServer = new IPServer(configHelper.getAddress(), configHelper.getPort(), configHelper.getMaxConnections(), 0,
            ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // create a file registry
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator(ConfigHelper.IDGENERATOR_NAMESPACE);
        FileSystemRegistry registry = new FileSystemXmlRegistry(ConfigHelper.SERVER_FILES_FILE,
            ConfigHelper.SERVER_GROUPS_FILE, idGenerator);
        // create the file persistence
        FileSystemPersistence serverPersistence = new FileSystemPersistence();
        // create the upload request validator
        ObjectValidator validator = new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(
            ConfigHelper.SERVER_FILE_LOCATION));
        // creat the search manager
        SearchManager searchManager = new SearchManager();
        searchManager.addFileSearcher("regex", new RegexFileSearcher(registry));
        searchManager.addGroupSearcher("regex", new RegexGroupSearcher(registry));
        searchManager.addGroupSearcher("fileId", new FileIdGroupSearcher(registry));
        // create the handler
        FileSystemHandler fileSystemHandler = new FileSystemHandler(configHelper.getMaxRequests(), registry,
            serverPersistence, ConfigHelper.SERVER_FILE_LOCATION, validator, searchManager);
        ipServer.addHandler(handlerId, fileSystemHandler);
    }

    /**
     * Perform pre-test initialization.
     * @throws Exception
     *             if the initialization fails for some reason
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigManager.getInstance().add("test_files/failuretests/RemoteFileUpload.xml");
        ConfigManager.getInstance().add("test_files/failuretests/MessageFactory.xml");
        ConfigManager.getInstance().add("test_files/failuretests/IPServerManager.xml");
        ConfigManager.getInstance().add("test_files/failuretests/DBConnectionFactoryImpl.xml");
        
        initServer();
        ConfigHelper.startServer(ipServer);
        
        remoteFileUpload = new RemoteFileUpload("com.topcoder.servlet.request.failuretests.valid");
    }

    /**
     * Perform post-test clean-up.
     * @throws Exception
     *             if the clean-up fails for some reason
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        ConfigHelper.stopServer(ipServer);
        ConfigManager configManager = ConfigManager.getInstance();
        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * Aggregates all tests in this class.
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(RemoteFileUploadFailureTests.class);
    }
}
