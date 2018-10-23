<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="com.topcoder.web.common.WebConstants" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "s_products.jsp";
    String action = request.getParameter("a");
    
    long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID;
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
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
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

<!-- About Us Catagories -->
<%@ include file="/includes/leftNavLearn.jsp" %>

            </table>
        </td>
        <td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0" /></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="100%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal"><img src="/images/hd_products.png" alt="Products" border="0" /></td></tr>
                <tr><td><h3>Driving down the cost of high quality software applications</h3></td></tr>
                <tr><td class="normal">
                        <p><strong>Component Subscriptions</strong><br />
                        <img src="../images/button_select.gif" alt="Select" width="78" height="29" hspace="3" vspace="3" border="0" align="right" />
                        Now available at three levels to meet any budget. <a href="s_subscriptions.jsp" class="bodyText">Component subscriptions</a> give you
                        unlimited access to the basic building blocks you need to build many complex systems. Your internal software development resources, or external
                        integration partners, can be more productive by only concentrating on customizing components and integrating them into your other systems. In
                        addition, the leverage that TopCoder Software gets from marketing components to multiple customers allows us to sell our components for less.</p>

                        <p><strong>Component Management Services</strong><br />
                        <img src="../images/icon_management.gif" alt="" width="126" height="71" hspace="3" vspace="3" border="0" align="right" />
                        TopCoder employs component development experts as Product Managers. Product Managers are responsible for managing the components developed
                        by the TopCoder Member Base using our Development Methodology.  Product Managers maximize the value of your Component Catalog by identifying
                        component opportunities and specifying requirements.  Their extensive knowledge of the Component Catalog allows them to identify the components
                        that will be of the highest value to you. <a href="s_subscriptions.jsp" class="bodyText">Enterprise Subscriptions</a> offer five hours of Component Management Services FREE.
                        Additional time can be purchased separately.</p>

                        <!-- <p><strong>Component Customization (comment this out)</strong><br />
                        TopCoder Software Components are built with re-usability as the highest priority.  They have been painstakingly developed to
                        encourage extension and customization.  Generic TopCoder components will often be extended and customized to meet a specific
                        customer�s needs.  TopCoder is available to extend and customize catalog components for individual customers. -->

                        <p><strong>Component Integration</strong><br />
                        <img src="../images/icon_complex_component.gif" alt="" width="109" height="61" hspace="3" vspace="3" border="0" align="right" />
                        TopCoder is constantly integrating components into both internal applications and more complex components for the
                        <a href="c_showroom.jsp" class="bodyText">Component Catalog.</a>  In cases where you need help integrating TopCoder Components into your
                        organization, TopCoder Software Integration Teams are available to help.</p>

                        <p><strong>Productivity Tools</strong><br />
                        TopCoder Software is developing productivity tools to help improve the development process. The first tool is the
                        <a href="c_prodTools.jsp?comp=600191" class="bodyText">TopCoder Rules Engine.</a> The Rules Engine centralizes and manages business logic for use in
                        multiple applications. The Rules Engine is available for purchase separately from the component catalog. To inquire about the
                        TopCoder Rules Engine send us an <a href="mailto:sales@topcoder.com?subject=I'm interested in the TopCoder Rules Engine" class="bodyText">email</a> or
                        call us toll free at <strong>866.867.2633.</strong></p>

                        <p>&#160;</p>

                        <p>See what is available today. Visit our <a href="c_showroom.jsp" class="bodyText">Component Sales Catalog</a></p>

                        <p>Speak your mind in our <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>" class="bodyText">Software Forums</a>.</p></td>
                </tr>
                <tr><td><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
            </table>
        </td>

<!-- Middle Column ends -->

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
