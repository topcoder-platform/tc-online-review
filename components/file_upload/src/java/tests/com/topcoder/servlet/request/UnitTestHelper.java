/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

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

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.Assert;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;

import java.util.Iterator;
import java.util.Properties;


/**
 * <p>
 * Defines helper methods used in tests.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public final class UnitTestHelper {
    /** The namespace to configure message factory. */
    public static final String MESSAGE_FACTORY_NAMESPACE = "com.topcoder.processor.ipserver.message";

    /** The idgenerator namespace used in test. */
    public static final String IDGENERATOR_NAMESPACE = "unit_test_id_sequence";

    /** Represents the server file location. */
    public static final String SERVER_FILE_LOCATION = "test_files/server/";

    /** Represents the cilent file location. */
    public static final String CLIENT_FILE_LOCATION = "test_files/client/";

    /** Represents the xml file for server files. */
    public static final File SERVER_FILES_FILE = new File(SERVER_FILE_LOCATION + "files.xml");

    /** Represents the xml file for server groups. */
    public static final File SERVER_GROUPS_FILE = new File(SERVER_FILE_LOCATION + "groups.xml");

    /** The singleton instance of this class. */
    private static UnitTestHelper instance = null;

    /** The address used for testing. */
    private String address = null;

    /** The port used for testing. */
    private int port = -1;

    /** Represents the maxRequests used for Handler test. */
    private int maxRequests = 100;

    /** Represents the maxConnections used for Handler test. */
    private int maxConnections = 100;

    /**
     * <p>
     * Creates a new instance of <code>UnitTestHelper</code> class. The private constructor prevents the creation of a
     * new instance.
     * </p>
     */
    private UnitTestHelper() {
        try {
            // load the config from file
            Properties prop = new Properties();
            prop.load(new FileInputStream("test_files/test.properties"));

            this.address = prop.getProperty("address");
            this.port = Integer.parseInt(prop.getProperty("port"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the singleton instance of this class.
     *
     * @return the singleton instance
     */
    static synchronized UnitTestHelper getInstance() {
        if (instance == null) {
            instance = new UnitTestHelper();
        }

        return instance;
    }

    /**
     * <p>
     * Get the port of IPServer that can be used by other unit tests.
     * </p>
     *
     * @return port of IPServer
     */
    int getPort() {
        return this.port;
    }

    /**
     * <p>
     * Get the address of IPServer that can be used by other unit tests.
     * </p>
     *
     * @return address of IPServer
     */
    String getAddress() {
        return this.address;
    }

    /**
     * <p>
     * Get the max connections of IPServer that can be used by other unit tests.
     * </p>
     *
     * @return max connections of IPServer.
     */
    int getMaxConnections() {
        return this.maxConnections;
    }

    /**
     * <p>
     * Get the max requests of Handler that can be used by other unit tests.
     * </p>
     *
     * @return max requests of Handler
     */
    int getMaxRequests() {
        return this.maxRequests;
    }

    /**
     * <p>
     * add the config of given config file.
     * </p>
     *
     * @param configFile the given config file.
     *
     * @throws Exception unexpected exception.
     */
    public static void addConfig(String configFile) throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(configFile);
    }

    /**
     * <p>
     * clear the config.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void clearConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance.
     * </p>
     *
     * @param type the class which the private field belongs to.
     * @param instance the instance which the private field belongs to.
     * @param name the name of the private field to be retrieved.
     *
     * @return the value of the private field or <code>null</code> if any error occurs.
     */
    public static Object getPrivateField(Class type, Object instance, String name) {
        Field field = null;
        Object obj = null;

        try {
            // Get the reflection of the field and get the value
            field = type.getDeclaredField(name);
            field.setAccessible(true);
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // Reset the accessibility
                field.setAccessible(false);
            }
        }

        return obj;
    }

    /**
     * <p>
     * Gets the value of a private field in the parent class of the given class. The field has the given name. The
     * value is retrieved from the given instance.
     * </p>
     *
     * @param type the class which the private field belongs to.
     * @param instance the instance which the private field belongs to.
     * @param name the name of the private field to be retrieved.
     *
     * @return the value of the private field or <code>null</code> if any error occurs.
     */
    public static Object getParentClassPrivateField(Class type, Object instance, String name) {
        return getPrivateField(type.getSuperclass(), instance, name);
    }

    /**
     * <p>
     * Gets the byte content from the given input stream.
     * </p>
     *
     * @param inputStream the given input stream to get the content.
     *
     * @return the byte content from the given input stream.
     *
     * @throws Exception any exception when try to get the byte content from the given input stream.
     */
    public static byte[] readContent(InputStream inputStream) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;

        while ((len = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }

        inputStream.close();
        output.close();

        return output.toByteArray();
    }

    /**
     * <p>
     * Asserts the two given byte arrays to be equals. The two byte arrays will be regarded to be equals only if both
     * the length and the content are equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two byte arrays are not equals.
     * @param expected the expected byte array.
     * @param actual the actual byte array.
     */
    public static void assertEquals(String errorMessage, byte[] expected, byte[] actual) {
        Assert.assertEquals(errorMessage, expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(errorMessage, expected[i], actual[i]);
        }
    }

    /**
     * <p>
     * Asserts the two given object arrays to be equals. The two object arrays will be regarded to be equals only if
     * both the length and the content are equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two object arrays are not equals.
     * @param expected the expected object array.
     * @param actual the actual object array.
     */
    public static void assertEquals(String errorMessage, Object[] expected, Object[] actual) {
        Assert.assertEquals(errorMessage, expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(errorMessage, expected[i], actual[i]);
        }
    }

    /**
     * <p>
     * Asserts the two given UploadedFile instances to be equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two object arrays are not equals.
     * @param expected the expected File instance.
     * @param actual the actual UploadedFile instance.
     *
     * @throws Exception any exception when try to get the byte content from the given input stream.
     */
    public static void assertEquals(String errorMessage, File expected, UploadedFile actual)
        throws Exception {
        Assert.assertEquals(errorMessage, expected.getName(), actual.getRemoteFileName());
        assertEquals(errorMessage, new FileInputStream(expected), actual.getInputStream());
    }

    /**
     * <p>
     * Asserts the two given InputStream instances to be equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two object arrays are not equals.
     * @param expected the expected InputStream instance.
     * @param actual the actual InputStream instance.
     *
     * @throws Exception any exception when try to get the byte content from the given input stream.
     */
    public static void assertEquals(String errorMessage, InputStream expected, InputStream actual)
        throws Exception {
        assertEquals(errorMessage, readContent(expected), readContent(actual));
    }

    /**
     * This method start the given server.
     *
     * @param server the server will be start
     *
     * @throws IOException to JUnit
     */
    public static void startServer(IPServer server) throws IOException {
        if (!server.isStarted()) {
            server.start();
        }
    }

    /**
     * This method ensure that the given server is stopped.
     *
     * @param server the server will be stopped.
     */
    public static void stopServer(IPServer server) {
        if (server.isStarted()) {
            server.stop();
        }

        // Loop until server is really stopped
        while (server.isStarted()) {
            synchronized (server) {
                try {
                    server.wait(100);
                } catch (InterruptedException e) {
                    // Clear the interrupted status
                    Thread.interrupted();
                }
            }
        }
    }

    /**
     * <p>
     * Create an server to listen.
     * </p>
     *
     * @return the ipserver.
     *
     * @throws Exception Exception to JUnit
     */
    public static IPServer createServer() throws Exception {
        UnitTestHelper configHelper = UnitTestHelper.getInstance();

        // create the IPServer
        IPServer ipServer = new IPServer(configHelper.getAddress(), configHelper.getPort(),
                configHelper.getMaxConnections(), 0, UnitTestHelper.MESSAGE_FACTORY_NAMESPACE);

        // create a file registry
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator(UnitTestHelper.IDGENERATOR_NAMESPACE);
        FileSystemRegistry registry = new FileSystemXmlRegistry(UnitTestHelper.SERVER_FILES_FILE,
                UnitTestHelper.SERVER_GROUPS_FILE, idGenerator);

        // create the file persistence
        FileSystemPersistence serverPersistence = new FileSystemPersistence();

        // create the upload request validator
        ObjectValidator validator = new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(
                    UnitTestHelper.SERVER_FILE_LOCATION));

        // creat the search manager
        SearchManager searchManager = new SearchManager();
        searchManager.addFileSearcher("regex", new RegexFileSearcher(registry));
        searchManager.addGroupSearcher("regex", new RegexGroupSearcher(registry));
        searchManager.addGroupSearcher("fileId", new FileIdGroupSearcher(registry));

        // create the handler
        FileSystemHandler fileSystemHandler = new FileSystemHandler(configHelper.getMaxRequests(), registry,
                serverPersistence, UnitTestHelper.SERVER_FILE_LOCATION, validator, searchManager);
        ipServer.addHandler("HanderId", fileSystemHandler);

        return ipServer;
    }

    /**
     * <p>
     * Clear all the files uploaded to the server.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public static void clearServerFiles() throws Exception {
        File[] files = new File(UnitTestHelper.SERVER_FILE_LOCATION).listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(
                    new File(UnitTestHelper.SERVER_FILE_LOCATION, "files.xml")));
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.write("<files></files>");
        writer.flush();
        writer.close();
        writer = new BufferedWriter(new FileWriter(new File(UnitTestHelper.SERVER_FILE_LOCATION, "groups.xml")));
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.write("<groups></groups>");
        writer.flush();
        writer.close();
    }
}
