package com.cronos.onlinereview.phases;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.cronos.onlinereview.phases.logging.LoggerMessage;
import com.topcoder.util.log.Level;
import com.topcoder.web.ejb.pacts.PactsClientServices;
import com.topcoder.web.ejb.pacts.PactsClientServicesHome;

/**
 * 
 *
 * @author Bauna
 * @version 1.0
 */
public class ServiceLocator {
	private final static String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.pacts.services"; 
	
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogFactory
			.getLog(ServiceLocator.class.getName());
	private static final ServiceLocator instance = new ServiceLocator();
	private static InitialContext context;
	
	private ServiceLocator() {}
	
	public static ServiceLocator getInstance() throws ServiceLocatorNamingException, ConfigurationException {
		if (context == null) {
			
			Hashtable props = new Hashtable();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.put(Context.PROVIDER_URL, PhasesHelper.getPropertyValue(DEFAULT_NAMESPACE, "pacts_jndi_address", true));
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			log.log(Level.INFO, "Pacts JNDI Address: " + props.get(Context.PROVIDER_URL));
			try {
				context = new InitialContext(props);
			} catch (NamingException e) {
				log.log(Level.FATAL, "Fail to find the PactsServicesHome." + LoggerMessage.getExceptionStackTrace(e));
				throw new ServiceLocatorNamingException(e);
			}
		}
		return instance;
	}
	
	public InitialContext getInitialContext() {
		return context;
	}
	
	public PactsClientServicesHome getPactsClientServicesHome() throws ServiceLocatorNamingException {
		try {
			return (PactsClientServicesHome) PortableRemoteObject.narrow(
					getInitialContext().lookup("com.topcoder.web.ejb.pacts.PactsClientServicesHome"),
					PactsClientServicesHome.class);
		} catch (NamingException e) {
			log.log(Level.FATAL, "Fail to find the PactsServicesHome." + LoggerMessage.getExceptionStackTrace(e));
			throw new ServiceLocatorNamingException(e);
		}
	}
	
	public PactsClientServices getPactsClientServices() throws ServiceLocatorNamingException, ServiceLocatorCreateException {
    	try {
			return getPactsClientServicesHome().create();
		} catch (RemoteException e) {
			log.log(Level.FATAL, "Fail to find the PactsServicesHome." + LoggerMessage.getExceptionStackTrace(e));
			throw new ServiceLocatorCreateException("error creating PactsServices",e);
		} catch (CreateException e) {
			log.log(Level.FATAL, "Fail to find the PactsServicesHome." + LoggerMessage.getExceptionStackTrace(e));
			throw new ServiceLocatorCreateException("error creating PactsServices", e);
		}
    }
}
