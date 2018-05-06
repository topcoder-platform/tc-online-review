/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests;

import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.DistributionTool;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Tests DistributionTool class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class DistributionToolTest extends TestCase {
    /**
     * Instance to test.
     */
    private DistributionTool target;

    /**
     * DistributionScript to use in tests.
     */
    private DistributionScript script;

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionToolTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        TestHelper.clearTemp();

        Map<String, DistributionScript> map = new HashMap<String, DistributionScript>();

        script = new DistributionScript();
        script.setScriptFolder("sf");
        script.setCommands(Arrays.<DistributionScriptCommand>asList(new MockDistributionScriptCommand()));
        script.setRequiredParams(Arrays.asList("abc", "xyz"));
        Map<String, String> dp = new HashMap<String, String>();
        dp.put("pqr", "000");
        dp.put("xxx", "111");
        script.setDefaultParams(dp);

        map.put("dt", script);
        target = new DistributionTool(map);
    }

    /**
     * <p>Tears down test environment.</p>
     *
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        TestHelper.clearTemp();
        super.tearDown();
    }

    /**
     * Tests constructor #2.
     *
     * @throws Exception when it occurs deeper
     */
    public void testConstructor1() throws Exception {
        target = new DistributionTool("test_files/accuracy/config.properties",
            "com.topcoder.util.distribution.DistributionTool");
        Field f = DistributionTool.class.getDeclaredField("scripts");
        f.setAccessible(true);
        Map<String, DistributionScript> result = (Map<String, DistributionScript>) f.get(target);

        script = result.get("dotnet");
        assertEquals("required params",
            new HashSet<String>(Arrays.asList("output_dir", "Component Name", "package.name", "rs")),
            new HashSet<String>(script.getRequiredParams()));
        Map dp = new HashMap();
        dp.put("component_description", "");
        dp.put("version", "1.0.0.1");
        assertEquals("default params", dp, script.getDefaultParams());
        assertEquals("script folder", "test_files/accuracy", script.getScriptFolder());
        assertEquals("commands", 3, script.getCommands().size());

        script = result.get("java");
        assertEquals("required params", new ArrayList<String>(), script.getRequiredParams());
        assertEquals("default params", new HashMap(), script.getDefaultParams());
        assertEquals("script folder", "test_files/accuracy", script.getScriptFolder());
        assertEquals("commands", 0, script.getCommands().size());
    }

    /**
     * Tests constructor #2 when config file is located in the root.
     *
     * @throws Exception when it occurs deeper
     */
    public void testConstructor1Root() throws Exception {
        // write file in the root
        File f = new File("temp_script.txt");
        Writer out = new FileWriter(f);
        out.write("undefine : abc");
        out.close();

        // just see that there is no error
        try {
            new DistributionTool("test_files/accuracy/config.properties",
                "com.topcoder.util.distribution.DistributionTool2");
        } finally {
            assertTrue("file should be deleted", f.delete());
        }
    }

    /**
     * Tests constructor #4.
     *
     * @throws Exception when it occurs deeper
     */
    public void testConstructor2() throws Exception {
        Map<String, DistributionScript> map = new HashMap<String, DistributionScript>();
        script = new DistributionScript();
        script.setScriptFolder("abc");
        map.put("abc", script);
        new DistributionTool(map);
    }

    /**
     * Tests createDistribution method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testCreateDistribution1() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("abc", "123");
        params.put("xyz", "567");
        params.put("pqr", "222");
        params.put(DistributionTool.VERSION_PARAM_NAME, "1.2.3.4.5.6");
        params.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.component.name");
        target.createDistribution("dt", params);

        Map<String, String> expected = new HashMap<String, String>(params);
        expected.put("xxx", "111");
        expected.put("version.major", "1");
        expected.put("version.minor", "2");
        expected.put("version.micro", "3");
        expected.put("version.build", "4");
        expected.put("component name", null);
        expected.put("Component_Name", null);
        expected.put("component_name", null);
        expected.put("package/name", "com/topcoder/component/name");
        expected.put("package\\name", "com\\topcoder\\component\\name");
        expected.put("distribution_type", "dt");
        expected.put("current_year", new SimpleDateFormat("yyyy").format(new Date()) + "");
        expected.put("script_dir", "sf");
        assertEquals(expected, MockDistributionScriptCommand.lastCall);
    }

    /**
     * Tests createDistribution method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testCreateDistribution2() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("abc", "123");
        params.put("xyz", "567");
        params.put("pqr", "222");
        params.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Some Very Important Component");
        target.createDistribution("dt", params);

        Map<String, String> expected = new HashMap<String, String>(params);
        expected.put("xxx", "111");
        expected.put("version.major", null);
        expected.put("version.minor", null);
        expected.put("version.micro", null);
        expected.put("version.build", null);
        expected.put("component name", "some very important component");
        expected.put("Component_Name", "Some_Very_Important_Component");
        expected.put("component_name", "some_very_important_component");
        expected.put("package/name", null);
        expected.put("package\\name", null);
        expected.put("distribution_type", "dt");
        expected.put("current_year", new SimpleDateFormat("yyyy").format(new Date()) + "");
        expected.put("script_dir", "sf");
        assertEquals(expected, MockDistributionScriptCommand.lastCall);
    }

    /**
     * Checks DistributionScriptExecutionContext to be valid.
     *
     * @param expected expected data (as map)
     * @param actual   actual value
     */
    static void assertEquals(Map<String, String> expected, DistributionScriptExecutionContext actual) {
        for (String key : expected.keySet()) {
            assertEquals(key + " value", expected.get(key), actual.getVariable(key));
        }
    }

    /**
     * Provides easy access to the method's parameter (context).
     *
     * @author orange_cloud
     * @version 1.0
     */
    public static class MockDistributionScriptCommand implements DistributionScriptCommand {
        /**
         * Argument from the last execute call.
         */
        public static DistributionScriptExecutionContext lastCall;

        /**
         * Simple constructor.
         */
        public MockDistributionScriptCommand() {
            lastCall = null;
        }

        /**
         * <p> Executes this command. </p>
         *
         * @param context the distribution script execution context
         * @throws IllegalArgumentException if context is null
         * @throws DistributionScriptCommandExecutionException
         *                                  if some other error occurred
         */
        public void execute(DistributionScriptExecutionContext context) throws
            DistributionScriptCommandExecutionException {
            lastCall = context;
        }
    }
}
