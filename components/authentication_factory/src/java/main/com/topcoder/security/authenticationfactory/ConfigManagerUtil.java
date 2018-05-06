/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.security.authenticationfactory;

import java.util.List;

import com.topcoder.util.config.Property;

/**
 * <p>
 * Package private class.
 * Here defined some help functions when load config from <code>ConfigManager</code>.
 * </p>
 * @author TCSDEVELOPER
 * @version 2.0
 */
class ConfigManagerUtil {
    /**
     * Empty constructor.
     */
    private ConfigManagerUtil() {
        // just empty
    }

    /**
     * <p>
     * Create object from <tt>property</tt> which has the &quot;class&quot; and &quot;namespace&quot;
     * property.
     * </p>
     * @param property the <code>Property</code> contains &quot;class&quot; and &quot;namespace&quot;
     *        property.
     * @return the created object
     * @throws NullPointerException if property is null.
     * @throws ConfigurationException if any required properties is missing, or create object failed.
     */
    static Object createInstance(Property property) throws ConfigurationException {
        if (property == null) {
            throw new NullPointerException("property is null");
        }
        List subProperties = property.list();
        String cs = null; // the class name
        String ns = null; // the namespace

        // get the value of "class" and "namespace"
        for (int j = 0; j < subProperties.size(); ++j) {
            Property sub = (Property) subProperties.get(j);
            String subName = sub.getName();
            // get the class name and namespace
            if (subName.equals("class")) {
                cs = sub.getValue();
            } else if (subName.equals("namespace")) {
                ns = sub.getValue();
            }
        }
        // the required value is missing
        if (cs == null || ns == null) {
            throw new ConfigurationException("required properties is missing");
        }

        try {
            return Class.forName(cs).getConstructor(new Class[] {String.class}).newInstance(new Object[] {ns});
        } catch (Exception e) {
            throw new ConfigurationException("load config failed, please refer to inner "
                    + "exception for detail information", e);
        }
    }
}
