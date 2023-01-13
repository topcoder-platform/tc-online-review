/*
 * Copyright (C) 2006 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.cronos.onlinereview.Constants;
import com.topcoder.onlinereview.component.dataaccess.ProjectDataAccess;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.external.ExternalUser;
import com.topcoder.onlinereview.component.external.UserRetrieval;
import com.topcoder.onlinereview.component.jwt.InvalidTokenException;
import com.topcoder.onlinereview.component.jwt.JWTException;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.resource.ResourceFilterBuilder;
import com.topcoder.onlinereview.component.resource.ResourceRole;
import com.topcoder.onlinereview.component.search.filter.AndFilter;
import com.topcoder.onlinereview.component.search.filter.Filter;
import com.topcoder.onlinereview.component.security.groups.model.GroupPermissionType;
import com.topcoder.onlinereview.component.security.groups.model.ResourceType;
import com.topcoder.onlinereview.component.security.groups.services.AuthorizationService;
import com.topcoder.onlinereview.component.webcommon.SSOCookieService;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

import java.security.interfaces.RSAPublicKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.topcoder.onlinereview.component.util.SpringUtils.getBean;

/**
 * This class provides helper methods that can be used to determine if the user
 * has particular role, or to check if user is authorized to perform particular
 * action. The methods are designed to check directly the
 * <code>HttpServletRequest</code>, as it simplifies their usage.
 * <p>
 * This class is thread safe as it contains only static methods and no inner
 * state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class AuthorizationHelper {

    /**
     * This member variable is an integer constant that specifies the value which is
     * used to denote that no user is logged into application.
     */
    public static final long NO_USER_LOGGED_IN_ID = -1;

    /**
     * Represent the redirect back url constant.
     */
    public static final String REDIRECT_BACK_URL_ATTRIBUTE = "redirectBackUrl";

    /**
     * Represent the authorization service constant.
     */
    private static final String AUTHORIZATION_SERVICE_NAME = "authorizationService";

    /**
     * <p>
     * An <code>SSOCookieService</code> to be used for user authentication based on
     * cookies.
     * </p>
     */
    private static SSOCookieService ssoCookieService;

    /**
     * Private constructor, just to prevent instantiation of the class.
     */
    public AuthorizationHelper() {
        // Just do nothing
    }

    /**
     * This static method puts into session an attribute which specifies the address
     * of the page to return to after successful login.
     *
     * @param request        an <code>HttpServletRequest</code> object containing
     *                       additional information, and which session will receive
     *                       the newly set attribute (if the address can be
     *                       determined).
     * @param getFromReferer determines whether the method should use Referer
     *                       request header, or record current URI the user is
     *                       trying to access. This is required for save type of
     *                       actions, since these actions often contain additional
     *                       information passed as form's fields in request, and so
     *                       returning to such kind of page by just its URI will
     *                       lead to error.
     */
    public static void setLoginRedirect(HttpServletRequest request, boolean getFromReferer) {
        if (getFromReferer) {
            final String referer = request.getHeader("Referer");

            if (referer != null && referer.trim().length() != 0) {
                request.getSession().setAttribute(REDIRECT_BACK_URL_ATTRIBUTE, referer);
            }
            return;
        }

        StringBuilder redirectBackUrl = new StringBuilder();

        redirectBackUrl.append(request.getScheme());
        redirectBackUrl.append("://");
        redirectBackUrl.append(request.getServerName());
        redirectBackUrl.append(':');
        redirectBackUrl.append(request.getServerPort());
        redirectBackUrl.append(request.getRequestURI());
        if (request.getQueryString() != null && request.getQueryString().trim().length() != 0) {
            redirectBackUrl.append('?');
            redirectBackUrl.append(request.getQueryString());
        }

        request.getSession().setAttribute(REDIRECT_BACK_URL_ATTRIBUTE, redirectBackUrl.toString());
    }

    /**
     * This static method removes the attribute that specifies the address of the
     * page to return to. This is needed in case the user did not use the Login page
     * he was redirected to, but went to some page which he was allowed to see.
     * Then, (possibly after some time) they decide to login and click on Login
     * link. In this case, after logging in, they get redirected to the previous
     * page of failure, since redirect attribute is already in the session. This
     * method needs to be called by every action that gets past the check whether
     * the user is logged in.
     *
     * @param request an <code>HttpServletRequest</code> object that possibly
     *                contains redirect attribute to be removed.
     */
    public static void removeLoginRedirect(HttpServletRequest request) {
        request.getSession().removeAttribute(REDIRECT_BACK_URL_ATTRIBUTE);
    }

    /**
     * This static method returns the ID of the user currently logged in.
     *
     * @return the ID of the currently logged in user, or
     *         <code>NO_USER_LOGGED_IN_ID</code> if no user has been logged in.
     * @param request an <code>HttpServletRequest</code> object containing the
     *                information about the user possibly logged in.
     */
    public static long getLoggedInUserId(HttpServletRequest request) {
        Long userId = null;
        try {
            userId = getSsoCookieService().getUserIdFromSSOCookie(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (userId != null) ? (userId) : NO_USER_LOGGED_IN_ID;
    }

    /**
     * This static method checks whether any user has been logged in into the
     * application.
     *
     * @return <code>true</code> if there is a user logged in, <code>false</code> if
     *         there isn't.
     * @param request an <code>HttpServletRequest</code> object containing the
     *                information about the possibly logged in user.
     */
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        return (getLoggedInUserId(request) != NO_USER_LOGGED_IN_ID);
    }

    /**
     * This static method gathers the Resource Roles that the currently logged in
     * user has. It then places the gathered roles into the object specified by the
     * <code>request</code> parameter.
     *
     * @param request an <code>HttpServletRequest</code> object containing
     *                additional information such as the ID of the currently logged
     *                in user that will help gathering Resource Roles.
     *                <p>
     *                On return, the object specified by this parameter will receive
     *                the list of all gathered roles.
     *                </p>
     * @throws BaseException if any error occurs.
     */
    public static void gatherUserRoles(HttpServletRequest request) throws BaseException {
        // Prepare the set which will contain all the roles the user has
        Set<String> roles = new HashSet<String>();

        // Place the set into the request.
        // It will be populated with the roles a little bit later in this method
        request.setAttribute("roles", roles);

        // Add "Public" role, as all users will have one out of project's context
        roles.add(Constants.PUBLIC_ROLE_NAME);

        // If the user is not logged in, he cannot have any other roles
        if (!isUserLoggedIn(request)) {
            return;
        }

        // Obtain an instance of the User Retrieval object
        UserRetrieval usrMgr = ActionsHelper.createUserRetrieval(request);
        // Get External User object for the currently logged in user
        ExternalUser extUser = usrMgr.retrieveUser(getLoggedInUserId(request));
        // Place handle of the user into session as attribute
        request.getSession().setAttribute("userHandle", extUser.getHandle());
        request.getSession().setAttribute("userFirstName", extUser.getFirstName());
        request.getSession().setAttribute("userLastName", extUser.getLastName());
        request.getSession().setAttribute("userEmail", extUser.getEmail());

        // Perform search for resources
        Resource[] resources = ActionsHelper.searchUserResources(getLoggedInUserId(request), null);

        // Iterate over all resources retrieved and take into
        // consideration only those ones that have Manager role
        for (Resource resource : resources) {
            if (resource.getProject() != null || resource.getPhase() != null) {
                continue;
            }

            // Get the role this resource has
            ResourceRole role = resource.getResourceRole();
            // If this resource has the Manager role and no projects associated with it
            if (role.getName().equalsIgnoreCase(Constants.MANAGER_ROLE_NAME)) {
                // Add Global Manager role to the roles set
                roles.add(Constants.GLOBAL_MANAGER_ROLE_NAME);
                request.setAttribute("global_resource", resource);
            }

            // If this resource has the Payment Manager role and no projects associated with
            // it,
            // add the global payment manager role
            if (role.getName().equalsIgnoreCase(Constants.PAYMENT_MANAGER_ROLE_NAME)) {
                // Add Global PAYMENT Manager role to the roles set
                roles.add(Constants.GLOBAL_PAYMENT_MANAGER_ROLE_NAME);
            }

        }

        // Admin users are the super users that have all possible access.
        if (ConfigHelper.getAdminUsers().contains(getLoggedInUserId(request))) {
            roles.add(Constants.ADMIN_ROLE_NAME);
            roles.add(Constants.GLOBAL_MANAGER_ROLE_NAME);
            roles.add(Constants.GLOBAL_PAYMENT_MANAGER_ROLE_NAME);
        }

        // Determine some common permissions
        request.setAttribute("isAllowedToCreateProject",
                hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME));

    }

    /**
     * This static method gathers the Resource Roles that the currently logged in
     * user has based on the context of the Project specified by its ID. It then
     * places the gathered roles into the object specified by the
     * <code>request</code> parameter.
     *
     * @param request   an <code>HttpServletRequest</code> object containing
     *                  additional information such as the ID of the currently
     *                  logged in user that will help gathering Resource Roles.
     *                  <p>
     *                  On return, the object specified by this parameter will
     *                  receive the list of all gathered roles.
     *                  </p>
     * @param projectId an ID of the project which context should be used to gather
     *                  the Resource Roles.
     * @throws BaseException if any error occurs.
     */
    public static void gatherUserRoles(HttpServletRequest request, long projectId) throws BaseException {
        // Call shorter version of this function first
        gatherUserRoles(request);

        // At this moment the request should have "roles" attribute
        Set roles = (Set) request.getAttribute("roles");

        // Check if user is Cockpit Project User for selected project
        ProjectDataAccess projectDataAccess = getBean(ProjectDataAccess.class);
        if (projectDataAccess.isCockpitProjectUser(projectId, getLoggedInUserId(request))) {
            roles.add(Constants.COCKPIT_PROJECT_USER_ROLE_NAME);
        }

        // Create an instance of Project Manager
        ProjectManager projMgr = ActionsHelper.createProjectManager();
        // Retrieve the project with specified project ID
        Project project = projMgr.getProject(projectId);

        // If the project with specified ID does not exist, return
        if (project == null) {
            return;
        }

        long userId = getLoggedInUserId(request);

        Resource[] resources;
        if (userId != NO_USER_LOGGED_IN_ID) {
            // Create filter to filter only the resources for the currently logged user
            Filter filterUserID = ResourceFilterBuilder.createUserIdFilter(userId);

            // Create filter to filter only the resources for the project in question
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(projectId);
            // Create combined final filter
            Filter filter = new AndFilter(filterUserID, filterProject);

            // Obtain an instance of Resource Manager and run the search
            resources = ActionsHelper.createResourceManager().searchResources(filter);
        } else {
            resources = new Resource[0];
        }

        for (Resource resource : resources) {
            ActionsHelper.populateEmailProperty(request, resource);
        }
        // Place resources for currently logged in user into the request
        request.setAttribute("myResources", resources);

        // Iterate over all resources and retrieve their roles
        for (Resource resource : resources) {
            // Get the role this resource has
            ResourceRole role = resource.getResourceRole();
            // Add the name of the role to the roles set (gather the role)
            roles.add(role.getName());
        }

        // retrieve user id and client id
        long clientId;
        try {
            clientId = projectDataAccess.getProjectClient(project.getTcDirectProjectId());
        } catch (Exception e) {
            throw new BaseException("error occurs while retrieving client id", e);
        }

        // check whether user has cockpit project user role
        AuthorizationService authorizationService = retrieveAuthorizationService(request);
        try {
            if (authorizationService.isCustomerAdministrator(userId, clientId)) {
                roles.add(Constants.COCKPIT_PROJECT_USER_ROLE_NAME);
            } else {
                GroupPermissionType permission = authorizationService.checkAuthorization(userId,
                        project.getTcDirectProjectId(), ResourceType.PROJECT);
                if (null != permission) {
                    roles.add(Constants.COCKPIT_PROJECT_USER_ROLE_NAME);
                }
            }
        } catch (Exception e) {
            throw new BaseException(e);
        }

    }

    /**
     * This static method checks whether the logged in user has the specified role.
     * The information about logged in user is taken from the session associated
     * with the specified request.
     * <p>
     * Note, one of the <code>gatherUserRoles</code> static methods must be called
     * prior making a call to this method, or the result of this call will be
     * <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the logged in user has the specified role in
     *         current context, <code>false</code> if he doesn't.
     * @param request the <code>HttpServletRequest</code> to take the information
     *                about roles currently logged in user has in current context.
     * @param role    the role to check for belonging to user.
     * @see #gatherUserRoles(HttpServletRequest)
     * @see #gatherUserRoles(HttpServletRequest, long)
     */
    public static boolean hasUserRole(HttpServletRequest request, String role) {
        // Retrieve set with roles that the user has in the current context from the
        // request
        Set roles = (Set) request.getAttribute("roles");
        // If nothing has been retrieved, or the set is empty
        // (no permissions, how sad), return false immediately
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        // Otherwise allow set to test if it contains the value under question
        return roles.contains(role);
    }

    /**
     * This static method checks whether the logged in user has at least one role
     * from the specified array of resource roles. The information about logged in
     * user is taken from the session associated with the specified request.
     * <p>
     * Note, one of the <code>gatherUserRoles</code> static methods must be called
     * prior making a call to this method, or the result of this call will be
     * <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the logged in user has at least one of the
     *         specified roles in current context, <code>false</code> if he doesn't.
     * @param request the <code>HttpServletRequest</code> to take the information
     *                about roles currently logged in user has in current context.
     * @param roles   an array of roles to check.
     * @see #gatherUserRoles(HttpServletRequest)
     * @see #gatherUserRoles(HttpServletRequest, long)
     */
    public static boolean hasUserRole(HttpServletRequest request, String[] roles) {
        for (String role : roles) {
            if (hasUserRole(request, role)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This static method checks whether the logged in user has the permission to
     * perform the specified action. The information about logged in user is taken
     * from the session associated with the specified request.
     * <p>
     * Note, one of the <code>gatherUserRoles</code> static methods must be called
     * prior making a call to this method, or the result of this call will be
     * <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the logged in user has the permission to perform
     *         the specified action, <code>false</code> if he doesn't have.
     * @param request        the <code>HttpServletRequest</code> to take the
     *                       information about logged in user from (actually it is
     *                       taken from session associated with this request).
     * @param permissionName the name of Permission to check if the user has.
     * @see #gatherUserRoles(HttpServletRequest)
     * @see #gatherUserRoles(HttpServletRequest, long)
     */
    public static boolean hasUserPermission(HttpServletRequest request, String permissionName) {
        String[] roles = ConfigHelper.getRolesForPermission(permissionName);
        for (String role : roles) {
            if (hasUserRole(request, role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Gets the sso cookie service to be used for authenticating users based on
     * cookie.
     * </p>
     *
     * @return a a <code>SSOCookieService</code> to be used for user authentication
     *         based on cookie.
     */
    private static SSOCookieService getSsoCookieService() {
        return ssoCookieService;
    }

    /**
     * Get authorization service from spring.
     *
     * @param request the http servlet request
     * @return retrieved authorization service
     */
    private static AuthorizationService retrieveAuthorizationService(HttpServletRequest request) {
        return (AuthorizationService) WebApplicationContextUtils
                .getWebApplicationContext(request.getSession().getServletContext()).getBean(AUTHORIZATION_SERVICE_NAME);
    }

    /**
     * <p>
     * Sets the sso cookie service to be used for authenticating users based on
     * cookie.
     * </p>
     *
     * @param ssoCookieService the ssoCookieService to set
     */
    public void setSsoCookieService(SSOCookieService ssoCookieService) {
        AuthorizationHelper.ssoCookieService = ssoCookieService;
    }

    /**
     * <p>
     * Validate jwt token
     * </p>
     *
     * @param token the jwt token
     * @throws JWTException if any error occurs
     * @return the DecodedJWT result
     */
    public static DecodedJWT validateJWTToken(String token) throws JWTException {
        if (token == null) {
            throw new IllegalArgumentException("token must be specified.");
        }
        DecodedJWT decodedJWT = null;
        // Decode only first to get the algorithm
        try {
            decodedJWT = JWT.decode(token);
        } catch (JWTDecodeException e) {
            throw new InvalidTokenException(token, "Error occurred in decoding token. " + e.getLocalizedMessage(), e);
        }
        String algorithm = decodedJWT.getAlgorithm();
        Algorithm alg = null;
        // Create the algorithm
        if ("RS256".equals(algorithm)) {
            List<String> validIssuers = ConfigHelper.getValidIssuers();
            // Validate the issuer
            if (decodedJWT.getIssuer() == null || !validIssuers.contains(decodedJWT.getIssuer())) {
                throw new InvalidTokenException(token, "Invalid issuer: " + decodedJWT.getIssuer());
            }

            // Create the JWK provider with caching
            JwkProvider urlJwkProvider = new UrlJwkProvider(decodedJWT.getIssuer());
            JwkProvider jwkProvider = new GuavaCachedJwkProvider(urlJwkProvider);

            // Get the public key and create the algorithm
            try {
                Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());
                RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

                alg = Algorithm.RSA256(publicKey, null);
            } catch (Exception e) {
                throw new JWTException(token, "Error occurred in creating algorithm. " + e.getLocalizedMessage(), e);
            }
        } else {
            throw new JWTException(token, "Algorithm not supported: " + algorithm);
        }

        // Verify
        try {
            Verification verification = JWT.require(alg);

            JWTVerifier verifier = verification.build();
            decodedJWT = verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException(token);
        } catch (SignatureVerificationException | IllegalStateException e) {
            throw new InvalidTokenException(token, "Token is invalid. " + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            throw new JWTException(token, "Error occurred in verifying token. " + e.getLocalizedMessage(), e);
        }
        return decodedJWT;
    }

}
