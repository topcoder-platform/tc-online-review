/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.junit.Assert;

import junit.framework.TestCase;

import com.topcoder.commons.utils.JDBCUtility;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * The base test case for Unit tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0.1
 */
public abstract class BaseTestCase extends TestCase {
    /**
     * Constant for one day time.
     */
    public static final long DAY = 24 * 60 * 60 * 1000;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    protected void setUp() throws Exception {
    }

    /**
     * <p>
     * Cleans up the test environment.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    protected void tearDown() throws Exception {
    }

    /**
     * Clears all namespace under config manager.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearNamespace() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator<?> iter = configManager.getAllNamespaces();

        while (iter.hasNext()) {
            String ns = (String) iter.next();
            if (!"com.topcoder.util.log".equals(ns)) {
                configManager.removeNamespace(ns);
            }
        }
    }

    /**
     * <p>
     * Sets the field value to bean instance.
     * </p>
     *
     * @param <T>
     *            The generic type.
     * @param clazz
     *            the class type.
     * @param instance
     *            the instance to be set.
     * @param fieldName
     *            The field name.
     * @param value
     *            The field value to set.
     * @throws Exception
     *             to JUnit.
     */
    protected static <T> void setField(Class<T> clazz, T instance, String fieldName, Object value)
        throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
        field.setAccessible(false);
    }

    /**
     * <p>
     * Gets value for field of given object.
     * </p>
     *
     * @param obj
     *            the given object.
     * @param field
     *            the field name.
     * @return the field value.
     * @throws Exception
     *             to JUnit.
     */
    public static Object getField(Object obj, String field) throws Exception {
        Object value = null;
        Field declaredField = obj.getClass().getDeclaredField(field);
        declaredField.setAccessible(true);

        value = declaredField.get(obj);
        declaredField.setAccessible(false);

        return value;
    }

    /**
     * Helper method to create a phase instance.
     *
     * @param phaseId
     *            phase id.
     * @param phaseStatusId
     *            phase Status Id.
     * @param phaseStatusName
     *            phase Status Name.
     * @param phaseTypeId
     *            phase Type Id.
     * @param phaseTypeName
     *            phase Type Name.
     * @return phase instance.
     */
    protected Phase createPhase1(long phaseId, long phaseStatusId, String phaseStatusName, long phaseTypeId,
        String phaseTypeName) {
        Project project = new Project(new Date(), new DefaultWorkdays());
        project.setId(1);

        Phase phase = new Phase(project, 2000);
        phase.setId(phaseId);
        phase.setPhaseStatus(new PhaseStatus(phaseStatusId, phaseStatusName));
        phase.setPhaseType(new PhaseType(phaseTypeId, phaseTypeName));

        return phase;
    }

    /**
     * Helper method to create Resource instance.
     *
     * @param resourceId
     *            resource Id.
     * @param phaseId
     *            phase Id.
     * @param projectId
     *            project Id.
     * @param resourceRoleId
     *            resource Role Id.
     * @return Resource instance.
     */
    protected Resource createResource(long resourceId, Long phaseId, long projectId, long resourceRoleId) {
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setPhase(phaseId);
        resource.setProject(new Long(projectId));
        resource.setResourceRole(new ResourceRole(resourceRoleId));

        return resource;
    }

    /**
     * Returns a connection instance.
     *
     * @return a connection instance.
     * @throws Exception
     *             not for this test case.
     */
    protected static Connection getConnection() throws Exception {
        Properties config = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream("test_files/config/db.properties");
            config.load(stream);
        } catch (IOException e) {
            // Ignore
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                // Ignore
            }
        }

        Class.forName(config.getProperty("driverClassName"));
        return DriverManager.getConnection(config.getProperty("connectionString"),
            config.getProperty("username"), config.getProperty("password"));
    }

    /**
     * Closes the connection.
     *
     * @param conn
     *            the connection.
     */
    protected static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                // do nothing.
            }
        }
    }

    /**
     * helper method to close a statement.
     *
     * @param stmt
     *            statement to close.
     */
    protected static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                // do nothing
            }
        }
    }

    /**
     * Gets the configuration object from the given file with the given namespace.
     *
     * @param file
     *            the configuration file.
     * @param configStr
     *            the configuration string.
     * @param namespace
     *            the namespace.
     * @return the configuration object.
     * @throws Exception
     *             to JUnit.
     */
    public static ConfigurationObject getConfigurationObject(String file, String configStr, String namespace)
        throws Exception {

        ConfigurationFileManager manager = new ConfigurationFileManager(file);
        ConfigurationObject config = manager.getConfiguration(configStr);
        return config.getChild(namespace);
    }

    /**
     * Clears the database.
     *
     * @throws Exception
     *             to JUnit
     */
    public static void clearData() throws Exception {
        executeSQL("test_files/accuracytests/db_clear.sql");
    }

    /**
     * Inserts test records into the database.
     *
     * @throws Exception
     *             to JUnit
     */
    public static void insertData() throws Exception {
        executeSQL("test_files/config/db_insert.sql");
    }

    /**
     * Gets the count of resources with the given project.
     * @param projectId the project id.
     * @return the count of resource.
     * @throws Exception
     *             to JUnit
     */
    public static long getResourcesCount(long projectId) throws Exception {
        Connection connection = getConnection();
        connection.setAutoCommit(true);
        try {
            Object[][] result = JDBCUtility.executeQuery(connection,
                "SELECT count(*) FROM resource WHERE project_id=?", new int[] {Types.DECIMAL},
                new Object[] {projectId}, new Class<?>[] {Long.class}, Exception.class);
            return (Long) result[0][0];
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Reads the content of a given file.
     *
     * @param fileName
     *            the name of the file to read
     *
     * @return a string with content of the given file
     *
     * @throws IOException
     *             if any error occurs during reading
     */
    private static String readFile(String fileName) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line).append(System.getProperty("line.separator"));
                line = reader.readLine();
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Run the given SQL file.
     *
     * @param filePath
     *            SQL file path
     *
     * @throws Exception
     *             to JUnit
     */
    public static void executeSQL(String filePath) throws Exception {
        Connection connection = getConnection();
        Statement statement = null;

        connection.setAutoCommit(true);
        try {
            String content = readFile(filePath);
            String[] contents = content.split(";");
            for (int i = 0; i < contents.length; i++) {
                String st = contents[i].trim();
                if (st.length() == 0 || st.startsWith("--")) {
                    continue;
                }
                statement = connection.createStatement();
                statement.execute(st);
            }
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    /**
     * Runs the main method with given arguments.
     *
     * @param args
     *            the arguments.
     * @throws Exception
     *             to JUnit.
     */
    protected void runMain(String[] args) throws Exception {
        final PipedOutputStream out = new PipedOutputStream();
        InputStream in = new PipedInputStream(out);
        InputStream org = System.in;
        System.setIn(in);

        try {
            class MainThread extends Thread {
                /**
                 * <p>
                 * The arguments.
                 * </p>
                 */
                private String[] args;

                /**
                 * <p>
                 * Creates an instance of MainThread.
                 * </p>
                 *
                 * @param args
                 *            the arguments.
                 */
                public MainThread(String[] args) {
                    this.args = args;
                }

                /**
                 * Runs this thread. It will sleep 2 minutes then write a char to stop the job.
                 */
                @Override
                public void run() {
                    ReviewAssignmentUtility.main(args);

                    try {
                        // sleep to make sure the job run
                        Thread.sleep(90 * 1000);
                    } catch (InterruptedException e) {
                        // ignore
                    }

                    // write a char to stop
                    try {
                        out.write(1);
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }

            Thread t = new MainThread(args);
            t.start();

            Thread.sleep(1000);
        } finally {
            // set back
            System.setIn(org);
        }
    }
}
