/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling.stresstests;

import com.topcoder.util.scheduler.scheduling.ScheduledEnable;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectCreationException;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactory;

/**
 * This is a simple class implementing the <code>ScheduledEnableObjectFactory</code> used for testing purpose.
 * 
 * @author 80x86
 * @version 3.1
 */
public class CustomScheduledEnableObjectFactoryStress implements ScheduledEnableObjectFactory {

    /**
     * Creates the <code>SimpleScheduledEnableObjectFactory</code> instance.
     */
    public CustomScheduledEnableObjectFactoryStress() {
    }

    /**
     * Returns the <code>SimpleJob</code> instance for testing.
     * 
     * @return a <code>ScheduledEnable</code> instance.
     * @throws ScheduledEnableObjectCreationException
     *             if <code>throwException</code> is set <code>true</code>.
     */
    public ScheduledEnable createScheduledEnableObject() throws ScheduledEnableObjectCreationException {
        return null;
    }

}
