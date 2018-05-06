/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.accuracytests;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.PersistenceException;
import com.topcoder.project.phases.template.PhaseGenerationException;
import com.topcoder.project.phases.template.PhaseTemplatePersistence;

import java.util.Date;


/**
 * mock class only for testing purpose.
 *
 * @author FireIce
 * @version 1.2
 *
 * @since 1.0
 */
public class MockPhaseTemplatePersistence implements PhaseTemplatePersistence {
    /**
     * default no-arg constructor.
     */
    public MockPhaseTemplatePersistence() {
    }

    /**
     * constructor which use file to config.
     */
    public MockPhaseTemplatePersistence(String namespace) {
    }

    /**
     * mock method.
     *
     * @param templateName just mock
     * @param project just mock
     * @param leftOutPhaseIds just mock
     */
    public void generatePhases(String templateName, Project project, long[] leftOutPhaseIds)
        throws PhaseGenerationException, PersistenceException {
        int count = "FireIce".equals(templateName) ? 5 : 6;

        for (int i = 0; i < count; i++) {
            Phase phase = new Phase(project, 1000);
            phase.setId(i + 1);
            project.addPhase(phase);
        }
    }

    /**
     * mock method.
     *
     * @return just mock
     */
    public String[] getAllTemplateNames() {
        return new String[0];
    }

    /**
     * just mock
     *
     * @param category just mock
     *
     * @return just mock
     *
     * @throws PersistenceException just mock
     */
    public String[] getAllTemplateNames(int category) throws PersistenceException {
        return null;
    }

    /**
     * just mock
     *
     * @param category just mock
     *
     * @return just mock
     *
     * @throws PersistenceException just mock
     */
    public String getDefaultTemplateName(int category)
        throws PersistenceException {
        return null;
    }

    /**
     * just mock
     *
     * @param templateName just mock
     *
     * @return just mock
     *
     * @throws PersistenceException just mock
     */
    public int getTemplateCategory(String templateName)
        throws PersistenceException {
        return 0;
    }

    /**
     * just mock
     *
     * @param templateName just mock
     *
     * @return just mock
     *
     * @throws PersistenceException just mock
     */
    public Date getTemplateCreationDate(String templateName)
        throws PersistenceException {
        return null;
    }

    /**
     * just mock
     *
     * @param templateName just mock
     *
     * @return just mock
     *
     * @throws PersistenceException just mock
     */
    public String getTemplateDescription(String templateName)
        throws PersistenceException {
        return null;
    }
}
