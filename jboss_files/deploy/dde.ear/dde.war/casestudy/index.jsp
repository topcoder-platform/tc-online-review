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
    <title>Software Components form the basis of the applications built by TopCoder Software</title>
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

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="index"/>
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td align="center">
            <img src="/images/clear.gif" width="1" height="15" alt="" border="0"><br>
            <img src="/images/headCaseStudy.gif" alt="Case Study" width="545" height="32" border="0" /><br>
            <div align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="530">
                <tr>
                    <td width="510" align="left">
                        <p><img src="/images/case_study_promoA.gif" alt="We put our money where our mouth is" width="510" height="48" border="0" /><br>
                        <img src="/images/case_study_promoB.gif" alt="Online Review application" width="510" height="250" border="0" /><br>
                        <img src="/images/clear.gif" alt="" width="10" height="10" border="0" /><br>
                        <strong>Learn about how we saved OURSELVES time and money developing our Online Review application using our member base, component
                        catalogs and competition-based methodology.</strong></p>
                    </td>
                </tr>
            </table>
            </div>

            <hr width="530" size="1px" noshade>

            <table border="0" cellpadding="0" cellspacing="0" width="530">
                <tr valign="top">
                    <td width="100%" class="bodyText">
                        <font size="3"><strong>Fast Facts:</strong></font>
                        <ul>
                            <li>Once the catalog has been purchased, a project of this size would be completed for $85,396 at a cost per hour of $50.17.</li>
                            <li>Project management is billed out at a rate of $110/hr.  Excluding project management, the rate is $41.27 per hour including
                            the cost of the catalog.</li>
                            <li>The rate excluding project management once the catalog has been purchased would be $32.09.</li>
                            <li><a target="_new" href="online_review_case_study.pdf">Download the PDF</a> of the case study</li>
                        </ul>

                        <hr width="530" size="1px" noshade>

                        <p><strong>Business Problem</strong><br>
                        The TopCoder Software Development Methodologies, for both components and applications, have a peer review process at their core.
                        Architecture Review Boards review and score design submissions and Development Review Boards test and score development submissions.
                        This manual process can be extremely time consuming and logistically difficult.  Automating this process will save in development costs as
                        well as development timeframes.</p>

                        <p><strong>Specification</strong><br>
                        A TopCoder Software Project Manager and a TopCoder Software Information Architect completed the Specification phase of this project in
                        170 hours.  Our price to customers for this phase would be $16,900. </p>

                        <p><strong>Architecture and Design</strong><br>
                        The detailed design of the application was in this case left as one design project. This design project, including the associated architecture
                        reviews and project management, was completed in a total of 228 hours.  The customer cost for this phase would be $15,596.  </p>

                        <p><strong>Development and Testing</strong><br>
                        Next these design deliverables were broken into three development projects.  These development projects, including all components
                        used, associated development reviews and project management were completed in a total of 1065 hours.  Customers have a choice regarding
                        payment for development.  TopCoder is very focused on the utilization of software components in the development of software applications.

                        <p><strong>Component Catalog</strong><br>
                        In the case of Online Review, 15 components were used from the TopCoder Software Component Catalog.  The cost for these 15 components
                        is $15,000.  As an alternative, customers can purchase a subscription to the TopCoder Software Component Catalog for $1,200 per
                        developer per year.  A subscription includes all components in the catalog, all components added to the catalog for the year, component
                        support and component upgrades.  Customers receive a license for all components to modify and distribute internally at their company.
                        The minimum enterprise subscription is for ten developers.   Therefore, including the $12,000 for a subscription, a customer would
                        pay a total of $44,850 for the development phase of the project (as compared to $47,850 purchasing components separately).</p>

                        <p><strong>Integration</strong><br>
                        The TopCoder Software Project Manager, Information Architect, all TopCoder Designers, all TopCoder developers and all TopCoder Review
                        Board Members associated with this application project completed Integration at TopCoder in 129 hours.  Cost to customers for this
                        phase would be $10,350.

                        <p><strong>Quality Assurance</strong><br>
                        Although already deployed in our QA environment, we estimate QA deployment at a customer site and associated User Acceptance testing
                        would be completed in 110 hours.  The cost to customers for this phase would be $9,700.</p>

                        <p><strong>Final Cost</strong><br>
                        The entire on-line review system was completed in 1702 hours for a total customer cost of $97,396 (including the cost of the TopCoder
                        Software Component Catalog). This translates to a cost per hour of $57.22.</p>
                    </td>
                </tr>
            </table>

             <hr width="530" size="1px" noshade>

            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td colspan="2" class="bodyText" align="center">
                        <p><font size="3"><strong><a href="http://www.topcoder.com/tc?module=Static&d1=about&d2=contactus">Contact us</a> today so we can get started developing your next application.</strong></font></p>
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
        <jsp:include page="/includes/right.jsp" >
            <jsp:param name="level1" value="index"/>
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
