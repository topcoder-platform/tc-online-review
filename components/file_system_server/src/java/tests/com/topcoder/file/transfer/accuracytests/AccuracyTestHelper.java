/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * an helper class for Accuracy test. 
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class AccuracyTestHelper {
    /**
     * <p>
     * Represent the config file for ipserver manager.
     * </p>
     * */
    public static final String IPSERVER_CONFIG = "accuracy/conf/IPServerManager.xml";

    /**
     * <p>
     * Represents the config file for message factory.
     * </p>
     */
    public static final String MESSAGE_FACTORY_CONFIG = "accuracy/conf/MessageFactory.xml";
    
    /**
     * <p>
     * Represent the config file for db connection factory.
     * </p>
     */
    public static final String DB_CONNECTION_FACTORY= "DBConnectionFactoryImpl.xml";
    
    /**
     * <p>
     * Represents the namespace for message factory.
     * </p>
     */
    public static final String MESSAGE_FACTORY_NAMESPACE = "com.topcoder.processor.ipserver.message";
    
    /**
     * <p>
     * Represents a server path to store the uploaded file.
     * </p>
     */
    public static String SERVER_PATH = "test_files/accuracy/store";
    
    /**
     * <p>
     * Represents data files for client.
     * </p>
     */
    public static String CLIENT_PATH = "test_files/accuracy/dataFiles";
    
    /**
     * <p>
     * Represents the namespace of idgenerator.
     * </p>
     */
    public static String IDGENERATOR_NAMESPACE = "accuracy_id_generator";
    
    /**
     * <p>
     * Represents the assume FREE DISKSPACE for FSSDiskSpaceChecker.
     * </p>
     */
    public static long MAX_FREE_DISKSPACE = 10000000l; 
    
    /**
     * Load namespaces for the tests.
     * @throws Exception Exception
     *             to JUnit
     */
    public static void addConfigFiles() throws Exception {
        removeConfigFiles();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(IPSERVER_CONFIG);
        cm.add(MESSAGE_FACTORY_CONFIG);
        cm.add(DB_CONNECTION_FACTORY);
    }

    /**
     * <p>
     * Release namespaces in configuratin manager.
     * </p>
     * 
     * @throws Exception Exception
     *             to JUnit
     */
    public static void removeConfigFiles() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator itor = cm.getAllNamespaces();
        while (itor.hasNext()) {
            cm.removeNamespace((String) itor.next());
        }
    }

    /**
     * <p>
     * Compare two files.
     * </p>
     * 
     * @param expected
     *            the file expected.
     * @param content
     *            the file to compare.
     * @return true for the two file are same, else false.
     */
    public static boolean diffFile(String expected, String content) {
        InputStream expectedStream = null;
        InputStream contentStream = null;
        try {
            if (new File(expected).length() == new File(content).length()) {
                expectedStream = new FileInputStream(expected);
                contentStream = new FileInputStream(content);
                int x = -1;
                int y = -1;
                do {
                    x = expectedStream.read();
                    y = contentStream.read();
                } while (x != -1 && y != -1 && x == y);
                if (x == -1 && y == -1) {
                    return true;
                }
            }
        } catch (IOException e) {
            // ignore
        } finally {
            try {
                if (expectedStream != null) {
                    expectedStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
            try {
                if (contentStream != null) {
                    contentStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
            
        }
        return false;
    }
    
    /**
     * <p>
     * Copy file.
     * </p>
     * 
     * @param file1 the src file
     * @param file2 the dest file
     */
    public static void copy(String file1,String file2) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in= new FileInputStream (file1);
            out = new FileOutputStream (file2);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1)
                out.write(bytes, 0, c);
        } catch (Exception e) {
            try {
                in.close();
            } catch (Exception e1) {
                // ok
            }
            try {
                out.close();
            } catch (Exception e2) {
                // ok
            }
        }
    }
}
