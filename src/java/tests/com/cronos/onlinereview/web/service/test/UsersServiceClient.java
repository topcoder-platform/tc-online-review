package com.cronos.onlinereview.web.service.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class UsersServiceClient {
//	63.118.154.178
//	private final static String END_POINT = "http://63.118.154.181:8880/review/services/UploadService";
	private final static String END_POINT = "http://csf.dev.topcoder.com/review/services/UsersService";
	
	
	public void addSubmitter(long projectId, long ownerId) 
			throws ServiceException, MalformedURLException, RemoteException {
		
		Service service = new Service();
		Call call = (Call) service.createCall();

		call.setTargetEndpointAddress(new URL(END_POINT));
		call.setOperationName(new QName("urn:UsersService", "addSubmitter"));
		call.addParameter("projectId", org.apache.axis.Constants.XSD_LONG, javax.xml.rpc.ParameterMode.IN);
		call.addParameter("ownerId", org.apache.axis.Constants.XSD_LONG, javax.xml.rpc.ParameterMode.IN);
		//call.setReturnType(org.apache.axis.Constants.XSD_INT);
		// call.
		try {
			Object ret = call.invoke(new Object[] { new Long(projectId), new Long(ownerId)});
			System.out.println("return: " + ret); 
		} catch (RemoteException e) {
			System.out.println("RE class: " + e.getClass().getSimpleName());
			e.printStackTrace();
			throw e;
		}
	}
}
