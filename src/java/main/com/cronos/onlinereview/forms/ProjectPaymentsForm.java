/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.forms;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.list.LazyList;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * This class extends <code>ActionForm</code> which will be used in &quot;Edit/Save Project Payments&quot; page.
 * It will be used to store the project payments data submitted by end user.
 *
 * @author flexme
 * @version 1.0 (Online Review - Project Payments Integration Part 2 v1.0)
 */
public class ProjectPaymentsForm extends ActionForm {
    /**
     * The project id.
     */
    private Long pid;
    /**
     * The payments of submitter resources.
     */
    private List<ResourcePayments> submitterPayments;
    /**
     * The payments of reviewer resources.
     */
    private List<ResourcePayments> reviewerPayments;
    /**
     * The payments of copilot resources.
     */
    private List<ResourcePayments> copilotPayments;

    /**
     * Empty constructor.
     */
    public ProjectPaymentsForm() {
    }

    /**
     * Reset the form.
     *
     * @param actionMapping the action mapping.
     * @param httpServletRequest the http request.
     */
    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        Factory factory = new Factory() {
            /**
             * Create a new <code>ResourcePayments</code> instance.
             *
             * @return a new <code>ResourcePayments</code> instance.
             */
            public Object create() {
                return new ResourcePayments();
            }
        };
        submitterPayments = ListUtils.lazyList(new ArrayList(), factory);
        reviewerPayments = ListUtils.lazyList(new ArrayList(), factory);
        copilotPayments = ListUtils.lazyList(new ArrayList(), factory);
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
     * @version 1.0 (Online Review - Project Payments Integration Part 2 v1.0)
     */
    public static class ResourcePayments {
        /**
         * The resource id.
         */
        private Long resourceId;
        /**
         * The project payment ids.
         */
        private List<String> paymentIds;
        /**
         * The project payment type ids.
         */
        private List<String> paymentTypes;
        /**
         * The submission ids.
         */
        private List<String> submissionIds;
        /**
         * The payment amounts.
         */
        private List<String> amounts;
        /**
         * A flag indicates whether the resource's payment is automatic.
         */
        private Boolean automatic;

        /**
         * Construct a new <code>ResourcePayments</code> instance.
         */
        public ResourcePayments() {
            Factory factory = new Factory() {
                /**
                 * Create a new <code>String</code> instance.
                 *
                 * @return a new <code>String</code> instance.
                 */
                public Object create() {
                    return new String();
                }
            };
            paymentIds = new LazySetList(new ArrayList(), factory);
            paymentTypes = new LazySetList(new ArrayList(), factory);
            submissionIds = new LazySetList(new ArrayList(), factory);
            amounts = new LazySetList(new ArrayList(), factory);
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

    /**
     * This class extends <code>LazyList</code> providing an additional lazy &quot;set&quot; operation.
     *
     * @author TCSASSEMBLER
     * @version 1.0 (Online Review - Project Payments Integration Part 2 v1.0)
     */
    static class LazySetList extends LazyList {
        /**
         * Construct a new <code>LazySetList</code> instance.
         *
         * @param list the list used to store the elements.
         * @param factory the factory used to create a new element.
         */
        public LazySetList(List list, Factory factory) {
            super(list, factory);
        }

        /**
         * Sets an element in the list at a specific index. The index can be greater or
         * equal to the size of the list.
         *
         * @param index the specific index.
         * @param obj the element.
         * @return the element set in the specific index.
         */
        public Object set(int index, Object obj) {
            get(index);
            return super.set(index, obj);
        }
    }
}
