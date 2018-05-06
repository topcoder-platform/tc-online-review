package com.topcoder.util.commandline.stresstests;

import com.topcoder.util.commandline.*;
import junit.framework.*;
import java.util.*;

/**
 * <p>Tests the CommandLineUtility.parse() throughput.</p>
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 *
 * @author valeriy
 * @version 1.0
 */
public class CommandLineUtilityTest extends TestCase {

    private static String args1[] = {"-a", "arg1","arg2","arg3","arg4","arg5",
            "-b=arg1,arg2,arg3","-c","arg1","param1","param2"} ;

    private static String args2[] = {"-aarg1", "arg2,","arg3,","arg4,","arg5",
            "-barg1, arg2, arg3","-carg1","param1,param2","param3"} ;

    public CommandLineUtilityTest(String name) {
        super(name);
    }

    /** Tests CommandLineUtility in one thread with 1000 operations
      */
    public void testCLU1_1_1000() throws Exception {
        run(args1, 1, 1000);
    }

    /** Tests CommandLineUtility in 10 threads with 1000 operations
      */
    public void testCLU1_10_1000() throws Exception {
        run(args1, 10, 1000);
    }

    /** Tests CommandLineUtility in 100 threads with 1000 operations
      */
    public void testCLU1_100_1000() throws Exception {
        run(args1, 100, 1000);
    }

    /** Tests CommandLineUtility in one thread with 1000 operations
      */
    public void testCLU2_1_1000() throws Exception {
        run(args2, 1, 1000);
    }

    /** Tests CommandLineUtility in 10 threads with 1000 operations
      */
    public void testCLU2_10_1000() throws Exception {
        run(args2, 10, 1000);
    }

    /** Tests CommandLineUtility in 100 threads with 1000 operations
      */
    public void testCLU2_100_1000() throws Exception {
        run(args2, 100, 1000);
    }

    private void run(String[] args, int threadCount, int opCount) throws Exception {
        Worker[] threads = new Worker[threadCount];
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Worker(args, opCount);
            threads[i].start();
        }
        int failed = 0;
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
            if (threads[i].failed) failed++;
        }
        long total = System.currentTimeMillis() - start;
        System.out.println(threadCount+" "+opCount+" "+total+" "+((double)threadCount*opCount/total*1000)+" "+failed);
        if (failed > 0) fail("Test failed");
    }


    private class Worker extends Thread {

        CommandLineUtility util = getCommandLineUtility();
        String[] args;
        int count;
        boolean failed = false;

        Worker(String[] args, int count) {
            this.args = args;
            this.count = count;
        }

        public void run() {
            try {
                for ( int i = 0; i < count; i++) {
                    util.parse((String[])args.clone());
                }
            } catch (Exception e) {
                failed = true;
                e.printStackTrace();
            }
        }
    }

    private CommandLineUtility getCommandLineUtility() {
        CommandLineUtility util = new CommandLineUtility();
            
        try {    
            Switch swA = new Switch("a", false, 0, -1, null);
            util.addSwitch(swA);
            Switch swB = new Switch("b", true, 1, 10, null);
            util.addSwitch(swB);
            Switch swC = new Switch("c", false, 1, 1, null);
            util.addSwitch(swC);
        } catch (IllegalSwitchException e) {
            fail() ;
        }
        return util;
    }

    public static Test suite() {
        return new TestSuite(CommandLineUtilityTest.class);
    }
}
