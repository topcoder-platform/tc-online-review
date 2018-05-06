/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.stresstests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.Constructor;

import java.util.Iterator;
import java.util.Map;


/**
 * <p>
 * Utility class. Provides utilities used in this test.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public final class TestHelper {
    /**
     * <p>
     * Dummy connection implementation used in test.
     * </p>
     */
    public static final Constructor DUMMY_CONNECTION_IMPL;

    /**
     * <p>
     * Initialize the static member.
     * </p>
     */
    static {
        Constructor con = null;

        try {
            boolean jdbc3 = true;

            try {
                Class.forName("java.sql.Savepoint");
            } catch (ClassNotFoundException e) {
                // JDBC 2.0
                jdbc3 = false;
            }

            if (jdbc3) {
                // JDBC 3.0
                System.out.println("JDBC 3.0");

                Class connectionClass = Class.forName("com.topcoder.db.connectionfactory.jdbc3.DummyConnectionImpl");
                con = connectionClass.getConstructor(new Class[] {String.class});
            } else {
                // JDBC 2.0
                System.out.println("JDBC 2.0");

                Class connectionClass = Class.forName("com.topcoder.db.connectionfactory.jdbc2.DummyConnectionImpl");
                con = connectionClass.getConstructor(new Class[] {String.class});
            }
        } catch (ClassNotFoundException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
        } finally {
            DUMMY_CONNECTION_IMPL = con;
        }
    }

    /**
     * <p>
     * Create a new <code>TestHelper</code> instance.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * <p>
     * Generate a configuration file. The given map contains the property names and values in the configuration under
     * property &quot;property&quot;.
     * </p>
     *
     * @param config configuration file
     * @param map map contains property names and values
     * @param namespace namespace of the configuration
     *
     * @throws IOException if any I/O error occurred
     */
    public static void generateConfig(File config, Map map, String namespace)
        throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(config));
        pw.println("<CMConfig>");
        pw.println("<Config name='" + namespace + "'>");
        pw.println("<Property name='property'>");

        // Enumerate every entry in map
        Iterator iter = map.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            pw.println("<Property name='" + entry.getKey() + "'><Value>" + entry.getValue() + "</Value></Property>");
        }

        pw.println("</Property>");
        pw.println("</Config>");
        pw.println("</CMConfig>");
        pw.close();
    }
}

