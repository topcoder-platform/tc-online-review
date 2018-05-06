/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template;

import java.util.Calendar;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence;

/**
 * The Class UtilVersion12Test.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UtilVersion12Test {

    /**
     * Suite.
     *
     * @return the junit.framework. test
     */
    public static junit.framework.Test suite() {

        return new JUnit4TestAdapter(UtilVersion12Test.class);
    }

    /**
     * Test check array duplicate value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayDuplicateValue() {

        Util.checkArrayDuplicateValue(new long[]{6, 7, 6});
    }

    /**
     * Test check array size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckArraySize() {

        Util.checkArraySize(8, 9);
    }

    /**
     * Test process dependencies.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testProcessDependencies() throws Exception {

        // load configurations
        Helper.loadConfig("test_files/config/Project_Phase_Template_Config.xml");

        // create persistence instances
        XmlPhaseTemplatePersistence persistence = new XmlPhaseTemplatePersistence(
            new String[]{"test_files/new_template_12/new_file.xml"});

        // create needed objects
        Date startDate = Calendar.getInstance().getTime();
        Workdays workdays = new DefaultWorkdays();
        Project project = new Project(startDate, (DefaultWorkdays) workdays);

        // generate phases
        persistence.generatePhases("new_file", project, new long[]{3, 5});
        // check the generated phases
        // Helper.checkSamplePhases();

        Assert.assertEquals("The array size is 5", 2 + 2 + 1, project.getAllPhases().length);

        Helper.assertDependencies(project, 6, 4L, 2L, 1L);

        Helper.clearConfig();
    }

}
