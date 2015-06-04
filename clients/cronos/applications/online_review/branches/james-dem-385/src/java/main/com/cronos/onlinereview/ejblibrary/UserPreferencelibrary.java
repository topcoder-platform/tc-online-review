/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;
import com.topcoder.web.common.RowNotFoundException;
import com.topcoder.web.ejb.user.UserPreference;
import com.topcoder.web.ejb.user.UserPreferenceBean;

/**
 * <p>An implementation of {@link UserPreference} interface which provides the library-call style for API of
 * <code>User Preference EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UserPreferencelibrary extends BaseEJBLibrary implements
        UserPreference {
    /**
     * <p>
     * A <code>UserPreferenceBean</code> which is delegated the processing of
     * the calls to methods of this class.
     * </p>
     */
    private final UserPreferenceBean bean;

    /**
     * <p>
     * Constructs new <code>UserPreferenceBean</code> instance.
     * </p>
     */
    public UserPreferencelibrary() {
        this.bean = new UserPreferenceBean();
    }

    /**
     * Create user preference.
     * 
     * @param userId
     *            the user id
     * @param preferenceId
     *            the preference id
     * @param dataSource
     *            the data source
     */
    public void createUserPreference(long userId, int preferenceId,
            String dataSource) throws EJBException, RemoteException {
        this.bean.createUserPreference(userId, preferenceId, dataSource);
    }

    /**
     * Get user preference value id.
     * 
     * @param userId
     *            the user id
     * @param preferenceId
     *            the preference id
     * @param dataSource
     *            the data source
     * @return preference value id
     */
    public int getPreferenceValueID(long userId, int preferenceId,
            String dataSource) throws RowNotFoundException, RemoteException {
        return this.bean.getPreferenceValueID(userId, preferenceId, dataSource);
    }

    /**
     * Get preferences by group.
     * 
     * @param userId
     *            the user id
     * @param groupId
     *            the group id
     * @param dataSource
     *            the data source
     * @return the results
     */
    public ResultSetContainer getPreferencesByGroup(long userId, int groupId,
            String dataSource) throws EJBException, RemoteException {
        return this.bean.getPreferencesByGroup(userId, groupId, dataSource);
    }

    /**
     * Get user preference value.
     * 
     * @param userId
     *            the user id
     * @param preferenceId
     *            the preference id
     * @param dataSource
     *            the data source
     * @return the value
     */
    public String getValue(long userId, int preferenceId, String dataSource)
            throws RowNotFoundException, RemoteException {
        return this.bean.getValue(userId, preferenceId, dataSource);
    }

    /**
     * Remove user preference value.
     * 
     * @param userId
     *            the user id
     * @param preferenceId
     *            the preference id
     * @param dataSource
     *            the data source
     */
    public void removeUserPreference(long userId, int preferenceId,
            String dataSource) throws EJBException, RemoteException {
        this.bean.removeUserPreference(userId, preferenceId, dataSource);
    }

    /**
     * Get user preference value id.
     * 
     * @param userId
     *            the user id
     * @param preferenceId
     *            the preference id
     * @param valueId
     *            the value id
     * @param dataSource
     *            the data source
     */
    public void setPreferenceValueID(long userId, int preferenceId,
            int valueId, String dataSource) throws EJBException,
            RemoteException {
        this.bean.setPreferenceValueID(userId, preferenceId, valueId,
                dataSource);
    }

    /**
     * Set user preference value.
     * 
     * @param userId
     *            the user id
     * @param preferenceId
     *            the preference id
     * @param dataSource
     *            the data source
     */
    public void setValue(long userId, int preferenceId, String value,
            String dataSource) throws EJBException, RemoteException {
        this.bean.setValue(userId, preferenceId, value, dataSource);
    }

}
