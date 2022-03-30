/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for deleting submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DeleteSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 6044848683009571004L;

    /**
     * Creates a new instance of the <code>DeleteSubmissionAction</code> class.
     */
    public DeleteSubmissionAction() {
    }

    /**
     * This method is an implementation of &quot;Delete Submission&quot; Struts Action defined for
     * this assembly, which is supposed to delete (mark as deleted) submission for particular upload
     * (denoted by <code>uid</code> parameter). This action gets executed twice &#x96; once to
     * display the page with the confirmation, and once to process the confirmed delete request to
     * actually delete the submission.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmDeleteSubmission.jsp
     *         page, which displays the confirmation dialog where user can confirm his intention to
     *         remove the submission. If this action was called during the post back (the second
     *         time), then this method verifies if everything is correct, and marks submission and
     *         its current active upload as deleted. After this it returns a forward to the View
     *         Project Details page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);
        // TODO: Delete this method and the whole action.
        throw new BaseException("Not supported anymore");
    }
}

