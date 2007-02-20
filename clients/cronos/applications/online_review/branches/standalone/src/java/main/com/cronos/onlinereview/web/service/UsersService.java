package com.cronos.onlinereview.web.service;

import java.rmi.RemoteException;

/**
 * Defines the interface that must be provided by the UsersService implementors  
 * 
 * @author Bauna
 */
public interface UsersService {
	
	/**
	 * Add a user as submitter to a project. 
	 * If the user already has a role in the project 
	 * this method fail throwing an exception
	 * 
	 * @param projectId the projects's id
	 * @param userId the user's id
	 * @throws RemoteException if any error occurs.
	 */
	void addSubmitter(long projectId, long userId) throws RemoteException;
}
