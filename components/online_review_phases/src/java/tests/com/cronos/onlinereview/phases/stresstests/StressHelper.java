/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>
 * This is a stress test helper.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0.0
 */
public class StressHelper {

    /**
     * Field.
     */
    private File file;

    /**
     * Field.
     */
    private String fileName = "stressText.txt";

    /**
     * Default construct, for create file by file name.
     */
    public StressHelper() {

        file = new File(fileName);
    }

    /**
     * Clear the file content.
     */
    public void initStressFile() {

        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * Destroy the temporary file. and the result will output.
     */
    public void showAndDestroyFile() {

        BufferedReader input = null;
        String str;
        try {
            input = new BufferedReader(new FileReader(file));
            while ((str = input.readLine()) != null) {
                System.out.println(str);
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException e) {
            // ignore
        } finally {
            try {
                if (input != null) {
                    input.close();
                    input = null;
                }
                initStressFile();
            } catch (IOException e) {
                // ignore
            }

        }
    }

}
