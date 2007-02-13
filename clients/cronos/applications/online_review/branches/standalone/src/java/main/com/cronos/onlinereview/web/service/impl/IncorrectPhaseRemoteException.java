package com.cronos.onlinereview.web.service.impl;

import java.rmi.RemoteException;

public class IncorrectPhaseRemoteException extends RemoteException {

	public IncorrectPhaseRemoteException() {
	}

	public IncorrectPhaseRemoteException(String s) {
		super(s);
	}

	public IncorrectPhaseRemoteException(String s, Throwable cause) {
		super(s, cause);
	}

}
