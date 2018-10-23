<%@ page import="javax.naming.*,
                 com.topcoder.shared.util.ApplicationServer,
                 com.topcoder.dde.catalog.Document" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ taglib uri="/WEB-INF/tc-webtags.tld" prefix="tc-webtag" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "c_component.jsp";
    String action = request.getParameter("a");
%>

<%@ include file="/includes/componentCategories.jsp" %>
<% // PAGE SPECIFIC DECLARATIONS %>
<%@ page import="com.topcoder.dde.catalog.*" %>
<%@ page import="com.topcoder.shared.dataAccess.Request"%>
<%@ page import="com.topcoder.shared.dataAccess.DataAccessInt"%>
<%@ page import="com.topcoder.web.common.CachedDataAccess"%>
<%@ page import="com.topcoder.shared.util.DBMS"%>
<%@ page import="com.topcoder.shared.dataAccess.resultSet.ResultSetContainer"%>
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
    long lngVersion = -1;
    try {
        lngComponent = Long.parseLong(request.getParameter("comp"));
        lngVersion = Long.parseLong(request.getParameter("ver"));
    } catch (NumberFormatException nfe) {
        // invalid parameter, redirect to main page
        if (lngComponent == 0) response.sendRedirect("c_showroom.jsp");
        if (lngComponent == 600191) response.sendRedirect("c_prodTools.jsp?comp=600191");
    }

    ComponentDetail details = null;

    try {
        details = catalog.getComponentDetail(lngComponent, lngVersion);
    } catch (Exception e) {
        // redirect to an error page
        response.sendRedirect("/attention/error_componentnotfound.jsp");
        return;
    }

    // Get only visible categories.
    long rootCategory = details.getSummary().getRootCategory();
    Category[] baseCategories = catalog.getBaseCategories(true);
    boolean visibleCategory = false;
    for (int i = 0; i < baseCategories.length && !visibleCategory; i++) {
        if (rootCategory == baseCategories[i].getId()) {
            visibleCategory = true;
        }
    }

    if (!visibleCategory) {
        // redirect to an error page
        response.sendRedirect("/attention/error_componentnotfound.jsp");
        return;
    }

    lngVersion = Math.max(lngVersion, 0);

    Technology technologies[] = details.getTechs();

    TeamMemberRole teamMemberRoles[] = details.getMembers();

    LicenseLevel levels[] = (LicenseLevel[])catalog.getLicenseLevels().toArray(new LicenseLevel[0]);
    int cost[] = new int[levels.length];
    boolean hasCost = false;
    for (int i=0; i < levels.length; i++) {
        cost[i] = levels[i].calculateUnitCost(details.getVers().getPrice());
        if (cost[i] > 0) {
            hasCost = true;
        }
    }

    ComponentSummary summaries[] = details.getDependencies();

    //Separate screen shots and displayable documents
    Document[] tempDocs = details.getDocs();
    Collection colClassDiagrams = new ArrayList();
    Collection colUseCases = new ArrayList();
    Collection colSequenceDiagrams = new ArrayList();
    Collection colRequirements = new ArrayList();
    Collection colJavaDocs = new ArrayList();
    Collection colScorecards = new ArrayList();
    Collection colOther = new ArrayList();

    long thumbnailId = 0;
    long screenshotId = 0;
    for (int i = 0; i < tempDocs.length; i++) {
        Document doc = tempDocs[i];
        if ((int)doc.getType() != com.topcoder.dde.catalog.Document.SCREEN_SHOT &&
                    (int)doc.getType() != com.topcoder.dde.catalog.Document.SCREEN_SHOT_THUMBNAIL) {
            if((int)doc.getType() == Document.CLASS_DIAGRAM) {
                colClassDiagrams.add(doc);
            } else if((int)doc.getType() == Document.USE_CASE_DIAGRAM) {
                colUseCases.add(doc);
            } else if((int)doc.getType() == Document.SEQUENCE_DIAGRAM) {
                colSequenceDiagrams.add(doc);
            } else if((int)doc.getType() == Document.COMPONENT_SPECIFICATION || doc.getType() == Document.REQUIREMENT_SPECIFICATION) {
                colRequirements.add(doc);
            } else if((int)doc.getType() == Document.JAVADOCS) {
                colJavaDocs.add(doc);
            } else if((int)doc.getType() == Document.SCORECARD) {
                colScorecards.add(doc);
            } else {
                colOther.add(doc);
            }
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

    Document docClassDiagrams[] = (Document[])colClassDiagrams.toArray(new Document[0]);
    Document docUseCases[] = (Document[])colUseCases.toArray(new Document[0]);
    Document docSequenceDiagrams[] = (Document[])colSequenceDiagrams.toArray(new Document[0]);
    Document docRequirements[] = (Document[])colRequirements.toArray(new Document[0]);
    Document docJavaDocs[] = (Document[])colJavaDocs.toArray(new Document[0]);
    Document docScorecards[] = (Document[])colScorecards.toArray(new Document[0]);
    Document docOther[] = (Document[])colOther.toArray(new Document[0]);
    //Document screenshots[] = (Document[])colScreenShots.toArray(new Document[0]);

    boolean hasPreviousForums = false;

    com.topcoder.dde.catalog.ForumCategory activeSpec = null;
    ComponentVersionInfo versions[] = null;
    com.topcoder.dde.catalog.ForumCategory specifications[] = null;

    ComponentVersionInfo versionInfo = details.getVers();
    ComponentInfo componentInfo = details.getInfo();

    try {
        ComponentManagerHome component_manager_home = (ComponentManagerHome) PortableRemoteObject.narrow(CONTEXT.lookup(ComponentManagerHome.EJB_REF_NAME), ComponentManagerHome.class);
        ComponentManager componentManager;

        if (lngVersion == 0) {
            componentManager = component_manager_home.create(lngComponent);
        } else {
            componentManager = component_manager_home.create(lngComponent, lngVersion);
        }

        Collection colVersions = componentManager.getAllVersionInfo();
        versions = (ComponentVersionInfo[])colVersions.toArray(new ComponentVersionInfo[0]);
        specifications = new com.topcoder.dde.catalog.ForumCategory[versions.length];
        for (int i=0; i < versions.length; i++) {
            if (versions[i].getPhase() != ComponentVersionInfo.COMPLETED) {
                specifications[i] = null;
                continue;
            } else {
                componentManager.setVersion(versions[i].getVersion());
                try {
                    specifications[i] = componentManager.getForumCategory();
                } catch (CatalogException ce) {
                    // getForum returns multiple forums of a type which is not supposed to happen
                    // what to do?
                }
                if (specifications[i] != null) {
                    hasPreviousForums = true;
                }
            }
        }
        componentManager.setVersion(versionInfo.getVersion());
        try {
            activeSpec = componentManager.getActiveForumCategory();
        } catch (CatalogException ce) {
        }
    } finally {}
%>

<%!
    boolean exists(long componentId, long versionId, long phaseId) throws Exception {
        Request r = new Request();
        r.setContentHandle("find_projects");

        // Find all the projects that match with the component id, version and phase
        r.setProperty("compid", String.valueOf(componentId));
        r.setProperty("vr", String.valueOf(versionId));
        r.setProperty("ph", String.valueOf(phaseId));

        DataAccessInt dai = new CachedDataAccess(DBMS.TCS_DW_DATASOURCE_NAME);
        Map result = dai.getData(r);
        return !((ResultSetContainer) result.get("find_projects")).isEmpty();
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Catalogs of Java and .NET Components at TopCoder Software</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <link rel="stylesheet" type="text/css" href="http://<%=ApplicationServer.SERVER_NAME%>/css/coders.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />

<STYLE TYPE="text/css">
.statusIconOff, .statusIconOn{ width: 25%; text-align: center; }
.statusIconOff{ border-bottom:  1px solid #000000; }
.statusIconOn{ border:  1px solid #000000; }
.authorsHeader{ border-bottom:  1px solid #000000; margin-bottom: 3px; padding-bottom: 3px; float: left; width: 100% }
</STYLE>

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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="catalog" />
            <jsp:param name="level2" value="Java" />
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
<!-- Gutter 1 ends -->

<%
    int refCatalog = -1;
    String catalogName = "No";
    if (details.getSummary().getAolComponent()) {
        refCatalog = 3;
    } else {
        if (javaCategory.getId() == details.getSummary().getRootCategory()) {
            refCatalog = 0;
            catalogName = "Java";
        } else if (netCategory.getId() == details.getSummary().getRootCategory()) {
            refCatalog = 1;
            catalogName = ".NET";
        } else if (flashCategory.getId() == details.getSummary().getRootCategory()) {
            refCatalog = 2;
            catalogName = "Flash";
        }
    }
%>

<!-- Middle Column begins -->
        <td width="99%" style="padding: 15px">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="padding-bottom: 15px">
                <tr><td class="normal" colspan="2" style="padding-bottom: 15px"><img src="/images/hd_comp_catalog.png" alt="Component Catalogs" border="0" /></td></tr>
                <tr>
                    <td colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0" style="padding-bottom: 15px">
                            <tr valign="middle">
                                <%
                                    switch (refCatalog) {
                                        case 0:
                                            %><td><img src="/images/catalog/catpg_java_logo.gif" alt="" border="0" /></td><%
                                            break;
                                        case 1:
                                            %><td><img src="/images/catalog/catpg_dotnet_logo.gif" alt="" border="0" /></td><%
                                            break;
                                        case 2:
                                            %><td><img src="/images/catalog/catpg_flash_logo.gif" alt="" border="0" /></td><%
                                            break;
                                        case 3:
                                            %><td style="padding-right:20px;"><img src="/images/catalog/catpg_AOL_logo.gif" alt="" border="0" /></td><%
                                            break;
                                        default:
                                            %><td></td><%
                                            break;

                                    }
                                %>
                                <td><img src="/images/spacer.gif" alt="" width="5" height="17" border="0" /></td>
                                <td class="testHead2"><strong><%= componentInfo.getName() %></strong> Version <%= versionInfo.getVersionLabel() %></td>
                            </tr>
                        </table>
                    </td>
                </tr>
<!-- Status Bar -->
                <tr valign="top">
               <td class="display" colspan="2"><strong>Status: </strong><br /><br />
               <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
               <td class="<%=(versionInfo.getPhase() == ComponentVersionInfo.COLLABORATION ? "statusIconOn" : "statusIconOff" )%>"><img src="/images/catalog/catpg_status_spec.gif" alt="Specification" border="0"/></td>
               <td class="<%=(versionInfo.getPhase() == ComponentVersionInfo.SPECIFICATION ? "statusIconOn" : "statusIconOff" )%>"><img src="/images/catalog/catpg_status_desarch.gif" alt="Design & Architecture" />
               <td class="<%=(versionInfo.getPhase() == ComponentVersionInfo.DEVELOPMENT ? "statusIconOn" : "statusIconOff" )%>"><img src="/images/catalog/catpg_status_devtest.gif" alt="Development & Testing" />
               <td class="<%=(versionInfo.getPhase() == ComponentVersionInfo.COMPLETED  ? "statusIconOn" : "statusIconOff" )%>"><img src="/images/catalog/catpg_status_complete.gif" alt="Complete" /><br />
               </tr></table>
               </td>
            </tr>
<!-- Overview, Functionality -->
                <tr valign="top">
                    <td width="50%" class="display" style="padding-right: 15px">
                    	<br />
                        <p><strong>Overview</strong><br />

                        <%  BufferedReader reader = new BufferedReader(new StringReader(componentInfo.getDescription()));
                            String line = reader.readLine();
                            while (line != null) {
                        %>

                        <%= line.trim() %><br />

                        <%      line = reader.readLine();
                            }
                        %>

                        </p>
						<br />
                        <p><strong>Functionality</strong><br /></p>
                        <ul>

                        <%  reader = new BufferedReader(new StringReader(componentInfo.getFunctionalDescription()));
                            line = reader.readLine();
                            while (line != null) {
                                //Lines with a '-' as the first character should be treated as a bullet point
                                if (line.charAt(0) == '-') {
                        %>
                            <li><%= line.substring(1,line.length()).trim() %></li>

                        <% } else { %>

                            <%= line.trim() %>
                        <%   }
                                line = reader.readLine();
                            }
                        %>
                        </ul>

                    </td>

                    <td width="50%" class="display">
<!-- Screen Shot -->
                        <%  if (thumbnailId > 0 && screenshotId > 0) { %>

                        <p><span class="small"><a href="/catalog/document?id=<%=screenshotId%>"><img src="/catalog/document?id=<%=thumbnailId%>" alt="Screen Shot" border="0" /><br />
                        Click to download full-size screen shot</a></span></p>

                        <%  }  %>

<!-- Technologies -->
                        <% if (technologies.length > 0) { %>
						<br />
                        <p><strong>Technologies</strong><br />

                        <% for (int i=0; i < technologies.length - 1; i++) { %>
                            <%= technologies[i].getName() %>,&nbsp;
                        <%  }  %>
                            <%= technologies[technologies.length-1].getName() %>
                        </p>

                        <%  }  %>

<!-- Authors -->
                        <% if (teamMemberRoles.length > 0) { %>
						<br />
                        <strong>Authors</strong>
                   <table cellspacing="0" cellpadding="0" border="0" width="100%">
                         <tr valign="top">
                            <td width="50%" style="padding-right: 10px">
                              <div class="authorsHeader">
                                 <div style="float: right;">
                                    <% if (exists(componentInfo.getId(), versionInfo.getVersion(), ComponentVersionInfo.SPECIFICATION)) { %>
                                        <A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=CompContestDetails&amp;compid=<%=componentInfo.getId()%>&amp;vr=<%=versionInfo.getVersion()%>&amp;ph=<%=ComponentVersionInfo.SPECIFICATION%>" class="small">contest details</A>
                                    <% } else { %>
                                        &#160;
                                     <% } %>
                                 </div>
                                 <font class="small">Designer(s):</font>
                              </div>
                                                <%boolean first = true;%>
                                 <%  for (int i=0; i < teamMemberRoles.length; i++) { if( teamMemberRoles[i].getRoleId() == 5) { if(first) { first = false; } else {%><br><%}%><tc-webtag:handle coderId='<%= teamMemberRoles[i].getUserId()%>' context="design"/><%  }  }  %>
                              <br><br>
                            </td>
                            <td width="50%">
                              <div class="authorsHeader">
                                 <font class="small">Design Review Board:</font>
                              </div>

                                <% first = true;%>
                                 <%  for (int i=0; i < teamMemberRoles.length; i++) { if( teamMemberRoles[i].getRoleId() == 6) { if(first) { first = false; } else {%><br><%}%><tc-webtag:handle coderId='<%= teamMemberRoles[i].getUserId()%>' context="design"/><%  }  }  %>
                                </font>
                              <br><br>
                               </td>
                            </tr>
                            <tr valign="top">
                               <td style="padding-right: 10px">
                              <div class="authorsHeader">
                                 <div style="float: right;">
                                    <% if (exists(componentInfo.getId(), versionInfo.getVersion(), ComponentVersionInfo.DEVELOPMENT)) { %>
                                     <A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=CompContestDetails&amp;compid=<%=componentInfo.getId()%>&amp;vr=<%=versionInfo.getVersion()%>&amp;ph=<%=ComponentVersionInfo.DEVELOPMENT%>" class="small">contest details</A>
                                    <% } else { %>
                                     &#160;
                                     <% } %>
                                 </div>
                                 <font class="small">Developer(s):</font>
                              </div>


                                  <% first = true;%>
                                 <%  for (int i=0; i < teamMemberRoles.length; i++) { if( teamMemberRoles[i].getRoleId() == 7) { if(first) { first = false; } else {%><br><%}%><tc-webtag:handle coderId='<%= teamMemberRoles[i].getUserId()%>' context="development"/><%  }  }  %>
                                  </font>
                              <br><br>
                               </td>
                               <td>
                              <div class="authorsHeader">
                                 <font class="small">Development Review Board:</font>
                              </div>
                                  <% first = true;%>
                                 <%  for (int i=0; i < teamMemberRoles.length; i++) { if( teamMemberRoles[i].getRoleId() == 8) { if(first) { first = false; } else {%><br><%}%><tc-webtag:handle coderId='<%= teamMemberRoles[i].getUserId()%>' context="development"/><%  }  }  %>
                                  </font>
                              <br><br>
                               </td>
                            </tr>
                   </table>
                        <%  }  %>
                        <p><strong>Availability</strong><br />
                        Version <%= versionInfo.getVersionLabel() %><br />



<%  String strAvailability;
    switch( (int)versionInfo.getPhase() ) {
    case (int)ComponentVersionInfo.COLLABORATION:
        strAvailability = "Collaboration";
        break;
    case (int)ComponentVersionInfo.SPECIFICATION:
        strAvailability = "Design and Architecture";
        break;
    case (int)ComponentVersionInfo.DEVELOPMENT:
        strAvailability = "Development and Testing";
        break;
    default:
    case (int)ComponentVersionInfo.COMPLETED:
        strAvailability = "<strong><a href=\"/tcs?module=ViewComponentTerms&comp=" + componentInfo.getId() + "&ver=" + versionInfo.getVersion() + "\"><img src='/images/catalog/catpg_download.jpg' alt='' border='0'  /></a></strong>";
        break;
    }
%>
                        <%= strAvailability %></p>


                    </td>
                </tr>

               <tr>
<!-- Documentation-->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="padding-bottom: 20px">
                <tr><td align="left"><img src="/images/catalog/catpg_document.gif" alt="Documentation" width="116" height="13" border="0" />
                      <font class="small"><a href="http://www.adobe.com/products/acrobat/readstep.html" target="_blank">Adobe Acrobat</a> is required to view TopCoder Software specification documentation.<br />
                  <hr width="100%" size="1" noshade="noshade" />
                      <table border="0" cellpadding="10" cellspacing="0" width="100%">
                          <tr>
                              <td class="display" valign="top" width="33%">
                                  <strong>Class Diagrams</strong><br/>
                              </td>
                              <td class="display" valign="top" width="33%">
                                  <strong>Use Case Diagrams</strong><br/>
                              </td>
                              <td class="display" valign="top" width="33%">
                                  <strong>Sequence Diagrams</strong><br/>
                              </td>
                          </tr>
                          <tr>
                              <td class="rightColOff" valign="top">
                                  <%  if (docClassDiagrams.length == 0) {%>
                                  None available at this time
                                  <% } else {
                                      String url = lngComponent+"/"+versionInfo.getVersionId() + "/";
                                      for (int i=0; i < docClassDiagrams.length; i++) {%>
                                          <%if (docClassDiagrams[i].getId() < 0) {%>
                                      <a href="<%=docClassDiagrams[i].getURL()%>" target="_blank" ><%=docClassDiagrams[i].getName()%></a><br/>
                                          <% } else {%>
                                      <a href="/catalog/document?id=<%=docClassDiagrams[i].getId()%>" target="_blank" ><%=docClassDiagrams[i].getName()%></a><br/>
                                          <% } %>
                                      <% }
                                     } %>
                              </td>
                              <td class="rightColOff" valign="top">
                                  <%  if (docUseCases.length == 0) {%>
                                  None available at this time
                                  <% } else {
                                      String url = lngComponent+"/"+versionInfo.getVersionId() + "/";
                                      for (int i=0; i < docUseCases.length; i++) {%>
                                          <%if (docUseCases[i].getId() < 0) {%>
                                      <a href="<%=docUseCases[i].getURL()%>" target="_blank" ><%=docUseCases[i].getName()%></a><br/>
                                          <% } else {%>
                                      <a href="/catalog/document?id=<%=docUseCases[i].getId()%>" target="_blank" ><%=docUseCases[i].getName()%></a><br/>
                                          <% } %>
                                      <% }
                                     } %>
                              </td>
                              <td class="rightColOff" valign="top">
                                  <%  if (docSequenceDiagrams.length == 0) {%>
                                  None available at this time
                                  <% } else {
                                      String url = lngComponent+"/"+versionInfo.getVersionId() + "/";
                                      for (int i=0; i < docSequenceDiagrams.length; i++) {%>
                                          <%if (docSequenceDiagrams[i].getId() < 0) {%>
                                      <a href="<%=docSequenceDiagrams[i].getURL()%>" target="_blank" ><%=docSequenceDiagrams[i].getName()%></a><br/>
                                          <% } else {%>
                                      <a href="/catalog/document?id=<%=docSequenceDiagrams[i].getId()%>" target="_blank" ><%=docSequenceDiagrams[i].getName()%></a><br/>
                                          <% } %>
                                      <% }
                                     } %>
                              </td>
                          </tr>
                          <tr>
                              <td class="display" valign="top">
                                  <strong>Requirements</strong><br/>
                              </td>
                              <td class="display" valign="top">
                                  <strong>Developer Documentation</strong><br/>
                              </td>
                              <td class="display" valign="top">
                                  <strong>Scorecards</strong><br/>
                              </td>
                          </tr>
                          <tr>
                              <td class="rightColOff" valign="top">
                                  <%  if (docRequirements.length == 0) {%>
                                  None available at this time
                                  <% } else {
                                      String url = lngComponent+"/"+versionInfo.getVersionId() + "/";
                                      for (int i=0; i < docRequirements.length; i++) {%>
                                          <%if (docRequirements[i].getId() < 0) {%>
                                      <a href="<%=docRequirements[i].getURL()%>" target="_blank" ><%=docRequirements[i].getName()%></a><br/>
                                          <% } else {%>
                                      <a href="/catalog/document?id=<%=docRequirements[i].getId()%>" target="_blank" ><%=docRequirements[i].getName()%></a><br/>
                                          <% } %>
                                      <% }
                                     } %>
                              </td>
                              <td class="rightColOff" valign="top">
                                  <%  if (docJavaDocs.length == 0) {%>
                                  None available at this time
                                  <% } else {
                                      String url = lngComponent+"/"+versionInfo.getVersionId() + "/";
                                      for (int i=0; i < docJavaDocs.length; i++) {%>
                                      <%if (docJavaDocs[i].getType() == Document.JAVADOCS) {%>
                                      <a href="<%= "/catalog/javadoc/"+url+"index.html" %>" target="_blank"><%=docJavaDocs[i].getName()%></a><br/>
                                       <%} else if (docJavaDocs[i].getId() < 0) {%>
                                      <a href="<%=docJavaDocs[i].getURL()%>" target="_blank" ><%=docJavaDocs[i].getName()%></a><br/>
                                          <% } else {%>
                                      <a href="/catalog/document?id=<%=docJavaDocs[i].getId()%>" target="_blank" ><%=docJavaDocs[i].getName()%></a><br/>
                                          <% } %>
                                      <% }
                                     } %>
                              </td>
                              <td class="rightColOff" valign="top">
                                  <%  if (docScorecards.length == 0) {%>
                                  None available at this time
                                  <% } else {
                                      String url = lngComponent+"/"+versionInfo.getVersionId() + "/";
                                      for (int i=0; i < docScorecards.length; i++) {%>
                                          <%if (docScorecards[i].getId() < 0) {%>
                                      <a href="<%=docScorecards[i].getURL()%>" target="_blank" ><%=docScorecards[i].getName()%></a><br/>
                                          <% } else {%>
                                      <a href="/catalog/document?id=<%=docScorecards[i].getId()%>" target="_blank" ><%=docScorecards[i].getName()%></a><br/>
                                          <% } %>
                                      <% }
                                     } %>
                              </td>
                          </tr>
                          <tr>
                              <td class="display" valign="top" colspan="3">
                                  <strong>Other</strong><br/>
                              </td>
                          </tr>
                          <tr>
                              <td class="rightColOff" valign="top" colspan="3">
                                  <%  if (docOther.length == 0) {%>
                                  None available at this time
                                  <% } else {
                                      String url = lngComponent+"/"+versionInfo.getVersionId() + "/";
                                      for (int i=0; i < docOther.length; i++) {%>
                                          <%if (docOther[i].getId() < 0) {%>
                                      <a href="<%=docOther[i].getURL()%>" target="_blank" ><%=docOther[i].getName()%></a><br/>
                                          <% } else {%>
                                      <a href="/catalog/document?id=<%=docOther[i].getId()%>" target="_blank" ><%=docOther[i].getName()%></a><br/>
                                          <% } %>
                                      <% }%>

                                     <%} %>
                              </td>
                          </tr>
                      </table>
                    </td>
                </tr>
            </table>

<!-- Component Dependencies -->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="padding-bottom: 20px">
                <tr><td align="left"><img src="/images/catalog/catpg_compdep.gif" alt="Component Hierarchy" width="187" height="17" border="0" /><br />
                      <hr width="100%" size="1" noshade="noshade" />
                        <table border="0" cellpadding="0" cellspacing="0">
<%  if (summaries.length == 0) {
%>
                            <tr valign="top">
                                <td class="rightColOff">This component is a Base Component</td>
                            </tr>
<%  } else {
        %>
<%--                            <tr valign="top">
                                <td class="rightColDisplay">
                                    <embed width="100%" height="200" type="image/svg+xml" src="/catalog/svg?id=<%=versionInfo.getVersionId()%>" >
                                </td>
                            </tr>       --%>
        <%
        for (int i=0; i < summaries.length; i++) {
%>
                            <tr valign="top">
                                <td class="rightColDisplay"><a href="/catalog/c_component.jsp?comp=<%= summaries[i].getComponentId() %>"><%= summaries[i].getName() %></a></td>
                            </tr>
<%      }
    }
%>
                        </table>
                    </td>
                </tr>
            </table>

<!-- Enhancements -->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="padding-bottom: 25px">
                <tr><td align="left"><img src="/images/catalog/catpg_enhance.gif" alt="Enhancements" width="113" height="13" border="0" /><br />
                      <hr width="100%" size="1" noshade="noshade" />
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr valign="top">
                                <td class="rightColDisplay"><a href="/components/request.jsp">Request an enhancement</a>.</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

<!-- Forums for This Component -->
           <table border="0" cellpadding="0" cellspacing="0" width="100%" style="padding-bottom: 15px">
<!-- Current Forums begin -->
                <tr><td align="left"><img src="/images/catalog/catpg_cforums.gif" alt="Current Forums" width="121" height="13" border="0" />
                      <font class="small">Participation in current forums requires user login and may require authorization</font><br />
                  <hr width="100%" size="1" noshade="noshade" />

                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
<%  if (activeSpec != null) { %>
                            <tr><td class="rightColDisplay"><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%= activeSpec.getId() %>">Developer Forum</a></td></tr>
<%  } else { %>
                            <tr><td class="rightColOff">No active developer forum</td></tr>
<%  } %>

                            <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>

                            <tr><td width="170"><img src="/images/clear.gif" alt="" width="170" height="1" border="0" /></td></tr>
                        </table><br />
                    </td>
                </tr>
<!-- Current Forums end -->

<!-- Previous Forums begin -->
<%  if (hasPreviousForums) { %>
                <tr><td align="left"><img src="/images/catalog/catpg_pforums.gif" alt="Previous Forums" width="129" height="13" border="0" />
                	<hr width="100%" size="1" noshade="noshade" />
                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
<%  for (int i=0; i < versions.length; i++) { %>
            <%  if (specifications[i] != null) {  %>
                            <tr><td class="rightColDisplay"><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%= specifications[i].getId() %>">Developer Forum Version <%= "" + versions[i].getVersionLabel() %></a></td></tr>
            <%  }
}  %>
                            <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
                            <tr><td width="170"><img src="/images/clear.gif" alt="" width="170" height="1" border="0" /></td></tr>
                        </table>
                </tr>
<% } %>
<!-- Previous Forums end -->
            </table>

        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
<!-- Gutter removed due to cellspacing in middle column table -->
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr><td height="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td></tr>
                <tr><td>
                <jsp:include page="/includes/topDownloads.jsp" />
                <jsp:include page="/includes/newReleases.jsp" />
              <jsp:include page="/includes/right.jsp" >
                  <jsp:param name="level1" value="index"/>
              </jsp:include>
              </td></tr>
            </table>
        </td>
<!--Right Column ends -->

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
