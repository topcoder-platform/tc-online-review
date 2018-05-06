/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * A mock implementation of BaseTermsOfUseDao. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class MockTermsOfUseDao extends BaseTermsOfUseDao {
    /**
     * Creates an instance of MockTermsOfUseDao.
     *
     * @param configurationObject
     *            the configuration object containing the configuration.
     *
     * @throws IllegalArgumentException
     *             if the configurationObject is null.
     * @throws TermsOfUseDaoConfigurationException
     *             if any exception occurs while initializing the instance.
     */
    public MockTermsOfUseDao(ConfigurationObject configurationObject) {
        super(configurationObject);
    }
}
