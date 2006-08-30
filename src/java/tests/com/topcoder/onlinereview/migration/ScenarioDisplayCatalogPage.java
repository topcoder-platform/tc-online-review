/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.5.1	Display Catalog Page Activity":
 * </p>
 * <ul>
 * <li>Scenario #97 (FTC10) Display Catalog Page</li>
 * </ul>
 * 
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioDisplayCatalogPage extends TestCase {
	private OnlineReviewUser user = null;

    /**
     * <p>
     * Scenario #97 (FTC10) Display Catalog Page
     * 1.	Display Catalog Page
     * Expected Outcome:
     * Catalog scorecard links are displayed and functional
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario97() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToCatalogPage();
    	assertTrue("The Catalog Page will be shown correctly", user.displayCatalogPage());
    }
}
