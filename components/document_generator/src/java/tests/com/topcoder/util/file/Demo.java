/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.NodeListUtility;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSource;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;

import junit.framework.TestCase;

/**
 * <p>
 * The unit test class is used for component demonstration.
 * </p>
 * @author TCSDEVELOPER
 * @version 3.0
 * @since 2.1
 */
public class Demo extends TestCase {
    /**
     * <p>
     * Create DocumentGenerator and Manage TemplateSources.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testDemo1() throws Exception {
        // Create DocumentGenerator from constructor
        DocumentGenerator docgen = new DocumentGenerator();

        // A workable ConfigurationObject
        ConfigurationObject config = TestHelper.createConfigurationObject("demo.properties",
            "com.topcoder.util.file");
        // Create DocumentGenerator from factory
        docgen = DocumentGeneratorFactory.getDocumentGenerator(config);

        // Modify TemplateSources
        TemplateSource ts = new FileTemplateSource("file_source", config);
        // Any number of TemplateSource can be added
        docgen.setTemplateSource("file_source", ts);
        docgen.getTemplateSource("file_source");
        // The getter should return ts
        docgen.removeTemplateSource("file_source");
        // ts should be removed
        docgen.clearTemplateSources();
        // All template sources should be removed

        // Modify default TemplateSource
        docgen.setDefaultTemplateSource(ts);
        // Now ts is used as the default one
        docgen.getDefaultTemplateSource();
        // ts should be returned from getter
    }

    /**
     * <p>
     * User Scenario of Generating a Document.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testDemo2() throws Exception {
        Project project = new Project();
        project.setProjectType("design");

        Component configManager = new Component();
        configManager.setComponentName("configmanager");
        configManager.setComponentLongName("configuration_manager");
        configManager.setComponentVersion("2.1.5");

        Component baseException = new Component();
        baseException.setComponentName("baseexception");
        baseException.setComponentLongName("base_exception");
        baseException.setComponentVersion("2.0");

        project.setDependencies(new Component[] {configManager, baseException });

        // A document can be programmatically retrieved by using:
        // A workable ConfigurationObject
        ConfigurationObject config = TestHelper.createConfigurationObject("demo.properties",
            "com.topcoder.util.file");
        // Create DocumentGenerator from factory
        DocumentGenerator docGen = DocumentGeneratorFactory.getDocumentGenerator(config);

        Template buildTemplate = docGen.getTemplate("fileSource", "test_files/buildTemplate.txt");

        TemplateFields root = docGen.getFields(buildTemplate);
        Node[] nodes = root.getNodes();
        NodeList nodeList = new NodeList(nodes);

        // This is a lot simpler than looping through all the nodes and looking
        // up the respective project property. Note that it is also possible to
        // do this manually.
        NodeListUtility.populateNodeList(nodeList, project);
        // Applying the template can also be done without using the Document Generator,
        // using Template#applyTemplate method.
        String designBuildTemplate = docGen.applyTemplate(root);
        System.out.println(designBuildTemplate);

        XmlTemplateData xslTemplateData = new XmlTemplateData();
        xslTemplateData.setTemplateData(root);

        project.setProjectType("development");
        NodeListUtility.populateNodeList(new NodeList(nodes), project);
        String devBuildTemplate = docGen.applyTemplate(root);
        System.out.println(devBuildTemplate);

        displayNodes(nodes);
    }

    /**
     * <p>
     * Displays the nodes which are given in an array.
     * </p>
     * @param nodes
     *            The node array to be displayed
     */
    private void displayNodes(Node[] nodes) {
        for (int x = 0; x < nodes.length; x++) {
            if (nodes[x] instanceof Condition) {
                Condition condition = (Condition) nodes[x];
                System.out.println(condition.getName());
                System.out.println(condition.getValue());
                System.out.println(condition.getDescription());
                System.out.println(condition.getConditionalStatement());
            }

            if (nodes[x] instanceof Field) {
                Field field = (Field) nodes[x];
                System.out.println(field.getName());
                System.out.println(field.getValue());
                System.out.println(field.getDescription());
            }

            if (nodes[x] instanceof Loop) {
                Loop loop = (Loop) nodes[x];
                System.out.println(loop.getLoopElement());
                System.out.println(loop.getDescription());
            }
        }

    }
}
