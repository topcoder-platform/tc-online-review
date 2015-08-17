/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to create the post mortem.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class CreatePostMortemAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -8155023506424980309L;

    /**
     * This method is an implementation of &quot;Create Post-Mortem&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editReview.jsp page, which will fill the required fields and post them to the
     * &quot;Save Post-Mortem&quot; action. The action implemented by this method is executed to edit
     * Post-Mortem that does not exist yet, and hence is supposed to be created.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent submission id, or the lack of
     *         permissions, etc.).
     * @throws BaseException if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);
        return createGenericReview(request, "Post-Mortem");
    }
}

