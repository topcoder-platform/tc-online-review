/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import java.io.IOException;

import com.cronos.onlinereview.Constants;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for downloading contest submission.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DownloadContestSubmissionAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 2233592271939211100L;

    /**
     * Creates a new instance of the <code>DownloadContestSubmissionAction</code> class.
     */
    public DownloadContestSubmissionAction() {
    }

    /**
     * This method is an implementation of &quot;Download Submission&quot; Struts Action defined for
     * this assembly, which is supposed to let the user download a submission from the server.
     *
     * @return a <code>null</code> code if everything went fine, or an error string to
     *         /jsp/userError.jsp page which will display the information about the cause of error.
     * @throws BaseException
     *             if any error occurs.
     * @throws IOException
     *             if some error occurs during disk input/output operation.
     */
    public String execute() throws BaseException, IOException {
        return handleDownloadSubmission(request, response, "ViewSubmission",
                Constants.VIEW_ALL_SUBM_PERM_NAME, Constants.VIEW_MY_SUBM_PERM_NAME,
                Constants.VIEW_SCREENER_SUBM_PERM_NAME, Constants.VIEW_RECENT_SUBM_PERM_NAME,
                Constants.DOWNLOAD_CUSTOM_SUBM_PERM_NAME,
                Constants.VIEW_WINNING_SUBM_PERM_NAME,
                Constants.SCREENING_PHASE_NAME, Constants.REVIEW_PHASE_NAME,
                Constants.SCREENER_ROLE_NAMES, Constants.REVIEWER_ROLE_NAMES, 1);
    }
}

