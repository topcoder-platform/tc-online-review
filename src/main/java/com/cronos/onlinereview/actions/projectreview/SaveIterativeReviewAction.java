/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to save the iterative review.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveIterativeReviewAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -5234881142377007638L;

    /**
     * This method is an implementation of &quot;Save Iterative Review&quot; Struts Action defined
     * for this assembly, which is supposed to save information posted from /jsp/editReview.jsp
     * page. This method will either create new review or update (edit) an existing one depending
     * on which action was called to display /jsp/editReview.jsp page.
     *
     * @return &quot;success&quot;, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent submission id, or the lack of permissions, etc.).
     * @throws BaseException
     *         if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);
        return saveGenericReview(getModel(), request, "Iterative Review");
    }
}

