/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import com.cronos.onlinereview.Constants;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for uploading checkpoint submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UploadCheckpointSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 7392418568484575612L;

    /**
     * Creates a new instance of the <code>UploadCheckpointSubmissionAction</code> class.
     */
    public UploadCheckpointSubmissionAction() {
    }

    /**
     * <p>This method is an implementation of &quot;Upload Checkpoint Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his checkpoint submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.</p>
     * @return a string result of this action
     * @throws BaseException if an unexpected error occurs.
     */
    public String execute() throws BaseException {
        return handleUploadSubmission(getModel(), request, Constants.CHECKPOINT_SUBMISSION_TYPE_NAME,
                Constants.PERFORM_CHECKPOINT_SUBMISSION_PERM_NAME,
                Constants.CHECKPOINT_SUBMISSION_PHASE_NAME);
    }
}

