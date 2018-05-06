/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;

/**
 * This class is helpful to test the method who want to read config file. This class read neccessary
 * data from config file.
 *
 * @author qiucx0161
 * @version 1.0
 */
public class TestHelper {

    /**
     * The namespace from which to read the configuration values from the ConfigManager.
     */
    private static final String NAMESPACE = TestHelper.class.getName();

    /**
     * ConfigManager instance will be used to load config file. It is initialized in constructor.
     */
    private ConfigManager cm = null;

    /**
     * The constructor will initialize the ConfigManager instance.
     */
    public TestHelper() {
        cm = ConfigManager.getInstance();
    }

    /**
     * It is used to create a DataSource instance which can be used in testing.
     *
     * @return The DataSource instance.
     * @throws Exception
     *             to JUnit
     */
    public DataSource getDataSource() throws Exception {

        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        try {
            cm.add(NAMESPACE, "test.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
            String jndiName = cm.getString(NAMESPACE, "JNDI.jndi_name");

            return (DataSource) new InitialContext().lookup(jndiName);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * It is used to create a Property instance which can be used for testing JNDIConnectionProducer
     * class.
     *
     * @return The Property instance.
     * @throws Exception
     *             to JUnit
     */
    public Property getJndiProperty() throws Exception {

        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        try {
            cm.add(NAMESPACE, "getJndiProperty.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            return cm.getPropertyObject(NAMESPACE, "connections");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * It is used to create a Property instance which can be used for testing
     * ReflectingConnectionProducer class.
     *
     * @return The Property instance.
     * @throws Exception
     *             to JUnit
     */
    public Property getJdbcProperty() throws Exception {

        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        try {
            cm.add(NAMESPACE, "test.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            Property proper = cm.getPropertyObject(NAMESPACE, "JDBC");
            return proper;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * It is used to release the resource.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void releaseSource() throws Exception {
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

    }

    /**
     * It is used to get the jndi name.
     *
     * @throws Exception
     *             to JUnit.
     * @return the jndi name.
     */
    public String getJndiName() throws Exception {
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        try {
            cm.add(NAMESPACE, "jndiName.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
            return cm.getString(NAMESPACE, "jndiName");
        } catch (Exception e) {
            throw e;
        }
    }
}
