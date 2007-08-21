package com.topcoder.onlinereview.fixer;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * Runtime Exception to indicate runtime failure of OnlineReviewScoreRankFixer.
 * 
 * @author hohosky
 */
public class OnlineReviewScoreRankFixerException extends BaseRuntimeException {
    
    /**
     * Constructor with error message
     *  
     * @param msg the error message.
     */
    public OnlineReviewScoreRankFixerException(String msg) {
        super(msg);
    }
    
    /**
     * Constructor with error message and underlying cause
     * 
     * @param msg the error message.
     * @param cause the underlying cause.
     */
    public OnlineReviewScoreRankFixerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
