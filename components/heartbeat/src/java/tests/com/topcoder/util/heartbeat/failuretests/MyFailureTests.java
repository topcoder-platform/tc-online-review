package com.topcoder.util.heartbeat.failuretests;

import java.util.List;
import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;
import java.io.IOException;
import java.io.Serializable;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.net.Socket;


/**
 * <p>This test case contains all failure tests.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */

public class MyFailureTests extends TestCase {


  /**
   * tests proper failure of add methods
   */
    public void testAddFailure(){

        HeartBeatManager man = new HeartBeatManager();

        try{
            man.add(null, 70);
            fail();
        }
        catch(NullPointerException a){}

        try{
            man.add(new SocketHeartBeat(new Socket(), new File("c:\none")), 0);
            fail();
        }
        catch(IllegalArgumentException a){}

    }


    /**
     * tests proper failure of remove methods
     */
    public void testRemoveFailure(){

        HeartBeatManager man = new HeartBeatManager();

        try{
            man.remove((HeartBeat)null);
            fail();
        }
        catch(NullPointerException e){}

    }


    /**
     * tests proper failure of contains methods
     */
    public void testContainsFailure(){

        HeartBeatManager man = new HeartBeatManager();

        try{
            man.contains((HeartBeat)null);
            fail();
        }
        catch(NullPointerException e){}
    }


    /**
     * tests proper failure of manual add methods
     */
    public void testManualAddFailure(){

        ManualTrigger man = new ManualTrigger();

        try{
            man.add((HeartBeat)null);
            fail();
        }
        catch(NullPointerException a){}

    }


    /**
     * tests proper failure of manual remove methods
     */
    public void testManualRemoveFailure(){

        ManualTrigger man = new ManualTrigger();

        try{
            man.remove((HeartBeat)null);
            fail();
        }
        catch(NullPointerException e){}

    }


    /**
     * tests proper failure of manual contains methods
     */
    public void testManualContainsFailure(){

        ManualTrigger man = new ManualTrigger();

        try{
            man.contains((HeartBeat)null);
            fail();
        }
        catch(NullPointerException e){}
    }


    /**
     * tests proper failure of output methods
     */
    public void testOutputFailure() throws Exception{

        try{
            OutputStreamHeartBeat out = new OutputStreamHeartBeat(null, new File("none"));
            fail();
        }
        catch(NullPointerException e){}

        try{
            OutputStreamHeartBeat out = new OutputStreamHeartBeat(new FileOutputStream(new File("none")), null);
            fail();
        }
        catch(NullPointerException e){}
    }


    /**
     * tests proper failure of socket methods
     */
    public void testSocketFailure(){

        try{
            SocketHeartBeat out = new SocketHeartBeat(null, new File("none"));
            fail();
        }
        catch(NullPointerException e){}

        try{
            SocketHeartBeat out = new SocketHeartBeat(new Socket(), null);
            fail();
        }
        catch(NullPointerException e){}
    }

}