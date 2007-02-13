package com.cronos.onlinereview.web.service.test;

import java.net.URL;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import junit.framework.TestCase;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;

/**
 * This class tests Struts Actions performed by <code>ProjectActions</code> class.
 * 
 * @author TCAssemblyTeam
 * @version 1.0
 */
public class ProjectActionsTest extends TestCase {

	/*
	 * private void pp() { // javax.activation.MimetypesFileTypeMap map=
	 * (javax.activation.MimetypesFileTypeMap)javax.activation.MimetypesFileTypeMap.getDefaultFileTypeMap();
	 * //map.addMimeTypes("application/x-org-apache-axis-wsdd wsdd");
	 * 
	 * 
	 * //Create the data for the attached file. DataHandler dhSource = new DataHandler(new FileDataSource(filename));
	 * 
	 * Service service = new Service();
	 * 
	 * Call call = (Call) service.createCall();
	 * 
	 * 
	 * call.setTargetEndpointAddress(new URL(opts.getURL())); //Set the target service host and service location,
	 * 
	 * call.setOperationName(new QName("urn:EchoAttachmentsService", "echo")); // This is the target services method to
	 * invoke.
	 * 
	 * QName qnameAttachment = new QName("urn:EchoAttachmentsService", "DataHandler");
	 * 
	 * call.registerTypeMapping( dhSource.getClass(), // Add serializer for attachment. qnameAttachment,
	 * JAFDataHandlerSerializerFactory.class, JAFDataHandlerDeserializerFactory.class);
	 * 
	 * 
	 * call.addParameter("source", qnameAttachment, ParameterMode.IN); //Add the file.
	 * 
	 * call.setReturnType(qnameAttachment);
	 * 
	 * call.setUsername(opts.getUser());
	 * 
	 * call.setPassword(opts.getPassword());
	 * 
	 * if (doTheDIME) call.setProperty(call.ATTACHMENT_ENCAPSULATION_FORMAT, call.ATTACHMENT_ENCAPSULATION_FORMAT_DIME);
	 * 
	 * 
	 * Object ret = call.invoke(new Object[]{ dhSource } ); //Add the attachment.
	 * 
	 * if (null == ret) { System.out.println("Received null "); throw new AxisFault("", "Received null", null, null); }
	 * 
	 * if (ret instanceof String) { System.out.println("Received problem response from server: " + ret); throw new
	 * AxisFault("", (String) ret, null, null); }
	 * 
	 * if (!(ret instanceof DataHandler)) { //The wrong type of object that what was expected.
	 * System.out.println("Received problem response from server:" + ret.getClass().getName()); throw new AxisFault("",
	 * "Received problem response from server:" + ret.getClass().getName(), null, null);
	 *  } //Still here, so far so good. //Now lets brute force compare the source attachment // to the one we received.
	 * DataHandler rdh = (DataHandler) ret;
	 * 
	 * //From here we'll just treat the data resource as file. String receivedfileName = rdh.getName();//Get the
	 * filename.
	 * 
	 * if (receivedfileName == null) { System.err.println("Could not get the file name."); throw new AxisFault("",
	 * "Could not get the file name.", null, null); }
	 * 
	 * 
	 * System.out.println("Going to compare the files.."); boolean retv = compareFiles(filename, receivedfileName);
	 * 
	 * java.io.File receivedFile = new java.io.File(receivedfileName);
	 * 
	 * receivedFile.delete();
	 * 
	 * return retv; }
	 */

	public void testUploadSubmission() throws Exception {
		new UploadServiceClient().uploadSubmission(30000080, 100130, "C:/Temp/jbossws-samples-1.0.4.GA/version.properties");
	}
}
