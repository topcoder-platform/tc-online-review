package com.topcoder.util.scheduler.processor.failuretests;

import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.SchedulingException;

/**
 * This class simply implements the Scheduler interface in Job Scheduling
 * component. All the methods are leaving blank.
 * 
 * @author Blues
 * @version 1.0
 */
class MockScheduler implements Scheduler {

	public void addGroup(JobGroup group) {
		// TODO Auto-generated method stub

	}

	public void addJob(Job job) {
		// TODO Auto-generated method stub

	}

	public void deleteGroup(String name) {
		// TODO Auto-generated method stub

	}

	public void deleteJob(Job job) {
		// TODO Auto-generated method stub

	}

	public Job[] getAllDependentJobs(Job job) {
		// TODO Auto-generated method stub
		return null;
	}

	public JobGroup[] getAllGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	public JobGroup getGroup(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Job getJob(String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Job[] getJobList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateGroup(JobGroup group) {
		// TODO Auto-generated method stub

	}

	public void updateJob(Job job) {
		// TODO Auto-generated method stub

	}

}
