<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.security.*" %>
<%@ page import="com.topcoder.security.admin.*" %>
<%@ page import="com.topcoder.security.login.*" %>
<%@ page import="com.topcoder.security.policy.*" %>

<%@ page import="com.topcoder.dde.user.*" %>

<%@ include file="/includes/util.jsp" %>

<%
	////////////////////////////////////////////
	// Load context
	////////////////////////////////////////////

	Context CONTEXT = new InitialContext();

	////////////////////////////////////////////
	// Load Stateless EJB's
	////////////////////////////////////////////

	Object objUserManager = CONTEXT.lookup("dde/UserManager");
	UserManagerRemoteHome userManagerHome = (UserManagerRemoteHome)  PortableRemoteObject.narrow(objUserManager, UserManagerRemoteHome.class);
  UserManagerRemote USER_MANAGER = userManagerHome.create();

/*
	Object objPrincipalManager = CONTEXT.lookup(PrincipalMgrRemoteHome.EJB_REF_NAME);
	PrincipalMgrRemoteHome principalManagerHome = (PrincipalMgrRemoteHome) PortableRemoteObject.narrow(objPrincipalManager, PrincipalMgrRemoteHome.class);
	PrincipalMgrRemote PRINCIPAL_MANAGER = principalManagerHome.create();

	Object objPolicyManager = CONTEXT.lookup(PolicyMgrRemoteHome.EJB_REF_NAME);
	PolicyMgrRemoteHome policyManagerHome = (PolicyMgrRemoteHome) PortableRemoteObject.narrow(objPolicyManager, PolicyMgrRemoteHome.class);
	PolicyMgrRemote POLICY_MANAGER = policyManagerHome.create();

	Object objLogin = CONTEXT.lookup(LoginRemoteHome.EJB_REF_NAME);
	LoginRemoteHome loginHome = (LoginRemoteHome) PortableRemoteObject.narrow(objLogin, LoginRemoteHome.class);
	LoginRemote LOGIN = loginHome.create();

	Object objPolicy = CONTEXT.lookup(PolicyRemoteHome.EJB_REF_NAME);
	PolicyRemoteHome policyHome = (PolicyRemoteHome) PortableRemoteObject.narrow(objPolicy, PolicyRemoteHome.class);
	PolicyRemote POLICY = policyHome.create();
*/
%>

<%
/*
 	Handle session information, redirecting if necessary
*/

//////////////////////////////////////////////
// Get current user information from session
//////////////////////////////////////////////

/*
TCSubject tcSubject = null;
User tcUser = null;
Object objTCSubject = session.getValue("TCSubject");
if (objTCSubject != null && objTCSubject instanceof TCSubject) {
	tcSubject = (TCSubject) objTCSubject;
	try {
  	tcUser = USER_MANAGER.getUser(tcSubject.getUserId());
		debug.addMsg("session", "loaded tcUser with id " + tcSubject.getUserId());
	} catch (UndeclaredThrowableException e) {
		debug.addMsg("session", "" + e.getUndeclaredThrowable());
	} catch (Exception e) {
		debug.addMsg("session", "" + e);
		e.printStackTrace();
	}
} else {
	tcSubject = null;
}
*/

//////////////////////////////////////////////
// Get page requested
//////////////////////////////////////////////

String requestURI = request.getRequestURI();

//////////////////////////////////////////////
// Check for permissions for the requested page
//////////////////////////////////////////////

String pagesRequiringLogin = "[][]";

//////////////////////////////////////////////
// Redirect to login page if login is required
//////////////////////////////////////////////
/*
if (pagesRequiringLogin.indexOf(requestURI) > -1 && requestURI == null) {
	session.putValue("nav_redirect", requestURI);
	response.sendRedirect("/login.jsp");
}
*/
%>