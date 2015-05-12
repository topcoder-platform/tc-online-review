/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import com.cronos.onlinereview.Constants;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for uploading contest submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UploadContestSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 4979856709151493843L;

    /**
     * Creates a new instance of the <code>UploadContestSubmissionAction</code> class.
     */
    public UploadContestSubmissionAction() {
    }

    /**
     * This method is an implementation of &quot;Upload Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user upload his submission to the server. This
     * action gets executed twice &#x96; once to display the page with the form, and once to process
     * the uploaded file.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to uploadSubmission.jsp page, which
     *         displays the form where user can specify the file he/she wants to upload. If this
     *         action was called during the post back (the second time), then the request should
     *         contain the file uploaded by user. In this case, this method verifies if everything
     *         is correct, stores the file on file server and returns a forward to the View Project
     *         Details page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        return handleUploadSubmission(getModel(), request, Constants.CONTEST_SUBMISSION_TYPE_NAME,
                                          Constants.PERFORM_SUBM_PERM_NAME, Constants.SUBMISSION_PHASE_NAME);
    }
}

