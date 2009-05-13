package com.cronos.onlinereview.actions;

import javax.xml.namespace.QName;
import java.net.URL;
import javax.xml.ws.Service;
import com.topcoder.service.facade.project.ProjectServiceFacade;

public class ProjectServiceLocator {

	static ProjectServiceFacade getService() {
	    // Determine the requested operation
	    String operation = request.getParameter("operation");
	    calledOperation = "operation";
	
	    // Obtain a client stub for accessing the web service
	    URL wsdlLocation = new URL("http://174.129.88.66:8005/projectfacade/ProjectServiceFacadeBean?wsdl");
	    QName serviceName = new QName("http://ejb.project.facade.service.topcoder.com/",
	                                  "ProjectServiceFacadeBeanService");
	    Service service = Service.create(wsdlLocation, serviceName);
	    ProjectServiceFacade port = service.getPort(ProjectServiceFacade.class);
	    
//	    ((StubExt) port).setConfigName("Standard WSSecurity Client");
//	    ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
//	                                                     request.getUserPrincipal().getName());
//	    ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "password");

	    return port;
	}

}
