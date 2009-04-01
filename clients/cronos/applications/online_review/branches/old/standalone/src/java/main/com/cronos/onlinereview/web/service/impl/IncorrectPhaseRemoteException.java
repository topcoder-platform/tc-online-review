package com.cronos.onlinereview.web.service.impl;

import java.rmi.RemoteException;

/**
 * This exception is thrown when UploadServices is requested to add a submission to a project 
 * with submission phase not open.
 * 
 * @author Bauna
 */
public class IncorrectPhaseRemoteException extends RemoteException {

	/**
	 * Constructs a <code>IncorrectPhaseRemoteException</code>.
	 */
	public IncorrectPhaseRemoteException() {
	}

	/**
	 * Constructs a <code>IncorrectPhaseRemoteException</code> with the specified detail message.
	 * 
	 * @param s the detail message
	 */
	public IncorrectPhaseRemoteException(String s) {
		super(s);
	}

	/**
     * Constructs a <code>IncorrectPhaseRemoteException</code> with the specified detail
     * message and cause.  This constructor sets the {@link #detail}
     * field to the specified <code>Throwable</code>.
     *
     * @param s the detail message
     * @param cause the cause
     */
	public IncorrectPhaseRemoteException(String s, Throwable cause) {
		super(s, cause);
	}

}
