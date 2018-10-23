<%@ page import="javax.naming.*,
                 com.topcoder.dde.catalog.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%
	// STANDARD PAGE VARIABLES
	String page_name = "c_advanced_search.jsp";
	String action = request.getParameter("a");
	String strMsg = "";
    Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
    CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
    Catalog catalog = home.create();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/2000/REC-xhtml1-20000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptCheckAll.js">
</script>
</head>
<body class="body" marginheight="0" marginwidth="0">
<!-- Header begins -->
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/nav.jsp" %>
<!-- Header ends -->
<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr><td width="100%" height="2" bgcolor="#000000"><img src="/images/clear.gif" alt="" width="10" height="2" border="0"/></td></tr>
</table>
<!-- breadcrumb ends -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
	<tr valign="top">
<!-- Left Column begins -->
		<td width="165" class="leftColumn">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="165" height="5" border="0" /></td></tr>
			</table>
		</td>
		<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0" /></td>
<!-- Left Column ends -->
<!-- Gutter 1 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->
<!-- Middle Column begins -->
		<td width="100%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal"><img src="/images/headAdvancedSearch.gif" alt="Advanced Search" width="400" height="32" border="0" /></td></tr>
            </table>
            <div align="center">
            <form action="c_showroom_search.jsp" method="post" name="frmSiteSearch">
            <input type="hidden" name="a" value="search"/>
            <table width="60%" border="0" cellpadding="0" cellspacing="1" align="center" class="forumBkgd">
            <tr valign="top">
            	<td class="forumTextEven"><b>Status</b></td>
            </tr>
            <tr valign="top">
            	<td class="forumTextOdd" valign="top" align="center">
            	<table cellspacing="0" celpadding="1" border="0" width="100%">
            	<!-- options 1-->
            		<tr>
            			<td valign="middle"><input type="checkbox" name="status_all" value="true" onclick="checkallclicked(this, comp_status)"/></td>
            			<td valign="middle" align="left" width="40%"><font color="#990000">Select All</font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            			<td valign="middle"></td>
            			<td valign="middle" align="left" width="40%"></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
            		<tr>
            			<td valign="middle"><input type="checkbox" name="comp_status" value="<%= ComponentVersionInfo.COLLABORATION %>" onclick="singleclicked(this, status_all)" /></td>
            			<td valign="middle" align="left" width="40%">Collaboration</font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            			<td valign="middle"><input type="checkbox" name="comp_status" value="<%= ComponentVersionInfo.DEVELOPMENT %>" onclick="singleclicked(this, status_all)" /></td>
            			<td valign="middle" align="left" width="40%">Development and Testing</td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
            		<tr>
            			<td valign="middle"><input type="checkbox" name="comp_status" value="<%= ComponentVersionInfo.SPECIFICATION %>" onclick="singleclicked(this, status_all)" /></td>
            			<td valign="middle" align="left" width="40%">Design and Architecture</font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            			<td valign="middle"><input type="checkbox" name="comp_status" value="<%= ComponentVersionInfo.COMPLETED %>" onclick="singleclicked(this, status_all)" /></td>
            			<td valign="middle" align="left" width="40%">Complete</td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
            		<tr>
            			<td colspan="6"><img src="/images/clear.gif" width="10" height="5" alt="" border="0"></td>
            		</tr>
            	<!-- options 1-->
            	</table>
            	</td>
            </tr>
          	<tr valign="top">
            	<td class="forumTextEven"><b>Catalog</b></td>
            </tr>
            <tr valign="top">
            	<td class="forumTextOdd" valign="top">
            	<table cellspacing="0" celpadding="1" border="0" width="100%">
            	<!-- options 2-->
            		<tr>
            			<td valign="middle"><input type="checkbox" name="catalog_all" value="true"  onclick="checkallclicked(this, catalog)"/></td>
            			<td valign="middle" align="left" width="40%"><font color="#990000">Select All</font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            			<td valign="middle"></td>
            			<td valign="middle" align="left" width="40%"></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%
    // Get only visible categories.
    Category[] baseCategories = catalog.getBaseCategories(true);
    for (int i = 0; i < baseCategories.length; i++) {
        if (i % 2 == 0) {
%>
            		<tr>
            			<td valign="middle"><input type="checkbox" name="catalog" value="<%= baseCategories[i].getId() %>" onclick="singleclicked(this, catalog_all)" /></td>
            			<td valign="middle" align="left" width="40%"><%= baseCategories[i].getName() %></font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
<%      } else {
%>
            			<td valign="middle"><input type="checkbox" name="catalog" value="<%= baseCategories[i].getId() %>" onclick="singleclicked(this, catalog_all)" /></td>
            			<td valign="middle" align="left" width="40%"><%= baseCategories[i].getName() %></font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%      }
    }
    if (baseCategories.length % 2 == 1) {
%>
            			<td valign="middle"></td>
            			<td valign="middle" align="left" width="40%"></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%  }
%>
            		<tr>
            			<td colspan="6"><img src="/images/clear.gif" width="10" height="5" alt="" border="0"></td>
            		</tr>
            	<!-- options 2-->
            	</table>
            	</td>
            </tr>
            <tr valign="top">
            	<td class="forumTextEven"><b>Technology</b></td>
            </tr>
            <tr valign="top">
            	<td class="forumTextOdd" valign="top">
            	<table cellspacing="0" celpadding="1" border="0" width="100%">
            	<!-- options 3-->
            		<tr>
            			<td valign="middle"><input type="checkbox" name="technology_all" value="true" onclick="checkallclicked(this, technology)" /></td>
            			<td valign="middle" align="left" width="40%"><font color="#990000">Select All</font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            			<td valign="middle"></td>
            			<td valign="middle" align="left" width="40%"></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%
    Technology[] technologies = catalog.getAllTechnologies();
    for (int i = 0; i < technologies.length; i++) {
        if (i % 2 == 0) {
%>
            		<tr>
            			<td valign="middle"><input type="checkbox" name="technology" value="<%= technologies[i].getId() %>" onclick="singleclicked(this, technology_all)" /></td>
            			<td valign="middle" align="left" width="40%"><%= technologies[i].getName() %></font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
<%      } else {
%>
            			<td valign="middle"><input type="checkbox" name="technology" value="<%= technologies[i].getId() %>" onclick="singleclicked(this, technology_all)" /></td>
            			<td valign="middle" align="left" width="40%"><%= technologies[i].getName() %></font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%      }
    }
    if (technologies.length % 2 == 1) {
%>
            			<td valign="middle"></td>
            			<td valign="middle" align="left" width="40%"></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%  }
%>
            		<tr>
            			<td colspan="6"><img src="/images/clear.gif" width="10" height="5" alt="" border="0"></td>
            		</tr>
            	<!-- options 3-->
            	</table>
            	</td>
            </tr>
            <tr valign="top">
            	<td class="forumTextEven"><b>Category</b></td>
            </tr>
            <tr valign="top">
            	<td class="forumTextOdd" valign="top">
            	<table cellspacing="0" celpadding="1" border="0" width="100%">
            	<!-- options 4-->
            		<tr>
            			<td valign="middle"><input type="checkbox" name="category_all" value="true" onclick="checkallclicked(this, category)" /></td>
            			<td valign="middle" align="left" width="40%"><font color="#990000">Select All</font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            			<td valign="middle"></td>
            			<td valign="middle" align="left" width="40%"></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%
    String[] categories = catalog.getUniqueCategoryNames(false, true);
    for (int i = 0; i < categories.length; i++) {
        if (i % 2 == 0) {
%>
            		<tr>
            			<td valign="middle"><input type="checkbox" name="category" value="<%= categories[i] %>" onclick="singleclicked(this, category_all)" /></td>
            			<td valign="middle" align="left" width="40%"><%= categories[i] %></font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
<%      } else {
%>
            			<td valign="middle"><input type="checkbox" name="category" value="<%= categories[i] %>" onclick="singleclicked(this, category_all)" /></td>
            			<td valign="middle" align="left" width="40%"><%= categories[i] %></font></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%      }
    }
    if (categories.length % 2 == 1) {
%>
            			<td valign="middle"></td>
            			<td valign="middle" align="left" width="40%"></td>
            			<td valign="middle" width="10"><img src="/images/clear.gif" width="10" height="3" alt="" border="0"></td>
            		</tr>
<%  }
%>
            		<tr>
            			<td colspan="6"><img src="/images/clear.gif" width="10" height="5" alt="" border="0"></td>
            		</tr>
            	<!-- options 4-->
            	</table>
            	</td>
            </tr>
            <tr valign="top">
            	<td class="forumTextEven">
            	<table cellspacing="0" cellpadding="0" border="0" width="100%">
            		<tr>
            			<td class="forumTextEven" valign="middle"><b>Keyword(s)</b></td>
            			<td class="forumTextEven" valign="middle" width="5"><img src="/images/clear.gif" width="5" height="3" alt="" border="0"></td>
            			<td class="forumTextEven" valign="middle"><input type="text" name="keywords" size="40" maxlength="50" /></td>
            		</tr>
            	</table>
            	</td>
            </tr>
            </table>
                <table border="0" cellpadding="3" cellspacing="0">
                <tr>
                    <td><img src="/images/clear.gif" width="1" height="3" alt="" border="0"></td>
                </tr>
                <tr>
                    <td class="bodyText" align="center"><input type="reset" name="Clear" value="Clear"></td>
                    <td class="bodyText" align="center"><img src="/images/clear.gif" width="2" height="3" alt="" border="0"></td>
                    <td class="bodyText" align="center"><input type="submit" name="Search" value="Search"></td>
                </tr>
                <tr>
                    <td><img src="/images/clear.gif" width="1" height="20" alt="" border="0"></td>
                </tr>
           		</table>
           		</form>
           		</div>
		</td>
<!--Middle Column ends -->
<!-- Gutter 3 begins -->
		<td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->
	</tr>
</table>
<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->
</body>
</html>