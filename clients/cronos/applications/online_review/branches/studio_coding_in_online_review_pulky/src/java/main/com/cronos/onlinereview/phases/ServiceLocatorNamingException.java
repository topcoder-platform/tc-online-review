/*
 * Created on 06/03/2007
 * Copyright by Refert Argentina 2003-2007
 */
package com.cronos.onlinereview.phases;

import com.topcoder.util.errorhandling.BaseException;

/**
 * 
 * @author Bauna
 * @version 1.0
 */
public class ServiceLocatorNamingException extends BaseException {

	/**
	 * 
	 */
	public ServiceLocatorNamingException() {
		super();
	}

	/**
	 * @param message
	 */
	public ServiceLocatorNamingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceLocatorNamingException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceLocatorNamingException(String message, Throwable cause) {
		super(message, cause);
	}

}
