/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to view the aggregation.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewAggregationAction extends BaseViewOrExportGenericReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 5246807609526823323L;

    /**
     * This method is an implementation of &quot;View Aggregation&quot; Struts Action defined for
     * this assembly, which is supposed to view completed aggregation.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/viewAggregation.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        String actionForward = viewOrExportAggregation(request, response, false);
        if (actionForward != null) {
            return actionForward;
        } else {
            return Constants.SUCCESS_FORWARD_NAME;
        }
    }
}

