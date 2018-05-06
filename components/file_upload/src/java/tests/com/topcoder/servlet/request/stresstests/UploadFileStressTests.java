/**
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.stresstests;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import javax.servlet.ServletRequest;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.UploadFileSpec;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
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
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.LocalFileUpload;
import com.topcoder.servlet.request.MemoryFileUpload;
import com.topcoder.servlet.request.RemoteFileUpload;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * The test of UploadFileStressTests.
 *
 * @author brain_cn
 * @version 1.0
 */
public class UploadFileStressTests extends TestCase {
    /** Represents the configuration file. */
    private static final String CONFIG_FILE = "stresstests/stress_tests_config.xml";

    /** Represents the local file upload namespace used for testing. */
    private static final String FILE_UPLOAD_NAMESPACE = "local_file_upload";

    /** Represents the local file remote namespace used for testing. */
    private static final String REMOTE_FILE_UPLOAD_NAMESPACE = "remote_file_upload";

    /** Represents the all namespaces. */
    private static final String[] NAMESPACES = new String[] {FILE_UPLOAD_NAMESPACE,
        REMOTE_FILE_UPLOAD_NAMESPACE,
        "com.topcoder.processor.ipserver.message",
        "com.topcoder.processor.ipserver",
        "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl"
        };

    /** Represents the debug flag. */
    private static final boolean debug = true;

    /** Current time. */
    private static long current = -1;

    /** The test count. */
    private static final int NUMBER = 50;

    /** Represents the default dir for testing. */
    private static final String TEMP_DIR = "test_files/stresstests/temp";

    /** Represents the <code>FileUpload</code> instance used for testing. */
    private static FileUpload fileUpload = null;

    /** Represents the file system server dir. */
    private static final String SERVER_DIR = "test_files/stresstests/server";

    /** Represents the ip server instance. */
    private static IPServer ipServer = null;

    /** Represents the test file name. */
    private static final String REMOTE_FILE = "test_files.txt";

    /** Represents the test file size. */
    private static final long FILE_SIZE = 3814;

    /**
     * Load namespace for testing.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        loadNamespaces();
    }

    /**
     * Clears the configuration namespaces.
     *
     * @throws Exception if any unexpected exception occurs.
     */
    public void tearDown() throws Exception {
        releaseNamespaces();

        stopIPServer(ipServer);

        // delete all temp files
        clearFiles(TEMP_DIR);
        clearRemoteUploadFiles();
    }

    /**
     * Test of Method of MemoryFileUpload.
     *
     * @throws Exception to JUnit
     */
    public void testMemoryFileUpload() throws Exception {
        start();
        fileUpload = new MemoryFileUpload(FILE_UPLOAD_NAMESPACE);

        // Start all upload threads
        UploadThread[] threads = new UploadThread[NUMBER];
        for (int j = 0; j < NUMBER; j++) {
            threads[j] = new UploadThread(fileUpload);
            threads[j].start();
        }

        // Wait until all are stopped
        for (int j = 0; j < NUMBER; j++) {
            threads[j].join();
        }

        // Check if the result is correct
        for (int j = 0; j < NUMBER; j++) {
            if (threads[j].getErrorMessage() != null) {
                log("error: ", threads[j].getErrorMessage());
            }
            assertFalse("Incorrect output", threads[j].hasError);
        }

        printResult("testMemoryFileUpload");
    }

    /**
     * Test of Method of LocalFileUpload.
     *
     * @throws Exception to JUnit
     */
    public void testLocalFileUpload() throws Exception {
        start();
        fileUpload = new LocalFileUpload(FILE_UPLOAD_NAMESPACE);

        // Start all upload threads
        UploadThread[] threads = new UploadThread[NUMBER];
        for (int j = 0; j < NUMBER; j++) {
            threads[j] = new UploadThread(fileUpload);
            threads[j].start();
        }

        // Wait until all are stopped
        for (int j = 0; j < NUMBER; j++) {
            threads[j].join();
        }

        // Check if the result is correct
        for (int j = 0; j < NUMBER; j++) {
            if (threads[j].getErrorMessage() != null) {
                log("error: ", threads[j].getErrorMessage());
            }
            assertFalse("Incorrect output", threads[j].hasError);
        }

        printResult("testLocalFileUpload");
    }

    /**
     * Test of Method of RemoteFileUpload.
     *
     * @throws Exception to JUnit
     */
    public void testRemoteFileUpload() throws Exception {
        start();
        IPServer ipServer = prepareIPServer();
        if (!ipServer.isStarted()) {
            ipServer.start();
        }
        fileUpload = new RemoteFileUpload(REMOTE_FILE_UPLOAD_NAMESPACE);

        // Start all upload threads
        UploadThread[] threads = new UploadThread[NUMBER];
        for (int j = 0; j < NUMBER; j++) {
            threads[j] = new UploadThread(fileUpload);
            threads[j].run();
        }

        // Wait until all are stopped
        for (int j = 0; j < NUMBER; j++) {
            //threads[j].join();
        }

        // Check if the result is correct
        for (int j = 0; j < NUMBER; j++) {
            if (threads[j].getErrorMessage() != null) {
                log("error: ", threads[j].getErrorMessage());
            }
            assertFalse("Incorrect output", threads[j].hasError);
        }

        printResult("testRemoteFileUpload");
    }

    /**
     * Output the content.
     *
     * @param name the output name
     * @param output the output content
     */
    private static void log(String name, Object output) {
        if (debug) {
            System.out.println(name + ": " + output);
        }
    }

    /**
     * Start to count time.
     */
    private static void start() {
        current = System.currentTimeMillis();
    }

    /**
     * Print test result.
     *
     * @param name the test name
     */
    private static void printResult(String name) {
        System.out.println("The test " + name + " running " + NUMBER + " times, took time: " +
            (System.currentTimeMillis() - current) + " ms");
    }

    /**
     * Loads the namespaces under the default configuration file.
     *
     * @throws Exception to JUnit
     */
    private static void loadNamespaces() throws Exception {
        releaseNamespaces();

        ConfigManager config = ConfigManager.getInstance();

        config.add(CONFIG_FILE);
    }

    /**
     * Clears all the namespaces.
     *
     * @throws Exception to JUnit
     */
    private static void releaseNamespaces() throws Exception {
        ConfigManager config = ConfigManager.getInstance();

        for (int i = 0; i < NAMESPACES.length; i++) {
            if (config.existsNamespace(NAMESPACES[i])) {
                config.removeNamespace(NAMESPACES[i]);
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
    private static IPServer prepareIPServer() throws Exception {
        // create the IPServer
        ipServer = new IPServer("127.0.0.1", 8888,
                100, 0, "com.topcoder.processor.ipserver.message");

        // create a file registry
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator("unit_test_id_sequence");
        FileSystemRegistry registry = new FileSystemXmlRegistry(new File(SERVER_DIR, "files.xml"),
                new File(SERVER_DIR, "groups.xml"), idGenerator);

        // create the file persistence
        FileSystemPersistence serverPersistence = new FileSystemPersistence();

        // create the upload request validator
        ObjectValidator validator = new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(
                SERVER_DIR));

        // creat the search manager
        SearchManager searchManager = new SearchManager();
        searchManager.addFileSearcher("regex", new RegexFileSearcher(registry));
        searchManager.addGroupSearcher("regex", new RegexGroupSearcher(registry));
        searchManager.addGroupSearcher("fileId", new FileIdGroupSearcher(registry));

        // create the handler
        FileSystemHandler fileSystemHandler = new FileSystemHandler(100, registry,
                serverPersistence, SERVER_DIR, validator, searchManager);
        ipServer.addHandler("HanderId", fileSystemHandler);

        return ipServer;
    }

    /**
     * <p>
     * Clear all the files uploaded to the file system server.
     * </p>
     *
     * @throws Exception to JUnit
     */
    private static void clearRemoteUploadFiles() throws Exception {
        clearFiles(SERVER_DIR);

        // Initial files and groups contents
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                    new File(SERVER_DIR, "files.xml")));
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.write("<files></files>");
        writer.flush();
        writer.close();
        writer = new BufferedWriter(new FileWriter(new File(SERVER_DIR, "groups.xml")));
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.write("<groups></groups>");
        writer.flush();
        writer.close();
    }

    /**
     * <p>
     * Clear all the files from given dir.
     * </p>
     *
     * @throws Exception to JUnit
     */
    private static void clearFiles(String dir) throws Exception {
        File[] files = new File(dir).listFiles();

        for (int i = 0; files != null && i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * This method ensure that the given server is stopped.
     *
     * @param server the ipserver
     */
    private static void stopIPServer(IPServer server) {
        if (server == null || !server.isStarted()) {
            return;
        }

        // Server is running
        server.stop();

        while (server.isStarted()) {
            synchronized (server) {
                try {
                    server.wait(100);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
        }
    }

    /**
     * <p>
     * Prepares the <code>ServletRequest</code> instance for testing.
     * </p>
     *
     * @param files the files
     * @param parameters the parameters
     *
     * @return the <code>ServletRequest</code> instance.
     *
     * @throws Exception any exception to JUnit.
     */
    private synchronized static ServletRequest createRequest() throws Exception {
        PostMethodWebRequest webRequest = new PostMethodWebRequest("http://localhost:8080/");
        webRequest.setMimeEncoded(true);

        File file = new File("test_files/stresstests", "test_files.txt");
        webRequest.setParameter("file1", new UploadFileSpec[] {new UploadFileSpec(file)});
        InvocationContext ic = new ServletRunner().newClient().newInvocation(webRequest);

        return new MockHttpServletRequest(readAsBytes(ic.getRequest().getInputStream()));
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
    private static byte[] readAsBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;

        while ((len = inputStream.read(buf)) != -1) {
            output.write(buf, 0, len);
        }

        inputStream.close();
        output.close();

        return output.toByteArray();
    }

    /**
     * UploadThread which accept one FileUpload instance to upload
     *
     */
    class UploadThread extends Thread {
        private FileUpload upload = null;
        private boolean hasError = false;
        private String errorMessage = null;
        UploadThread(FileUpload upload) {
            this.upload = upload;
        }

        /**
         * Execute upload file thread.
         */
        public void run() {
            try {
                FileUploadResult uploadResult = this.upload.uploadFiles(createRequest());
                UploadedFile[] files = uploadResult.getAllUploadedFiles();

                if (files.length != 1) {
                    this.hasError = true;
                    this.errorMessage = "Incorrect size";
                    return;
                }

                if (!REMOTE_FILE.equals(files[0].getRemoteFileName())) {
                    this.hasError = true;
                    this.errorMessage = "Incorrect remote file name";
                    return;
                }

                if (FILE_SIZE != files[0].getSize()) {
                    this.hasError = true;
                    this.errorMessage = "Incorrect remote file size";
                    return;
                }
            } catch (Exception e) {
                this.hasError = true;
                this.errorMessage = e.getMessage();
            }
        }

        /**
         * Return if the error occurs.
         *
         * @return if error occurs
         */
        public boolean hasError() {
            return this.hasError;
        }

        /**
         * Return if the error occurs.
         *
         * @return if error occurs
         */
        public String getErrorMessage() {
            return this.errorMessage;
        }
    }
}
