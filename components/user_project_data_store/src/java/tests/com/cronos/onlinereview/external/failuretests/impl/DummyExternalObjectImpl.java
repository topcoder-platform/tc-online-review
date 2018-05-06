/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import com.cronos.onlinereview.external.impl.ExternalObjectImpl;

/**
 * <p>
 * Mock class of the <code>ExternalObjectImpl</code> class.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class DummyExternalObjectImpl extends ExternalObjectImpl {

    /**
     * <p>
     * Mock constructor.
     * </p>
     *
     * @param id identifier of this object.
     * @throws IllegalArgumentException if id is negative.
     */
    public DummyExternalObjectImpl(long id) {
        super(id);
    }
}
