/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.TestCase;
import com.cronos.onlinereview.project.UserSimulator;

/**
 * A test case for the <code>2.49.1: Advance Submission Activity</code> Use Case.
 *
 * @author  Mike Feldmeier, <a href="mailto:mfeldmeier@topcoder.com">mfeldmeier@topcoder.com</a>
 * @version 1.0
 */
public class AdvanceSubmissionFunctionalTest extends TestCase
{
	/**
	 * <p>Scenario #195 (FTC 293-295)</p>
	 * <pre>
	 * 	<li>User clicks on "All Open Projects" tab</li>
	 * 	<li>User views project list and selects a project</li>
	 * 	<li>User clicks on "Submission/Screening" tab</li>
	 * 	<li>There are no outstanding deliverables for Submitters</li>
	 * 	<li>Screening Result for all Submissions are labeled "Passed"</li>
	 * 	<li>The next phase in the Timelines is labeled "Open"</li>
	 * </pre>
	 * <p>
	 * 	<b>Expected Outcome:</b><br/>
	 * 	System advances submission to next phase.
	 * </p>
	 */
	public void testScenario195() throws Exception
	{
		UserSimulator user = new UserSimulator(UserSimulator.MANAGER);
		user.clickAllOpenProjects();
	}

	/**
	 * <p>Scenario #196 (FTC 293)</p>
	 * <pre>
	 * 	<li>User clicks on "All Open Projects" tab</li>
	 * 	<li>User views project list and selects a project</li>
	 * 	<li>User clicks on "Submission/Screening" tab</li>
	 * 	<li>There are outstanding deliverables for Submitters</li>
	 * </pre>
	 * <p>
	 * 	<b>Expected Outcome:</b><br/>
	 * 	Subsequent phase in not open and System is not able to advance submission to next phase. 
	 * </p>
	 */
	public void testScenario196() throws Exception
	{
	}

	/**
	 * <p>Scenario #197 (FTC 294)</p>
	 * <pre>
	 * 	<li>User clicks on "All Open Projects" tab</li>
	 * 	<li>User views project list and selects a project</li>
	 * 	<li>Subsequent phase is not labeled "Open"</li>
	 * </pre>
	 * <p>
	 * 	<b>Expected Outcome:</b><br/>
	 * 	System is not able to advance submission to next phase.
	 * </p>
	 */
	public void testScenario197() throws Exception
	{
	}
}
