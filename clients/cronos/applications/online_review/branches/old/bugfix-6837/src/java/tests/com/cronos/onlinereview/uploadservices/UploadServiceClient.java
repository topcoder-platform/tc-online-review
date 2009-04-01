package com.cronos.onlinereview.uploadservices;

import java.io.File;
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
import org.apache.axis.encoding.XMLType;

/**
 * This class is a sample client which demonstrates the web service method invocation using a SOAP call.
 * 
 * @author evilisneo
 * @version 1.0
 */
public class UploadServiceClient {
    /**
     * The end location of the SOAP call, which contains the method to be executed.
     */
    private final static String END_POINT = "http://63.118.154.175/review/services/UploadService";

    /**
     * Uploads the submission using the SOAP call.
     * 
     * @param projectId
     *            the project id
     * @param ownerId
     *            the owner/user id
     * @param filename
     *            the file name of the submission
     * @return the submission id
     * @throws ServiceException
     *             if any while creating the SOAP call.
     * @throws MalformedURLException
     *             if any while creating the SOAP call.
     * @throws RemoteException
     *             if any while executing.
     */
    public long uploadSubmission(long projectId, long ownerId, String filename, int tryCount) throws ServiceException,
            MalformedURLException, RemoteException {

        // Create the data for the attached file.
        DataHandler dhSource = new DataHandler(new FileDataSource(filename));

        Service service = new Service();
        Call call = (Call) service.createCall();

        call.setTargetEndpointAddress(new URL(END_POINT));
        QName qnameAttachment = new QName("urn:EchoAttachmentsService", "DataHandler");


        call.setOperationName(new QName("urn:UploadService", "uploadSubmission"));
        call.addParameter("projectId", XMLType.XSD_LONG, ParameterMode.IN);
        call.addParameter("ownerId", XMLType.XSD_LONG, ParameterMode.IN);
        call.addParameter("filename", XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter(qnameAttachment, XMLType.MIME_DATA_HANDLER, ParameterMode.IN); // Add the file.
        call.setReturnType(XMLType.XSD_LONG);
        
        // call.
        return ((Number) call.invoke(new Object[] {new Long(projectId), new Long(ownerId), 
        		tryCount + "-" + new File(filename).getName(), dhSource})).longValue();
    }

    /**
     * The starter.
     * 
     * @param args
     *            not used.
     */
    public static void main(String[] args) {
        UploadServiceClient client = new UploadServiceClient();
        try {
        	for (int i = 0; i < 60; i++) {
        		long ret = client.uploadSubmission(38102092, 154896, "c:/dummySubmission.txt", i);
        		System.out.println(i + " - Submission ID: " + ret);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
