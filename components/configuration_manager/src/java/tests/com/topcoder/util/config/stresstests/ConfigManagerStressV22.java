package com.topcoder.util.config.stresstests;

import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;

/**
 * @author mumujava
 *
 * This class stress tests the ConfigManager Component.
 * 
 * 
 * Repeats the following code in 2 threads:
 * 
 * if (mgr.existsNamespace(namespace)) {
 *     mgr.refresh(namespace);
 * }
 *
 */
public class ConfigManagerStressV22 extends TestCase {
	private static final int ITERATIONS = 1000;
	private static final int ThreadCount = 10;
	private static final String NAMESPACE = "com.topcoder.selfservice.cm_stresstest";
	private Throwable error;
	
	public void testRunThreads() throws Throwable {
		//clear the config namespaces
		ConfigManager cm = ConfigManager.getInstance();
        for (Iterator<String> itr = cm.getAllNamespaces(); itr.hasNext();) {
            cm.removeNamespace((String) itr.next());
        }
        cm.add("stress/CMTest.xml");
		
		Runnable runnable = new MyRunnable();
		Thread[] threads = new Thread[ThreadCount];
		for (int i = 0 ; i < ThreadCount; i++) {
			threads[i] = new Thread(runnable);
		}
		for (int i = 0 ; i < ThreadCount; i++) {
			threads[i].start();
		}
		for (int i = 0 ; i < ThreadCount; i++) {
			threads[i].join();
		}
		if (error != null) {
			throw error;
		}
	}
	
	private class MyRunnable implements Runnable {		
		public void run() {
			try {
				for (int i = 0; i < ITERATIONS; i++) {
					try {
						ConfigManager mgr = ConfigManager.getInstance();
						if (mgr.existsNamespace(NAMESPACE)) {
							mgr.refresh(NAMESPACE);
							System.out.println("finished refreshing");
						} else {
							System.out.println("NAMESPACE DOES NOT EXIST");
							throw new IllegalStateException("NAMESPACE DOES NOT EXIST");
						}
					} catch (Throwable e) {
						error = e;
						System.out.println("problem refreshing CM: " + e.getMessage());
						e.printStackTrace();
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				// ignore
			}
			
			System.out.println("runnable finished");
		}
	}

}
