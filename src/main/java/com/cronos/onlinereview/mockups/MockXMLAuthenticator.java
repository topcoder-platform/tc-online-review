/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.mockups;

import com.topcoder.onlinereview.component.authenticationfactory.AbstractAuthenticator;
import com.topcoder.onlinereview.component.authenticationfactory.AuthenticateException;
import com.topcoder.onlinereview.component.authenticationfactory.ConfigurationException;
import com.topcoder.onlinereview.component.authenticationfactory.Principal;
import com.topcoder.onlinereview.component.authenticationfactory.Response;
import com.topcoder.onlinereview.component.security.TCSubject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A mock implementation of <code>AbstractAuthenticator</code>.
 * <p>
 * It is implemented for testing purpose. It read login handles from an XML file.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class MockXMLAuthenticator extends AbstractAuthenticator {

    /**
     * The users x ID map.
     */
    private static final Map<String, TCSubject> usersMap = new HashMap<String, TCSubject>();

    /**
     * The users x password map.
     */
    private static final Map<String, String> passwordsMap = new HashMap<String, String>();

    /**
     * Creates <code>AbstractAuthenticator</code> concrete instance for test.
     * 
     * @throws ConfigurationException from super class.
     */
    public MockXMLAuthenticator(String xmlfile) throws ConfigurationException {

        InputStream input;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            input = getClass().getResourceAsStream(xmlfile);
            Document doc = docBuilder.parse(input);
            NodeList mappings = doc.getElementsByTagName("user");
            for (int i = 0; i < mappings.getLength(); i++) {
                Node node = mappings.item(i);
                NamedNodeMap attributes = node.getAttributes();
                String userName = attributes.getNamedItem("name").getNodeValue();
                String password = attributes.getNamedItem("password").getNodeValue();
                String id = attributes.getNamedItem("id").getNodeValue();
                usersMap.put(userName, new TCSubject(Long.parseLong(id)));
                passwordsMap.put(userName, password);
            }

        } catch (NumberFormatException ex) {
            throw new ConfigurationException("Invalid id", ex);
        } catch (ParserConfigurationException ex) {
            throw new ConfigurationException("Unable to read XML file", ex);
        } catch (SAXException ex) {
            throw new ConfigurationException("Unable to read XML file", ex);
        } catch (IOException ex) {
            throw new ConfigurationException("Unable to read XML file", ex);
        }
    }

    /**
     * The actual authenticate method.
     * 
     * @return response.
     * @param principal the principal instance.
     * 
     * @throws AuthenticateException if any error occurs.
     */
    protected Response doAuthenticate(Principal principal) throws AuthenticateException {
        String userName = (String) principal.getValue("userName");
        String password = (String) principal.getValue("password");

        if (!passwordsMap.containsKey(userName)) {
            return new Response(false, "Failed");
        }
        
        if (!password.equalsIgnoreCase(passwordsMap.get(userName))) {
            return new Response(false, "Failed");
        }

        return new Response(true, "Succeeded", usersMap.get(userName));
    }
}
