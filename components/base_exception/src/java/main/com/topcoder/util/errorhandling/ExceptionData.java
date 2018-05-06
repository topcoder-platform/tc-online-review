/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Container class for all data associated to an exception within this component -
 * such as error/module codes, creation information, a logging flag and
 * unrestricted key/value pairs.
 * </p>
 * <p>
 * When first created, each data instance is filled with default values, and the
 * thread name and creation date are set to their current values. The remaining
 * members all have setXXX methods, which all return the 'this' instance to
 * allow the data to be populated on a single line. Each member also has a
 * getXXX method for access.
 * </p>
 * <p>
 * <b>Thread safety</b>: This class is not thread safe - the get/set methods
 * allow for concurrent access and modification. In their intended usage
 * patterns (within exceptions being thrown on single threads) this is not an
 * issue, but if thread safety is a concern later, it must be provided by a
 * handling class.
 * </p>
 * <p>
 * <b>Serialization</b>: To allow the exceptions to be serializable, this class
 * must also implement the serializable interface. All members are serializable,
 * except the map if it contains key or value objects which cannot be serialized -
 * these should be avoided if serialization is required.
 * </p>
 *
 * @author sql_lall, TCSDEVELOPER
 * @version 2.0
 */
public class ExceptionData implements Serializable {

    /**
     * <p>
     * Stores a collection of (key, value) pairs attached to the exception
     * owning this data - these are maintained as a HashMap mapping key to
     * value, allowing null values for both.
     * </p>
     * <p>
     * The values inside can be changed/access by set/get methods, though the
     * underlying hash map instance doesn't change.
     * </p>
     * <p>
     * It is set on construction to a new hash map, empty, and is filled through
     * calling set methods.
     * </p>
     */
    private final Map information = new HashMap();

    /**
     * <p>
     * Transient flag storing whether the exception owning this has been logged.
     * </p>
     * <p>
     * Initially false, it has related is/set methods to allow access and
     * modification.
     * </p>
     */
    private boolean logged = false;

    /**
     * <p>
     * Member storing the application code for the exception - used to identify
     * the exceptions's application within a system running multiple programs.
     * </p>
     * <p>
     * This is mutable, and has associated get/set methods to allow modification
     * and access.
     * </p>
     * <p>
     * Note that although it can only be set directly, this is not set-able
     * through the exception classes. The intention is that each exception
     * attaches its own application code before passing it to the super's
     * constructor.
     * </p>
     */
    private String applicationCode = null;

    /**
     * <p>
     * Member storing the module code for the exception - used to identify the
     * exceptions's general area within a larger application.
     * </p>
     * <p>
     * This is mutable, and has associated get/set methods to allow modification
     * and access.
     * </p>
     * <p>
     * Note that although it can only be set directly, this is not set-able
     * through the exception classes. The intention is that each exception
     * attaches its own module code before passing it to the super's
     * constructor.
     * </p>
     */
    private String moduleCode = null;

    /**
     * <p>
     * Member storing the error code for the exception - used to identify an
     * exception within an application.
     * </p>
     * <p>
     * This is mutable, and has associated get/set methods to allow modification
     * and access.
     * </p>
     * <p>
     * Note that although it can only be set directly, this is not set-able
     * through the exception classes. The intention is that each exception
     * attaches its own error code before passing it to the super's constructor.
     * </p>
     */
    private String errorCode = null;

    /**
     * <p>
     * Member storing the date/time when the exception was first created.
     * </p>
     * <p>
     * This is initialized on construction to a new <code>Date()</code>, and
     * afterwards is immutable, obtained through calling <code>getCreationDate()</code>.
     * </p>
     */
    private final Date creationDate;

    /**
     * <p>
     * Member storing the name of the thread which the exception was created
     * from.
     * </p>
     * <p>
     * This is initialized on construction to <code>Thread.currentThread().getName()</code>,
     * and afterwards is immutable, obtained through calling <code>getThreadName()</code>.
     * </p>
     */
    private final String threadName;

    /**
     * <p>
     * Empty constructor, initializes a new instance using default values.
     * </p>
     * <p>
     * This can be used with the setXXX methods to only initialize the
     * information needed. e.g.
     * <code>new ExceptionData().setXXX().setXXX()</code>.
     * </p>
     */
    public ExceptionData() {
        // set creationDate to current date
        this.creationDate = new Date();
        // set threadName to the name of the current thread
        this.threadName = Thread.currentThread().getName();
    }

    /**
     * <p>
     * Returns the information related to a given <code>key</code> - if none
     * is found, null should be returned.
     * </p>
     * <p>
     * The key may be null, and is used to look up the value within the
     * information member.
     * </p>
     *
     * @param key The key used to identify which piece of information to return -
     *            may be null.
     * @return The value associated to the key, or null if none is set.
     */
    public Object getInformation(Object key) {
        // get the value associated to the key
        return information.get(key);
    }

    /**
     * <p>
     * Adds a (key/value) pair into the exception data, overwriting any value
     * previously set at that key. This accepts nulls for the key and/or value.
     * </p>
     * <p>
     * This method returns the 'this' object, allowing multiple sets to be
     * performed consecutively.
     * </p>
     *
     * @param key The key used to identify which piece of information to set -
     *            may be null.
     * @param value The new value to associate to the key - may be null.
     * @return this object.
     */
    public ExceptionData setInformation(Object key, Object value) {
        // adds a (key/value) pair into the exception data
        information.put(key, value);
        // return this object after adding the (key/value) pair
        return this;
    }

    /**
     * <p>
     * Returns the current value of the logged member.
     * </p>
     *
     * @return the flag storing whether the exception owning this has been
     *         logged
     */
    public boolean isLogged() {
        return this.logged;
    }

    /**
     * <p>
     * Sets the value of the logged member to the parameter.
     * </p>
     * <p>
     * This method returns the 'this' object, allowing multiple sets to be
     * performed consecutively.
     * </p>
     *
     * @param logged whether the exception has been logged
     * @return this object
     */
    public ExceptionData setLogged(boolean logged) {
        this.logged = logged;
        // return this object after setting the logged flag
        return this;
    }

    /**
     * <p>
     * Returns the application code for the exception data, stored in the member
     * of the same name.
     * </p>
     * <p>
     * This is possibly null, if it hasn't yet been set.
     * </p>
     *
     * @return the application code for the exception data
     */
    public String getApplicationCode() {
        return this.applicationCode;
    }

    /**
     * <p>
     * Sets the value of the application code to the given parameter.
     * </p>
     * <p>
     * This method returns the 'this' object, allowing multiple sets to be
     * performed consecutively.
     * </p>
     *
     * @param code The new application code to use for the exception
     * @return this object
     */
    public ExceptionData setApplicationCode(String code) {
        this.applicationCode = code;
        // return this object after setting application code
        return this;
    }

    /**
     * <p>
     * Returns the module code for the exception data, stored in the member of
     * the same name.
     * </p>
     * <p>
     * This is possibly null, if it hasn't yet been set.
     * </p>
     *
     * @return the module code for the exception data
     */
    public String getModuleCode() {
        return this.moduleCode;
    }

    /**
     * <p>
     * Sets the value of the module code to the given parameter.
     * </p>
     * <p>
     * This method returns the 'this' object, allowing multiple sets to be
     * performed consecutively.
     * </p>
     *
     * @param moduleCode The new module code to use for the exception
     * @return this object
     */
    public ExceptionData setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
        // return this object after setting module code
        return this;
    }

    /**
     * <p>
     * Returns the error code for the exception data, stored in the member of
     * the same name.
     * </p>
     * <p>
     * This is possibly null, if it hasn't yet been set.
     * </p>
     *
     * @return the error code for the exception data
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * <p>
     * Sets the value of the error code to the given parameter.
     * </p>
     * <p>
     * This method returns the 'this' object, allowing multiple sets to be
     * performed consecutively.
     * </p>
     *
     * @param errorCode The new error code to use for the exception
     * @return this object
     */
    public ExceptionData setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        // return this object after setting error code
        return this;
    }

    /**
     * <p>
     * Returns the date when the exception was first created - stored within
     * creationDate member, which will never be null.
     * </p>
     *
     * @return the date when the exception was first created
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * <p>
     * Returns the name of the thread which the exception was created from.
     * </p>
     * <p>
     * This is stored within the threadName member, and will never be null.
     * </p>
     *
     * @return the name of the thread which the exception was created from
     */
    public String getThreadName() {
        return this.threadName;
    }
}
