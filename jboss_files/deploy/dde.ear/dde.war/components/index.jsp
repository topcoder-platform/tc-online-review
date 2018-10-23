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
    String page_name = "s_definition.jsp";
    String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Software Components form the basis of the applications built by TopCoder</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true" />
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
<tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="components" />
            <jsp:param name="level2" value="comp-overview" />
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td align="left">
            <img src="/images/clear.gif" width="1" height="15" alt="" border="0" /><br />
            <img src="/images/hd_comp_what.png" alt="Components" border="0" /><br />
            <div align="left">
            <table cellspacing="0" cellpadding="0" border="0" width="100%">
                <tr><%--
                    <td width="510" align="left"><br /><br />
                        <img src="/images/comps_home_promoA.gif" alt="Component-Based Development" width="510" height="65" border="0" /><br />
                        <img src="/images/comps_home_promoB.gif" alt="gives a company the best of both worlds" width="510" height="97" border="0" /><br />
                        <img src="/images/comps_home_promoC.gif" alt="" width="510" height="83" border="0" />
                    </td>
                    --%>
                    <td width="651px" align="left"><br /><br />
                        <img src="/i/componentdev_image5.jpg" style="width:615px; height:261px;" alt="Component Development" />
                    </td>
                </tr>
            </table>
            </div>

            <hr width="95%" size="1px" noshade="noshade" />

            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="100%" class="bodyText">
                        <p><strong>Making the Most of the Code</strong><br />
                        Neither end of the software development spectrum allows a company to get what they need at the price they desire. Packaged software
                        is monolithic, difficult to integrate, and expensive to customize. Custom software development is costly and time consuming.
                        Progressive companies have begun to take a new approach: Component Based Development (CBD). CBD gives a company the best of
                        both worlds. Companies can use what are essentially pieces of applications that can be customized and tied together to form a unique solution
                        that suits their needs. Cost savings are achieved through a market approach: Because each component is sold to multiple companies, the
                        development cost for each component is shared among all the companies.</p>
                    </td>
                </tr>
            </table>

             <hr width="95%" size="1px" noshade="noshade" />

            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="100%" class="bodyText">
                        <p><strong>Standard Frameworks</strong><br />
                        At TopCoder, we develop all of our components using industry frameworks such as J2EE and .NET. This makes every one of our components
                        more flexible and easily customizable. The use of frameworks-based software components also helps speed up the development cycle. These
                        components can be extended and customized for additional functionality. They can also be bundled together to form more complex components
                        and full-blown applications.</p>
                    </td>
                </tr>
            </table>

             <hr width="95%" size="1px" noshade="noshade" />

            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="100%" class="bodyText">
                        <p><strong>Increased Productivity</strong><br />
                       Use of TopCoder components drives down the cost of software development in two ways. The first is by introducing re-usable components to the 
                       development life cycle, which reduces the amount of software that must be built. The development team can focus on customizing and integrating 
                       these components into a business solution. The second way TopCoder drives down the cost of development is by lowering the cost of the components 
                       themselves. We utilize our member base of over 100,000 talented individuals as a distributed development resource. This way, the components 
                       themselves are developed at a lower cost, and that cost can be passed along to our customers. The end result is lower cost, higher quality software 
                       components, which leads to lower cost, higher quality <a href="/applications/index.jsp">software applications.</a></p>
                    </td>
                </tr>
            </table>

             <hr width="95%" size="1px" noshade="noshade" />

            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="100%" class="bodyText">
                        <p><strong>Unique Methodology</strong><br />
                        <a href="/components/methodology.jsp">Learn more</a> about TopCoder's Competition-Based Application Methodology.</p>
                    </td>
                </tr>
            </table>

             <hr width="95%" size="1px" noshade="noshade" />

           <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="100%" class="bodyText">
                        <p><strong>Catalog Subscriptions</strong><br />
                        <a href="/components/subscriptions.jsp">Subscribe</a> to the Topcoder Software Component Catalog today.  <strong>FREE</strong> developer subscriptions available.</p>
                    </td>
                </tr>
            </table>

             <hr width="95%" size="1px" noshade="noshade" />

           <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td colspan="2" class="bodyText" align="center">
                        <p><strong>Have a great idea for a component? Use our <a href="/components/request.jsp">Suggestion Form</a> to let us know.</strong></p>

                        <p class="learn_more"><a href="http://www.topcoder.com/tc?module=Static&d1=about&d2=contactus">Contact us</a> today so we can get started developing your next application.</p>
                    </td>
                </tr>
            </table>
            <p><br /></p>
        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
        <jsp:include page="/includes/right.jsp" >
            <jsp:param name="level1" value="components" />
        </jsp:include>
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
