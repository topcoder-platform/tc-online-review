/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.archiving.accuracytests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.topcoder.util.archiving.ZipCreator;
import com.topcoder.util.archiving.ZipExtractor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test the functionality of <code>ZipCreator</code>.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ZipCreatorAccuracyTest extends TestCase {
    /**
     * The <code>ZipCreator</code> instance.
     */
    private ZipCreator creator =  new ZipCreator();
    
    /**
     * test ArchiveFiles(File, File);
     */
    public void testArchiveFilesFileFile() {
        ZipExtractor extractor = new ZipExtractor();
        File sourceDir = new File("test_files/src");
        File target = new File("test_files/result.zip");
        target.delete();
        assertEquals("target should not exist", false, target.exists());
        
        try {
            creator.archiveFiles(sourceDir, target);
            assertEquals(true, target.exists());
            
            List list = extractor.listFiles(target);
            assertEquals(true, list.size() == 3);
        } catch (Exception e) {
            fail("can't be fail");
        } finally {
            target.delete();
        }
    }

    /**
     * test archiveFiles(File, File, File[])
     */
    public void testArchiveFilesFileFileFileArray() {
        ZipExtractor extractor = new ZipExtractor();
        File sourceDir = new File("test_files/src");
        File target = new File("test_files/result.zip");
        target.delete();
        assertEquals("target should not exist", false, target.exists());
        
        try {
            File[] files = new File[2];
            files[0] = new File("test_files/src/input.txt");
            files[1] = new File("test_files/src/blah/");
            creator.archiveFiles(sourceDir, target, files);
            
            assertEquals(true, target.exists());
            
            List list = extractor.listFiles(target);
            assertEquals(2, list.size());
        } catch (Exception e) {
            fail("can't be fail");
        } finally {
            target.delete();
        }
    }

    /**
     * test archiveFiles(File, File, List)
     */
    public void testArchiveFilesFileFileList() {
        ZipExtractor extractor = new ZipExtractor();
        File sourceDir = new File("test_files/src");
        File target = new File("test_files/result.zip");
        target.delete();
        assertEquals("target should not exist", false, target.exists());
        
        try {
            List files = new ArrayList();
            files.add(new File("test_files/src/input.txt"));
            files.add(new File("test_files/src/blah/"));
            creator.archiveFiles(sourceDir, target, files);
            
            assertEquals(true, target.exists());
            
            List list = extractor.listFiles(target);
            assertEquals(2, list.size());
        } catch (Exception e) {
            fail("can't be fail");
        } finally {
            target.delete();
        }
    }
}
