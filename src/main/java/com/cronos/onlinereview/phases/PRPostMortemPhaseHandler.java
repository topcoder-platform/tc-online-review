/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.EmailOptions;
import com.topcoder.onlinereview.component.project.phase.handler.EmailScheme;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;
import com.topcoder.onlinereview.component.project.phase.handler.PostMortemPhaseHandler;

import java.util.List;

/**
 * The extend from PostMortemPhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRPostMortemPhaseHandler extends PostMortemPhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();
    
    /**
     * Create a new instance of PRPostMortemPhaseHandler using the given namespace for loading configuration settings.
     *
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRPostMortemPhaseHandler(ManagerHelper managerHelper,
                                    List<EmailScheme> emailSchemes,
                                    EmailScheme reviewFeedbackEmailScheme,
                                    EmailOptions defaultStartEmailOption,
                                    EmailOptions defaultEndEmailOption) {
        super(managerHelper, emailSchemes, reviewFeedbackEmailScheme, defaultStartEmailOption, defaultEndEmailOption);
    }

    /**
     * Provides additional logic to execute a phase. this extension will update placed, final_score
     * field of project_result table.</p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        prHelper.processPostMortemPR(phase.getProject().getId(), toStart, operator);
    }
}
