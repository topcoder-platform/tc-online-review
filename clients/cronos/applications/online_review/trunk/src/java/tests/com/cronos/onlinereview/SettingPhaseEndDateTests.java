/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Online review functional tests 2, setting the end date of the phase.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class SettingPhaseEndDateTests extends ProjectTests {

    /**
     * Test Case Number: FTC30 RS5.1 Verify Manager can change "Specification Submission   1" phase timeline by setting end date.
     *
     * @throws Exception if any error occurs
     */
    public void testSettingEndDate() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[2]/div/table/tbody");
        assertTrue("Specification Submission period is 24 hrs at first", status.contains("Specification Submission 24 hrs"));
    	browser.click("//img[@alt='Edit Project']");
		browser.waitForPageToLoad(TIMEOUT);
		browser.click("phase_use_duration[1]");
		DateFormat format = new SimpleDateFormat("MM.dd.yy");
		String endDate = format.format(new Date());
		browser.type("phase_end_date[1]", endDate);
		browser.type("explanation", "explain");
		browser.click("//input[@name='']");
		browser.waitForPageToLoad(TIMEOUT);
        status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[2]/div/table/tbody");
        assertTrue("Specification Submission period is changed", status.contains("Specification Submission 48 hrs"));
        assertNoErrorsOccurred();
    }
}
