/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.web.tags;


/**
 * This is the URL tag implementation which is used to render the customized url tag.
 * <p>
 * This class is used in thread-safe way.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class URLTag extends org.apache.struts2.views.jsp.URLTag {
    /**
         * The default serial version UID.
         */
    private static final long serialVersionUID = -8207106898230946625L;

    /**
    * Default constructor.
    *
    * Initialize the tag to instruct the base class not to encode the URL values.
    */
    public URLTag() {
        setEscapeAmp("false");
        setEncode("false");
    }
}
