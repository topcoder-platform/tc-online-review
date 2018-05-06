/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import java.io.File;
import java.io.IOException;

/**
 * the unil class for junit test only.
 * @author netsafe
 * @version 2.0
 */
final class TestHelper {
    /**
     * delete the dir and it's sub dir
     * @param filepath
     *            the dir to delete.
     * @throws IOException
     *             throws to caller
     */
    public static void deltree(String filepath) throws IOException {
        File f = new File(filepath);
        if (f.exists() && f.isDirectory()) {
            if (f.listFiles().length == 0) {
                return;
            } else {
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        deltree(delFile[j].getAbsolutePath());
                    }
                    delFile[j].delete();
                }
            }
            deltree(filepath);
        }

    }
}
