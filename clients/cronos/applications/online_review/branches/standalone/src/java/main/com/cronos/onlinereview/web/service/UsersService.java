package com.cronos.onlinereview.web.service;

import java.rmi.RemoteException;

public interface UsersService {
	void addSubmitter(long projectId, long userId) throws RemoteException;
}
