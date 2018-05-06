/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template;

import com.cronos.onlinereview.ajax.TestHelper;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.MockPhase;
import com.topcoder.project.phases.MockPhaseType;
import com.topcoder.project.phases.MockProject;
import com.topcoder.project.phases.Project;

import java.util.Date;

/**
 * Mock implementation of <code>PhaseTemplate</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockPhaseTemplate implements PhaseTemplate {

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * Apply template.
     * @param template the template
     * @param startDate the start date.
     * @return the project
     */
    public Project applyTemplate(String template, Date startDate) throws PhaseTemplateException {
        if (MockPhaseTemplate.globalException != null) {
            if (MockPhaseTemplate.globalException instanceof PhaseTemplateException) {
                throw (PhaseTemplateException) MockPhaseTemplate.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockPhaseTemplate.globalException);
            }
        }
        Project project = new MockProject(new Date(), new DefaultWorkdays());
        project.setId(4);
        project.setStartDate(startDate);

        MockPhase phase = new MockPhase();
        phase.setFixedStartDate(TestHelper.DATE_1);
        phase.calcStartDate = TestHelper.DATE_2;
        phase.calcEndDate = TestHelper.DATE_2;
        phase.setLength(1000);
        phase.setId(33);

        phase.setPhaseType(new MockPhaseType(0, "name"));

        project.addPhase(phase);

        return project;
    }

    /**
     * Apply template.
     * @param template the template
     * @return the project
     */
    public Project applyTemplate(String template) throws PhaseTemplateException {
        if (MockPhaseTemplate.globalException != null) {
            if (MockPhaseTemplate.globalException instanceof PhaseTemplateException) {
                throw (PhaseTemplateException) MockPhaseTemplate.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockPhaseTemplate.globalException);
            }
        }
        Project project = new MockProject(new Date(), new DefaultWorkdays());
        project.setId(4);

        MockPhase phase = new MockPhase();
        phase.setFixedStartDate(TestHelper.DATE_1);
        phase.calcStartDate = TestHelper.DATE_2;
        phase.calcEndDate = TestHelper.DATE_2;
        phase.setLength(1000);
        phase.setId(33);

        phase.setPhaseType(new MockPhaseType(1, "name"));

        project.addPhase(phase);

        return project;
    }

    /**
     * Get names for all the template.
     * @return all the template names
     */
    public String[] getAllTemplateNames() {
        return new String[] {"Screening", "Review", "Appeal", "Appeal Response"};
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockPhaseTemplate.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockLog</code> so all collected method arguments, configured method results and
     * exceptions are lost.</p>
     */
    public static void releaseState() {
        MockPhaseTemplate.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockPhaseTemplate</code> class.</p>
     */
    public static void init() {
    }
}
