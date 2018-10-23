<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<%
    // STANDARD PAGE VARIABLES
    String page_name = "lists.jsp";
    String action = request.getParameter("a");
%>


<HTML>
<HEAD>
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>


<%
Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
Catalog catalog = home.create();

String strError = "";
String strMessage = "";

if (action != null) {
    // License Level
    if (action.equalsIgnoreCase("Add License Level")) {
        String desc = request.getParameter("txtLicenseLevelDescription");
        String priceMult = request.getParameter("txtLicenseLevelPriceMultiplier");
        catalog.addLicenseLevel(new LicenseLevel(desc, Double.parseDouble(priceMult)));
    }
    if (action.equalsIgnoreCase("Update License Level")) {
        String id = request.getParameter("licenseLevel");
        String desc = request.getParameter("txtLicenseLevelDescription");
        String priceMult = request.getParameter("txtLicenseLevelPriceMultiplier");
        LicenseLevel licenseLevel = catalog.getLicenseLevel(Long.parseLong(id));
        licenseLevel.setDescription(desc);
        licenseLevel.setPriceMultiplier(Double.parseDouble(priceMult));
        catalog.updateLicenseLevel(licenseLevel);
    }
    if (action.equalsIgnoreCase("Delete License Level")) {
        String id = request.getParameter("licenseLevel");
        catalog.removeLicenseLevel(Long.parseLong(id));
    }

    // Role
    if (action.equalsIgnoreCase("Add Role")) {
        String name = request.getParameter("txtRoleName");
        String desc = request.getParameter("txtRoleDescription");
        catalog.addRole(new Role(name, desc));
    }
    if (action.equalsIgnoreCase("Update Role")) {
        String id = request.getParameter("role");
        String name = request.getParameter("txtRoleName");
        String desc = request.getParameter("txtRoleDescription");
        Role role = catalog.getRole(Long.parseLong(id));
        role.setName(name);
        role.setDescription(desc);
        catalog.updateRole(role);
    }
    if (action.equalsIgnoreCase("Delete Role")) {
        String id = request.getParameter("role");
        catalog.removeRole(Long.parseLong(id));
    }

    // Technology
    if (action.equalsIgnoreCase("Add Technology")) {
        String name = request.getParameter("txtTechnologyName");
        String desc = request.getParameter("txtTechnologyDescription");
        catalog.addTechnology(new Technology(name, desc));
    }
    if (action.equalsIgnoreCase("Update Technology")) {
        String id = request.getParameter("technology");
        String name = request.getParameter("txtTechnologyName");
        String desc = request.getParameter("txtTechnologyDescription");
        Technology technology = catalog.getTechnology(Long.parseLong(id));
        technology.setName(name);
        technology.setDescription(desc);
        catalog.updateTechnology(technology);
    }
    if (action.equalsIgnoreCase("Delete Technology")) {
        String id = request.getParameter("technology");
        catalog.removeTechnology(Long.parseLong(id));
    }
}

// License level
LicenseLevel licenseLevels[] = (LicenseLevel[])catalog.getLicenseLevels().toArray(new LicenseLevel[0]);

// Role
Role roles[] = (Role[])catalog.getRoles().toArray(new Role[0]);

// Technology
Technology technologies[] = (Technology[])catalog.getTechnologies().toArray(new Technology[0]);

%>
</HEAD>
<BODY>

<!-- Header begins -->
<%@ include file="/includes/adminHeader.jsp" %>
<%@ include file="/includes/adminNav.jsp" %>
<!-- Header ends -->


<% if (strError.length() > 0) { %>
<H3><FONT COLOR="RED"><%= strError %></FONT></H3>
<HR>
<% } %>

<% if (strMessage.length() > 0) { %>
<H3><%= strMessage %></H3>
<HR>
<% } %>

<H2> License Levels </H2>
<TABLE>
    <TR>
        <TD>Description
        <TD>Unit Cost
        <TD>Action
    </TR>
<% for (int i=0; i < licenseLevels.length; i++) { %>
<FORM NAME="frmLicenseLevel" ACTION="lists.jsp" METHOD="POST">
<INPUT TYPE="HIDDEN" NAME="licenseLevel" VALUE="<%= licenseLevels[i].getId() %>">
    <TR>
        <TD><INPUT TYPE="TEXT" NAME="txtLicenseLevelDescription" VALUE="<%= licenseLevels[i].getDescription() %>">
        <TD><INPUT TYPE="TEXT" NAME="txtLicenseLevelPriceMultiplier" VALUE="<%= licenseLevels[i].getPriceMultiplier() %>">
        <TD>
            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Update License Level">
            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Delete License Level">
    </TR>
</FORM>
<% } %>
<FORM NAME="frmLicenseLevel" ACTION="lists.jsp" METHOD="POST">
    <TR>
        <TD><INPUT TYPE="TEXT" NAME="txtLicenseLevelDescription" VALUE="">
        <TD><INPUT TYPE="TEXT" NAME="txtLicenseLevelPriceMultiplier" VALUE="">
        <TD><INPUT TYPE="SUBMIT" NAME="a" VALUE="Add License Level">
    </TR>
</FORM>
</TABLE>

<H2> Roles </H2>

<TABLE>
    <TR>
        <TD>Name
        <TD>Description
        <TD>Action
    </TR>
<% for (int i=0; i < roles.length; i++) { %>
<FORM NAME="frmRole" ACTION="lists.jsp" METHOD="POST">
<INPUT TYPE="HIDDEN" NAME="role" VALUE="<%= roles[i].getId() %>">
    <TR>
        <TD><INPUT TYPE="TEXT" NAME="txtRoleName" VALUE="<%= roles[i].getName() %>">
        <TD><INPUT TYPE="TEXT" NAME="txtRoleDescription" VALUE="<%= roles[i].getDescription() %>">
        <TD>
            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Update Role">
            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Delete Role">
    </TR>
</FORM>
<% } %>
<FORM NAME="frmRole" ACTION="lists.jsp" METHOD="POST">
    <TR>
        <TD><INPUT TYPE="TEXT" NAME="txtRoleName" VALUE="">
        <TD><INPUT TYPE="TEXT" NAME="txtRoleDescription" VALUE="">
        <TD><INPUT TYPE="SUBMIT" NAME="a" VALUE="Add Role">
    </TR>
</FORM>
</TABLE>

<H2> Technologies </H2>

<TABLE>
    <TR>
        <TD>Name
        <TD>Description
        <TD>Action
    </TR>
<% for (int i=0; i < technologies.length; i++) { %>
<FORM NAME="frmTechnology" ACTION="lists.jsp" METHOD="POST">
<INPUT TYPE="HIDDEN" NAME="technology" VALUE="<%= technologies[i].getId() %>">
    <TR>
        <TD><INPUT TYPE="TEXT" NAME="txtTechnologyName" VALUE="<%= technologies[i].getName() %>">
        <TD><INPUT TYPE="TEXT" NAME="txtTechnologyDescription" VALUE="<%= technologies[i].getDescription() %>">
        <TD>
            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Update Technology">
            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Delete Technology">
    </TR>
</FORM>
<% } %>
<FORM NAME="frmTechnology" ACTION="lists.jsp" METHOD="POST">
    <TR>
        <TD><INPUT TYPE="TEXT" NAME="txtTechnologyName" VALUE="">
        <TD><INPUT TYPE="TEXT" NAME="txtTechnologyDescription" VALUE="">
        <TD><INPUT TYPE="SUBMIT" NAME="a" VALUE="Add Technology">
    </TR>
</FORM>
</TABLE>

</BODY>
</HTML>