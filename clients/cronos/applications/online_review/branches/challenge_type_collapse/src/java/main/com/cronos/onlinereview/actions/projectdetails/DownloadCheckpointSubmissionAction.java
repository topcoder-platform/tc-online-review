/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import java.io.IOException;

import com.cronos.onlinereview.Constants;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for downloading checkpoint submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DownloadCheckpointSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -705809070877866541L;

    /**
     * Creates a new instance of the <code>DownloadCheckpointSubmissionAction</code> class.
     */
    public DownloadCheckpointSubmissionAction() {
    }

    /**
     * This method is an implementation of &quot;Download Checkpoint Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a checkpoint submission from the server.
     *
     * @return a <code>null</code> code if everything went fine, or an error forward to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @throws BaseException if any error occurs.
     * @throws IOException if some error occurs during disk input/output operation.
     */
    public String execute() throws BaseException, IOException {
        return handleDownloadSubmission(request, response, "ViewCheckpointSubmission",
                Constants.VIEW_ALL_CHECKPOINT_SUBMISSIONS_PERM_NAME,
                Constants.VIEW_MY_CHECKPOINT_SUBMISSIONS_PERM_NAME,
                Constants.VIEW_SCREENER_CHECKPOINT_SUBMISSION_PERM_NAME,
                Constants.VIEW_RECENT_CHECKPOINT_SUBMISSIONS_PERM_NAME,
                Constants.DOWNLOAD_CUSTOM_CHECKPOINT_SUBMISSION_PERM_NAME,
                Constants.VIEW_WINNING_CHECKPOINT_SUBMISSION_PERM_NAME,
                Constants.CHECKPOINT_SCREENING_PHASE_NAME,
                Constants.CHECKPOINT_REVIEW_PHASE_NAME,
                new String[] {Constants.CHECKPOINT_SCREENER_ROLE_NAME},
                new String[] {Constants.CHECKPOINT_REVIEWER_ROLE_NAME}, 3);
    }
}

