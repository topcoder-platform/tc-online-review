/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * Local home interface to the IDGeneratorBean stateless session bean.
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public interface IDGeneratorLocalHome extends EJBLocalHome {
    /**
     * The default create() method for this home interface, which returns an
     * IDGenerator implementation.
     *
     * @return an IDGenerator stub implementation
     * @throws CreateException if the session bean cannot be created for some
     * reason
     */
    public IDGeneratorLocal create() throws CreateException;
}





