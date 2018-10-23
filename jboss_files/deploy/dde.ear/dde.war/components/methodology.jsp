<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="com.topcoder.web.common.WebConstants" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "s_methodology.jsp";
    String action = request.getParameter("a");
    
    long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Component Development Methodology at TopCoder</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp">
    <jsp:param name="isSoftwarePage" value="true" />
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
<tr valign="top">

<!-- Left Column begins -->
<td width="165" class="leftColumn">
    <jsp:include page="/includes/left.jsp">
        <jsp:param name="level1" value="components" />
        <jsp:param name="level2" value="comp-methodology" />
    </jsp:include>
</td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
<td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
    <td align="left">
        <table cellspacing="0" cellpadding="0" border="0" width="95%">
            <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
            <tr><td class="normal">
                <img src="/images/hd_comp_meth.png" border="0" alt="Components - Methodology" /></td></tr>
            <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
        </table>

    <div align="left">
        <%--
        <table cellspacing="0" cellpadding="0" border="0" width="95%">
            <tr>
                <td rowspan="3"><img src="/images/clear.gif" width="59" height="204" alt="" /></td>
                <td><img src="/images/meth_complete.gif" width="161" height="67" alt="Complete" border="0" /></td>
                <td><img src="/images/meth_software_02.gif" width="118" height="67" alt="" /></td>
                <td><img src="/images/meth_collaboration.gif" width="177" height="67" alt="Collaboration" border="0" />
                </td>
                <td rowspan="3"><img src="/images/clear.gif" width="50" height="204" alt="" /></td>
            </tr>

            <tr>
                <td><img src="/images/meth_software_04.gif" width="161" height="56" alt="" /></td>
                <td><img src="/images/meth_software_05.gif" width="118" height="56" alt="" /></td>
                <td><img src="/images/meth_software_06.gif" width="177" height="56" alt="" /></td>
            </tr>

            <tr>
                <td><img src="/images/meth_development.gif" width="161" height="81" alt="Development" border="0" /></td>
                <td><img src="/images/meth_software_8.gif" width="118" height="81" alt="" /></td>
                <td><img src="/images/meth_design.gif" width="177" height="81" alt="Design" border="0" /></td>
            </tr>
        </table>
        --%>
        <img src="/i/meth_comp.jpg" alt="Component Methodology" />
    </div>

    <%--
    <hr width="640" size="1px" noshade="noshade" />
    --%>

    <table cellspacing="0" cellpadding="0" border="0" width="95%">
        <tr valign="top">
            <td class="bodyText">
                <!-- Collaboration begins -->
                <h3>Specification</h3>
                   <p> TopCoder Project Managers (PM) moderate the TopCoder <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Software
                    Forums</a> to gather requirements about a new Component Project. Once the project scope has been determined, the PM
                    creates a Requirements Specification for the Design Phase of the project. New projects are regularly posted to the TopCoder
                    <a href="http://www.topcoder.com/tc?module=ViewActiveContests&amp;ph=112">Software Development site</a>
                    and emails are sent out to members notifying them of the new projects.</p>
                <!-- Collaboration ends -->

                <!-- Design begins -->
                <h3>Architecture and Design</h3>
                    <p>The PM creates an Architecture Review Board made up of three TopCoder members who regularly submit designs. 
                    Submitted designs are scored using a standardized <a href="http://www.topcoder.com/tc?module=Static&d1=dev&d2=support&d3=desReviewSample">scorecard</a>. 
                    The design with the best score above the minimum 
                    score is chosen for the project. The winning designer is given additional time to incorporate suggestions from the 
                    Review Board. If the time frame is not met, the designer will be disqualified and the next design, in order of score, 
                    will be declared the winner. Once a winning design is determined, the PM posts the Development Phase of the 
                    project on the TopCoder Software Development page.</p>
                <!-- Design ends -->

                <!-- Development begins -->
                <h3>Development and Testing</h3>
                    <p>The PM creates a Development Review Board made up of three TopCoder members who regularly submit
                    development proposals for TopCoder Software
                    Projects. Submitted development solutions are scored using a standardized
                    <a href="http://www.topcoder.com/tc?module=Static&d1=dev&d2=support&d3=devReviewSample">scorecard</a>.
                    The development submission with the best score above the 
                     miniumum score is chosen for the project. The winning developer is given additional time to
                    incorporate suggestions from the
                    Review Board. If the time frame is not met, the developer is disqualified and the next design in
                    order of score will be declared the winner.</p>
                <!-- Development ends -->

                <!-- Complete begins -->
                <h3>Complete</h3>
                   <p>A complete TopCoder Software Component is added to a Component Catalog and is ready for download by
                    subscribers. It has been thoroughly
                    tested to ensure acceptable performance, accuracy of results, and abillity to handle bad data and
                    incorrect usage. Extensive documentation
                    is downloaded along with the component. Customers, the PM and other TopCoder members interact in the
                    <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Software Forums</a> to discuss the current complete version of
                    the component, as well as make
                    suggestions for future versions of the component.</li><br /><br />
                <!-- Complete ends -->
            </td>
        </tr>
    </table>
    
    <div align="center"><img src="http://topcoder.com/home/wp-content/themes/V5-Client-Theme/images/tcs_white_logo.png" alt="TopCoder Software" /></div>

    <table cellspacing="0" cellpadding="0" border="0" width="95%">
        <tr>
            <td colspan="2" class="bodyText" align="center">

                <p class="learn_more"><a href="http://www.topcoder.com/tc?module=Static&d1=about&d2=contactus">Contact us</a> today so we can get started developing your next application.</p>
            </td>
        </tr>
    </table>
    <p><br></p>
</td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
<td width="170">
    <jsp:include page="/includes/right.jsp">
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
