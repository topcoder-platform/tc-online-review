<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "c_prodTools.jsp";
    String action = request.getParameter("a");
%>

<% // PAGE SPECIFIC DECLARATIONS %>
<%@ page import="com.topcoder.dde.catalog.*" %>
<%@ include file="/includes/clsCategoryNode.jsp" %>
<%
  // PHASES
  //  public static final long COLLABORATION = 111;
  //  public static final long SPECIFICATION = 112;
  //  public static final long DEVELOPMENT = 113;
  //  public static final long COMPLETED = 114;
%>

<%
    long lngCategory = 0;
    long lngComponent = 0;
    long lngVersion = 0;
    try {
        lngComponent = Long.parseLong(request.getParameter("comp"));
        lngVersion = Long.parseLong(request.getParameter("ver"));
    } catch (NumberFormatException nfe) {
        // invalid parameter, redirect to main page
        if (lngComponent == 0) response.sendRedirect("c_showroom.jsp");
    }

    Collection categoryPath = null;
    try {
        lngCategory = Long.parseLong(request.getParameter("cat"));

        if (lngCategory > 0) {
            CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(CONTEXT.lookup(CatalogHome.EJB_REF_NAME), CatalogHome.class);
            Catalog catalog = home.create();

            categoryPath = catalog.getCategoryPath(lngCategory);
            }
    } catch (NumberFormatException nfe) {
    }

    ComponentManagerHome component_manager_home = (ComponentManagerHome) PortableRemoteObject.narrow(CONTEXT.lookup(ComponentManagerHome.EJB_REF_NAME), ComponentManagerHome.class);
    ComponentManager componentManager;

    if (lngVersion == 0) {
        componentManager = component_manager_home.create(lngComponent);
    } else {
        componentManager = component_manager_home.create(lngComponent, lngVersion);
    }

    ComponentInfo componentInfo = componentManager.getComponentInfo();
    ComponentVersionInfo versionInfo = componentManager.getVersionInfo();

    Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
    CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
    Catalog catalog = home.create();

    Technology technologies[] = (Technology[])componentManager.getTechnologies().toArray(new Technology[0]);

    TeamMemberRole teamMemberRoles[] = (TeamMemberRole[])componentManager.getTeamMemberRoles().toArray(new TeamMemberRole[0]);

    LicenseLevel levels[] = (LicenseLevel[])catalog.getLicenseLevels().toArray(new LicenseLevel[0]);
    int cost[] = new int[levels.length];
    boolean hasCost = false;
    for (int i=0; i < levels.length; i++) {
        cost[i] = levels[i].calculateUnitCost(versionInfo.getPrice());
        if (cost[i] > 0) {
            hasCost = true;
        }
    }

    ComponentSummary summaries[] = (ComponentSummary[])componentManager.getDependencies().toArray(new ComponentSummary[0]);

    Example examples[] = (Example[])componentManager.getExamples().toArray(new Example[0]);

    //Separate screen shots and displayable documents
    Collection colTempDocuments = componentManager.getDocuments();
    Hashtable hashDocumentTypes = new Hashtable();
    //Collection colScreenShots = new ArrayList();
    long thumbnailId = 0;
    long screenshotId = 0;
    Collection colDocuments = new ArrayList();
    Iterator itr = colTempDocuments.iterator();
    while( itr.hasNext()) {
        Document doc = (Document) itr.next();
        if ((int)doc.getType() != com.topcoder.dde.catalog.Document.SCREEN_SHOT &&
                    (int)doc.getType() != com.topcoder.dde.catalog.Document.SCREEN_SHOT_THUMBNAIL) {
            colDocuments.add(doc);
        } else {
            if ((int)doc.getType() == com.topcoder.dde.catalog.Document.SCREEN_SHOT) {
                screenshotId = doc.getId();
            } else {
                thumbnailId = doc.getId();
            }
        }
    }
    if (screenshotId > 0 && thumbnailId == 0) {
        thumbnailId = screenshotId;
    }
    if (thumbnailId > 0 && screenshotId == 0) {
        screenshotId = thumbnailId;
    }
    Document documents[] = (Document[])colDocuments.toArray(new Document[0]);
    //Document screenshots[] = (Document[])colScreenShots.toArray(new Document[0]);

    boolean hasPreviousForumCategories = false;
    Collection colVersions = componentManager.getAllVersionInfo();
    ComponentVersionInfo versions[] = (ComponentVersionInfo[])colVersions.toArray(new ComponentVersionInfo[0]);
    com.topcoder.dde.catalog.ForumCategory specifications[] = new com.topcoder.dde.catalog.ForumCategory[versions.length];
    for (int i=0; i < versions.length; i++) {
        if (versions[i].getPhase() != ComponentVersionInfo.COMPLETED) {
            specifications[i] = null;
            continue;
        } else {
            componentManager.setVersion(versions[i].getVersion());
            try {
                specifications[i] = componentManager.getForumCategory();
            } catch (CatalogException ce) {
                // getForumCategory returns multiple forum categories of a type which is not supposed to happen
                // what to do?
            }
            if (specifications[i] != null) {
                hasPreviousForumCategories = true;
            }
        }
    }
    componentManager.setVersion(versionInfo.getVersion());
    com.topcoder.dde.catalog.ForumCategory activeSpec = null;
    try {
        activeSpec = componentManager.getActiveForumCategory();
    } catch (CatalogException ce) {
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>TopCoder Software</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
</head>

<body class="body">

<!-- Header begins -->
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/nav.jsp" %>
<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td width="100%" height="2" bgcolor="#000000"><img src="/images/clear.gif" alt="" width="10" height="2" border="0" /></td></tr>
</table>
<!-- breadcrumb ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <table border="0" cellspacing="0" cellpadding="0">
                <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
<!-- Prod Tools Nav -->
                <tr valign="top">
                    <td class="leftNav">
                        <table border="0" cellspacing="0" cellpadding="0">
                            <tr valign="middle">
                                <td width="10" class="leftHead"><img src="/images/clear.gif" alt="" width="10" height="22" border="0" /></td>
                                <td width="145" class="leftHead">Productivity Tools</td>
                                <td width="10" class="leftHead" valign="top"><img src="/images/leftNavHead.gif" alt="" width="10" height="10" border="0" /></td>
                            </tr>

                            <tr valign="top">
                                <td class="leftNavHilite" width="10"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
                                <td class="leftNavHilite">Rules Engine</td>
                                <td class="leftNavHilite" width="10"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
                            </tr>

                            <tr valign="top">
                                <td width="10"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
                                <td class="leftNavText"><a href="s_about_tools.jsp">About Tools</a></td>
                                <td width="10"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
                            </tr>

                            <tr>
                                <td width="10"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>
                                <td width="145"><img src="/images/clear.gif" alt="" width="145" height="1" border="0" /></td>
                                <td width="10"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>
                            </tr>

                            <tr><td class="leftFoot" colspan="3"><img src="/images/leftNavFooter.gif" alt="" width="165" height="11" border="0" /></td></tr>
                        </table>
                    </td>
                </tr>
                <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
<!-- Subscription promo -->
                <tr valign="top"><td><a href="s_subscriptions.jsp"><img src="/images/promoSubscription.gif" alt="Buy a Subscription" width="165" height="90" border="0" /></a></td></tr>
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
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal"><img src="/images/headProductivityTools.gif" alt="Productivity Tools" width="545" height="32" border="0" /></td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td width="100%" class="subhead"><%= componentInfo.getName() %>&nbsp;&nbsp;<span class="version">version <%= versionInfo.getVersionLabel() %></span></td></tr>

                <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center">
                <tr valign="top">
                    <td width="49%" class="display">
                        <p><strong>Overview</strong><br />
                        <%  BufferedReader reader = new BufferedReader(new StringReader(componentInfo.getDescription()));
                            String line = reader.readLine();
                            while (line != null) {
                        %>

                        <%= line.trim() %><br />

                        <%  line = reader.readLine();
                            }
                        %>
                        </p>

                        <p><strong>Functionality</strong></p>
                            <ul>
                        <%  reader = new BufferedReader(new StringReader(componentInfo.getFunctionalDescription()));
                            line = reader.readLine();
                            while (line != null) {
                            //Lines with a '-' as the first character should be treated as a bullet point
                            if (line.charAt(0) == '-') {
                        %>

                                <li><%= line.substring(1,line.length()).trim() %></li>
                            <%      } else { %>

                            <%= line.trim() %>

                            <%      }
                                line = reader.readLine();
                                }
                            %>
                        </ul>

                        <p><img src="/images/clear.gif" alt="" width="265" height="5" border="0" /></p></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="49%" class="display">

<!-- Screen Shot -->
<%  if (thumbnailId > 0 && screenshotId > 0) { %>
                        <p><span class="small"><a href="/catalog/document?id=<%=screenshotId%>"><img src="/catalog/document?id=<%=thumbnailId%>" alt="Screen Shot" border="0" /><br />
                        :: Click to download full-size screen shot</a></span></p>
<%  } %>

<!-- Technologies -->
                        <%  if (technologies.length > 0) {
                        %>
                        <p><strong>Technologies</strong><br />
                        <%      for (int i=0; i < technologies.length - 1; i++) { %>
                            <%= technologies[i].getName() %>,&nbsp;
                        <%      } %>
                            <%= technologies[technologies.length-1].getName() %>
                        </p>
                        <%  } %>

<!-- Authors -->
                        <%
                            if (teamMemberRoles.length > 0) { %>
                        <p><strong>Authors</strong><br />
                        <%      for (int i=0; i < teamMemberRoles.length - 1; i++) { %>
                        <%= teamMemberRoles[i].getUsername() %>,&nbsp;
                        <%      } %>
                            <%= teamMemberRoles[teamMemberRoles.length-1].getUsername() %>
                        </p>
                        <%  } %>

<!-- Price
                        <p><strong>Price</strong><br />
                        $35,000*</p>  -->


<!-- Availability -->
                        <p><strong>Availability</strong><br />

                        <%  String strAvailability;
                            switch( (int)versionInfo.getPhase() ) {
                        case (int)ComponentVersionInfo.COLLABORATION:
                            strAvailability = "Collaboration*";
                            break;
                        case (int)ComponentVersionInfo.SPECIFICATION:
                            strAvailability = "Specification*";
                            break;
                        case (int)ComponentVersionInfo.DEVELOPMENT:
                            strAvailability = "Development*";
                            break;
                        default:
                        case (int)ComponentVersionInfo.COMPLETED:
                            strAvailability = "<strong><a href=\"/tcs?module=ViewComponentTerms&comp=" + componentInfo.getId() + "&ver=" + versionInfo.getVersion() + "\">Download Now*</a></strong>";
                            break;
                        }
                        %>
                        <%= strAvailability %></p>

                        <p>*TopCoder Software Productivity Tools are NOT part of the <a href="c_showroom.jsp">Component Catalog,</a> although
                        they utilize components as part of their design. Discounts apply for TopCoder Software Subscription customers.
                        See <a href="s_about_tools.jsp">About Tools</a> for more information, or call us at 1-866-TOP-CODE.</p>

                        <p><img src="/images/clear.gif" alt="" width="265" height="5" border="0" /></p>
                        </td>
                </tr>
            </table>
        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="245">

<!-- Base Components Included -->
            <table border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td><img src="/images/headBaseCompIncl.gif" alt="Base Components Included" width="245" height="18" border="0" /></td></tr>
                <tr><td><hr width="245" size="1" noshade="noshade" /></td></tr>
                <tr>
                    <td>
                        <table border="0" cellpadding="0" cellspacing="0">
<%  if (summaries.length == 0) {
%>
                            <tr valign="top">
                                <td class="rightColOff">This component is a base component</td>
                            </tr>
<%  } else {
        for (int i=0; i < summaries.length; i++) {
%>
                            <tr valign="top">
                                <td class="rightColDisplay"><a href="c_component.jsp?comp=<%= summaries[i].getComponentId() %>"><%= summaries[i].getName() %></a></td>
                            </tr>
<%      }
    }
%>
                        </table>
                    </td>
                </tr>
                <tr><td><hr width="245" size="1" noshade="noshade" /></td></tr>
                <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
            </table>

<!-- Documentation-->
            <table border="0" cellpadding="0" cellspacing="0">
                <tr><td><img src="/images/headDocumentation.gif" alt="Documentation" width="245" height="18" border="0" /></td></tr>
                <tr><td><hr width="245" size="1" noshade="noshade" /></td></tr>
                <tr>
                    <td>
                        <table border="0" cellpadding="0" cellspacing="0">
<%  if (documents.length == 0) {
%>
                            <tr valign="top">
                                <td class="rightColOff">None available at this time</td>
                            </tr>
<%  } else {
        for (int i=0; i < documents.length; i++) {
%>
                            <tr valign="top">
                                <td class="rightColDisplay"><a href="/catalog/document?id=<%=documents[i].getId()%>" target="_blank" ><%= documents[i].getName() %></a></td>
                            </tr>
<%      }
    }
%>
                        </table>
                    </td>
                </tr>
                <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
                <tr><td class="small"><a href="http://www.adobe.com/products/acrobat/readstep.html" target="_blank">Adobe Acrobat 5</a> is required to view some TopCoder Software documentation.</td></tr>
                <tr><td><hr width="245" size="1" noshade="noshade" /></td></tr>
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
            </table>
        </td>
<!--Right Column ends -->

<!-- Gutter 3 begins -->
        <td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->

    </tr>
</table>

<jsp:include page="/includes/foot.jsp" flush="true" />

</body>
</html>
