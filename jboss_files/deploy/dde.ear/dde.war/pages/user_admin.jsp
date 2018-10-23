<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/2000/REC-xhtml1-20000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="tcs_style.css" />

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

<%
	String strMessage = "";
	String action = request.getParameter("butAction");
	String handle = "";
	String password = "";
	TCSubject user = null;

	if (action != null) {
		strMessage += "Action occurred<BR>";
		handle = request.getParameter("txtHandle");
		password = request.getParameter("txtPassword");

		try {
			Context jndiContext = new InitialContext();

		  Object prinmgr = jndiContext.lookup(PrincipalMgrRemoteHome.EJB_REF_NAME);
		  PrincipalMgrRemoteHome home = (PrincipalMgrRemoteHome) PortableRemoteObject.narrow(prinmgr, PrincipalMgrRemoteHome.class);
		  PrincipalMgrRemote remote = home.create();

		  Object polmgr = jndiContext.lookup(PolicyMgrRemoteHome.EJB_REF_NAME);
		  PolicyMgrRemoteHome polHome = (PolicyMgrRemoteHome) PortableRemoteObject.narrow(polmgr, PolicyMgrRemoteHome.class);
		  PolicyMgrRemote polRemote = polHome.create();

		  Object login = jndiContext.lookup(LoginRemoteHome.EJB_REF_NAME);
		  LoginRemoteHome loginHome = (LoginRemoteHome) PortableRemoteObject.narrow(login, LoginRemoteHome.class);
		  LoginRemote loginRemote = loginHome.create();

		  Object policy = jndiContext.lookup(PolicyRemoteHome.EJB_REF_NAME);
		  PolicyRemoteHome policyHome = (PolicyRemoteHome) PortableRemoteObject.narrow(policy, PolicyRemoteHome.class);
		  PolicyRemote policyRemote = policyHome.create();

		  TCSubject requestor = new TCSubject( new PrincipalList(), 10);

			if (action.equals("LOOKUP")) {
				try {
					user = loginRemote.login(handle, password);
					strMessage += "logged in with login_id: " + user.getUserId();
				} catch (com.topcoder.security.login.AuthenticationException ae) {
					strMessage += ae.getMessage();
				}
			}

			if (action.equals("CREATE")) {
				TCPrincipal tcprincipal = remote.createUser(handle, password, requestor);
				user = new TCSubject(tcprincipal.getId());
				strMessage += "created user with login_id: " + user.getUserId();
			}

			if (action.equals("DELETE")) {
				try {
					UserPrincipal userPrincipal = remote.getUser(handle);
					remote.removeUser(userPrincipal, requestor);
					strMessage += "deleted user with login_id: " + user.getUserId();
				} catch (com.topcoder.security.login.AuthenticationException ae) {
					strMessage += ae.getMessage();
				} catch (com.topcoder.security.NoSuchUserException nsue) {
					strMessage += nsue.getMessage();
				}
			}

		} catch (UndeclaredThrowableException e) {
			System.out.println(e.getUndeclaredThrowable());
		} catch (Exception e) {
			System.out.println("" + e);
			e.printStackTrace();
		}
	}
%>

</head>

<body bgcolor="white">

<%= strMessage %>

<H2>USER ADMIN</H2>
<FORM NAME="frmAdmin" ACTION="user_admin.jsp" METHOD="POST">
<TABLE>
	<TR>
		<TD>Handle
		<TD>
		<TD><INPUT TYPE="TEXT" NAME="txtHandle" VALUE="">
	</TR>
	<TR>
		<TD>Password
		<TD>
		<TD><INPUT TYPE="TEXT" NAME="txtPassword" VALUE="">
	</TR>
	<TR>
		<TD COLSPAN=3>
			<INPUT TYPE="SUBMIT" NAME="butAction" VALUE="LOOKUP">
			<INPUT TYPE="SUBMIT" NAME="butAction" VALUE="CREATE">
			<INPUT TYPE="SUBMIT" NAME="butAction" VALUE="DELETE">
		</TD>
	</TR>
</TABLE>
</FORM>

</body>
</html>
