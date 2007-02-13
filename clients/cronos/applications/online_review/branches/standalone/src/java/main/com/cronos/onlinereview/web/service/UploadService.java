package com.cronos.onlinereview.web.service;

import java.rmi.RemoteException;

import javax.activation.DataHandler;

public interface UploadService {

	int uploadSubmission(long projectId, long ownerId, String filename, DataHandler submission) throws RemoteException;

}