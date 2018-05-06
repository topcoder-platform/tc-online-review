package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.DefaultKeyConverter;
import junit.framework.TestCase;


/**
 * Accuracy test for DefaultKeyConverter class.
 */
public class DefaultKeyConverterAccuracyTest extends TestCase {
    /**
     * The namespace to load DefaultKeyConverter.
     */
    private static final String NAMESPACE = "com.topcoder.security.authenticationfactory.DefaultKeyConverter";

    /**
     * The DefaultKeyConverter instance.
     */
    private DefaultKeyConverter converter = null;

    /**
     * Setup for each test case.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig.xml");
        converter = new DefaultKeyConverter(NAMESPACE);
    }

    /**
     * Clean up for each test cases.
     *
     * @throws Exception to Junit.
     */
    protected void tearDown() throws Exception {
        TestUtil.clearAllNamespace();
    }

    /**
     * Test convert key in normal case.
     *
     * @throws Exception to Junit.
     */
    public void testConverterKey() throws Exception {
        // convert exist string, will return corresponding string
        assertEquals(converter.convert("username"), "UserName");
        assertEquals(converter.convert("password"), "Pwd");

        // convert non-exist string, return itself
        assertEquals(converter.convert("not_exist"), "not_exist");
    }

    /**
     * Test convert key with a non-existent key, the same key should be returned.
     *
     * @throws Exception to Junit.
     */
    public void testConverterKeyNotExist() throws Exception {
        // convert non-exist string, return itself
        assertEquals(converter.convert("not_exist"), "not_exist");
    }
}
