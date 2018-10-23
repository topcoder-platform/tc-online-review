<%@ page import="javax.naming.*,
                 com.topcoder.dde.catalog.CatalogSearchView,
                 com.topcoder.dde.catalog.Category,
                 com.topcoder.dde.catalog.Catalog" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "c_showroom_search.jsp";
    String action = request.getParameter("a");
%>

<%@ include file="/includes/componentCategories.jsp" %>
<%

    String target = request.getParameter("target");
    if (target == null) target = "advanced";

    boolean allCatalogs = target.equals("all_catalogs") || request.getParameter("catalog_all") != null;
    boolean allTechnologies = request.getParameter("technology_all") != null;
    boolean allStatuses = request.getParameter("status_all") != null;
    boolean allCategories = request.getParameter("category_all") != null;

    // Get only visible categories.
    Category[] baseCategories = catalog.getBaseCategories(false);
    Category java = null;
    Category net = null;
    Category flash = null;
    Category javaCustom = null;
    Category netCustom = null;
    for (int i = 0; i < baseCategories.length; i++) {
        if (baseCategories[i].getId() == Catalog.NET_CATALOG) net = baseCategories[i];
        if (baseCategories[i].getId() == Catalog.JAVA_CATALOG) java = baseCategories[i];
        if (baseCategories[i].getId() == Catalog.FLASH_CATALOG) flash = baseCategories[i];
        if (baseCategories[i].getId() == Catalog.JAVA_CUSTOM_CATALOG) javaCustom = baseCategories[i];
        if (baseCategories[i].getId() == Catalog.NET_CUSTOM_CATALOG) netCustom = baseCategories[i];
    }

    long[] catalogs;
    long[] technologies;
    long[] statuses;
    String[] categories;

    if (allCatalogs) catalogs = new long[0];
    else {
        String[] vals = request.getParameterValues("catalog");
        if (vals == null) vals = new String[0];
        catalogs = new long[vals.length];
        for (int i = 0; i < vals.length; i++) {
            try {
                catalogs[i] = Long.parseLong(vals[i]);
            } catch (Exception e) {
                debug.addMsg("search", e.toString());
            }
        }
    }

    if (allTechnologies) technologies = new long[0];
    else {
        String[] vals = request.getParameterValues("technology");
        if (vals == null) vals = new String[0];
        technologies = new long[vals.length];
        for (int i = 0; i < vals.length; i++) {
            try {
                technologies[i] = Long.parseLong(vals[i]);
            } catch (Exception e) {
                debug.addMsg("search", e.toString());
            }
        }
    }

    if (allStatuses) statuses = new long[0];
    else {
        String[] vals = request.getParameterValues("comp_status");
        if (vals == null) vals = new String[0];
        statuses = new long[vals.length];
        for (int i = 0; i < vals.length; i++) {
            try {
                statuses[i] = Long.parseLong(vals[i]);
            } catch (Exception e) {
                debug.addMsg("search", e.toString());
            }
        }
    }

    if (allCategories) categories = new String[0];
    else categories = request.getParameterValues("category");
    if (categories == null) categories = new String[0];

    String keywords = request.getParameter("keywords");
    if (keywords == null) {
        keywords = "";
    }

    CatalogSearchView componentSearchResults = null;
    CatalogSearchView forumSearchResults = null;

    // If action is null, show advanced search form
    // If action is not null, try to do the search
    if (action != null) {
        if (target.equals("advanced")) {
            componentSearchResults = catalog.searchComponents(keywords, statuses, catalogs, technologies, categories, true);
        } else if (target.equals("all_catalogs")) {
            componentSearchResults = catalog.searchComponents(keywords, statuses, new long[0], technologies, categories, true);
        } else if (target.equals("net_catalog")) {
            componentSearchResults = catalog.searchComponents(keywords, statuses, new long[] { net.getId() }, technologies, categories, true);
        } else if (target.equals("java_catalog")) {
            componentSearchResults = catalog.searchComponents(keywords, statuses, new long[] { java.getId() }, technologies, categories, true);
        } else if (target.equals("forum")) {
            forumSearchResults = null;
        } else if (target.equals("entire_site")) {
            componentSearchResults = catalog.searchComponents(keywords, statuses, catalogs, technologies, categories, true);
            forumSearchResults = null;
        }


    } else {
        debug.addMsg("search", "searching not performed");
    }

%>

<html>
<head>
    <title>Search the Component Catalog for software at TopCoder</title>
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" src="/scripts/javascript.js">
</script>

</head>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <jsp:include page="/includes/left.jsp" >
                <jsp:param name="level1" value="catalog"/>
                <jsp:param name="level2" value="search"/>
            </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
<% if (componentSearchResults != null && forumSearchResults == null) {
%>
        <td width="100%" class="minheight">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr valign="top">
                    <td width="98%">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                            <tr><td class="normal"><img src="/images/hd_search_results.png" alt="Search Results" border="0"></td></tr>
                        </table>
                    <td width="1%">
                        <table width="199" border="0" cellpadding="0" cellspacing="0">
                            <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                            <tr><td class="normal"><img src="/images/statusKey.gif" alt="Status Key" width="454" height="35" border="0"></td></tr>
                        </table>
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr valign="middle">
                    <td class="searchResults">Your search for <c:out value="<%= keywords %>" /> found <%= componentSearchResults.size() %> result<%= componentSearchResults.size() == 1 ? "" : "s" %>.</td>
                    <td width="1%" align="right">
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" class="forumBkgd">
<%  if (componentSearchResults != null && componentSearchResults.size() > 0) { %>
                <tr valign="top">
                    <td width="195" class="forumTitle">Software</td>
                    <td width="75%" class="forumTitle">Description</td>
                    <td width="5%" class="forumTitleCenter">Download</td>
                </tr>
<%      Iterator itr = componentSearchResults.iterator();
        int row = 0;
        String oddeven;
        while (itr.hasNext()) {
            ComponentSummary summary = (ComponentSummary)itr.next();
            String strStatusIcon;
            switch( (int)summary.getPhase() ) {
            case (int)ComponentVersionInfo.COLLABORATION:
                strStatusIcon = "/images/iconStatusCollabSm.gif";
                break;
            case (int)ComponentVersionInfo.SPECIFICATION:
                strStatusIcon = "/images/iconStatusSpecSm.gif";
                break;
            case (int)ComponentVersionInfo.DEVELOPMENT:
                strStatusIcon = "/images/iconStatusDevSm.gif";
                break;
            default:
            case (int)ComponentVersionInfo.COMPLETED:
                strStatusIcon = "/images/iconStatusCompleteSm.gif";
                break;
            }
            row++;
            oddeven = (row%2==0)?"Even":"Odd";
            int refCatalog = -1;
            if (summary.getAolComponent()) {
                refCatalog = 5;
            } else {
                if (java.getId() == summary.getRootCategory()) refCatalog = 0;
                else if (net.getId() == summary.getRootCategory()) refCatalog = 1;
                else if (flash.getId() == summary.getRootCategory()) refCatalog = 2;
                else if (javaCustom.getId() == summary.getRootCategory()) refCatalog = 3;
                else if (netCustom.getId() == summary.getRootCategory()) refCatalog = 4;
                else refCatalog = 6;
            }
%>
                <tr valign="top">
                    <td class="forumText<%=oddeven%>">
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr valign="top">
                                <td width="25" class="forumText"><img src="<%= strStatusIcon %>" alt="" width="25" height="17" border="0" /></td>
                                <td width="5" class="forumText"><img src="/images/clear.gif" alt="" width="5" height="5" border="0"/></td>
                                <td width="35" class="forumText"><%

                        if (refCatalog == 0) {
                            %><img src="/images/javaSm.gif" alt="" border="0" /><%
                        } else if (refCatalog == 1) {
                            %><img src="/images/dotnetSm.gif" alt="" border="0" /><%
                        } else if (refCatalog == 2) {
                            %><img src="/images/flashSm.gif" alt="" border="0" /><%
                        } else if (refCatalog == 3) {
                            %><img src="/images/javaCustomSm.gif" alt="" border="0" /><%
                        } else if (refCatalog == 4) {
                            %><img src="/images/dotnetCustomSm.gif" alt="" border="0" /><%
                        } else if (refCatalog == 5) {
                            %><img src="/images/aolSm.gif" alt="" border="0" /><%
                        } else if (refCatalog == 6) {
                            %><img src="/images/otherSm.gif" alt="" border="0" /><%
                        }
                            %></td>
                                <td width="5" class="forumText"><img src="/images/clear.gif" alt="" width="5" height="5" border="0"/></td>
                                <td width="165" class="forumText"><a href="c_component.jsp?comp=<%= summary.getComponentId() %>" class="top"><strong><%= summary.getName() %></strong></a><br />
                                    <img src="/images/clear.gif" alt="" width="165" height="1" border="0"/></td>
                            </tr>
                        </table></td>
                    <td class="forumText<%=oddeven%>"><%= summary.getShortDescription() %><a class="more" href="c_component.jsp?comp=<%= summary.getComponentId() %>">::&nbsp;more</a></td>
                    <td class="forumTextCenter<%=oddeven%>">
<%            if (summary.getPhase() == ComponentVersionInfo.COMPLETED) {%>
                                    <a href="/tcs?module=ViewComponentTerms&comp=<%= summary.getComponentId() %>&ver=<%= summary.getVersion() %>"><img src="/images/iconDownloadSm.gif" alt="Download" width="19" height="17" border="0"></a>
<%            }%>
                    </td>
                </tr>
<%      }
    } else {
%>
                <tr><td width="100%" class="normal">No results were found. Please try broadening your search criteria.</td></tr>
<%  } %>
                <tr valign="top"><td class="forumHeadFoot" colspan="3"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
            </table>

            <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                <TBODY>
                    <TR><TD><IMG height=20 alt="" src="/images/clear.gif" width=10 border=0></TD></TR>
                    <TR><TD align=right><a href="/components/request.jsp"><IMG height=29 alt="" src="/images/promoNewCompRequest.gif" width=245 border=0></a></TD></TR>
                    <TR><TD><IMG height=40 alt="" src="/images/clear.gif" width=10 border=0></TD></TR>
                </TBODY>
            </TABLE>

            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td><img src="/images/clear.gif" alt="" width="10" height="40" border="0"/></td></tr>
            </table>
        </td>

<% } else if (componentSearchResults == null && forumSearchResults != null) {
%>
Forum Search Result Display
<% } else if (componentSearchResults != null && forumSearchResults != null) {
%>
Combined Search Result Display
<% }
%>
<!-- Middle Column ends -->

<!-- Gutter 3 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter 3 ends -->

    </tr>
</table>

<jsp:include page="/includes/foot.jsp" flush="true" />

<!--<%= (cameFromCache ? "The categories on this page came from cache." : "") %>-->

</body>
</html>
