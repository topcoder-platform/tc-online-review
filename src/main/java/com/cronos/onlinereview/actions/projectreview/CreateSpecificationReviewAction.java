/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to create the specification review.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class CreateSpecificationReviewAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 2226114329366068859L;

    /**
     * This method is an implementation of &quot;Create Specification Review&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editSpecificationReview page, which will fill the required fields and post them to the
     * &quot;Save Specification Review&quot; action. The action implemented by this method is executed to edit
     * specification review that does not exist yet, and hence is supposed to be created.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/editSpecificationReview.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent submission id, or the lack of
     *         permissions, etc.).
     * @throws BaseException if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);
        return createGenericReview(request, "Specification Review");
    }
}

