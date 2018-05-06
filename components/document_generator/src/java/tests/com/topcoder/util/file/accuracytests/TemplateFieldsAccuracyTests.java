/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.file.accuracytests;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

import junit.framework.TestCase;

/**
 * <p>Test the TemplateFields class</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class TemplateFieldsAccuracyTests extends TestCase 
{
    /**
     * TemplateFields instance for test..
     */
    TemplateFields tf = null;

    /**
     * Tests constructor and property.
     */
    public void testTemplateFields() 
    {
        Node[] nodes = new Node[2];
        XsltTemplate template = new XsltTemplate();
            
        nodes[0] = new Field("testName", "testVal", "testDesc", true);
        nodes[1] = new Field("testName2", "testVal2", "testDesc2", false);
        tf = new TemplateFields(nodes, template);

        nodes = tf.getNodes();
        assertEquals(2, nodes.length);
        assertEquals("testName", ((Field) nodes[0]).getName());
        assertEquals("testName2", ((Field) nodes[1]).getName());
        assertTrue(tf.getTemplate() == template);
    }
}
