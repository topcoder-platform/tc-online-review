/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.web.common.security.SSOCookieService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Main servlet class of the component.
 * This class extends HttpServlet class in order to process Ajax requests and produce Ajax responses.
 *
 * This class keeps a map of all Ajax request handlers,
 * and when the doPost method is called it follows these steps to process the request:
 * <ol>
 * <li>Get the user ID from the HttpSession.</li>
 * <li>Parse the Ajax request XML stream to produce an AjaxRequest object.</li>
 * <li>Pass the user id and the Ajax request to the correct request handler.</li>
 * <li>Write back the Ajax response XML produced by the request handler.</li>
 * </ol>
 *
 * When the Ajax request is incorrect, invalid or the target handler was not found,
 * then this servlet will return an error response to the client,
 * and log that error using the Logging Wrapper component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable and thread safe. all accesses to its internal state are read only once.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @author George1
 * @version 1.1
 */
public final class AjaxSupportServlet extends HttpServlet {
	/**
     * Represents the property name of handlers.
     */
    private static final String HANDLERS_PROPERTY = "Handlers";

    /**
     * Represents the property name for SSO Cookie Service object factory key.
     */
    private static final String SSO_COOKIE_SERVICE_KEY_PROPERTY = "SSOCookieServiceKey";

    /**
     * Represents the namespace to retrieve the properties.
     */
    private static final String NAMESPACE = "com.cronos.onlinereview.ajax";
    /**
     * The logger.
     */
    private static final Log logger = LogManager.getLog(AjaxSupportServlet.class.getName());

    /**
     * <p>An <code>AuthCookieManager</code> to be used for user authentication based on cookies.</p>
     *
     * @since 1.1
     */
    private static SSOCookieService ssoCookieService;

   
    /**
     * <p>
     * The Ajax request handlers map, as defined in the configuration.
     * This variable is immutable, both the variable and its content.
     * It is filled by the "init" method with handlers data. The "destroy" method must clear its content.<br><br>
     * <ul>
     * <li>Map Keys - are of type String, they can't be null, or empty strings,
     *                they represents the operation name handled by the handler</li>
     * <li>Map Values - are of type AjaxRequestHandler, they can't be null</li>
     * </ul>
     * </p>
     */
    private final Map<String, AjaxRequestHandler> handlers = new HashMap<String, AjaxRequestHandler>();

    /**
     * <p>
     * Creates an instance of this class.
     * </p>
     */
    public AjaxSupportServlet() {
        // do nothing
    }

    /**
     * <p>
     * Initialize the state of this servlet with configuration data.
     * </p>
     *
     * @param config the initial configuration of the servlet
     * @throws ServletException if an exception was caught
     */
    public void init(ServletConfig config) throws ServletException {

    	logger.log(Level.DEBUG, "Init Ajax Support Servlet from namespace:" + NAMESPACE);
        super.init(config);

        // create a new instance of ConfigManager class
        ConfigManager cm = ConfigManager.getInstance();

        try {
            ObjectFactory factory = AjaxSupportHelper.createObjectFactory();

            String ssoCookieServiceKey = ConfigManager.getInstance().getString(NAMESPACE, SSO_COOKIE_SERVICE_KEY_PROPERTY);
            ssoCookieService = (SSOCookieService) factory.createObject(ssoCookieServiceKey);
            
            // get the list of all the Ajax request handlers names from the config manager
            String[] handlerNames = cm.getStringArray(NAMESPACE, HANDLERS_PROPERTY);
            if (handlerNames != null) {
            	for (String handlerName : handlerNames) {
                    if (handlerName == null || handlerName.trim().length() == 0) {
                    	logger.log(Level.FATAL, "The handler name should not be null/empty in namespace:" + NAMESPACE);
                        throw new ServletException("The handler name should not be null/empty.");
                    }
                    logger.log(Level.DEBUG, "Get property array [" + HANDLERS_PROPERTY
                    		+ "] with one value :" + handlerName + " from namespace:" + NAMESPACE);
                    AjaxRequestHandler handler = (AjaxRequestHandler) factory.createObject(handlerName);
                    this.handlers.put(handlerName, handler);
                }
            }
        } catch (UnknownNamespaceException e) {
        	logger.log(Level.FATAL, "The namespace[" + NAMESPACE + "] is not loaded.");
            throw new ServletException("The namespace can't be found.", e);
        } catch (InvalidClassSpecificationException e) {
        	logger.log(Level.FATAL, "Can not create object.\n" + AjaxSupportHelper.getExceptionStackTrace(e));
            throw new ServletException("Can't create handler : " + e.getMessage() + ", " + e.getCause().getMessage(), e);
        } catch (ConfigurationException e) {
        	logger.log(Level.FATAL, "Can not create object or sso cookie service.\n" + AjaxSupportHelper.getExceptionStackTrace(e));
            throw new ServletException("Can't create factory or sso cookie service.", e);
        }
    }

    /**
     * <p>
     * Destroy the state of this servlet.
     * </p>
     */
    public void destroy() {
        this.handlers.clear();
    }

    /**
     * <p>
     * Process an Ajax request by forwarding it to the appropriate Ajax request handler.
     * </p>
     *
     * @param request an HttpServletRequest object that contains the request the client has made of the servlet
     * @param response an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if the request for the POST could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        Long userId;
        try {
            userId = ssoCookieService.getUserIdFromSSOCookie(request);
        } catch (Exception e) {
            logger.log(Level.ERROR, e, "Could not retrieve user ID from SSO cookie.");
            throw new ServletException("Could not retrieve user ID from SSO cookie.", e);
        }

        // get the reader from the request
        Reader reader = request.getReader();

        try {
            // parse the content from the reader
            AjaxRequest ajaxRequest = AjaxRequest.parse(reader);

            // get the handler from the map
            AjaxRequestHandler handler = handlers.get(ajaxRequest.getType());

            // if the handler is null, response it with status "request error"
            if (handler == null) {
                AjaxSupportHelper.responseAndLogError(ajaxRequest.getType(), "Request error",
                        "There is no corresponding handler : " + ajaxRequest.getType(), response);
                return;
            }

            // serve the request and get the response
            AjaxResponse resp = handler.service(ajaxRequest, userId);
            
            if (resp == null) {
                AjaxSupportHelper.responseAndLogError(ajaxRequest.getType(),
                        "Server error", "Server can't satisfy this request", response);
                return;
            }
            if (!"success".equalsIgnoreCase(resp.getStatus())) {
            	StringBuilder buf = new StringBuilder();
                for (Object param : ajaxRequest.getAllParameterNames()) {
                    String paramName = (String) param;
                    buf.append(',')
                            .append(paramName)
                            .append(" = ")
                            .append(ajaxRequest.getParameter(paramName));
                }
            	logger.log(Level.WARN, "problem handling request, status: " + resp.getStatus() +
            			"\ntype: " + resp.getType() +
            			"\nparams: " + (buf.length() == 0 ? "" : buf.substring(1)) + 
            			"\nuserId: " + userId +
            			"\nencoding: " + request.getCharacterEncoding());
            }
            // response at last, this is the normal condition
            AjaxSupportHelper.doResponse(response, resp);

        } catch (RequestParsingException e) {
            // if there is a parsing error then create an AjaxResponse containing the error message,
            // use the status "invalid request error"
            AjaxSupportHelper.responseAndLogError("Unknown", "invalid request error", e.getMessage(), response, e);
        }
    }

}
