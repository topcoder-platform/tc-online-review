/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;

import com.topcoder.project.phases.Phase;

import com.topcoder.util.config.ConfigManager;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * All test cases for AbstractPhaseHandler class.
 * </p>
 * <p>
 * Version 1.3 change notes : since the configuration for phases handler are changed, this test cases are fully
 * updated correspondingly.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>As EmailTemplateSource does not exist,remove
 * testAbstractPhaseHandlerWithInvalidCF_EmailTemplateSource_invalid</li>
 * </ul>
 * </p>
 * @author bose_java, waits, microsky
 * @version 1.6.1
 */
public class AbstractPhaseHandlerTest extends BaseTest {
    /**
     * Sets up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);
        configManager.add(EMAIL_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
    }

    /**
     * cleans up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will null argument.
     * @throws ConfigurationException
     *             not under test.
     */
    public void testAbstractPhaseHandlerWithNull() throws ConfigurationException {
        try {
            new AbstractPhaseHandlerSubClass(null);
            fail("AbstractPhaseHandler(String) did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will empty argument.
     * @throws ConfigurationException
     *             not under test.
     */
    public void testAbstractPhaseHandlerWithEmpty() throws ConfigurationException {
        try {
            new AbstractPhaseHandlerSubClass("  ");
            fail("AbstractPhaseHandler(String) did not throw IllegalArgumentException for empty argument.");
        } catch (IllegalArgumentException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will not-exists argument.
     * @throws Exception
     *             into JUnit
     */
    public void testAbstractPhaseHandlerWithNotExistNS() throws Exception {
        try {
            new AbstractPhaseHandlerSubClass("notExist");
            fail("The namespace does not exist.");
        } catch (ConfigurationException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will invalid config, the 'ConnectionFactoryNS' is
     * missing.
     * @throws Exception
     *             into JUnit
     */
    public void testAbstractPhaseHandlerWithInvalidCF_ConnectionFactoryNS_invalid() throws Exception {
        try {
            new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.Invalid1");
            fail("The namespace is invalid.");
        } catch (ConfigurationException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will invalid config, the 'ManagerHelperNamespace' is
     * invalid.
     * @throws Exception
     *             into JUnit
     */
    public void testAbstractPhaseHandlerWithInvalidCF_ManagerHelperNamespace_invalid() throws Exception {
        try {
            new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.Invalid2");
            fail("The namespace is invalid.");
        } catch (ConfigurationException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will invalid config, the
     * 'StartPhaseEmail/EmailFromAddress' is
     * missing.
     * @throws Exception
     *             into JUnit
     */
    public void testAbstractPhaseHandlerWithInvalidCF_EmailFromAddress_missing() throws Exception {
        try {
            new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.Invalid4");
            fail("The namespace is invalid.");
        } catch (ConfigurationException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will invalid config, the
     * 'StartPhaseEmail/EmailTemplateName'
     * is invalid.
     * @throws Exception
     *             into JUnit
     */
    public void testAbstractPhaseHandlerWithInvalidCF_EmailTemplateName_invalid() throws Exception {
        try {
            new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.Invalid5");
            fail("The namespace is invalid.");
        } catch (ConfigurationException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will invalid config, the 'StartPhaseEmail/SendInvalid' is
     * invalid value.
     * @throws Exception
     *             into JUnit
     */
    public void testAbstractPhaseHandlerWithInvalidCF_SendEmail_invalid() throws Exception {
        try {
            new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.Invalid6");
            fail("The namespace is invalid.");
        } catch (ConfigurationException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will invalid config, the 'StartPhaseEmail/Priority' is
     * invalid
     * value.
     * @throws Exception
     *             into JUnit
     */
    public void testAbstractPhaseHandlerWithInvalidCF_Priority_invalid() throws Exception {
        try {
            new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.Invalid7");
            fail("The namespace is invalid.");
        } catch (ConfigurationException ex) {
            // expected.
        }
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will valid argument. The configuration is the simplest
     * config.
     * @throws ConfigurationException
     *             not under test.
     */
    public void testAbstractPhaseHandlerWithValid() throws ConfigurationException {
        new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.simplest");
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will valid argument. The configuration is the simplest
     * config.
     * @throws ConfigurationException
     *             not under test.
     */
    public void testAbstractPhaseHandlerWithValid1() throws ConfigurationException {
        new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.emptySchemes");
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will valid argument.
     * @throws ConfigurationException
     *             not under test.
     */
    @SuppressWarnings("unchecked")
    public void testAbstractPhaseHandler_accuracy() throws ConfigurationException {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        // verify
        Map<String, EmailOptions> startPhaseEmailOptions = (Map<String, EmailOptions>) getPrivateField(
                AbstractPhaseHandler.class, handler, "startPhaseEmailOptions");
        assertNotNull("The field is null.", startPhaseEmailOptions);
        assertEquals("The field is invalid.", 2, startPhaseEmailOptions.size());
        assertEquals("The value is invalid.", "Phase Start", startPhaseEmailOptions.get("Manager").getSubject());
        assertNotNull("The value is invalid.", startPhaseEmailOptions.get("Reviewer"));
        assertEquals("The value is invalid.", 0, startPhaseEmailOptions.get("Reviewer").getPriority());
        // verify
        Map<String, EmailOptions> endPhaseEmailOptions = (Map<String, EmailOptions>) getPrivateField(
                AbstractPhaseHandler.class, handler, "endPhaseEmailOptions");
        assertNotNull("The field is null.", endPhaseEmailOptions);
        assertEquals("The field is invalid.", 2, endPhaseEmailOptions.size());
        assertEquals("The value is invalid.", "Phase End", endPhaseEmailOptions.get("Manager").getSubject());
        assertNotNull("The value is invalid.", endPhaseEmailOptions.get("Reviewer"));
        assertEquals("The value is invalid.", 0, endPhaseEmailOptions.get("Reviewer").getPriority());
    }

    /**
     * Tests the constructor AbstractPhaseHandler(String) will valid argument.
     * @throws ConfigurationException
     *             not under test.
     */
    @SuppressWarnings("unchecked")
    public void testAbstractPhaseHandler_accuracy1() throws ConfigurationException {
        AbstractPhaseHandler handler =
            new AbstractPhaseHandlerSubClass("com.cronos.onlinereview.phases.AbstractPhaseHandler.matchAllRoles");

        // verify
        Map<String, EmailOptions> startPhaseEmailOptions = (Map<String, EmailOptions>) getPrivateField(
                AbstractPhaseHandler.class, handler, "startPhaseEmailOptions");
        assertNotNull("The field is null.", startPhaseEmailOptions);
        assertEquals("The field is invalid.", 25, startPhaseEmailOptions.size());
        assertNotNull("The value is invalid.", startPhaseEmailOptions.get("Manager"));
        assertNotNull("The value is invalid.", startPhaseEmailOptions.get("Reviewer"));

        // verify
        Map<String, EmailOptions> endPhaseEmailOptions = (Map<String, EmailOptions>) getPrivateField(
                AbstractPhaseHandler.class, handler, "endPhaseEmailOptions");
        assertNotNull("The field is null.", endPhaseEmailOptions);
        assertEquals("The field is invalid.", 25, endPhaseEmailOptions.size());
        assertNotNull("The value is invalid.", endPhaseEmailOptions.get("Manager"));
        assertNotNull("The value is invalid.", endPhaseEmailOptions.get("Reviewer"));
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with null.
     * @throws PhaseHandlingException
     *             into JUnit
     * @throws ConfigurationException
     *             into JUnit
     */
    public void testSendEmail_nullPhase() throws PhaseHandlingException, ConfigurationException {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            handler.sendEmail(null);
            fail("sendEmail(Phase) did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException ex) {
            // expected.
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase, Map) method with null phase.
     * @throws Exception
     *             into JUnit
     */
    public void testSendEmailpb_nullPhase() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            handler.sendEmail(null, new HashMap<String, Object>());
            fail("sendEmail(Phase, Map) did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException ex) {
            // expected.
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase, Map) method with null Map.
     * @throws Exception
     *             into JUnit
     */
    public void testSendEmailpb_nullMap() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            handler.sendEmail(createPhase(1, 1, "Scheduled", 1, "Registration"), null);
            fail("sendEmail(Phase, Map) did not throw IllegalArgumentException for null Map.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase, Map) method with null Map.
     * @throws Exception
     *             into JUnit
     */
    public void testSendEmailpb_invalidMap() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            Map<String, Object> values = new HashMap<String, Object>();

            // invalid value
            values.put("SUBMITTER", null);
            handler.sendEmail(createPhase(1, 1, "Scheduled", 1, "Registration"), values);
            fail("sendEmail(Phase, Map) did not throw IllegalArgumentException for invalid Map.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase, Map) method with null Map.
     * @throws Exception
     *             into JUnit
     */
    public void testSendEmailpb_invalidMap2() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            Map<String, Object> values = new HashMap<String, Object>();

            // invalid value
            values.put(" ", "value");
            handler.sendEmail(createPhase(1, 1, "Scheduled", 1, "Registration"), values);
            fail("sendEmail(Phase, Map) did not throw IllegalArgumentException for invalid Map.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with Scheduled phase status.
     * @throws Exception
     *             not under test.
     */
    public void testSendEmailWithStartPhase() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            cleanTables();
            setupProjectResourcesNotification("All");

            Phase phase = createPhase(1, 1, "Scheduled", 1, "Registration");
            handler.sendEmail(phase);

            // manually check mailbox.
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with Open phase status.
     * @throws Exception
     *             not under test.
     */
    public void testSendEmailWithStopPhase() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            cleanTables();
            setupProjectResourcesNotification("All");

            Phase phase = createPhase(1, 2, "Open", 1, "Registration");

            handler.sendEmail(phase);

            // manually check mailbox.
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with Open phase status.
     * @throws Exception
     *             not under test.
     */
    public void testSendEmailWithStopPhase1() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE + "1");

        try {
            cleanTables();
            setupProjectResourcesNotification("All");

            Phase phase = createPhase(1, 2, "Open", 1, "Registration");

            handler.sendEmail(phase);

            // manually check mailbox.
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with zero length phase, email should not send.
     * @throws Exception
     *             not under test.
     */
    public void testSendEmailWithZeroLenthPhase() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);

        try {
            cleanTables();
            setupProjectResourcesNotification("All");

            Phase phase = createPhase(1, 2, "Open", 1, "Registration");
            phase.setLength(0);

            handler.sendEmail(phase);

            // manually check mailbox.
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests AbstractPhaseHandler.createConnection() for accuracy.
     * @throws Exception
     *             not under test.
     */
    public void testCreateConnection() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);
        Connection conn = null;

        try {
            conn = handler.createConnection();
            assertNotNull("create Connection returned null.", conn);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Tests AbstractPhaseHandler.getManagerHelper() returns non-null.
     * @throws Exception
     *             not under test.
     */
    public void testGetManagerHelper() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE);
        assertNotNull("ManagerHelper not instantiated.", handler.getManagerHelper());
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with Open phase status.
     * @throws Exception
     *             not under test.
     */
    public void testSendEmailWithStartPhase1() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE + "2");

        try {
            cleanTables();
            setupProjectResourcesNotification("All");

            Phase phase = createPhase(1, 1, "Scheduled", 1, "Registration");

            handler.sendEmail(phase);

            // manually check mailbox.
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with Open phase status.
     * @throws Exception
     *             not under test.
     */
    public void testSendEmailWithStartPhase2() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE + "3");

        try {
            cleanTables();
            setupProjectResourcesNotification("All");

            Phase phase = createPhase(1, 1, "Scheduled", 1, "Registration");

            handler.sendEmail(phase);

            // manually check mailbox.
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AbstractPhaseHandler.sendEmail(Phase) method with Open phase status.
     * @throws Exception
     *             not under test.
     */
    public void testSendEmailWithStartPhase3() throws Exception {
        AbstractPhaseHandler handler = new AbstractPhaseHandlerSubClass(ABSTRACT_HANDLER_NAMESPACE + "4");

        try {
            cleanTables();
            setupProjectResourcesNotification("All");

            Phase phase = createPhase(1, 1, "Scheduled", 1, "Registration");

            handler.sendEmail(phase);

            // manually check mailbox.
        } finally {
            cleanTables();
        }
    }
}
