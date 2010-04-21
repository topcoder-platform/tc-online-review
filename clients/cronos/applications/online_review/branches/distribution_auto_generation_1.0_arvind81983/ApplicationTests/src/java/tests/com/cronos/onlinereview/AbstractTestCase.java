/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.cronos.onlinereview.tests.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

abstract public class AbstractTestCase extends TestCase
{
    private WebClient client;
    
    public AbstractTestCase()
    {
        client = new WebClient(Configuration.getBrowserVersion());
        DefaultCredentialsProvider auth = new DefaultCredentialsProvider();
        auth.addCredentials(Configuration.getHttpAuthUsername(), Configuration.getHttpAuthPassword());
        client.setRedirectEnabled(true);
        client.setCredentialsProvider(auth);
        client.setJavaScriptEnabled(false);
        client.setThrowExceptionOnScriptError(false);
    }
    
    public WebClient getWebClient()
    {
        return client;
    }
    
    public List findHtmlElementsByAttribute(HtmlElement source, String elementName, String attributeName, String attributeValue)
    {
        List elements = new ArrayList();
        
        if(source != null && elementName != null && attributeName != null && attributeValue != null)
        {
            Iterator iter = source.getAllHtmlChildElements();
            while(iter.hasNext())
            {
                HtmlElement child = (HtmlElement)iter.next();
                
                if(child.getTagName().equals(elementName) && attributeValue.equals(child.getAttributeValue(attributeName)))
                    elements.add(child);
            }
        }
        
        return elements;
    }
    
    public HtmlElement findOneHtmlElementByAttribute(HtmlElement source, String elementName, String attributeName, String attributeValue)
    {
        List elements = findHtmlElementsByAttribute(source, elementName, attributeName, attributeValue);
        
        if(elements.isEmpty())
            throw new ElementNotFoundException(elementName, attributeName, attributeValue);
        
        return (HtmlElement)elements.get(0);
    }
    
    public HtmlAnchor getFirstAnchorByText(HtmlPage page, String text)
    {
        HtmlAnchor anchor = null;
        
        if(page != null && text != null)
        {
            try
            {
                anchor = page.getFirstAnchorByText(text);
            }
            catch(ElementNotFoundException x)
            {
                // Ignore
            }
        }
        
        return anchor;
    }
    
    public HtmlInput getInputByValue(HtmlForm form, String value)
    {
        HtmlInput input = null;
        
        if(form != null && value != null)
        {
            try
            {
                input = form.getInputByValue(value);
            }
            catch(ElementNotFoundException x)
            {
                // Ignore
            }
        }
        
        return input;
    }
    
    public HtmlPage login(HtmlPage page, User user) throws IOException
    {
        return (HtmlPage)client.getPage(Configuration.getAdminUrl() + "/login.do?method=login&userName=" + user.getUserName() + "&password=" + user.getPassword());
        /*
        HtmlElement document = page.getDocumentElement();
        ((HtmlInput)findOneHtmlElementByAttribute(document, "input", "name", "userName")).setValueAttribute(user.getUserName());
        ((HtmlInput)findOneHtmlElementByAttribute(document, "input", "name", "password")).setValueAttribute(user.getPassword());
        return (HtmlPage)((HtmlInput)findOneHtmlElementByAttribute(document, "input", "type", "submit")).click();
        */
    }
    
    public void assertNotLoginPage(HtmlPage page)
    {
        try
        {
            page.getFormByName("loginForm");
            fail("Login failed.  Flow was returned to login page.");
        }
        catch(ElementNotFoundException x)
        {
            // Passed
        }
    }
    
    public HtmlAnchor getFirstAnchorByImage(HtmlPage page, String imagePath)
    {
        DomNode node = findOneHtmlElementByAttribute(page.getDocumentElement(), "img", "src", imagePath);
        
        while(node != null && !(node instanceof HtmlAnchor))
            node = node.getParentNode();
        
        return (node != null ? (HtmlAnchor)node : null);
    }
    
    public HtmlPage clickCreateScorecard(HtmlPage page) throws IOException
    {
        return (HtmlPage)page.getFormByName("scorecardForm").submit();
    }
}
