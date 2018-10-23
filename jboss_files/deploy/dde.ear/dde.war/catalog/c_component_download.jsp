<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="javax.servlet.http.*" %>




<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "c_component_download.jsp";
%>

<html>

<head>
<%
    Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
    CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
    Catalog catalog = home.create();

    Object objComponentMgr = CONTEXT.lookup(ComponentManagerHome.EJB_REF_NAME);
    ComponentManagerHome component_manager_home = (ComponentManagerHome) PortableRemoteObject.narrow(objComponentMgr, ComponentManagerHome.class);
    ComponentManager componentManager = null;

    long lngComponent = 0;
    long lngVersion = 0;

    try {
        lngComponent = Long.parseLong(request.getParameter("comp"));
    } catch (NumberFormatException nfe) {

    }

    try {
        lngVersion = Long.parseLong(request.getParameter("ver"));
    } catch (NumberFormatException nfe) {
        lngVersion = 0;
    }

    if (lngVersion == 0) {
        componentManager = component_manager_home.create(lngComponent);
    } else {
        componentManager = component_manager_home.create(lngComponent, lngVersion);
    }

    // If user is not logged in, redirect to login first
    // or if user was logged in via cookies
    if (tcSubject == null || tcUser == null || session.getAttribute("LOGIN_FLAG") == null) {
        session.putValue("nav_redirect", "/catalog/c_component_download.jsp?comp=" + lngComponent + "&ver=" + lngVersion);
        session.putValue("nav_redirect_msg", "You must login before you can download components.");
        response.sendRedirect("/login.jsp");
    }

    try {
        if (!componentManager.canDownload(tcSubject)) {
            response.sendRedirect("/tcs?module=ViewComponentTerms&comp=" + lngComponent + ((lngVersion == 0) ? "" : "&ver=" + lngVersion) );
        }
    } catch (Exception e) {
        response.sendRedirect("/tcs?module=ViewComponentTerms&comp=" + lngComponent + ((lngVersion == 0) ? "" : "&ver=" + lngVersion) );
    }

    Download downloads[] = (Download[])componentManager.getDownloads().toArray(new Download[0]);
    LicenseLevel licenses[] = (LicenseLevel[])catalog.getLicenseLevels().toArray(new LicenseLevel[0]);
%>

<title>Download software from the Component Catalog at TopCoder</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>

</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->


<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <jsp:include page="/includes/left.jsp" >
                <jsp:param name="level1" value="catalog"/>
                <jsp:param name="level2" value="download"/>
            </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter ends -->

<!-- Center Column begins -->
        <td width="99%">
        	<div class="minheight">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                <tr><td class="normal"><img src="/images/headDownloadNow.gif" alt="Download Now" width="545" height="32" border="0" /></td></tr>
                <tr><td><h3><%= componentManager.getComponentInfo().getName() %> <span class="version"><%= componentManager.getVersionInfo().getVersionLabel() %></span></h3></td></tr>
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
            </table>

            <table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
                <tr valign="top">
                    <td align="center">
                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tr><td><img src="/images/downloadPackageHead.gif" alt="Download Package" width="500" height="29" border="0" /></td></tr>
                        </table>

                        <table width="500" border="0" cellspacing="4" cellpadding="0" align="center" class="register">
<!-- Column Setting Row -->
                            <tr valign="middle">
                                <td width="5"><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td width="44%"><img src="/images/clear.gif" width="20" height="1" border="0"/></td>
                                <td width="2%"><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
                                <td width="44%" ><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
                                <td width="5"><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

<!--
    #### BEGIN FORM HEADER
    Note: The begin and end form tag has to wrap BOTH license loop and download loops
-->
<form name="frmDownload" action="/catalog/download" method="POST">
<input type="HIDDEN" name="comp" value="<%=lngComponent%>">
<input type="HIDDEN" name="ver" value="<%=lngVersion%>">
<!--
    END FORM HEADER
-->

<!-- Download Package Instructions -->
                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td colspan="3" class="registerText">Some TopCoder Software is packaged to run in certain development environments.
                                    Choose the Package below that best suits your environment. Installation instructions and other information are included with each Package.</td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Download Package Error Text -->
                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td class="registerText"></td>
                                <td class="errorText"></td>
                                <td class="registerText"></td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Download Package -->
<%  for (int i=0; i < downloads.length; i++) { %>
        <!-- LOOP CONTENT BEGIN -->
                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td class="registerText"></td>
                                <td class="registerText" nowrap><input type="RADIO" name="id" value="<%=downloads[i].getId()%>" <%=(i==0)?"checked":""%>>&nbsp;<%=downloads[i].getDescription()%></td>
                                <td class="registerText"></td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>
        <!-- LOOP CONTENT END -->
<% } %>
                            <tr><td height="15" colspan="5"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>

                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td class="registerText"></td>
                                <td class="registerTextCenter" ><input type="submit" name="a" value="Download"></td>
                                <td class="registerText"></td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

                            <tr><td height="15" colspan="5"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>

                        </table>

                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tr><td><img src="/images/regFoot.gif" alt="" width="500" height="10" border="0" /></td></tr>
                            <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td class="normal">If you experience any download problems, contact us at <a href="mailto:service@topcodersoftware.com?subject=Download Problems">service@topcodersoftware.com</a></td>
                </tr>

                <tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0"/></td></tr>
            </table>
           </div>
<!-- Center Column begins -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter ends -->

<!--Right Column begins -->
        <td width="170">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="170" height="15" border="0" /></td></tr>
            </table>
        </td>
<!--Right Column ends -->

<!-- Gutter begins -->
        <td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"></td>
<!-- Gutter ends -->

    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</form>
</body>
</html>
