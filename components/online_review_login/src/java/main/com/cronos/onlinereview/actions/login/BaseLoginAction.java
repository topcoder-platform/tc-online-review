/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.login;

import com.cronos.onlinereview.login.AuthResponseParser;
import com.cronos.onlinereview.login.ConfigurationException;

import com.opensymphony.xwork2.ActionSupport;

import com.topcoder.security.authenticationfactory.AuthenticationFactory;
import com.topcoder.security.authenticationfactory.Authenticator;
import com.topcoder.security.TCSubject;

import com.topcoder.web.common.security.SSOCookieService;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <code>BaseLoginAction</code> class defines base class of the Login/logout actions.
 *
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseLoginAction extends ActionSupport
    implements ServletRequestAware, ServletResponseAware {
    /** Represents the request. */
    protected HttpServletRequest request;

    /** Represents the response. */
    protected HttpServletResponse response;

    /** Represents the authentication factory. */
    private AuthenticationFactory authenticationFactory;

    /** Represents the logger name. */
    private String loggerName;

    /** Represents the authenticator name. */
    private String authenticatorName;

    /** Represents the auth response parser. */
    protected AuthResponseParser authResponseParser;

    /** Represents the sso cookie service. */
    protected SSOCookieService ssoCookieService;

    /** Represents the authenticator. */
    protected Authenticator authenticator;

    /** Represents the logger. */
    protected Log logger;

    /**
     * Used for dynamic result.
     */
    private String url;

    /**
     * Set the request to the namesake field.
     * @param request the http request
     */
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Set the response to the namesake field.
     * @param response the http response
     */
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Use the injected values to initialize the action.
     * @throws ConfigurationException if the authenticationFactory, authenticatorName are not injected correctly
     */
    public void init() throws ConfigurationException {
        checkNull(authenticationFactory, "authenticationFactory is not configured");
        checkNull(authenticatorName, "authenticatorName is not configured");
        authenticator = authenticationFactory.getAuthenticator(authenticatorName);
        checkNull(authenticator, "Cannot create authenticator: " + authenticatorName);

        if ((loggerName != null) && (loggerName.trim().length() != 0)) {
            logger = LogManager.getLog(loggerName);
        } else {
            logger = null;
        }
    }

    /**
     * Check if the given object is null.
     * @param obj the object to be checked
     * @param message the exception message
     * @throws ConfigurationException if the object is null
     */
    private void checkNull(Object obj, String message)
        throws ConfigurationException {
        if (obj == null) {
            throw new ConfigurationException(message);
        }
    }

    /**
     * Record the exception to <code>logger</code>(if exists).
     *
     * @param e
     *            the exception instance
     */
    protected void recordException(Exception e) {
        if (logger != null) {
            // record to logger
            StringWriter sw = new StringWriter();
            PrintWriter out = new PrintWriter(sw);
            e.printStackTrace(out);
            logger.log(Level.ERROR, sw.getBuffer());
        }
    }

    /**
     * Getter of url.
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter of url.
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Setter of authenticationFactory.
     * @param authenticationFactory the authenticationFactory to set
     */
    public void setAuthenticationFactory(
        AuthenticationFactory authenticationFactory) {
        this.authenticationFactory = authenticationFactory;
    }

    /**
     * Setter of authenticatorName.
     * @param authenticatorName the authenticatorName to set
     */
    public void setAuthenticatorName(String authenticatorName) {
        this.authenticatorName = authenticatorName;
    }

    /**
     * Setter of authResponseParser.
     * @param authResponseParser the authResponseParser to set
     */
    public void setAuthResponseParser(AuthResponseParser authResponseParser) {
        this.authResponseParser = authResponseParser;
    }

    /**
     * Setter of ssoCookieService.
     * @param ssoCookieService the ssoCookieService to set
     */
    public void setSsoCookieService(SSOCookieService ssoCookieService) {
        this.ssoCookieService = ssoCookieService;
    }

    /**
     * Setter of authenticator.
     * @param authenticator the authenticator to set
     */
    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * Setter of loggerName.
     * @param loggerName the loggerName to set
     */
    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }
}
