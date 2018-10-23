<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>

<%@ page import="javax.ejb.*" %>
<%@ page import="com.topcoder.dde.catalog.*" %>

<%
	// STANDARD PAGE VARIABLES
	String page_name = "usertech.jsp";
	String action = request.getParameter("a");	

	Object objTechTypes = CONTEXT.lookup("catalog/Catalog");
	CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
	Catalog catalog = home.create();
	Collection col = catalog.getTechnologies();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/2000/REC-xhtml1-20000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>
	<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<script language="JavaScript" src="/scripts/javascript.js"></script>
</head>

<body class="body">

<%
	if (col != null) {
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
%>

<%
			Object obj = iter.next();
			if (obj instanceof Technology) {
%>		
				<%= ((Technology)obj).getName() %>: <%= ((Technology)obj).getDescription() %><BR>
<%			
			}
		}
	}
%>


</body>
</html>