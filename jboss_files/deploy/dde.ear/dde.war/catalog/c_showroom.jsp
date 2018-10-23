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
    String page_name = "c_showroom.jsp";
    String action = request.getParameter("a");
    if (action == null) action = "";
%>

<%@ include file="/includes/componentCategories.jsp" %>
<%!

    class Flag {
        private boolean v = false;
        public Flag(boolean v) {
            this.v = v;
        }
        public void setValue(boolean v) {
            this.v = v;
        }
        public boolean getValue() {
            return this.v;
        }
    }

    String getStatusIcon(int phase) {
        switch( phase ) {
        case (int)ComponentVersionInfo.COLLABORATION:
            return "/images/iconStatusCollabSm.gif";
        case (int)ComponentVersionInfo.SPECIFICATION:
            return "/images/iconStatusSpecSm.gif";
        case (int)ComponentVersionInfo.DEVELOPMENT:
            return "/images/iconStatusDevSm.gif";
        case (int)ComponentVersionInfo.COMPLETED:
        default:
            return "/images/iconStatusCompleteSm.gif";
        }
    }

    String displayOverview(CategorySummary cat, int depth, int depthinc) {
        Flag odd = new Flag(true);
        String rv = "";
        CategorySummary[] subs = cat.getSubcategories();
        for (int i = 0; i < subs.length; i++) {
            if (subs[i].isEmpty()) continue;
            rv += "<tr>\n"
                + "    <td class=\"catalogCategory\">";
            for (int j = 0; j < depth; j++) rv += "&nbsp;";
            rv += "<a class=\"catalogCategoryLink\" href=\"c_showroom.jsp?cat=" + subs[i].getId() + "\">" + subs[i].getName() + "</a></td>\n"
                + "</tr>\n"
                + displayOverview(subs[i], depth+depthinc, depthinc);
        }

        ComponentSummary[] comps = cat.getComponents();
        for (int i = 0; i < comps.length; i++) {
            rv += "<tr>\n"
                + "    <td width=\"100%\" class=\"catalogText" + (odd.getValue() ? "Odd" : "Even" ) + "\">\n"
                + "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "             <tr valign=\"top\">\n"
                + "                 <td width=\"25\" class=\"catalogText\"><img src=\"" + getStatusIcon((int) comps[i].getPhase()) + "\" alt=\"Icon\" width=\"25\" height=\"17\" border=\"0\" /></td>\n"
                + "                 <td width=\"5\" class=\"catalogText\"><img src=\"/images/clear.gif\" alt=\"\" width=\"5\" height=\"5\" border=\"0\"/></td>\n"
                + "                 <td width=\"99%\" class=\"catalogText\">";
            rv += "<a href=\"c_component.jsp?comp=" + comps[i].getComponentId() + "&ver=" + comps[i].getVersion() + "\"><strong>" + comps[i].getName() + "</strong></a></td>\n"
                + "             </tr>\n"
                + "        </table>\n"
                + "    </td>\n"
                + "</tr>\n";
            odd.setValue(!odd.getValue());
        }

        return rv;
    }

    String displayDetailed(CategorySummary cat, int depth, int depthinc, int catalog) {
        String rv = "";
        Flag odd = new Flag(true);
        CategorySummary[] subs = cat.getSubcategories();
        for (int i = 0; i < subs.length; i++) {
            if (subs[i].isEmpty()) continue;
            rv += "<tr>\n"
                + "    <td colspan=\"2\" class=\"catalogCategory\">";
            for (int j = 0; j < depth; j++) rv += "&nbsp;";
            rv += "<a class=\"catalogCategoryLink\" href=\"c_showroom.jsp?cat=" + subs[i].getId() + "\">" + subs[i].getName() + "</a></td>\n"
                + "</tr>\n"
                + displayDetailed(subs[i], depth+depthinc, depthinc, catalog);
        }

        ComponentSummary[] comps = cat.getComponents();
        for (int i = 0; i < comps.length; i++) {
            rv += "<tr valign=\"top\">\n"
                + "    <td class=\"catalogText" + (odd.getValue() ? "Odd" : "Even" ) + "\">\n"
                + "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "            <tr valign=\"top\">\n"
                + "                <td width=\"25\" class=\"catalogText\"><img src=\"" + getStatusIcon((int) comps[i].getPhase()) + "\" alt=\"\" width=\"25\" height=\"17\" border=\"0\" /></td>\n"
                + "                <td width=\"5\" class=\"catalogText\"><img src=\"/images/clear.gif\" alt=\"\" width=\"5\" height=\"5\" border=\"0\"/></td>\n"
                + "                <td width=\"35\" class=\"catalogText\">";
            if (comps[i].getAolComponent()) {
                rv += "<img src=\"/images/aolSm.gif\" alt=\"\" border=\"0\" />";
            } else {
                switch (catalog) {
                    case 0:
                        rv += "<img src=\"/images/javaSm.gif\" alt=\"\" border=\"0\" />";
                        break;
                    case 1:
                        rv += "<img src=\"/images/netSm.gif\" alt=\"\" border=\"0\" />";
                        break;
                    case 2:
                        rv += "<img src=\"/images/flashSm.gif\" alt=\"\" border=\"0\" />";
                        break;
                    default:
                        break;
                }
            }
            rv += "</td>\n"
                + "                <td width=\"5\" class=\"catalogText\"><img src=\"/images/clear.gif\" alt=\"\" width=\"5\" height=\"5\" border=\"0\"/></td>\n"
                + "                <td width=\"99%\" class=\"catalogText\"><a href=\"c_component.jsp?comp=" + comps[i].getComponentId() + "&ver=" + comps[i].getVersion() + "\"><strong>" + comps[i].getName() + "</strong></a><br />\n"
                + "                    <img src=\"/images/clear.gif\" alt=\"\" width=\"165\" height=\"1\" border=\"0\"/></td>\n"
                + "            </tr>\n"
                + "        </table>\n"
                + "    </td>\n"
                + "    <td class=\"catalogText" + (odd.getValue() ? "Odd" : "Even" ) + "\">" + comps[i].getShortDescription() + "&nbsp;<a class=\"more\" href=\"c_component.jsp?comp=" + comps[i].getComponentId() + "&ver=" + comps[i].getVersion() + "\">more</a></td>\n"
                + "</tr>\n";
            odd.setValue(!odd.getValue());
        }

        return rv;
    }

    String displayDetailed(ComponentSummary comps[], long javaId, long netId, long flashId) {
        String rv = "";
        Flag odd = new Flag(true);
        for (int i = 0; i < comps.length; i++) {
            int catalog = -1;
            if (comps[i].getAolComponent()) {
                catalog = 3;
            } else {
                if (comps[i].getRootCategory() == javaId) catalog = 0;
                else if (comps[i].getRootCategory() == netId) catalog = 1;
                else if (comps[i].getRootCategory() == flashId) catalog = 2;
            }
            rv += "<tr valign=\"top\">\n"
                + "    <td class=\"catalogText" + (odd.getValue() ? "Odd" : "Even" ) + "\">\n"
                + "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "            <tr valign=\"top\">\n"
                + "                <td width=\"25\" class=\"catalogText\"><img src=\"" + getStatusIcon((int) comps[i].getPhase()) + "\" alt=\"\" width=\"25\" height=\"17\" border=\"0\" /></td>\n"
                + "                <td width=\"5\" class=\"catalogText\"><img src=\"/images/clear.gif\" alt=\"\" width=\"5\" height=\"5\" border=\"0\"/></td>\n"
                + "                <td width=\"35\" class=\"catalogText\">";
                switch (catalog) {
                    case 0:
                        rv += "<img src=\"/images/javaSm.gif\" alt=\"\" border=\"0\" />";
                        break;
                    case 1:
                        rv += "<img src=\"/images/netSm.gif\" alt=\"\" border=\"0\" />";
                        break;
                    case 2:
                        rv += "<img src=\"/images/flashSm.gif\" alt=\"\" border=\"0\" />";
                        break;
                    case 3:
                        rv += "<img src=\"/images/aolSm.gif\" alt=\"\" border=\"0\" />";
                        break;
                    default:
                        break;
                }
            rv += "</td>\n"
                + "                <td width=\"5\" class=\"catalogText\"><img src=\"/images/clear.gif\" alt=\"\" width=\"5\" height=\"5\" border=\"0\"/></td>\n"
                + "                <td width=\"99%\" class=\"catalogText\"><a href=\"c_component.jsp?comp=" + comps[i].getComponentId() + "&ver=" + comps[i].getVersion() + "\"><strong>" + comps[i].getName() + "</strong></a><br />\n"
                + "                    <img src=\"/images/clear.gif\" alt=\"\" width=\"165\" height=\"1\" border=\"0\"/></td>\n"
                + "            </tr>\n"
                + "        </table>\n"
                + "    </td>\n"
                + "    <td class=\"catalogText" + (odd.getValue() ? "Odd" : "Even" ) + "\">" + comps[i].getShortDescription() + "&nbsp;<a class=\"more\" href=\"c_component.jsp?comp=" + comps[i].getComponentId() + "&ver=" + comps[i].getVersion() + "\">more</a></td>\n"
                + "</tr>\n";
            odd.setValue(!odd.getValue());
        }

        return rv;
    }

    String buildTree(Category[] cats, Set expand, int depth, int depthinc) {
        String rv = "";
        for (int i = 0; i < cats.length; i++) {
            rv += " <tr valign=\"top\">\n"
                + "         <td width=\"10\"><img src=\"/images/clear.gif\" alt=\"\" width=\"10\" height=\"5\" border=\"0\"></td>\n"
                + "         <td class=\"leftNavText\">";
            for (int j = 0; j < depth; j++) rv += "&nbsp;";
            rv += "         <a href=\"c_showroom.jsp?cat=" + cats[i].getId() + "\">" + cats[i].getName() + (depth == depthinc ? " Catalog" : "") + "</a></td>\n"
                + "         <td width=\"10\"><img src=\"/images/clear.gif\" alt=\"\" width=\"10\" height=\"5\" border=\"0\" /></td>\n"
                + "</tr>\n";
            if (expand.contains(new Long(cats[i].getId()))) rv += buildTree((Category[]) cats[i].getSubcategories().toArray(new Category[0]), expand, depth+depthinc, depthinc);
        }
        return rv;
    }
%>
<%

    long lngCategory = -1;
    try {
        lngCategory = Long.parseLong(request.getParameter("cat"));
    } catch (Exception e) {}

    //Catalog catalog = null;
    CategorySummary summary = null;
    CategorySummary[] summaries = null;
    boolean topLevel = true;
    boolean all = false;

    Category[] cats = new Category[3];
    Category[] allCategories = (Category[]) colCategories.toArray(new Category[0]);
    for(int i=0;i<allCategories.length;i++) {
        if (allCategories[i].getId() == Catalog.JAVA_CATALOG) {
            cats[0] = allCategories[i];
        }
        if (allCategories[i].getId() == Catalog.NET_CATALOG) {
            cats[1] = allCategories[i];
        }
        if (allCategories[i].getId() == Catalog.FLASH_CATALOG) {
            cats[2] = allCategories[i];
        }
    }

    long javaId = Catalog.JAVA_CATALOG;
    long netId = Catalog.NET_CATALOG;
    long flashId = Catalog.FLASH_CATALOG;

    if (action.equals("all")) {
        all = true;
        lngCategory = -1;
        topLevel = false;
    } else {
        if (lngCategory != -1) {
            topLevel = false;
            try {
                summary = catalog.getCategorySummary(lngCategory);
            } catch (CatalogException e) {
                // redirect to an error page
                response.sendRedirect("/attention/error_categorynotfound.jsp");
                return;
            }
            long rootCategory = summary.getParent();
            if (rootCategory == 0) {
                rootCategory = summary.getId();
            }
            
            // Get only visible categories.
            Category[] baseCategories = catalog.getBaseCategories(true);
            boolean visibleCategory = false;
            for (int i = 0; i < baseCategories.length && !visibleCategory; i++) {
                if (rootCategory == baseCategories[i].getId()) {
                    visibleCategory = true;
                }
            }
        
            if (!visibleCategory) {
                // redirect to an error page
                response.sendRedirect("/attention//error_categorynotfound.jsp");
                return;
            }
        } else {
            try {
                summaries = new CategorySummary[cats.length];
                for (int i = 0; i < summaries.length; i++) summaries[i] = catalog.getCategorySummary(cats[i].getId());
            } catch (CatalogException e) {
                // redirect to an error page
                response.sendRedirect("/attention/error_categorynotfound.jsp");
                return;
            }
        }
    }

%>
<html>
<head>
    <title>TopCoder Software</title>
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" src="/scripts/javascript.js">
</script>
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">
</head>



<body class="body" marginheight="0" marginwidth="0">
<a name="top" />

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="components"/>
            <jsp:param name="level2" value="find"/>
        </jsp:include>

<%
    Set expand = null;
    for (int i = 0; i < cats.length; i++) {
        expand = cats[i].findForTree(lngCategory);
        if (expand != null) break;
    }
    if (expand == null) expand = new HashSet();
    //out.print(buildTree(cats, expand, 2, 2));
%>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
<% if(topLevel) {
    CategorySummary net = null;
    CategorySummary java = null;
    for (int i = 0; i < summaries.length; i++) {
        if (summaries[i].getId() == Catalog.NET_CATALOG) net = summaries[i];
        if (summaries[i].getId() == Catalog.JAVA_CATALOG) java = summaries[i];
    }
%>

        <td width="99%">
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td colspan="3">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td class="normal">
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" /><br />
                                    <img src="/images/hd_components.png" alt="Components" border="0"><br />
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                                </td>
                            </tr>

<%
    String level2 = "browse";

    if(request.getParameter("a")!=null&&request.getParameter("a").equals("all")){
       level2="viewall";
    }
    else if(request.getParameter("cat")!=null){
       if(request.getParameter("cat").equals("5801777")){
          level2="net";
       }
       else{
          level2="java";
       }

    }
%>
                            <%@ include file="terciary.jsp" %>

                        </table>
                        <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                    </td>
                </tr>

                <tr valign="top">

<!-- Begin Java Catalog -->
                    <td width="266">
                        <table width="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr>
                                <td align="center"><a href="/components/subscriptions.jsp"><img src="/images/promos/promo_subscribe_java_cat.gif" alt="Subscribe to our Java&trade; Catalog" width="266" height="76" border="0"></a><br />
                                    <img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
                            </tr>
                            <tr><td class="catalogType">Java&#8482; Catalog</td></tr>
                        </table>

                        <table width="100%" cellpadding="0" cellspacing="1" border="0" class="catalogTable">

                            <%
                                out.print(displayOverview(java, 0, 2));
                            %>

                            <tr valign="top"><td class="forumHeadFoot"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
                        </table>
                    </td>
<!-- End Java Cataolg -->

                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>

<!-- Begin NET Catalog -->
                    <td width="266">
                        <table width="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr>
                                <td align="center"><a href="/components/subscriptions.jsp"><img src="/images/promos/promo_subscribe_dotnet_cat.gif" alt="Subscribe to our .NET&trade; Catalog" width="266" height="76" border="0"></a><br />
                                    <img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
                            </tr>
                            <tr><td class="catalogType">.NET&#8482; Catalog</td></tr>
                        </table>

                        <table width="100%" cellpadding="0" cellspacing="1" border="0" class="catalogTable">

                            <%
                                out.print(displayOverview(net, 0, 2));
                            %>

                        </table>
                    </td>
<!-- End NET Catalog-->

                </tr>
                <tr><td height="10" colspan="2"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td></tr>
            </table>
        </td>

<% } else if (all) { %>

        <td width="99%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="top">
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td class="normal">
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" /><br />
                                    <img src="/images/hd_components.png" alt="Components"border="0"><br />
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                                </td>
                            </tr>

<%
    String level2 = "browse";

    if(request.getParameter("a")!=null&&request.getParameter("a").equals("all")){
       level2="viewall";
    }
    else if(request.getParameter("cat")!=null){
       if(request.getParameter("cat").equals("5801777")){
          level2="net";
       }
       else{
          level2="java";
       }

    }
%>
                            <%@ include file="terciary.jsp" %>

                        </table>
                        <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                    </td>
                </tr>

                <tr valign="top">
                    <td width="100%">
                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="catalogTable">
                            <tr valign="top">
                                <td width="195" class="catalogCategory">Component</td>
                                <td width="99%" class="catalogCategory">Description</td>
                            </tr>

                            <%
                                out.print(displayDetailed(catalog.getAllComponents(true), javaId, netId, flashId));
                            %>

                            <tr valign="top"><td class="forumHeadFoot" colspan="3"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
                        </table>

                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr><td height="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"/></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>

<% } else {

        int refCatalog = -1;
        if (expand.contains(new Long(javaId))) refCatalog = 0;
        else if (expand.contains(new Long(netId))) refCatalog = 1;
        else if (expand.contains(new Long(flashId))) refCatalog = 2;
%>

        <td width="99%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="top">
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td class="normal">
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" /><br />
                                    <img src="/images/hd_components.png" alt="Components" border="0"><br />
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                                </td>
                            </tr>

<%
    String level2 = "browse";

    if(request.getParameter("a")!=null&&request.getParameter("a").equals("all")){
       level2="viewall";
    }
    else if(request.getParameter("cat")!=null){
       if(request.getParameter("cat").equals("5801777")){
          level2="net";
       }
       else{
          level2="java";
       }

    }
%>
                            <%@ include file="terciary.jsp" %>

                        </table>
                        <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                    </td>
                </tr>

                <tr valign="top">
                    <td width="100%">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr valign="middle"><td class="catalogType"><%= summary.getName() %></td></tr>
                        </table>

                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="catalogTable">

                            <%
                                out.print(displayDetailed(summary, 0, 2, refCatalog));
                            %>

                            <tr valign="top"><td class="catalogFoot" colspan="2"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
                        </table>

                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr><td height="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"/></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>

<% } %>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
            <jsp:include page="/includes/right.jsp" >
                <jsp:param name="level1" value="catalog"/>
                <jsp:param name="level2" value="allCatalogs"/>
            </jsp:include>
        </td>
<!-- Right Column ends -->

<!-- Gutter 3 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->

    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

<%--<!--<%= (cameFromCache ? "The categories on this page came from cache." : "") %>-->--%>

</body>
</html>
