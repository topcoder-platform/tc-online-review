/*
 * Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be used in &quot;Edit/Save Project Payments&quot; page.
 * It will be used to store the project payments data submitted by end user.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ProjectPaymentsForm {
    /**
     * The project id.
     */
    private Long pid;
    /**
     * The payments of submitter resources.
     */
    private List<ResourcePayments> submitterPayments = new ArrayList<ResourcePayments>();
    /**
     * The payments of reviewer resources.
     */
    private List<ResourcePayments> reviewerPayments = new ArrayList<ResourcePayments>();
    /**
     * The payments of copilot resources.
     */
    private List<ResourcePayments> copilotPayments = new ArrayList<ResourcePayments>();

    /**
     * Empty constructor.
     */
    public ProjectPaymentsForm() {
    }

    /**
     * Gets the project id.
     *
     * @return the project id.
     */
    public Long getPid() {
        return pid;
    }

    /**
     * Sets the project id.
     *
     * @param pid the project id.
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * Gets the payments of submitter resource.
     *
     * @return the payments of submitter resource.
     */
    public List<ResourcePayments> getSubmitterPayments() {
        return submitterPayments;
    }

    /**
     * Sets the payments of submitter resource.
     *
     * @param submitterPayments the payments of submitter resource.
     */
    public void setSubmitterPayments(List<ResourcePayments> submitterPayments) {
        this.submitterPayments = submitterPayments;
    }

    /**
     * Gets the payments of reviewer resource.
     *
     * @return the payments of reviewer resource.
     */
    public List<ResourcePayments> getReviewerPayments() {
        return reviewerPayments;
    }

    /**
     * Sets the payments of copilot resource.
     *
     * @param reviewerPayments the payments of reviewer resource.
     */
    public void setReviewerPayments(List<ResourcePayments> reviewerPayments) {
        this.reviewerPayments = reviewerPayments;
    }

    /**
     * Gets the payments of copilot resource.
     *
     * @return the payments of copilot resource.
     */
    public List<ResourcePayments> getCopilotPayments() {
        return copilotPayments;
    }

    /**
     * Sets the payments of copilot resource.
     *
     * @param copilotPayments the payments of copilot resource.
     */
    public void setCopilotPayments(List<ResourcePayments> copilotPayments) {
        this.copilotPayments = copilotPayments;
    }

    /**
     * This is a simple POJO class which will be used to store the resource payments data submitted by end user.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    public static class ResourcePayments {
        /**
         * The resource id.
         */
        private Long resourceId;
        /**
         * The project payment ids.
         */
        private List<String> paymentIds = new ArrayList<String>();
        /**
         * The project payment type ids.
         */
        private List<String> paymentTypes = new ArrayList<String>();
        /**
         * The submission ids.
         */
        private List<String> submissionIds = new ArrayList<String>();
        /**
         * The payment amounts.
         */
        private List<String> amounts = new ArrayList<String>();
        /**
         * A flag indicates whether the resource's payment is automatic.
         */
        private Boolean automatic;

        /**
         * Construct a new <code>ResourcePayments</code> instance.
         */
        public ResourcePayments() {
        }

        /**
         * Gets the project payment ids.
         *
         * @return the project payment ids.
         */
        public List<String> getPaymentIds() {
            return paymentIds;
        }

        /**
         * Sets the project payment ids.
         *
         * @param paymentIds the project payment ids.
         */
        public void setPaymentIds(List<String> paymentIds) {
            this.paymentIds = paymentIds;
        }

        /**
         * Gets the project payment type ids.
         *
         * @return the project payment type ids.
         */
        public List<String> getPaymentTypes() {
            return paymentTypes;
        }

        /**
         * Sets the project payment type ids.
         *
         * @param paymentTypes the project payment type ids.
         */
        public void setPaymentTypes(List<String> paymentTypes) {
            this.paymentTypes = paymentTypes;
        }

        /**
         * Gets the submission ids.
         *
         * @return the submission ids.
         */
        public List<String> getSubmissionIds() {
            return submissionIds;
        }

        /**
         * Sets the submission ids.
         *
         * @param submissionIds the submission ids.
         */
        public void setSubmissionIds(List<String> submissionIds) {
            this.submissionIds = submissionIds;
        }

        /**
         * Gets the payment amounts.
         *
         * @return the payment amounts.
         */
        public List<String> getAmounts() {
            return amounts;
        }

        /**
         * Sets the payment amounts.
         *
         * @param amounts the payment amounts.
         */
        public void setAmounts(List<String> amounts) {
            this.amounts = amounts;
        }

        /**
         * Gets the flag indicates whether the resource's payment is automatic.
         *
         * @return false if resource's &quot;Manual Payments&quot; property is &quot;true&quot, true otherwise.
         */
        public Boolean getAutomatic() {
            return automatic;
        }

        /**
         * Sets the flag indicates whether the resource's payment is automatic.
         *
         * @param automatic false if resource's &quot;Manual Payments&quot; property is &quot;true&quot,
         *                  true otherwise.
         */
        public void setAutomatic(Boolean automatic) {
            this.automatic = automatic;
        }

        /**
         * Gets the resource id.
         *
         * @return the resource id.
         */
        public Long getResourceId() {
            return resourceId;
        }

        /**
         * Sets the resource id.
         *
         * @param resourceId the resource id.
         */
        public void setResourceId(Long resourceId) {
            this.resourceId = resourceId;
        }
    }
}
