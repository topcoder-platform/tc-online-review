/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.web.ejb.pacts.BasePayment;
import com.topcoder.web.ejb.pacts.DeleteAffirmedAssignmentDocumentException;
import com.topcoder.web.ejb.pacts.PactsClientServices;
import com.topcoder.web.ejb.pacts.PactsServicesBean;
import com.topcoder.web.ejb.pacts.assignmentdocuments.AssignmentDocument;
import com.topcoder.web.ejb.pacts.payments.InvalidStatusException;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>An implementation of {@link PactsClientServices} interface which provides the library-call style for API of
 * <code>PACTS Services EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class PactsClientServicesLibrary extends BaseEJBLibrary implements PactsClientServices {

    /**
     * <p>A <code>PactsServicesBean</code> which is delegated the processing of the calls to methods of this class.</p>
     */
    private PactsServicesBean bean;

    /**
     * <p>Constructs new <code>PactsClientServicesLibrary</code> instance. This implementation does nothing.</p>
     */
    public PactsClientServicesLibrary() {
        this.bean = new PactsServicesBean();
    }

    /**
     * <p>Gets the base payment.</p>
     *
     * @param paymentId id of the payment to load.
     * @return a <code>BasePayment</code> providing the payment data. 
     * @throws SQLException if an SQL error occurs.
     * @throws InvalidStatusException if invalid payment status found.
     */
    public BasePayment getBasePayment(long paymentId) throws SQLException, InvalidStatusException {
        return bean.getBasePayment(paymentId);
    }

    /**
     * <p>Gets the payments for specified coder.</p>
     * 
     * @param coderId the coder to find payments for.
     * @param paymentTypeId type of payment to look for.
     * @param referenceId the reference id.
     * @return a <code>List</code> providing the payments for coder.
     * @throws SQLException if an SQL error occurs.
     * @throws InvalidStatusException if invalid payment status found.
     */
    public List findCoderPayments(long coderId, int paymentTypeId, long referenceId)
        throws SQLException, InvalidStatusException {
        return bean.findCoderPayments(coderId, paymentTypeId, referenceId);
    }

    /**
     * <p>Gets the payments for specified coder.</p>
     *
     * @param coderId the coder to find payments for.
     * @param paymentTypeId type of payment to look for.
     * @return a <code>List</code> providing the payments for coder.
     * @throws SQLException if an SQL error occurs.
     * @throws InvalidStatusException if invalid payment status found.
     */
    public List findCoderPayments(long coderId, int paymentTypeId) throws SQLException, InvalidStatusException {
        return bean.findCoderPayments(coderId, paymentTypeId);
    }

    /**
     * <p>Gets the payments for specified coder.</p>
     *
     * @param coderId the coder to find payments for.
     * @return a <code>List</code> providing the payments for coder.
     * @throws SQLException if an SQL error occurs.
     * @throws InvalidStatusException if invalid payment status found.
     */
    public List findCoderPayments(long coderId) throws SQLException, InvalidStatusException {
        return bean.findCoderPayments(coderId);
    }

    /**
     * <p>Gets the payments for specified coder.</p>
     *
     * @param paymentTypeId type of payment to look for.
     * @param referenceId reference to look for
     * @return a <code>List</code> providing the payments for coder.
     * @throws SQLException if an SQL error occurs.
     */
    public List findPayments(int paymentTypeId, long referenceId) throws SQLException {
        return bean.findPayments(paymentTypeId, referenceId);
    }

    /**
     * <p>Gets the payments for specified coder.</p>
     *
     * @param paymentTypeId type of payment to look for.
     * @return a <code>List</code> providing the payments for coder.
     * @throws SQLException if an SQL error occurs.
     */
    public List findPayments(int paymentTypeId) throws SQLException {
        return bean.findPayments(paymentTypeId);
    }

    /**
     * <p>Adds payments for specified coder.</p>
     *
     * @param payments payments to add to DB.
     * @return a <code>List</code> providing the added payments for coder. 
     * @throws SQLException if an SQL error occurs.
     */
    public List addPayments(List payments) throws SQLException {
        return bean.addPayments(payments);
    }

    /**
     * <p>Adds specified payment.</p>
     *
     * @param payment payment to add.
     * @return a <code>BasePayment</code> providing the data for added payment.
     * @throws SQLException if an SQL error occurs.
     */
    public BasePayment addPayment(BasePayment payment) throws SQLException {
        return bean.addPayment(payment);
    }

    /**
     * <p>Updates the payment.</p>
     *
     * @param payment payment to update.
     * @return a <code>BasePayment</code> providing the data for updated payment.
     * @throws Exception if an unexpected error occurs.
     */
    public BasePayment updatePayment(BasePayment payment) throws Exception {
        return bean.updatePayment(payment);
    }

    /**
     * <p>Fills the payment data.</p>
     *
     * @param payment the payment to fill its information
     * @return a <code>BasePayment</code> providing the data for updated payment.
     * @throws SQLException if an SQL error occurs.
     */
    public BasePayment fillPaymentData(BasePayment payment) throws SQLException {
        return bean.fillPaymentData(payment);
    }

    /**
     * <p>Gets global ad for specified user.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @return a <code>long</code> providing the global ad ID.
     * @throws SQLException if an SQL error occurs.
     */
    public long getGlobalADId(long userId) throws SQLException {
        return bean.getGlobalADId(userId);
    }

    /**
     * <p>Checks if specified user has global ad.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @return <code>true</code> if there is global ad for specified user; <code>false</code> otherwise.
     * @throws SQLException if an SQL error occurs.
     */
    public boolean hasGlobalAD(long userId) throws SQLException {
        return bean.hasGlobalAD(userId);
    }

    /**
     * <p>Affirms specified assignment document.</p>
     *
     * @param ad the Assignment Document to store.
     */
    public void affirmAssignmentDocument(AssignmentDocument ad) {
        bean.affirmAssignmentDocument(ad);
    }

    /**
     * <p>Deletes the specified assignment document.</p>
     *
     * @param ad the Assignment Document to delete.
     * @throws DeleteAffirmedAssignmentDocumentException if document could not be deleted.
     */
    public void deleteAssignmentDocument(AssignmentDocument ad) throws DeleteAffirmedAssignmentDocumentException {
        bean.deleteAssignmentDocument(ad);
    }

    /**
     * <p>Adds specified assignment document.</p>
     *
     * @param ad the Assignment Document to store.
     * @return an <code>AssignmentDocument</code> providing the data for added document.
     * @throws DeleteAffirmedAssignmentDocumentException if document could not be deleted.
     */
    public AssignmentDocument addAssignmentDocument(AssignmentDocument ad)
        throws DeleteAffirmedAssignmentDocumentException {
        return bean.addAssignmentDocument(ad);
    }

    /**
     * <p>Gets the transformed text for assignment document.</p>
     *
     * @param assignmentDocumentTypeId the Assignment Document's type id.
     * @param ad the Assignment Document to transform.
     * @return a <code>String</code> providing the transformed text for assignment document.
     */
    public String getAssignmentDocumentTransformedText(long assignmentDocumentTypeId, AssignmentDocument ad) {
        return bean.getAssignmentDocumentTransformedText(assignmentDocumentTypeId, ad);
    }

    /**
     * <p>Gets the assignment documents for specified user and Studio contest.</p>
     *
     * @param userId the Assignment Document's user id to find.
     * @param studioContestId a <code>long</code> providing the Studio contest ID.
     * @return a <code>List</code> providing the data for assignment documents.
     */
    public List getAssignmentDocumentByUserIdStudioContestId(long userId, long studioContestId) {
        return bean.getAssignmentDocumentByUserIdStudioContestId(userId, studioContestId);
    }

    /**
     * <p>Gets the assignment documents for user.</p>
     *
     * @param userId the Assignment Document's user id to find.
     * @param assignmentDocumentTypeId the Assignment Document's type id to find.
     * @param onlyPending <code>true</code> if only pending documents are to be returned; <code>false</code> otherwise.
     * @return a <code>List</code> providing the assignment documents for user.
     */
    public List getAssignmentDocumentByUserId(long userId, long assignmentDocumentTypeId, boolean onlyPending) {
        return bean.getAssignmentDocumentByUserId(userId, assignmentDocumentTypeId, onlyPending);
    }

    /**
     * <p>Checks if specified user has hard-copy assignment documents or not.</p>
     *
     * @param userId the Assignment Document's user id to find.
     * @param assignmentDocumentTypeId the Assignment Document's type id to find.
     * @return <code>true</code> if specified user has hard copy assignment documents; <code>false</code> otherwise.
     */
    public Boolean hasHardCopyAssignmentDocumentByUserId(long userId, long assignmentDocumentTypeId) {
        return bean.hasHardCopyAssignmentDocumentByUserId(userId, assignmentDocumentTypeId);
    }

    /**
     * <p>Gets the assignment documents for specified project.</p>
     *
     * @param projectId the Assignment Document's project id to find
     * @return a <code>List</code> listing the assignment documents for specified project.
     */
    public List getAssignmentDocumentByProjectId(long projectId) {
        return bean.getAssignmentDocumentByProjectId(projectId);
    }

    /**
     * <p>Gets the assignment document mapped to specified ID.</p>
     *
     * @param assignmentDocumentId the Assignment Document id to find
     * @return an <code>AssignmentDocument</code> providing assignment document details. 
     */
    public AssignmentDocument getAssignmentDocument(long assignmentDocumentId) {
        return bean.getAssignmentDocument(assignmentDocumentId);
    }
}
