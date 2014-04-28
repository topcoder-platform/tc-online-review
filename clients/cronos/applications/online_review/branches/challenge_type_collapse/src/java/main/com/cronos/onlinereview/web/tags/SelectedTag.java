/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.web.tags;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * This is the select tag implementation which is used to render the customized select tag.
 * <p>
 * This class is used in thread-safe way.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SelectedTag extends TagSupport {
    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1992663198073218419L;

    /** Represents the value. */
    private String value;

    /**
     * Render the "selected=\"selected\"" if the given value matches one of the current values for the select tag.
     * @return SKIP_BODY
     * @throws JspException if any error
     */
    public int doStartTag() throws JspException {
        Object fieldName = pageContext.getAttribute("OR_FIELD_TO_SELECT");

        if (fieldName == null) {
            return SKIP_BODY;
        }

        String[] values = getArrayValues((String) fieldName);

        boolean matches = false;

        for (String v : values) {
            if (v.equals(value)) {
                matches = true;

                break;
            }
        }

        if (matches) {
            JspWriter out = pageContext.getOut();

            try {
                out.print("selected=\"selected\"");
            } catch (IOException e) {
                throw new JspException("IO error while creating the output", e);
            }
        }

        return SKIP_BODY;
    }

    /**
     * This method gets the String array property value - specified by the name - of the current action model.
     *
     * @param name the name
     * @return the array values from the given name
     * @throws JspException if any error
     */
    public String[] getArrayValues(String name) throws JspException {
        String[] values = null;

        Object action = ActionContext.getContext().getActionInvocation()
                                     .getAction();

        Object bean = action;

        if (action instanceof ModelDriven) {
            bean = ((ModelDriven<?>) action).getModel();
        }

        try {
            values = BeanUtils.getArrayProperty(bean, name);
        } catch (IllegalAccessException e) {
            throw new JspException("Illegal access to the bean", e);
        } catch (InvocationTargetException e) {
            throw new JspException(e);
        } catch (NoSuchMethodException e) {
            // /
            // / Don't throw exception
        }

        if (values == null) {
            values = new String[0];
        }

        return values;
    }

    /**
     * Setter of value.
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
