/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.mockups;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class defines a mock up for real TopCoder's <tc-webtag:handle> tag, which displays members'
 * handles using diffferent color schemas based on the users' rating.
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public final class MockHandleTag extends TagSupport {

    /**
     * This member variable is added here to eliminate annoying warning.
     */
    private static final long serialVersionUID = -2976400093393053817L;

    /**
     * This member variable holds the value assigned to <code>coderId</code> attribute.
     */
    private String coderId = null;

    /**
     * This member variable holds the value assigned to <code>context</code> attribute.
     */
    private String context = null;

    /**
     * This method is used to retrieve the value of the <code>codeId</code> attribute of a tag.
     *
     * @return current value of the <code>codeId</code> attribute.
     */
    public String getCoderId() {
        return this.coderId;
    }

    /**
     * This method is used to set <code>codeId</code> attribute of a tag.
     *
     * @param coderId
     *                new coderId to be set.
     */
    public void setCoderId(String coderId) {
        this.coderId = coderId;
    }

    /**
     * This method is used to retrieve the value of the <code>context</code> attribute of a tag.
     *
     * @return current value of the <code>context</code> attribute.
     */
    public String getContext() {
        return this.context;
    }

    /**
     * This method is used to set <code>context</code> attribute of a tag.
     *
     * @param context
     *                new context to be set.
     */
    public void setContext(String context) {
        this.context = context;
    }


    /**
     * This method processes the start tag for this instance.  It is invoked by the JSP page
     * implementation object.  Actually, it does nothing and simple returns <code>SKIP_BODY</code>
     * value, as it does not contain any body and all actual output will be done by the
     * <code>doEndTag</code> method.
     *
     * @exception JspException
     *                if a JSP exception has occurred.
     * @see #doEndTag
     */
    public int doStartTag() throws JspException {
	   return (SKIP_BODY);
    }

    /**
     * This method processes the end tag for this instance.  It does all the output based on the
     * value of the <code>coderId</code> attribute and sometimes <code>context</code> one.
     *
     * @return EVAL_PAGE to contininue processing of the page.
     * @exception JspException
     *                if a JSP exception has occurred.
     */
    public int doEndTag() throws JspException {
        // Verify correctness of the attributes
        if (coderId == null || coderId.trim().length() == 0) {
            throw new JspException("coderId should be specified and non-empty.");
        }
        if (context == null || context.trim().length() == 0) {
            context = "component";
        }
        if (!context.equals("design") && !context.equals("development") && !context.equals("component")) {
            throw new JspException("context should be either 'design', 'development', or 'component'.");
        }

        String tab = "";
        String textColor = "coderTextBlack";
        String coderName = "unknownCoder";

        // Retrieve pseudo-cache
        Map userCache = createExtUserCache(pageContext.getSession());
        // Try to retrieve user's object from cache
        ExternalUser user = (ExternalUser) userCache.get(coderId);

        // Determine the actual color based on the coderId and context attributes
        try {
            // If the object for this user has not been cached yet, ...
            if (user == null) {
                // ... obtain an information from database
                UserRetrieval ur = new DBUserRetrieval("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
                user = ur.retrieveUser(Long.parseLong(coderId));
                // If the user does not exist exit from current try block
                if (user == null) {
                    throw new BaseException("no such user");
                }
                // Store the external user in the cache for later use
                userCache.put(coderId, user);
            }

            coderName = user.getHandle();

            String ratingDesStr = user.getDesignRating();
            String ratingDevStr = user.getDevRating();
            int ratingDes = (ratingDesStr != null && ratingDesStr.trim().length() != 0 &&
                    !ratingDesStr.equalsIgnoreCase("N/A")) ? Integer.parseInt(ratingDesStr) : 0;
            int ratingDev = (ratingDevStr != null && ratingDevStr.trim().length() != 0 &&
                    !ratingDevStr.equalsIgnoreCase("N/A")) ? Integer.parseInt(ratingDevStr) : 0;
            int rating = 0;

            if (context.equalsIgnoreCase("design")) {
                rating = ratingDes;
                tab = "des";
            } else if (context.equalsIgnoreCase("development")) {
                rating = ratingDev;
                tab = "dev";
            } else {
                rating = (ratingDes > ratingDev) ? ratingDes : ratingDev;
            }

            if (rating > 0 && rating < 900) {
                textColor = "coderTextGray";
            } else if (rating >= 900 && rating < 1200) {
                textColor = "coderTextGreen";
            } else if (rating >= 1200 && rating < 1500) {
                textColor = "coderTextBlue";
            } else if (rating >= 1500 && rating < 2200) {
                textColor = "coderTextYellow";
            } else if (rating >= 2200) {
                textColor = "coderTextRed";
            }
        } catch (BaseException e) {
            // eat it as this is only a mockup
        }

        // Handle some special situations
        if (coderId.equals("1000") || // Coder "admin"
            coderId.equals("100142") || // Coder "mike"
            coderId.equals("305384") || // Coder "mess"
            coderId.equals("107160") || // Coder "uler3161"
            coderId.equals("278287") || // Coder "moffitt"
            coderId.equals("160049") || // Coder "FogleBird"
            coderId.equals("156859") || // Coder "ivern"
            coderId.equals("8547899")) { // Coder "TonyJ"
            textColor = "coderTextOrange";
        }

        // Start preparing resulting output
        StringBuffer results = new StringBuffer("<a href=\"http://www.topcoder.com/tc?module=MemberProfile&cr=");
        // appending coder's id to the generated link
        results.append(coderId);
        // tab will be specified only if tab local variable is not empty
        if (tab.length() != 0) {
            results.append("&tab=");
            results.append(tab);
        }

        // append other parameters: the color of a handle, handle's text, and closing </a> tag
        results.append("\" class=\"");
        results.append(textColor);
        results.append("\">");
        results.append(coderName);
        results.append("</a>");

        // Write prepared output to the output stream
    	JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        } catch (IOException ioe) {
            throw new JspException("error while writing to page's output stream", ioe);
        }

        // Let JSP container continue processing the page
        return (EVAL_PAGE);
    }

    /**
     * This method releases any acquired resources.
     */
    public void release() {
        super.release();
        this.coderId = null;
        this.context = null;
    }

    /**
     * This static method retrieves map object from session (if it was previously stored there).
     * This retrieved map acts as a cache to store <code>ExternalUser</code> objects.
     *
     * @return a map retrieved from session or just-created.
     * @param session
     *            an <code>HttpSession</code> object.
     */
    private static Map createExtUserCache(HttpSession session) {
        Map cache = (Map) session.getAttribute("extUserCache");
        if (cache == null) {
            cache = new HashMap();
            session.setAttribute("extUserCache", cache);
        }
        return cache;
    }
}
