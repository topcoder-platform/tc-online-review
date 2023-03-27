/*
 * Copyright (C) 2013-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHeaders;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.grpcclient.GrpcHelper;

/**
 * This class is the struts action class which is used for listing all projects.
 * <p>
 * Struts 2 Action objects are instantiated for each request, so there are no
 * thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SyncProjectAction extends BaseProjectAction {

    /**
     * Default constructor.
     */
    public SyncProjectAction() {
    }

    public String execute() throws BaseException {
        String projectId = request.getParameter("projectId");
        String tables = request.getParameter("tables");
        List<String> tableNames = new ArrayList<>();
        if (tables != null && !tables.isEmpty()) {
            tableNames = Arrays.asList(tables.split(","));
        }
        if (projectId.isEmpty() || tableNames.isEmpty()) {
            return NONE;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isEmpty()) {
            return NONE;
        }
        String[] headerParts = authHeader.split(" ");
        if (headerParts.length < 2) {
            return NONE;
        }
        String token = headerParts[1];

        DecodedJWT jwt;
        try {
            jwt = AuthorizationHelper.validateJWTToken(token);
        } catch (Exception e) {
            return NONE;
        }
        boolean hasAccess = false;
        for (String claimName : jwt.getClaims().keySet()) {
            if (claimName.endsWith("/roles")) {
                Claim claim = jwt.getClaim(claimName);
                for (String role : claim.asArray(String.class)) {
                    if (role.equals("administrator")) {
                        hasAccess = true;
                    }
                }
            }
        }
        if (!hasAccess) {
            return NONE;
        }

        GrpcHelper.getSyncServiceRpc().manualSync(Long.valueOf(projectId), tableNames);
        return NONE;
    }
}
