/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

/**
 * <p>
 * Demo for the Configuration Persistence component.
 * </p>
 * <p>
 * This component maintains an internal representation of the state of configuration represented in
 * any number of files when those files were last accessed, and it keeps track of which
 * configuration belongs to which file.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class Demo extends TestCase {
    /**
     * Demo for the component.
     *
     * @throws Exception
     *             exception any exception to junit
     */
    public void testDemo() throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager("test_files/demopreload.properties");
        // retrieve all stored configuration
        Map config = manager.getConfiguration();
        // we can retrieve the configuration object for com.topcoder.abc
        ConfigurationObject xmlObj = manager.getConfiguration("demo");
        ConfigurationObject defaultObj = xmlObj.getChild("default");
        ConfigurationObject xxxObj = xmlObj.getChild("com.topcoder.xxx");
        ConfigurationObject yyyObj = xmlObj.getChild("com.topcoder.yyy");
        Object bValue = defaultObj.getPropertyValue("b");
        // bValue should be valueb
        System.out.println("com.topcoder.abc.b = " + bValue);

        Object cValue = defaultObj.getChild("b").getPropertyValue("c");
        System.out.println("com.topcoder.abc.b.c=" + cValue);
        // get multi-values
        Object eValues[] = xxxObj.getPropertyValues("e");
        for (int i = 0; i < eValues.length; ++i) {
            System.out.println("com.topcoder.xxx.e = " + eValues[i]);
        }

        // A new file can easily be loaded into the current configuration
        String newNamespace = "com.topcoder.zzz";
        manager.loadFile(newNamespace, "test_files/test.properties");
        ConfigurationObject zzzObj = manager.getConfiguration("com.topcoder.zzz");
        ConfigurationObject newObj = new DefaultConfigurationObject("newProperty");
        zzzObj.addChild(newObj);
        newObj.setPropertyValue("demoProperty", "1");
        // We can persist these changes using the manager
        manager.saveConfiguration(newNamespace, zzzObj);

        // refresh a namespace
        // manager.refresh("com.topcoder.abc");
        // refresh the manager
        manager.refresh();
    }
}
