/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to view the checkpoint review.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewCheckpointReviewAction extends BaseViewOrExportGenericReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -5726414987902113382L;

    /**
     * This method is an implementation of &quot;View Checkpoint Review&quot; Struts Action defined for this
     * assembly, which is supposed to view completed screening.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/viewReview.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);
        return viewOrExportGenericReview(request, response, "Checkpoint Review", false);
    }
}

