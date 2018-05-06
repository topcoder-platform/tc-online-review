<%@ page import="com.cronos.onlinereview.login.TestUtil" %>
<%@ page import="com.topcoder.util.config.ConfigManager" %> 

<html>
	<head>
		<title>Preload.jsp</title>
	</head>

	<body>
	    <%
	    ConfigManager.getInstance().refreshAll();
	    String msg = null;
	    try {
	        TestUtil.createUser("myname", "mypw");
	        msg = "Registered! UserName:myname, Password:mypw";
	    } catch (Exception e) {
	    	msg = "Cannot register: " + e.toString();
	    }
	    %>
        <h1><%=msg%></h1>
        <a href="login.jsp">GOTO Login Page!</a>
	</body>
</html>
