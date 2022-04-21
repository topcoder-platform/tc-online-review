/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.web.tags;

import com.opensymphony.xwork2.ActionContext;

import org.apache.struts2.util.TextProviderHelper;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * This is the text tag implementation which is used to render the customized text tag.
 * <p>
 * This class is used in thread-safe way.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class TextTag extends TagSupport {
    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 5010906553861492311L;

    /** Represents the key. */
    private String key;

    /** Represents the arg0. */
    private String arg0;

    /** Represents the arg1. */
    private String arg1;

    /** Represents the arg2. */
    private String arg2;

    /** Represents the arg3. */
    private String arg3;

    /** Represents the arg4. */
    private String arg4;

    /** Represents the default value. */
    private String def;

    /**
     * Render the localized message.
     * @return SKIP_BODY
     * @throws JspException if any error
     */
    public int doStartTag() throws JspException {
        List<Object> args = new ArrayList<Object>();

        if (arg0 != null) {
            args.add(arg0);
        }

        if (arg1 != null) {
            args.add(arg1);
        }

        if (arg2 != null) {
            args.add(arg2);
        }

        if (arg3 != null) {
            args.add(arg3);
        }

        if (arg4 != null) {
            args.add(arg4);
        }

        String text = TextProviderHelper.getText(key, (def == null) ? key : def,
                args, ActionContext.getContext().getValueStack());

        JspWriter out = pageContext.getOut();

        try {
            out.print(text);
        } catch (IOException e) {
            throw new JspException("error printing text", e);
        }

        return SKIP_BODY;
    }

    /**
     * Setter of key.
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Setter of arg0.
     * @param arg0 the arg0 to set
     */
    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    /**
     * Setter of arg1.
     * @param arg1 the arg1 to set
     */
    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    /**
     * Setter of arg2.
     * @param arg2 the arg2 to set
     */
    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    /**
     * Setter of arg3.
     * @param arg3 the arg3 to set
     */
    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    /**
     * Setter of arg4.
     * @param arg4 the arg4 to set
     */
    public void setArg4(String arg4) {
        this.arg4 = arg4;
    }

    /**
     * Getter of def.
     * @return the def
     */
    public String getDef() {
        return def;
    }

    /**
     * Setter of def.
     * @param def the def to set
     */
    public void setDef(String def) {
        this.def = def;
    }
}
