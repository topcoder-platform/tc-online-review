<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Insert title here</title>
    </head>
    
    <body>
        <html:errors />
        <br />
        <html:form action="/login" >
            <input type="hidden" name="method" value="login">
            UserName:<html:text property="userName"/><br />
            Password:<html:password property="password"/><br />
            <html:submit value="Login"/><br />
			Remember me<html:checkbox property="rememberMe"/>
        </html:form>
        <br />
        <a href="preload.jsp">Need account? Register!</a>
    </body>
</html>