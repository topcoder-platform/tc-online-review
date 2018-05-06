/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Remote home interface to the IDGeneratorBean stateless session bean.
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public interface IDGeneratorHome extends EJBHome {
    /**
     * The default create() method for this home interface, which returns an
     * IDGenerator implementation.
     *
     * @return an IDGenerator stub implementation
     * @throws CreateException if the session bean cannot be created for some
     * reason
     * @throws RemoteException if an RMI error occurs between client and EJB
     * container
     */
    public IDGenerator create() throws CreateException, RemoteException;

}





