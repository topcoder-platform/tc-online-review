package com.cronos.onlinereview.web.service.test;

import junit.framework.TestCase;

/**
 * This class tests Struts Actions performed by <code>ProjectActions</code> class.
 * 
 * @author TCAssemblyTeam
 * @version 1.0
 */
public class UsersServiceTest extends TestCase {
	public void testUsersSubmission() throws Exception {
		new UsersServiceClient().addSubmitter(30000080, 100131);
	}
}
