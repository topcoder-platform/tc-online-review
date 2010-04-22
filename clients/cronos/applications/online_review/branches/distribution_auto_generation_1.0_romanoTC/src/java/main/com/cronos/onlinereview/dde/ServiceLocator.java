/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dde;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.cronos.onlinereview.actions.ConfigHelper;
import com.topcoder.dde.catalog.ComponentManager;
import com.topcoder.dde.catalog.ComponentManagerHome;

/**
 * ServiceLocator for catalog ComponentManager.
 * 
 * @author TCSASSEMBLER
 * @version 1.0 (Distribution Auto Generation Assembly v1.0)
 */
public class ServiceLocator {
    /**
     * Singleton instance.
     */
    private static final ServiceLocator instance = new ServiceLocator();

    /**
     * The initial context.
     */
    private static InitialContext context;

    /**
     * Default empty constructor.
     */
    private ServiceLocator() {
    }

    /**
     * Returns the singleton instance and setup the initial context.
     * 
     * @return the singleton instance
     * @throws NamingException if any error occurs during setup initial context.
     */
    public static ServiceLocator getInstance() throws NamingException {
        if (context == null) {

            Hashtable<String, String> props = new Hashtable<String, String>();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            props.put(Context.PROVIDER_URL, ConfigHelper.getCatalogJndiURL());
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
            context = new InitialContext(props);
        }
        return instance;
    }

    /**
     * Returns the initial context.
     * 
     * @return the initial context.
     */
    public InitialContext getInitialContext() {
        return context;
    }

    /**
     * Returns the ComponentManager home interface.
     * 
     * @return the ComponentManager home interface.
     * @throws NamingException if any error occurs while creating the home interface.
     */
    public ComponentManagerHome getComponentManagerHome() throws NamingException {
        return (ComponentManagerHome) PortableRemoteObject.narrow(getInitialContext().lookup(
            ComponentManagerHome.EJB_REF_NAME), ComponentManagerHome.class);
    }

    /**
     * Returns the ComponentManager EJB.
     * 
     * @return the ComponentManager EJB.
     * @throws NamingException if any error occurs while creating the home interface.
     * @throws RemoteException if any error occurs while creating the EJB.
     * @throws CreateException if any error occurs while creating the EJB
     */
    public ComponentManager getComponentManager() throws NamingException, RemoteException, CreateException {
        return getComponentManagerHome().create();
    }

    /**
     * Returns the ComponentManager EJB.
     * 
     * @param componentId the component ID.
     * @return the ComponentManager EJB.
     * @throws NamingException if any error occurs while creating the home interface.
     * @throws RemoteException if any error occurs while creating the EJB.
     * @throws CreateException if any error occurs while creating the EJB
     */
    public ComponentManager getComponentManager(long componentId) throws NamingException, RemoteException,
        CreateException {
        return getComponentManagerHome().create(componentId);
    }

    /**
     * Returns the ComponentManager EJB.
     * 
     * @param componentId the component ID.
     * @param versionId the component version ID.
     * @return the ComponentManager EJB.
     * @throws NamingException if any error occurs while creating the home interface.
     * @throws RemoteException if any error occurs while creating the EJB.
     * @throws CreateException if any error occurs while creating the EJB
     */
    public ComponentManager getComponentManager(long componentId, long versionId) throws NamingException,
        RemoteException, CreateException {
        return getComponentManagerHome().create(componentId, versionId);
    }
}
