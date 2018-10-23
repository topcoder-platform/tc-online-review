<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.dde.catalog.*" %>
<%@ page import="com.topcoder.dde.notification.Notification" %>
<%@ page import="com.topcoder.dde.notification.NotificationHome" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "catalog.jsp";
    String action = request.getParameter("a");
%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>

<%
	Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
	CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
	Catalog catalog = home.create();

        // Request
	Fields fieldsRequest = new Fields();
            fieldsRequest.add("name", new Field("Name", "name", "", true));
            fieldsRequest.add("shortdesc", new Field("Short Description", "shortdesc",  "", true));
            fieldsRequest.add("desc", new Field("Description", "desc",  "", true));
            fieldsRequest.add("funcdesc", new Field("Functional Description", "funcdesc",  "", true));
            fieldsRequest.add("keywords", new Field("Keywords", "keywords",  "", true));
            fieldsRequest.add("comments", new Field("Comments", "comments",  "", true));
            fieldsRequest.add("vlabel", new Field("Version Label", "vlabel",  "", true));

        boolean requestError = false;
        String strRequestError = "";
        String strRequestMsg = "";

        // View
        Collection colComponents = null;
        String status = "";

        if (action != null) {
            if (action.equalsIgnoreCase("Request")) {
                Enumeration enum = fieldsRequest.elements();
                while (enum.hasMoreElements()) {
                        Field f = (Field)enum.nextElement();
                        f.getFromRequest(request, "");
                        f.doBasicValidation();
                        if (f.hasError()) {
                                requestError = true;
                                debug.addMsg("admin->catalog->requestComponent", "error for field " + f.getLabel());
                                strRequestError += "Field " + f.getLabel() + " is not valid.<BR>";
                        }
                }
                if (!requestError) {
                    if (tcUser == null) {
                        requestError = true;
                        strRequestError += "You must be logged in to perform this action.";
                    } else {
                        try {
                            debug.addMsg("admin->catalog", "Requesting component");
                            catalog.requestComponent(
                                new ComponentRequest(
                                    fieldsRequest.get("name").getValue(),
                                    fieldsRequest.get("shortdesc").getValue(),
                                    fieldsRequest.get("desc").getValue(),
                                    fieldsRequest.get("funcdesc").getValue(),
                                    fieldsRequest.get("keywords").getValue(),
                                    fieldsRequest.get("comments").getValue(),
                                    fieldsRequest.get("vlabel").getValue(),
                                    tcUser.getId()
                                )
                            );
                            debug.addMsg("admin->catalog", "Requested component");
                            strRequestMsg += "Component (" + fieldsRequest.get("name").getValue() + " has been requested.";

			    NotificationHome notificationHome = (NotificationHome)
					    PortableRemoteObject.narrow(
					    CONTEXT.lookup(NotificationHome.EJB_REF_NAME),
					    NotificationHome.class);

  			    Notification notification = notificationHome.create();

			    Properties prop = new Properties();
			    prop.setProperty(notification.COMPONENT_NAME, fieldsRequest.get("name").getValue());
			    prop.setProperty(notification.REQUESTOR_NAME, tcUser.getRegInfo().getUsername());
			    notification.notifyEvent("com.topcoder.dde.admin.ComponentRequestEvent", prop);

                        } catch (RemoteException re) {
                            requestError = true;
                            strRequestError += "System error occurred: " + re.getMessage() + "<BR>";
                        } catch (CatalogException ce) {
                            requestError = true;
                            strRequestError += "Catalog could not create component: " + ce.getMessage() + "<BR>";
                        }
                    }
                }
            }
        }
%>

</head>

<body class="body">

<!-- Header begins -->
<%@ include file="/includes/adminHeader.jsp" %>
<%@ include file="/includes/adminNav.jsp" %>
<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td valign="middle" width="1%" class="adminBreadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0"/></td>
		<td valign="middle" width="1%" class="adminBreadcrumb" nowrap><strong>Catalog Admin</strong></td>
		<td valign="middle" width="98%" class="adminBreadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0"/></td>
	</tr>
</table>
<!-- breadcrumb ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
	<tr valign="top">

<!-- Left Column begins -->
		<td width="165" class="leftColumn">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="165" height="5" border="0" /></td></tr>
<!-- Catalog Admin Navigation -->
				<tr valign="top">
					<td class="adminLeftNav">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="5" class="adminLeftHead"><img src="/images/clear.gif" alt="" width="5" height="10" border="0"></td>
								<td width="155" class="adminLeftHead" colspan="2">Catalog Admin</td>
								<td width="10" class="adminLeftHead" valign="top"><img src="/images/leftNavHead.gif" alt="" width="10" height="22" border="0" /></td>
							</tr>
							<tr valign="top">
								<td><img src="/images/clear.gif" alt="" width="5" height="5" border="0"></td>
								<td class="adminLeftNavText">::&nbsp;</td>
								<td class="adminLeftNavText"><a href="catalog.jsp">Request Component</a></td>
								<td><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
							</tr>
							<tr valign="top">
								<td><img src="/images/clear.gif" alt="" width="5" height="5" border="0"></td>
								<td class="adminLeftNavText">::&nbsp;</td>
								<td class="adminLeftNavText"><a href="comp_status.jsp">View Components by Status</a></td>
								<td><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
							</tr>
							<tr><td colspan="4"><img src="/images/adminLeftNavFoot.gif" alt="" width="165" height="11" border="0" /></td></tr>
						</table>
					</td>
				</tr>
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
			</table>
		</td>
		<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0" /></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
		<td width="100%">

<% if (requestError) { %>
<font color="RED"><%= strRequestError %></font>
<% } else { %>

			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
				<tr><td class="normal"><img src="/images/headCatalogAdmin.gif" alt="Catalog Admin" width="545" height="35" border="0" /></td></tr>
				<tr><td><img src="/images/clear.gif" alt="" width="1" height="1" border="0"/></td></tr>
			</table>

			<table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
				<tr valign="top">
					<td align="center">

						<table cellpadding="0" cellspacing="0" border="0">
							<tr><td width="445" height="29" colspan="2">
								<form name="frmRequestComponent" action="catalog.jsp" method="POST">
								<img src="/images/adminReqNewCompHead.gif" alt="Request a New Component" width="500" height="29" border="0" /></td></tr>
						</table>

<!-- Request Parameters -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
	<%
		Enumeration requestKeys = fieldsRequest.keys();
		while (requestKeys.hasMoreElements()) {
			Field field = fieldsRequest.get("" + requestKeys.nextElement());
	%>
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminLabel" nowrap><%= field.getLabel() %></td>
	<% if (field.getName().indexOf("desc") > -1) { %>
								<td width="1%" class="adminText"><textarea rows="8" cols="35" name="<%= field.getName() %>"></textarea></td>
	<% } else { %>
								<td width="1%" class="adminText"><input class="adminSearchForm" type="text" name="<%= field.getName() %>" value="" size="35"></td>
		<% } %>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

	<% } %>

<!-- Submit Button -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td colspan="2"><img src="/images/clear.gif" alt="" width="5" height="5" border="0"/></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td colspan="2" class="adminTextCenter"><input class="adminButton" type="submit" name="a" value="Request"></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>
<!-- User Info Ends -->

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0"/></td></form></tr>
						</table>
					</td>
				</tr>
			</table>

<% } %>

		</td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
		<td width="245">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="245" height="15" border="0" /></td></tr>
			</table>
		</td>
<!--Right Column ends -->

<!-- Gutter 3 begins -->
		<td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"></td></form>
<!-- Gutter 3 ends -->
	</tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
