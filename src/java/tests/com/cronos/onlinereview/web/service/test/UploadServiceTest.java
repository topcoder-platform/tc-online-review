package com.cronos.onlinereview.web.service.test;

import junit.framework.TestCase;

/**
 * This class tests Struts Actions performed by <code>ProjectActions</code> class.
 * 
 * @author TCAssemblyTeam
 * @version 1.0
 */
public class UploadServiceTest extends TestCase {
	public void testUploadSubmission() throws Exception {
		new UploadServiceClient().uploadSubmission(30000080, 100130, "C:/Temp/jbossws-samples-1.0.4.GA/version.properties");
	}
}
