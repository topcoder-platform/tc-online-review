1.dependency: latest version of:
	1. base exception
	2. logging wrapper
	3. job scheduling
	4. typesafe enum

2. test dependency: latest version of:
	1. configuration manager
	2. document generator
	3. object formatter
	4. executable wrapper
	5. object factory

3. fix:
	1. jobProcessor.java: line 337 modified to compatible with job scheduling 3.1.0
	2. TriggerEventHandler.java: line 110 modified to compatible with job sheduling 3.1.0

4. test fix:
	1. changed LogFactory.get() to LogManager.getLog() in many files, to compatible with loggin wrapper 2.0
	2. changed assertEquals("", job1, job2) to string comparision as assertEquals(job1.getName(), job2.getName()),
	   because job scheduling will create a new Job instance according to the original Job, their names are
	   the same, but the reference is different.
	3. the 'nextRun' veriable is not correctly initialized in some moethods of accuracytests/JobProcessorTest.java,
	   fix these bugs.
