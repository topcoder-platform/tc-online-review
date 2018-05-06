/*
 * Copyright (C) 2007, 2008-2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.failuretests;

import java.util.Date;

import junit.framework.TestCase;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.ConfigurationException;
import com.topcoder.project.phases.template.PersistenceException;
import com.topcoder.project.phases.template.PhaseGenerationException;
import com.topcoder.project.phases.template.PhaseTemplatePersistence;
import com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence;

/**
 * <p>
 * Failure tests on class <code>XmlPhaseTemplatePersistence</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.2
 */
public class XmlPhaseTemplatePersistenceFailureTest extends TestCase {
    /**
     * <p>
     * The namespace.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence";

    /**
     * The template name.
     */
    private String templateName = "TCS Component Project 1";

    /**
     * The project.
     */
    private Project project = new Project(new Date(System.currentTimeMillis()),
            new DefaultWorkdays());

    /**
     * The persistence used in test.
     */
    private PhaseTemplatePersistence phaseTemplatePersistence;

    /**
     * Set up for each test.
     * @throws Exception to jUnit
     */
    protected void setUp() throws Exception {
        FailureTestsHelper.loadAllConfig();
        phaseTemplatePersistence = new XmlPhaseTemplatePersistence(
                new String[] { "test_files/failuretests/template_file1.xml" });
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringNamespaceNull()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence((String) null);
            fail("IllegalArgumentException must be thrown since namespace is null");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringNamespaceEmpty()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence("");
            fail("IllegalArgumentException must be thrown since namespace is empty");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringNamespaceEmptyTrimmed()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence("   ");
            fail("IllegalArgumentException must be thrown since namespace is empty trimmed");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringTemplateFilesNotExist() {
        try {
            new XmlPhaseTemplatePersistence(NAMESPACE + 1);
            fail("ConfigurationException must be thrown since template_files property is not defined in configuration");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringFileNotFound() {
        try {
            new XmlPhaseTemplatePersistence(NAMESPACE + 2);
            fail("ConfigurationException must be thrown since template file is not found");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringNameEmpty() {
        try {
            new XmlPhaseTemplatePersistence(NAMESPACE + 3);
            fail("ConfigurationException must be thrown since name attribute is empty");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringNameEmptyTrimmed() {
        try {
            new XmlPhaseTemplatePersistence(NAMESPACE + 4);
            fail("ConfigurationException must be thrown since name attribute is empty trimmed");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringWrongXML() {
        try {
            new XmlPhaseTemplatePersistence(NAMESPACE + 5);
            fail("ConfigurationException must be thrown since xml is wrong");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String[])'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayNull()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence((String[]) null);
            fail("IllegalArgumentException must be thrown since fileNames is null");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayFileNotFound() {
        try {
            new XmlPhaseTemplatePersistence(
                    new String[] { "test_files/failuretests/template_file_wrong.xml" });
            fail("PersistenceException must be thrown since template file is not found");
        } catch (PersistenceException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayNameEmpty() {
        try {
            new XmlPhaseTemplatePersistence(
                    new String[] { "test_files/failuretests/template_file4.xml" });
            fail("PersistenceException must be thrown since name attribute is empty");
        } catch (PersistenceException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayNameEmptyTrimmed() {
        try {
            new XmlPhaseTemplatePersistence(
                    new String[] { "test_files/failuretests/template_file5.xml" });
            fail("PersistenceException must be thrown since name attribute is empty trimmed");
        } catch (PersistenceException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String)'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayWrongXML() {
        try {
            new XmlPhaseTemplatePersistence(
                    new String[] { "test_files/failuretests/template_file6.xml" });
            fail("PersistenceException must be thrown since xml is wrong");
        } catch (PersistenceException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String[])'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayEmpty()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence(new String[0]);
            fail("IllegalArgumentException must be thrown since fileNames is empty");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String[])'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayInnerNull()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence(new String[] {
                    "test_files/failuretests/template_file1.xml", null,
                    "test_files/failuretests/template_file2.xml" });
            fail("IllegalArgumentException must be thrown since inner values of fileNames is null");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String[])'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayInnerEmpty()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence(new String[] {
                    "test_files/failuretests/template_file1.xml", "",
                    "test_files/failuretests/template_file2.xml" });
            fail("IllegalArgumentException must be thrown since inner values of fileNames is empty");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.XmlPhaseTemplatePersistence(String[])'
     */
    public final void testXmlPhaseTemplatePersistenceStringArrayInnerEmptyTrimmed()
            throws Exception {
        try {
            new XmlPhaseTemplatePersistence(new String[] {
                    "test_files/failuretests/template_file1.xml", "   ",
                    "test_files/failuretests/template_file2..xml" });
            fail("IllegalArgumentException must be thrown since inner values of fileNames is empty trimmed");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.generatePhases(String,
     * Project, long[])'
     */
    public final void testGeneratePhasesTemplateNameNull() throws Exception {
        try {
            phaseTemplatePersistence.generatePhases(null, project, null);
            fail("IllegalArgumentException must be thrown since template name is null");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.generatePhases(String,
     * Project, long[])'
     */
    public final void testGeneratePhasesTemplateNameEmpty() throws Exception {
        try {
            phaseTemplatePersistence.generatePhases("", project, null);
            fail("IllegalArgumentException must be thrown since template name is empty");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.generatePhases(String,
     * Project, long[])'
     */
    public final void testGeneratePhasesTemplateNameEmptyTrimmed()
            throws Exception {
        try {
            phaseTemplatePersistence.generatePhases("   ", project, null);
            fail("IllegalArgumentException must be thrown since template name is empty trimmed");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.generatePhases(String,
     * Project, long[])'
     */
    public final void testGeneratePhasesProjectNull() throws Exception {
        try {
            phaseTemplatePersistence.generatePhases(templateName, null, null);
            fail("IllegalArgumentException must be thrown since template name is null");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.generatePhases(String,
     * Project, long[])'
     */
    public final void testGeneratePhasesTemplateNotFound() throws Exception {
        try {
            phaseTemplatePersistence.generatePhases("TCS Component Project 2",
                    project, null);
            fail("IllegalArgumentException must be thrown since template name is not found");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.generatePhases(String,
     * Project, long[])'
     */
    public final void testGeneratePhasesTemplatePhaseTypeNotFound()
            throws Exception {
        phaseTemplatePersistence = new XmlPhaseTemplatePersistence(
                new String[] { "test_files/failuretests/template_file7.xml" });
        try {
            phaseTemplatePersistence.generatePhases(templateName, project, null);
            fail("PhaseGenerationException must be thrown since phase type is not defined");
        } catch (PhaseGenerationException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence.generatePhases(String,
     * Project, long[])'
     */
    public final void testGeneratePhasesTemplatePhaseNotFound()
            throws Exception {
        phaseTemplatePersistence = new XmlPhaseTemplatePersistence(
                new String[] { "test_files/failuretests/template_file8.xml" });
        try {
            phaseTemplatePersistence.generatePhases(templateName, project, null);
            fail("PhaseGenerationException must be thrown since phase is not defined");
        } catch (PhaseGenerationException e) {
            // good
        }
    }


    /**
     * <p>
     * Test the method <code>generatePhases(String, Project, long[])</code>for failure.
     * </p>
     *
     * <p>
     * It tests when leftOutPhaseIds contains unknown phase id.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGeneratePhases_Failure11() throws Exception {
        long[] leftOutPhaseIds = new long[] { 99901 };
        try {
            phaseTemplatePersistence.generatePhases("New_Design", project, leftOutPhaseIds);
            fail("PhaseGenerationException expected.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * <p>
     * Test the method <code>generatePhases(String, Project, long[])</code>for failure.
     * </p>
     *
     * <p>
     * It tests when leftOutPhaseIds contains duplicated phase id.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGeneratePhases_Failure12() throws Exception {
        long[] leftOutPhaseIds = new long[] { 1, 1 };
        try {
            phaseTemplatePersistence.generatePhases("New_Design", project, leftOutPhaseIds);
            fail("PhaseGenerationException expected.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * Tear down for each test.
     */
    protected void tearDown() throws Exception {
        FailureTestsHelper.clearTestConfig();
    }
}