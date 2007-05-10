package com.cronos.onlinereview.phases;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.topcoder.util.log.Level;
import com.topcoder.web.ejb.pacts.PactsServices;
import com.topcoder.web.ejb.pacts.PactsServicesHome;

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
				throw new ServiceLocatorNamingException(e);
			}
		}
		return instance;
	}
	
	public InitialContext getInitialContext() {
		return context;
	}
	
	public PactsServicesHome getPactsServicesHome() throws ServiceLocatorNamingException {
		try {
			return (PactsServicesHome) PortableRemoteObject.narrow(
					getInitialContext().lookup("com.topcoder.web.ejb.pacts.PactsServicesHome"),
					PactsServicesHome.class);
		} catch (NamingException e) {
			throw new ServiceLocatorNamingException(e);
		}
	}
	
	public PactsServices getPactsServices() throws ServiceLocatorNamingException, ServiceLocatorCreateException {
    	try {
			return getPactsServicesHome().create();
		} catch (RemoteException e) {
			throw new ServiceLocatorCreateException("error creating PactsServices",e);
		} catch (CreateException e) {
			throw new ServiceLocatorCreateException("error creating PactsServices", e);
		}
//    	PactsServicesHome home = (PactsServicesHome) PortableRemoteObject.narrow(
//    			getInitialContext().lookup("com.topcoder.web.ejb.pacts.PactsServicesHome"),
//    			PactsServicesHome.class);
//    	try {
//    		PactsServices pactsServices = home.create();
//    		List l =  pactsServices.getAssignmentDocumentByProjectId(23308946);
//    		for (Iterator i = l.iterator(); i.hasNext();) {
//				AssignmentDocument ad = (AssignmentDocument) i.next();
//				System.out.println("ad: " + ad.getComponentProjectId() + 
//						", " + ad.getStatus().getDescription());
//			}
//			System.out.println("pactsServices.getUserTypes(): " + pactsServices.getAssignmentDocumentByProjectId(23308946));
//			return pactsServices;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} 
    	
    }
}
