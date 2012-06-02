/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.cronos.onlinereview.dataaccess.CatalogDataAccess;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;

import javax.persistence.EntityManagerFactory;

/**
 * <p>A provider for <code>Spring</code> context set-up for <code>Online Review</code> application. The singleton
 * bean instance of this class must be defined in <code>Spring</code> context to be set with application context which
 * then can be accessed throughout the <code>Online Review</code> application.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls Assembly)
 */
public class SpringContextProvider implements ApplicationContextAware {

    /**
     * <p>A <code>ApplicationContext</code> providing the current application context.</p>
     */
    private static ApplicationContext applicationContext;

    /**
     * <p>Constructs new <code>SpringContextProvider</code> instance.</p>
     */
    public SpringContextProvider() {
    }

    /**
     * <p>Gets the current application context.</p>
     *
     * @return a <code>ApplicationContext</code> providing the current application context.
     */
    public static ApplicationContext getApplicationContext() {
        return SpringContextProvider.applicationContext;
    }

    /**
     * <p>Sets the current application context.</p>
     *
     * @param applicationContext a <code>ApplicationContext</code> providing the current application context.
     * @throws ApplicationContextException if an unexpected error occurs while accessing the JNDI context or reading
     *         configuration for JNDI utility.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextProvider.applicationContext = applicationContext;
    }

    /**
     * <p>Gets the provider to catalog data.</p>
     *
     * @return a <code>CatalogDataAccess</code> providing access to catalog data.
     */
    public static CatalogDataAccess getCatalogDataAccess() {
        return (CatalogDataAccess) getApplicationContext().getBean("catalogDataAccess");
    }

    /**
     * <p>Gets the entity manager factory.</p>
     *
     * @return a <code>EntityManagerFactory</code> providing a factory for entity managers.
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return (EntityManagerFactory) getApplicationContext().getBean("entityManagerFactory");
    }
}
