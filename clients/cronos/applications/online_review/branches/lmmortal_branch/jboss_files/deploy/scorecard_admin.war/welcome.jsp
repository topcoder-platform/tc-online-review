<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="com.cronos.onlinereview.actions.ScorecardActionsHelper" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <html:base />
    
    <title>welcome.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <h1>The user is logged on with ID <bean:write name="<%= ScorecardActionsHelper.getInstance().getUserIdSessionAttributeKey() %>"/></h1>
  <h1>Click <html:link action="/scorecardAdmin?actionName=listScorecards">Here</html:link> to navigate to scorecard admin page.</h1>
  </body>
</html:html>
