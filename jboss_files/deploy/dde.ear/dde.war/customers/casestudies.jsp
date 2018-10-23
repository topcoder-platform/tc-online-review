<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%--<%@ page import="javax.naming.*,
           com.topcoder.dde.util.ApplicationServer" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>--%>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>
<%@ taglib uri="/WEB-INF/tc-webtags.tld" prefix="tc-webtag" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "s_definition.jsp";
    String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Software Components form the basis of the applications built by TopCoder</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<jsp:include page="/includes/header-files.jsp" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
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
            <jsp:param name="level1" value="customers"/>
            <jsp:param name="level2" value="cases" />
        </jsp:include></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="99%" align="left">
            <img src="/images/clear.gif" width="1" height="15" alt="" border="0"><br>
            <img src="/images/hd_cust_case.png" alt="Customer  Case Studies" border="0" /><br><br>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
            </table>

<!-- retail client begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/siteMgmtCon_retailClient.gif" alt="Retail Client" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">The client is a leader in the design, marketing and distribution of premium lifestyle products. TopCoder created the Site Management Console (SMC), a client-server application that allows users to create “Launch Plans” for online stores. The application allows users to create over 35 different types of events and manage them in a calendar similar to Microsoft Outlook&reg;.<br><br>
                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=/pdfs/tcs/casestudies/siteMgmtCon_RetailClient.pdf&refer=customer_index" target="_blank">case study</a>                     </td>
                </tr>
            </table>
<!-- retail client ends -->

             <hr width="640" size="1px" noshade="noshade" />
            
<%--
 sports entertainment network begins 
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/scalable_sportsEntCo.gif" alt="Sports Entertainment Network" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">TopCoder successfully completed a research application for one of the world’s leading multimedia sports entertainment companies. In this particular project, TopCoder designed and built an application that embraces the client’s strategic vision of retiring the current suite of research applications and creating a unified platform upon which all current and future application needs can be successfully deployed.<br><br>
                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=/pdfs/tcs/casestudies/scalable_sportsEntCo.pdf&refer=customer_index" target="_blank">case study</A>
                     </td>
                </tr>
            </table>
 sports entertainment network ends 

             <hr width="640" size="1px" noshade="noshade" />
             --%>
            
<!-- Direct Energy begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/directenergy_logo.gif" alt="Direct Energy" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Direct Energy, one of the largest energy and related services providers in North America, required a mechanism for their credit risk group to assist in managing the credit request process. TopCoder’s solution was a web-based Credit Analysis Tool that allows access by all the sales coordinators and credit departments, regardless of location, and at the same time allows the credit risk department to evaluate input preliminary contract information.<br><br>
                    <a href="<tc-webtag:linkTracking link="http://www.directenergy.com/" refer="customer_index"/>" target="_blank">homepage</a> | 
                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=/pdfs/tcs/casestudies/direct_energy_casestudy.pdf&refer=customer_index" target="_blank">case study</a>                     </td>
                </tr>
            </table>
<!-- Direct Energy ends -->

             <hr width="640" size="1px" noshade="noshade" />
            
<!-- ABB begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/abb_logo.gif" alt="ABB" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">ABB, a leading power and automation technology enabler, needed a modern, user-friendly HTML interface that would allow users in its engineering and systems business units to access data on its mainframe legacy system. The Project Management Dashboard application delivered by TopCoder limited the amount of custom code built to 42 percent that of more traditional development models.<br><br>
                    <a href="<tc-webtag:linkTracking link="http://www.abb.com/" refer="customer_index"/>" target="_blank">homepage</a> | 
                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=/pdfs/tcs/casestudies/abb_casestudy.pdf&refer=customer_index" target="_blank">case study</a>                     </td>
                </tr>
            </table>
<!-- ABB ends -->

             <hr width="640" size="1px" noshade="noshade" />
            
<!-- financial institution begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/fin_services_logo.gif" alt="Financial Services" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">A national financial institution needed database management functionality delivered across a variety of platforms as part of an Information Quality Management initiative. TopCoder devised an IQM solution that allows users to intuitively navigate through data.<br><br>
                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=/pdfs/tcs/casestudies/fin_services_casestudy.pdf&refer=customer_index" target="_blank">case study</a>                     </td>
                </tr>
            </table>

<!-- financial institution ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!-- IDENTITY MANAGEMENT begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/id_manage_logo.gif" alt="Identity Management" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">With a proven track record of success within Fortune 100 organizations, TopCoder's identity and access management solutions centralize users and passwords, allowing organizations to reduce maintenance cost, improve accessibility and enforce consistent security policies.<br><br>
                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=http://software.topcoder.com/pdfs/casestudies/identity_management.pdf&refer=customer_index" target="_blank">case study</a>                     </td>
                </tr>
            </table>

<!-- IDENTITY MANAGEMENT ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!-- Salk begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/salk_logo.gif" alt="Salk Institute" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">The world-renowned Salk Institute, a private, non-profit, research organization dedicated to fundamental research in biology and its relation to health, wanted to make an extremely large amount of gene data resulting from years of research on the topic of spinal cord injury available to the scientific community. TopCoder devised a solution that enables users to access project data through an HTML-based interface.<br><br>
                    <a href="<tc-webtag:linkTracking link="http://www.salk.edu/" refer="customer_index"/>" target="_blank">homepage</a> | 
                     <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=/pdfs/tcs/casestudies/salk_institute_casestudy.pdf&refer=customer_index" target="_blank">case study</a>                    </td>
                </tr>
            </table>
<!-- Salk ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!-- StageStores begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/stagestores_logo.gif" alt="Stagestores" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">StageStores, Inc. brings nationally recognized brand name apparel, accessories, cosmetics and footwear for the entire family to small and mid-size towns and communities with over 520 stores in 27 states.<br><br>
                    <a href="<tc-webtag:linkTracking link="http://www.stagestores.com/" refer="customer_index"/>" target="_blank">homepage</a> | 
                     <a href="http://<%=ApplicationServer.SERVER_NAME%>/?t=sponsor&c=link&link=/i/downloads/Gift_Card_Case_Study.pdf&refer=customer_index" target="_blank">case study</a>                     </td>
                </tr>
            </table>
<!-- StageStores ends --><!-- Verisign ends -->

      	<table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td colspan="2" class="bodyText" align="center">
                        <p class="learn_more"><a href="http://www.topcoder.com/tc?module=Static&d1=about&d2=contactus">Contact us</a> today so we can get started developing your next application.</p>
                    </td>
                </tr>
            </table>
          <p><br></p>        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
        <jsp:include page="/includes/right.jsp" >
            <jsp:param name="level1" value="customers"/>
        </jsp:include>        </td>
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
