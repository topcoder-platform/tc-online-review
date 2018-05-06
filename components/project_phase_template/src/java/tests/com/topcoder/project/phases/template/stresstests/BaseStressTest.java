/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.project.phases.template.stresstests;

import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Base class for Stress test cases. </p>
 *
 * <p> This class tests the multiple thread situation for the component. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class BaseStressTest {

    /**
     * Number of thread.
     */
    protected static int numThreads;

    /**
     * Number of thread.
     */
    protected static int timesToExecute;

    /**
     * <p> Sets up test environment. </p>
     *
     * @throws Exception to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        numThreads = 4;
        timesToExecute = 4;
    }

    /**
     * Tears down the test environment.
     *
     * @throws Exception to Junit.
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Gets the log message.
     *
     * @param startTime      time to start.
     * @param numThreads     number of threads to perform the actions.
     * @param timesToExecute times to execute the actions.
     *
     * @return Log message.
     */
    protected String getLogMessage(long startTime, int numThreads, int timesToExecute) {

        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        StackTraceElement stack = stackTraces[2];

        String info = stack.getClassName() + "#" + stack.getMethodName();

        return MessageFormat.format("Perform \"{0}\" {1} times in {2} thread, cost {3} milliseconds\n",
                                    info, numThreads * timesToExecute, numThreads, getCostTime(startTime));
    }


    /**
     * Writes the test result to file.
     *
     * @param content content to write.
     *
     * @throws java.io.IOException to caller.
     */
    protected void outputTestResult(String content) throws IOException {
        File file = new File("test_files/stress/result.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        String dateInfo = new Timestamp(System.currentTimeMillis()).toString();
        FileOutputStream stream = new FileOutputStream(file, true);
        stream.write(dateInfo.getBytes());
        stream.write("\n".getBytes());
        stream.write(content.getBytes());
        stream.write("\n".getBytes());
        stream.flush();
        stream.close();
    }

    /**
     * <p> Gets the cost time. </p>
     *
     * @param startTime Start time.
     *
     * @return cost time.
     */
    private long getCostTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * <p> Run action with multiple thread. </p>
     *
     * @param action Action to be executed.
     * @param times  Times to execute the action.
     *
     * @throws Exception to JUnit.
     */
    protected void runMultiThreadActionShouldSuccess(Action action, int times) throws Exception {
        Thread[] threads = new Thread[numThreads];

        List runners = new ArrayList();

        for (int i = 0; i < threads.length; i++) {
            ActionRunner runner = new ActionRunner(action, times);
            runners.add(runner);
            threads[i] = new Thread(runner);
        }

        for (int i = 0, threadsLength = threads.length; i < threadsLength; i++) {
            Thread thread = threads[i];
            thread.start();
        }

        for (int i = 0, threadsLength = threads.length; i < threadsLength; i++) {
            Thread thread = threads[i];
            thread.join();
        }

        for (int i = 0, runnersSize = runners.size(); i < runnersSize; i++) {
            ActionRunner runner = (ActionRunner) runners.get(i);
            if (runner.getLastException() != null) {
                throw runner.getLastException();
            }

            if (runner.getLastError() != null) {
                throw runner.getLastError();
            }
        }
    }
}


