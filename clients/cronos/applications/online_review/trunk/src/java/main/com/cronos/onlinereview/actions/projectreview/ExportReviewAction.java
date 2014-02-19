/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to export the review.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ExportReviewAction extends BaseViewOrExportGenericReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 3264440018656160583L;

    /**
     * This method is supposed to export completed review result into xlsx file.
     * @return always null because the resulting file is expected to be written to response directly.
     * @throws BaseException
     *         if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);
        String reviewType = request.getParameter("reviewType");
        if (reviewType.equals("Aggregation")) {
            return viewOrExportAggregation(request, response, true);
        } else if (reviewType.equals("FinalReview")) {
            return viewOrExportFinalReview(request, response, true);
        } else {
            return viewOrExportGenericReview(request, response, reviewType, true);
        }
    }
}

