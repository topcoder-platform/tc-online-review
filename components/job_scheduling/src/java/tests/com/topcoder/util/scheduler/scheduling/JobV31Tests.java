/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;

/**
 * Unit test cases for the new features in <code>Job</code> class version 3.1.
 * @author fuyun
 * @version 3.1
 */
public class JobV31Tests extends TestCase {

    /**
     * Represents the configuration file to create the running object from
     * configuration manager.
     */
    private static final String CONFIG_FILE = "V31/SampleConfig.xml";

    /**
     * <p>
     * The <code>Job</code> instance for testing.
     * </p>
     */
    private Job job;

    /**
     * The reference to private member <code>attributes</code> of
     * <code>Job</code> instance.
     */
    private Map attributes = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * <p>
     * It creates the job to test and initializes the job's attributes map.
     * Finally, it adds the necessary namespace.
     * </p>
     * @throws Exception if there is any problem.
     */
    @Override
    protected void setUp() throws Exception {

        // create the job instance.
        job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");

        // get the attributes member.
        attributes = (Map) TestHelper.getPrivateFieldValue(job, Job.class,
                "attributes");

        // put some data into the attributes map.
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        ConfigManager manager = ConfigManager.getInstance();
        manager.add(CONFIG_FILE);
    }

    /**
     * <p>
     * Cleans up the test environment.
     * </p>
     * <p>
     * It clears the attributes map. Then it clears all the registered
     * namespaces.
     * </p>
     * @throws Exception if there is any problem.
     */
    @Override
    protected void tearDown() throws Exception {
        attributes.clear();

        ConfigManager manager = ConfigManager.getInstance();
        Iterator it = manager.getAllNamespaces();
        while (it.hasNext()) {
            manager.removeNamespace((String) it.next());
        }
    }

    /**
     * <P>
     * Accuracy test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_JAVA_CLASS</code>, so the java instance
     * should be created successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandAccuracyJavaClass() throws Exception {
        String clz = "com.topcoder.util.scheduler.scheduling.MyJob";
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        job.setRunCommand(clz);
        assertEquals("Fails to set run command.", clz, TestHelper
                .getPrivateFieldValue(job, Job.class, "runCommand"));
        assertTrue("Fails to set run command.",
                TestHelper.getPrivateFieldValue(job, Job.class,
                        "runnableObject") instanceof ScheduledEnable);
    }

    /**
     * <P>
     * Accuracy test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>,
     * so running object should be created by
     * <code>ScheduledEnableObjectFactory</code>. It tests that the running
     * object could be created successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandAccuracyObjectFactory() throws Exception {
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory(
                "key", new CustomScheduledEnableObjectFactory());
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY);
        job.setRunCommand("key");
        assertEquals("Fails to set run command.", "key", TestHelper
                .getPrivateFieldValue(job, Job.class, "runCommand"));
        ScheduledJobRunner runner = (ScheduledJobRunner) TestHelper
                .getPrivateFieldValue(job, Job.class, "runnableObject");
        assertTrue("Fails to set run command.",
                runner instanceof CustomScheduledJobRunner);
        assertSame("Fails to set run command.", job, runner.getJob());
    }

    /**
     * <P>
     * Accuracy test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * so running object should be created by the object factory from the given
     * namespace. It tests that the running object could be created
     * successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandAccuracyObjectNamespace() throws Exception {
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        job.setRunCommand("com.topcoder.util.scheduler.scheduling.job");
        assertEquals("Fails to set run command.",
                "com.topcoder.util.scheduler.scheduling.job", TestHelper
                        .getPrivateFieldValue(job, Job.class, "runCommand"));
        assertTrue("Fails to set run command.",
                TestHelper.getPrivateFieldValue(job, Job.class,
                        "runnableObject") instanceof CustomScheduledJobRunner);
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_JAVA_CLASS</code>
     * </p>
     * <p>
     * Verifies that the <code>IllegalArgumentException</code> will be thrown
     * if the class type is invalid.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureJavaClassInvalidClass() throws Exception {
        String clz = "java.util.ArrayList";
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        try {
            job.setRunCommand(clz);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_JAVA_CLASS</code>
     * </p>
     * <p>
     * Verifies that the <code>IllegalArgumentException</code> will be thrown
     * if the class is not found.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureJavaClassNonExistClass() throws Exception {
        String clz = "abc";
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        try {
            job.setRunCommand(clz);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_JAVA_CLASS</code>
     * </p>
     * <p>
     * Verifies that the <code>IllegalArgumentException</code> will be thrown
     * if the constructor's access is denied (not public).
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureJavaClassIllegalAccessException() throws Exception {
        String clz = "com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactoryManager";
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        try {
            job.setRunCommand(clz);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_JAVA_CLASS</code>
     * </p>
     * <p>
     * Verifies that the <code>IllegalArgumentException</code> will be thrown
     * if the class is interface or abstract class
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureJavaClassInstantiationException() throws Exception {
        String clz = "com.topcoder.util.scheduler.scheduling.ScheduledEnable";
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        try {
            job.setRunCommand(clz);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the factory can not be found.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectFactoryNotExist() throws Exception {
        ScheduledEnableObjectFactoryManager
                .clearScheduledEnableObjectFactories();
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY);

        try {
            job.setRunCommand("key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }

    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the factory throw <code>ScheduledEnableObjectCreationException</code>
     * when creating the object.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectFactoryThrowException() throws Exception {
        ScheduledEnableObjectFactory factory = new CustomScheduledEnableObjectFactory();
        ((CustomScheduledEnableObjectFactory) factory).setThrowException();
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory(
                "key", factory);
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY);

        try {
            job.setRunCommand("key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }

    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the namespace can not be found.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceNotExist() throws Exception {
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("nonexist");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the namespace can not be found.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceObjectFactoryNamespaceNotExist()
        throws Exception {
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("nonexist");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ObjectFactoryNamespace is missing.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFNSMissing() throws Exception {
        ConfigManager.getInstance().add("V31/OF_NS_Missing.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_NS_Missing");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the object factory configuration is invalid.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceInvalidOF1() throws Exception {
        ConfigManager.getInstance().add("V31/OF_Invalid1.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_Invalid");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the class to create is interface.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceInvalidOF2() throws Exception {
        ConfigManager.getInstance().add("V31/OF_Invalid2.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_Invalid");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the class to create does not exist.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceInvalidOF3() throws Exception {
        ConfigManager.getInstance().add("V31/OF_Invalid3.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_Invalid");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the class to create's type is invalid.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceInvalidOF4() throws Exception {
        ConfigManager.getInstance().add("V31/OF_Invalid4.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_Invalid");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ObjectFactoryNamespace is empty.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFNSEmpty() throws Exception {
        ConfigManager.getInstance().add("V31/OF_NS_Empty.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_NS_Empty");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ObjectFactoryNamespace has multiple values.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFNSMultiple() throws Exception {
        ConfigManager.getInstance().add("V31/OF_NS_Multiple.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_NS_Multiple");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ObjectFactoryNamespace is invalid (no such namespace).
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFNSInvalid() throws Exception {
        ConfigManager.getInstance().add("V31/OF_NS_Invalid.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_NS_Invalid");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ScheduledEnableObjectKey is missing.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFKEYMissing() throws Exception {
        ConfigManager.getInstance().add("V31/OF_KEY_Missing.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_KEY_Missing");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ScheduledEnableObjectKey is empty.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFKEYEmpty() throws Exception {
        ConfigManager.getInstance().add("V31/OF_KEY_Empty.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_KEY_Empty");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ScheduledEnableObjectKey has multiple values.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFKEYMultiple() throws Exception {
        ConfigManager.getInstance().add("V31/OF_KEY_Multiple.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_KEY_Multiple");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <P>
     * Failure test for method <code>setRunCommand(String)</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>,
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the ScheduledEnableObjectKey is invalid (no such namespace).
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testSetRunCommandFailureObjectNamespaceOFKEYInvalid() throws Exception {
        ConfigManager.getInstance().add("V31/OF_KEY_Invalid.xml");
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
        try {
            job.setRunCommand("OF_KEY_Invalid");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>getRunningJob()</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_EXTERNAL</code>, so it will return the
     * <code>runningJob</code> field. But if the job is not started yet, it
     * will return <code>null</code>.
     * </p>
     */
    public void testGetRunningJobAccuracyExternalNotStarted() {
        assertNull("Fails to get the running job.", job.getRunningJob());
    }

    /**
     * <p>
     * Accuracy test for method <code>getRunningJob()</code>.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_EXTERNAL</code>, so it will return the
     * <code>runningJob</code> field. It will return an object if it has been
     * started.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetRunningJobAccuracyExternalStarted() throws Exception {
        job.start();
        Thread.sleep(10);
        assertNotNull("Fails to get the running job.", job.getRunningJob());
        job.stop();
    }

    /**
     * <p>
     * Accuracy test for method <code>getRunningJob()</code>.
     * </p>
     * <p>
     * The job type is not <code>JOB_TYPE_EXTERNAL</code>, so it will return
     * the <code>runnableObject</code> field.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetRunningJobAccuracyNotExternal() throws Exception {
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        job.setRunCommand("com.topcoder.util.scheduler.scheduling.MyJob");
        assertTrue("Fails to get the running job.",
                job.getRunningJob() instanceof ScheduledEnable);
    }

    /**
     * <p>
     * Accuracy test for method <code>getAttribute(String)</code>.
     * </p>
     * <p>
     * It tests that the attribute value could be retrieved successfully. And
     * <code>null</code> will be returned if the name is not defined.
     * </p>
     */
    public void testGetAttributeAccuracy() {
        assertEquals("Fails to get the attribute.", "value1", job
                .getAttribute("key1"));
        assertNull("Fails to get the attribute.", job.getAttribute("key"));
    }

    /**
     * <p>
     * Failure test for method <code>getAttribute(String)</code>.
     * </p>
     * <p>
     * It tests that <code>IllegalArgumentException</code> will be thrown if
     * the argument is <code>null</code>.
     * </p>
     */
    public void testGetAttributeFailureNullName() {
        try {
            job.getAttribute(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getAttribute(String)</code>.
     * </p>
     * <p>
     * It tests that <code>IllegalArgumentException</code> will be thrown if
     * the argument is empty string.
     * </p>
     */
    public void testGetAttributeFailureEmptyName() {
        try {
            job.getAttribute(" ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>setAttribute(String, Object)</code>.
     * </p>
     * <p>
     * It tests that the attribute could be set successfully.
     * </p>
     */
    public void testSetAttributeAccuracy() {
        job.setAttribute("key", "value");
        assertEquals("Fails to set attribute.", "value", attributes.get("key"));
    }

    /**
     * <p>
     * Accuracy test for method <code>setAttribute(String, Object)</code>.
     * </p>
     * <p>
     * It tests that the new attribute will replace the old one if they have the
     * same name.
     * </p>
     */
    public void testSetAttributeAccuracyNameExists() {
        job.setAttribute("key1", "value");
        assertEquals("Fails to set attribute.", "value", attributes.get("key1"));
    }

    /**
     * <p>
     * Failure test for method <code>setAttribute(String, Object)</code>.
     * </p>
     * <p>
     * It tests that <code>IllegalArgumentException</code> will be thrown if
     * the <code>name</code> argument is <code>null</code>.
     * </p>
     */
    public void testSetAttributeFailureNullName() {
        try {
            job.setAttribute(null, "value");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>setAttribute(String, Object)</code>.
     * </p>
     * <p>
     * It tests that <code>IllegalArgumentException</code> will be thrown if
     * the <code>name</code> argument is empty string.
     * </p>
     */
    public void testSetAttributeFailureEmptyName() {
        try {
            job.setAttribute("  ", "value");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>setAttribute(String, Object)</code>.
     * </p>
     * <p>
     * It tests that <code>IllegalArgumentException</code> will be thrown if
     * the <code>value</code> argument is <code>null</code>.
     * </p>
     */
    public void testSetAttributeFailureNullValue() {
        try {
            job.setAttribute("key", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>getAttributeNames()</code>.
     * </p>
     * <p>
     * It tests that the attribute names array could be returned successfully.
     * </p>
     */
    public void testGetAttributeNamesAccuracy() {
        String[] names = job.getAttributeNames();
        assertEquals("Fails to get the attribute names.", 2, names.length);
        for (int i = 0; i < names.length; i++) {
            assertTrue("Fails to get the attribute names.", names[i]
                    .equals("key1")
                    || names[i].equals("key2"));
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>getAttributeNames()</code>.
     * </p>
     * <p>
     * It tests that an empty array will be returned if there is no attribute.
     * </p>
     */
    public void testGetAttributeNamesAccuracyEmptyArray() {
        attributes.clear();
        String[] names = job.getAttributeNames();
        assertEquals("Fails to get the attribute names.", 0, names.length);
    }

    /**
     * <p>
     * Accuracy test for method <code>removeAttribute(String)</code>.
     * </p>
     * <p>
     * It tests that the attribute could be removed successfully.
     * </p>
     */
    public void testRemoveAttributeAccuracy() {
        job.removeAttribute("key1");
        assertEquals("Fails to remove attribute.", 1, attributes.size());
        job.removeAttribute("key2");
        assertEquals("Fails to remove attribute.", 0, attributes.size());
    }

    /**
     * <p>
     * Accuracy test for method <code>removeAttribute(String)</code>.
     * </p>
     * <p>
     * It tests that nothing will happen if the attribute does not exist.
     * </p>
     */
    public void testRemoveAttributeAccuracyNameNotExist() {
        job.removeAttribute("key");
        assertEquals("Fails to remove attribute.", 2, attributes.size());
    }

    /**
     * <p>
     * Failure test for method <code>removeAttribute(String)</code>.
     * </p>
     * <p>
     * It tests that <code>IllegalArgumentException</code> will be thrown if
     * the <code>name</code> argument is <code>null</code>.
     * </p>
     */
    public void testRemoveAttributeFailureNullName() {
        try {
            job.removeAttribute(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>removeAttribute(String)</code>.
     * </p>
     * <p>
     * It tests that <code>IllegalArgumentException</code> will be thrown if
     * the <code>name</code> argument is empty string.
     * </p>
     */
    public void testRemoveAttributeFailureEmptyName() {
        try {
            job.removeAttribute("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>clearAttributes()</code>.
     * </p>
     * <p>
     * It tests that all the attributes could be cleared successfully.
     * </p>
     */
    public void testClearAttributesAccuracy() {
        job.clearAttributes();
        assertEquals("Fails to clear the attribuets.", 0, attributes.size());
    }
}
