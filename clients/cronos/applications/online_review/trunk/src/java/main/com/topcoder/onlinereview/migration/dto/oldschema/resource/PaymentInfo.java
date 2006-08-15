/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.resource;

/**
 * The PaymentInfo DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PaymentInfo {
    /** Represents the project_result table name. */
    public static final String TABLE_NAME = "payment_info";

    /** Represents payment field name. */
    public static final String PAYMENT_NAME = "payment";

    /** Represents payment_stat_id field name. */
    public static final String PAYMENT_STAT_ID_NAME = "payment_stat_id";
    private float payment;
    private int paymentStatId;

    /**
     * Returns the payment.
     *
     * @return Returns the payment.
     */
    public float getPayment() {
        return payment;
    }

    /**
     * Set the payment.
     *
     * @param payment The payment to set.
     */
    public void setPayment(float payment) {
        this.payment = payment;
    }

    /**
     * Returns the paymentStatId.
     *
     * @return Returns the paymentStatId.
     */
    public int getPaymentStatId() {
        return paymentStatId;
    }

    /**
     * Set the paymentStatId.
     *
     * @param paymentStatId The paymentStatId to set.
     */
    public void setPaymentStatId(int paymentStatId) {
        this.paymentStatId = paymentStatId;
    }
}
