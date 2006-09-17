/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

/**
 * This class defines a phase group bean.
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class PhaseGroup {

    /**
     * This member variable holds the name of this group. This member variable is initialized in the
     * constructor and can be accessed and changed via corresponding get/set methods. The default
     * value of this variable is an empty string.
     *
     * @see #getName()
     * @see #setName(String)
     */
    private String name = "";

    /**
     * This member variable holds the name of application's functionality that should be executed to
     * initially collect all required information for this phase group, and later present it
     * properly to the end user. This member variable is initialized in the constructor and can be
     * accessed and changed via corresponding get/set methods. The default value of this variable is
     * an empty string.
     */
    private String applicationFunction = "";

    /**
     * This member variable holds a reference to the object containing additional info for this
     * group. This object can typically be an array or a list. This member variable is initialized
     * in the constructor and can be accessed and changed via corresponding get/set methods. The
     * default value of this variable is <code>null</code>.
     *
     * @see #getAdditionalInfo()
     * @see #setAdditionalInfo(Object)
     */
    private Object additionalInfo = null;

    /**
     * Constructs a new instance of the <code>PhaseGroup</code> class setting all fields to their
     * default values.
     */
    public PhaseGroup() {

    }

    /**
     * This method returns the name of this group.
     *
     * @return the name of this group.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method sets new name of this group. Name cannot be <code>null</code>, but can be an
     * empty string.
     *
     * @param name
     *            a new name to set.
     * @throws IllegalArgumentException
     *             if parameter <code>name</code> is <code>null</code>.
     */
    public void setName(String name) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(name, "name");

        this.name = name;
    }

    /**
     * This method returns the name of application's functionality that should be executed for this
     * phase group.
     *
     * @return the name of application's functionality.
     */
    public String getAppFunc() {
        return this.applicationFunction;
    }

    /**
     * This method sets new name of application's functionality that should be executed for this
     * phase group.
     *
     * @param appFunc
     *            a new name of application's functionality. This parameter cannot be
     *            <code>null</code>, but can be an empty string.
     * @throws IllegalArgumentException
     *             if <code>appFunc</code> parameter is <code>null</code>.
     */
    public void setAppFunc(String appFunc) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(appFunc, "appFunc");

        this.applicationFunction = appFunc;
    }

    /**
     * This method returns additional info that could have been set for this phase group.
     *
     * @return an object representing additional info.
     */
    public Object getAdditionalInfo() {
        return this.additionalInfo;
    }

    /**
     * This method sets additional info object that can be needed to correctly display information
     * about this phase group.
     *
     * @param additionalInfo
     *            a reference to some object containing additional info. This parameter can be
     *            <code>null</code> if no additional info is needed.
     */
    public void setAdditionalInfo(Object additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
