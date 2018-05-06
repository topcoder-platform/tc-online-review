<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
    <head>
        <title>Project List</title>
    </head>
    <body>
        <h1>Project List</h1>
        <ul>
            <li>Online Review Login</li>
            <li>Online Review Ajax Support</li>
            <li>Online Review Deliverables</li>
        </ul>
        <hr />
        <html:errors />
        <html:link action="/logout?method=logout" linkName="Log me out" >Logout</html:link>
    </body>
</html>