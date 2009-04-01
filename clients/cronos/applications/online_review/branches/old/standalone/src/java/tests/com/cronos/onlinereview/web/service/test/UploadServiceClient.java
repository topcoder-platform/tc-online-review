package com.cronos.onlinereview.web.service.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;

public class UploadServiceClient {
//	63.118.154.178
//	private final static String END_POINT = "http://63.118.154.181:8880/review/services/UploadService";
	private final static String END_POINT = "http://63.118.154.178/review/services/UploadService";
	
	
	public void uploadSubmission(long projectId, long ownerId, String filename) 
			throws ServiceException, MalformedURLException, RemoteException {
		
		// Create the data for the attached file.
		DataHandler dhSource = new DataHandler(new FileDataSource(filename));

		Service service = new Service();
		Call call = (Call) service.createCall();

		call.setTargetEndpointAddress(new URL(END_POINT));
		QName qnameAttachment = new QName("urn:EchoAttachmentsService", "DataHandler");

		call.registerTypeMapping(dhSource.getClass(), // Add serializer for attachment.
				qnameAttachment, JAFDataHandlerSerializerFactory.class, JAFDataHandlerDeserializerFactory.class);

		call.setOperationName(new QName("urn:UploadService", "uploadSubmission"));
		call.addParameter("projectId", org.apache.axis.Constants.XSD_LONG, javax.xml.rpc.ParameterMode.IN);
		call.addParameter("ownerId", org.apache.axis.Constants.XSD_LONG, javax.xml.rpc.ParameterMode.IN);
		call.addParameter("filename", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
		call.addParameter("submissionDH", qnameAttachment, ParameterMode.IN); // Add the file.
		call.setReturnType(org.apache.axis.Constants.XSD_INT);
		// call.
		try {
			Object ret = call.invoke(new Object[] { new Long(projectId), new Long(ownerId), filename, dhSource });
			System.out.println("return: " + ret); 
		} catch (RemoteException e) {
			System.out.println("RE class: " + e.getClass().getSimpleName());
			e.printStackTrace();
			throw e;
		}
	}
}
