/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * This interface is the main interface of this component. It is the contract of project payment calculation. It
 * provides a single contract method which calculates for each resource role id the payments for a given project
 * identified by its id.
 * </p>
 * <p>
 * <b>Thread Safety:</b><br/>
 * Implementations of this interface are required to be thread safe.
 * </p>
 *
 * @author Schpotsky, TCSDEVELOPER
 * @version 1.0
 */
public interface ProjectPaymentCalculator {
    /**
     * <p>
     * Gets the default payments of each resource role id for the project identified by the given
     * <code>projectId</code>.
     * </p>
     * <p>
     * It returns the mapping between the resource role id and the corresponding payment for the project identified
     * by <code>projectId</code>. An empty map is returned by this method when there is no data found in the
     * persistence.
     * </p>
     *
     * @param projectId
     *            The id of the project for which to get the default payments per resource role id.
     * @param resourceRoleIDs
     *            The list of the resource role IDs for which to get the payment.
     * @return The mapping between the resource role id and the corresponding payment for the project identified by
     *         the specified <code>projectId</code>. (can be empty if there is no data found in the persistence).
     * @throws IllegalArgumentException
     *             If <code>projectId</code> is not positive or if <code>resourceRoleIDs</code> list is
     *             <code>null</code> or is an empty list or contains <code>null</code> elements.
     * @throws ProjectPaymentCalculatorException
     *             If any error occurred during the operation.
     */
    public Map<Long, BigDecimal> getDefaultPayments(long projectId, List<Long> resourceRoleIDs)
        throws ProjectPaymentCalculatorException;
}
