package com.cronos.onlinereview.web.service;

import java.rmi.RemoteException;

import javax.activation.DataHandler;

/**
 * Defines the interface that must be provided by the UploadService implementors  
 * 
 * @author Bauna
 */
public interface UploadService {

	
	/**
	 * Adds a new submission for an user on a specific project marking as deleted the previous submussion if it exists   
	 * 
	 * @param projectId the project's id
	 * @param ownerId the user's id
	 * @param filename the filename that will be used to store the submission. 
	 * @param submission the uploaded file
	 * @return returns 0 (zero) if the process finalizes correctly
	 * @throws RemoteException if any error occurs.
	 * @see DataHandler
	 */
	int uploadSubmission(long projectId, long ownerId, String filename, DataHandler submission) throws RemoteException;

}