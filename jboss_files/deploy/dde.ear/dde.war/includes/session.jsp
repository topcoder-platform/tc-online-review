
<%@ page import="com.topcoder.dde.user.User,
                 com.topcoder.dde.user.UserManagerRemote,
                 com.topcoder.dde.user.UserManagerRemoteHome,
                 com.topcoder.dde.util.Cookies,
                 com.topcoder.security.TCSubject,
                 com.topcoder.security.admin.PolicyMgrRemote,
                 com.topcoder.security.admin.PolicyMgrRemoteHome" %>
<%@ page import="com.topcoder.security.admin.PrincipalMgrRemote" %>
<%@ page import="com.topcoder.security.admin.PrincipalMgrRemoteHome" %>
<%@ page import="com.topcoder.security.login.LoginRemote" %>

<%@ page import="com.topcoder.security.login.LoginRemoteHome" %>
<%@ page import="com.topcoder.security.policy.PolicyRemote" %>
<%@ page import="com.topcoder.security.policy.PolicyRemoteHome" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%
	////////////////////////////////////////////
	// Load context
	////////////////////////////////////////////

	/*
	Hashtable environment = new Hashtable();
	environment.put(Context.PROVIDER_URL, "localhost:1099");
	environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
	Context CONTEXT = new InitialContext(environment);
	*/
	Context CONTEXT = new InitialContext();


	////////////////////////////////////////////
	// Load Stateless EJB's
	////////////////////////////////////////////

	Object objUserManager = CONTEXT.lookup("dde/UserManager");
	UserManagerRemoteHome userManagerHome = (UserManagerRemoteHome)  PortableRemoteObject.narrow(objUserManager, UserManagerRemoteHome.class);
  	UserManagerRemote USER_MANAGER = userManagerHome.create();

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

%>

<%
/*
 	Handle session information, redirecting if necessary
*/

//////////////////////////////////////////////
// Get current user information from session
//////////////////////////////////////////////

TCSubject tcSubject = (TCSubject)session.getValue("TCSUBJECT");
User tcUser = (User)session.getValue("TCUSER");

//Attempt to log in the user via cookies
if (tcSubject == null) {

    tcSubject = Cookies.getUserFromLoginCookies(request, response, USER_MANAGER);

    try {
        if (tcSubject != null) {
//            debug.addMsg("login", "retrieved user information from cookies");
//            debug.addMsg("login", "user logged in with user id " + tcSubject.getUserId());
//            debug.addMsg("login", "user has " + tcSubject.getPrincipals());
//            debug.addMsg("login", "principals are " + tcSubject.getPrincipals().toString());
            session.putValue("TCSUBJECT", tcSubject);

            tcUser = USER_MANAGER.getUser(tcSubject.getUserId());
//            debug.addMsg("login", "loaded tcUser with id " + tcSubject.getUserId());
            session.putValue("TCUSER", tcUser);
        }
    } catch (Exception e) {
//        debug.addMsg("login", "" + e);
        e.printStackTrace();
    }
}

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

if (pagesRequiringLogin.indexOf(requestURI) > -1 && requestURI == null) {
	session.putValue("nav_redirect", requestURI);
	response.sendRedirect("/login.jsp");
}

//////////////////////////////////////////////
// Set current page information so that any
// attempts to login know where to go back to
//////////////////////////////////////////////

if (requestURI != null && !requestURI.endsWith("login.jsp") && !requestURI.endsWith("c_component_doc.jsp")) {
    StringBuffer navRedirect = request.getRequestURL();
    if (request.getQueryString() != null) {
        navRedirect.append("?");
        navRedirect.append(request.getQueryString());
    }
    session.putValue("nav_redirect", navRedirect.toString());
    session.removeValue("nav_redirect_msg");
}
%>
