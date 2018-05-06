/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.file.accuracytests;

import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import java.io.File;

import junit.framework.TestCase;

/**
 * <p>Test the FileTemplateSource class</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class FileTemplateSourceAccuracyTests extends TestCase {
    /**
     * The template file name
     */
    String template_file = "test_files/acc.tpl";

    /**
     * FileTemplateSource instance for test..
     */
    FileTemplateSource fts = null;

    /**
     * Initialize FileTemplateSource instance
     */
    public void setUp() {
        fts = new FileTemplateSource();
    }

    /**
     * Tests addTemplate and GetTemplate method.
     * 
     * @throws TemplateSourceException should not throw
     */
    public void testAddTemplate() throws TemplateSourceException {
        File file = new File(template_file);

        fts.addTemplate(template_file, "Accuracy test");
        assertTrue(file.exists());
        assertEquals("Accuracy test", fts.getTemplate(template_file));
        file.delete();
    }

    /**
     * Tests removeTemplate method.
     * 
     * @throws TemplateSourceException should not throw
     */
    public void testRemoveTemplate() throws TemplateSourceException {
        File file = new File(template_file);

        fts.addTemplate(template_file, "Accuracy test");
        assertTrue(file.exists());
        fts.removeTemplate(template_file);
        assertTrue(!file.exists());
    }
}
