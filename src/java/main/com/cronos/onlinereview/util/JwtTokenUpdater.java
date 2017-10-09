/*
 * Copyright (C) 2017 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.apache.xerces.utils.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * Utility class to check and update token from  cookie
 *
 * @author TCCoder
 * @version 1.0
 */
public class JwtTokenUpdater {

	/**
     * The logger
     */
    private static final Logger logger = Logger.getLogger(JwtTokenUpdater.class);

    /**
     * v3 token
     */
    private String token;

    /**
     * The v2Token
     */
    private String v2Token = null;

    protected static final String ERROR_MESSAGE_FORMAT = "Service URL:%s, HTTP Status Code:%d, Error Message:%s";

    private static final String AUTHORIZATION_PARAMS = "{\"param\": {\"externalToken\": \"%s\"}}";

    /**
     * The objectMapper
     */
    protected static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * Create JwtTokenUpdater
     *
     */
    public JwtTokenUpdater() {

    }

    /**
     * Check token from cookie
     *
     * @return this class instance
     * @throws Exception if any error occurs
     */
    public JwtTokenUpdater check() throws Exception {
        Cookie[] cookies = ServletActionContext.getRequest().getCookies();

        Cookie jwtCookieV3 = null;
        Cookie jwtCookieV2 = null;
        String jwtCookieName = ConfigHelper.getV3jwtCookieName();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(jwtCookieName)) {
                	jwtCookieV3 = c;
                } else if (c.getName().equals(ConfigHelper.getV2jwtCookieName())) {
                	jwtCookieV2 = c;
                }  
            }
        }

        if (jwtCookieV2 == null) {
        	throw new BaseException("Please re-login");
        }
        boolean valid = true;
        if (jwtCookieV3 != null) {
        	String[] tokenSplit = jwtCookieV3.getValue().split("\\.");
        	if (tokenSplit.length <= 1) {
        		valid = false;
        	} else {
        		StringBuffer payloadStr = new StringBuffer(tokenSplit[1]);
                while (payloadStr.length() % 4 != 0) {
                	payloadStr.append('=');
                }
                
                String payload = new String(Base64.decode(payloadStr.toString().getBytes(StandardCharsets.UTF_8)));
                JsonNode jsonNode = objectMapper.readValue(payload.toString(), JsonNode.class);

                long exp = jsonNode.get("exp").asLong();
                Date expDate = new Date(exp * 1000);
                logger.info("token expire at: " + expDate);
                if (expDate.before(new Date())) {
                	valid = false;
                }
        	}
        }
        if (jwtCookieV3 == null || !valid) {
        	String newToken = getRefreshTokenFromApi(jwtCookieV2.getValue());
        	Cookie cookie = new Cookie(jwtCookieName, newToken);
        	cookie.setMaxAge(-1);
            cookie.setDomain(ConfigHelper.getSsoDomainForV3jwtCookie());
            cookie.setPath("/");
            ServletActionContext.getResponse().addCookie(cookie);
            jwtCookieV3 = cookie;
        }
        
        this.token = jwtCookieV3.getValue();
        this.v2Token = jwtCookieV2.getValue();
        
        return this;
    }


    /**
     * Get refresh token from api
     *
     * @param oldToken the oldToken to use
     * @throws BaseException if any error occurs
     * @return the String result
     */
    private String getRefreshTokenFromApi(String oldToken) throws BaseException {
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	try {
	        String authUrl = ConfigHelper.getV3jwtAuthorizationUrl();
	        URI authorizationUri = new URI(authUrl);
	        HttpPost httpPost = new HttpPost(authorizationUri);
	        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	
	        StringEntity body = new StringEntity(String.format(AUTHORIZATION_PARAMS, oldToken));
	        httpPost.setEntity(body);
	        HttpResponse response = httpClient.execute(httpPost);
	        HttpEntity entity = response.getEntity();
	        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
	            throw new BaseException("Failed to get the token:" + response.getStatusLine().getReasonPhrase());
	        }
	        String data = EntityUtils.toString(entity);
	        JsonNode result = objectMapper.readTree(data);
	
	        return result.path("result").path("content").path("token").asText();
    	} catch (BaseException be) {
    		throw be;
    	} catch (Exception exp) {
    		throw new BaseException("Failed to refresh the token", exp);
    	} finally {
            httpClient.getConnectionManager().shutdown();
        }
    }


    /**
     * True if user has logge-in and has v2token
     * Must be called after {@link #check()}
     * @return the login result
     */
    public boolean isLoggedIn() {
        return v2Token != null && !v2Token.isEmpty();
    }

    /**
     * Get v3 token
     * Must be called after {@link #check()}
     * @return
     */
    public String getToken() {
        return token;
    }
}