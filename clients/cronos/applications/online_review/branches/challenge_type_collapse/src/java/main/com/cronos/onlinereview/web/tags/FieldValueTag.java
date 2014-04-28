/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.web.tags;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


/**
 * This is the field tag implementation which is used to render the customized field tag.
 * <p>
 * This class is used in thread-safe way.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class FieldValueTag extends TagSupport {
    /**
     * Represents the default serial version id.
     */
    private static final long serialVersionUID = -3993265649490067423L;

    /**
    * Represents the field.
    */
    private String field;

    /**
    * Represents the default value.
    */
    private String def;

    /**
     * Render the value when the tag start.
     *
     * It get the property value of the current action model. The property is specified by the "field" and "def".
     * @return SKIP_BODY
     * @throws JspException if any error
     */
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            String text = "";

            Object value = null;

            value = getFieldValue(field);

            if (value != null) {
                text = value.toString();
            }

            out.print(text);
        } catch (IOException e) {
            throw new JspException("IO error while creating the output", e);
        }

        return SKIP_BODY;
    }

    /**
     * This method gets the property value of the current action model.
     * The property name is specified by the field name.
     *
     * @param name the field name
     * @return the field value
     * @throws JspException if any error
     */
    protected Object getFieldValue(String name) throws JspException {
        Object value = null;

        // / if the action is model driven:
        Object action = ActionContext.getContext().getActionInvocation()
                                     .getAction();

        Object bean = action;

        if (action instanceof ModelDriven) {
            bean = ((ModelDriven<?>) action).getModel();
        }

        try {
            value = PropertyUtils.getProperty(bean, name);
            if (value == null) {
                value = def;
            }
        } catch (IllegalAccessException e) {
            throw new JspException("Illegal access to the bean", e);
        } catch (InvocationTargetException e) {
            throw new JspException(e);
        } catch (NoSuchMethodException e) {
            // ignore
        }

        return value;
    }

    /**
     * Setter of field.
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Setter of default value.
     * @param def the default value to set
     */
    public void setDef(String def) {
        this.def = def;
    }
}
