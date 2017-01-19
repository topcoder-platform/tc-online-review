/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


/**
 * This is the checked tag implementation which is used to render the customized checked tag.
 * <p>
 * This class is used in thread-safe way.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class CheckedTag extends FieldValueTag {
    /** Represents the Constant serialVersionUID. */
    private static final long serialVersionUID = -8580138938981223276L;

    /** Represents the name. */
    private String name;

    /** Represents the value. */
    private String value;

    /**
     * Render the "checked=\"checked\"" if the given value matches the server value for this property name.
     *
     * @return SKIP_BODY
     * @throws JspException if any error
     */
    public int doStartTag() throws JspException {
        String[] values = value.split("\\|");

        Object value = getFieldValue(name);

        boolean matches = false;

        for (String v : values) {
            if ((value != null) && v.equals(String.valueOf(value))) {
                matches = true;

                break;
            }
        }

        if (matches) {
            JspWriter out = pageContext.getOut();

            try {
                out.print("checked=\"checked\"");
            } catch (IOException e) {
                throw new JspException("IO error while creating the output", e);
            }
        }

        return SKIP_BODY;
    }

    /**
     * Setter of name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter of value.
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
