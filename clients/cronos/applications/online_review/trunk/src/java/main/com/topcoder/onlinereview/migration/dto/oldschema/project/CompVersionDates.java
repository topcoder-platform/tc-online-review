/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.project;

/**
 * The CompVersionDates DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class CompVersionDates {
    /** Represents the comp_version_dates table name. */
    public static final String TABLE_NAME = "comp_version_dates";

    /** Represents comp_vers_id field name. */
    public static final String COMP_VERS_ID_NAME = "comp_vers_id";

    /** Represents price field name. */
    public static final String PRICE_NAME = "price";
    private float price;

    /**
     * Returns the price.
     *
     * @return Returns the price.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Set the price.
     *
     * @param price The price to set.
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
