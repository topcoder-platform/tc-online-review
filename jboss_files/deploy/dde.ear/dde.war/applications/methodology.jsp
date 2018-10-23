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
    String page_name = "s_methodology.jsp";
    String action = request.getParameter("a");
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
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true" />
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="applications" />
            <jsp:param name="level2" value="methodology" />
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="95%">
        <div align="left">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal"><img src="/images/hd_app_meth.png" alt="Application Methodology" border="0" /></td></tr>
                <tr><td><h3>Competition-Based Methodology</h3></td></tr>
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
			</table>
			<%--
            <img src="/images/apps/apps_meth_graph.png" alt="Application Methodology" width="740" height="200" border="0" />
            --%>
            <img src="/i/meth_app.jpg" alt="Application Methodology" />
            <br /><br />

            
            

            <table border="0" cellspacing="0" cellpadding="0" width="95%">
                <tr valign="top">
                    <td class="bodyText">

					<p>The TopCoder Software Application Development Methodology utilizes the TopCoder Component Catalog and distributed member base to create robust, 
					high quality applications. It combines our Component Development Methodology with our peer review, component customization, and application integration processes.  
					This methodology is designed to deliver a repeatable and consistent solution to our customers.</p>
<!-- Specification begins -->
                        <h3>Specification</h3>
						<p>A TopCoder Project Manager performs the specification phase of the project hand-in-hand with all appropriate client resources.  
						At the conclusion of the specification phase, the applicable deliverables outlined below are presented to the client:</p>
						<ul>
						<li><strong>Application Requirements Specification:</strong> Serves as a scope document, describing what functionality is to be included in the application, 
						as well as what functionality is not to be included.</li>

						<li><strong>High Level Use Cases Diagrams:</strong> Includes UML use cases that describe primary actors and processes involved in the application.</li>

						<li><strong>Activity Diagrams:</strong> UML Diagrams used to depict the business processes that will be automated by the application.</li>

						<li><strong>Architecture Diagram:</strong> Defines the logical and physical layout of the system and explains design constraints such as third-party software, 
						language, platforms, standards and protocols.</li>

						<li><strong>Site Map and Site Definition:</strong> Describes the logical layout and flow of the user interface as well as detailed page or screen definitions.</li>

						<li><strong>Prototype:</strong> A graphical rendition of the application user interface.  This includes the look, feel and navigation of the system.  
						The prototype is used both as an iterative tool for TopCoder Project Managers to develop Functional Requirements, as well as direction for TopCoder 
						Design and Development Teams.  Prototypes are developed by TopCoder Creative Designers and Information Architects.</li>

						<li><strong>Quality Assurance Plan:</strong> Defines the overall testing strategy and detailed test scenarios that verify the adherence of the application 
						to all of the requirements.</li>

						<li><strong>Logical ER Model:</strong> Describes the data storage model, including all major entities and relationships that the application will require.  
						This deliverable is produced only for applications that require persistent data storage.</li>
                        </ul>
<!-- Specification ends -->

<!-- Architecture begins -->
                        <h3>Application Architecture</h3>
                        <p>In this phase, the TopCoder Project Manager and Component Architect design the application using Component Based Development (CBD) techniques that 
                        result in the division of the application into the most granular components.  Deliverables include:</p>
						<ul>
						<li><strong>Design Specification Document:</strong> This document details the technical design of the application, which includes the interactions of components, 
						algorithms, and design patterns.  </li>

						<li><strong>Component Deployment Diagram:</strong> This UML diagram defines the functional components within the systems from the TopCoder Component Catalog 
						as well as proprietary components required for the application including any third party or legacy system components. </li>

						<li><strong>Component Sequence Diagrams:</strong> UML diagrams that displays the interaction between components.   </li> 

						<li><strong>Component Interface Diagram:</strong> This UML diagram splits the application project into its corresponding component pieces and defines key 
						interaction points between components.</li>

						<li><strong>Persistence Schemas:</strong> Persistence strategy including required data storage design.</li>
                        </ul>
<!-- Architecture ends -->

<!-- Component Production begins -->
                        <h3>Component Production</h3>
                        <p>In this phase, the TopCoder Component Development Methodology is used to design and develop components.  The deliverables for each component are:</p>
                        <ul>

						<li><strong>Component Specifications:</strong> Individual specifications are defined for each new component, whether the component is being added to 
						the TopCoder Component Catalog or custom built for the application. </li>

						<li><strong>Use Case Diagrams:</strong> Define the functionality required by each software component.  </li>

						<li><strong>Class Diagram:</strong> Displays the class and component relationships within the component.</li>

						<li><strong>Sequence Diagrams:</strong> Displays the class and component interaction within the component.</li>

						<li><strong>Configuration Data:</strong> Configurable parameters the application uses.</li>

						<li><strong>Working solution:</strong> Fully unit tested, documented and system tested implementation of the design.</li>

						<li><strong>Test Cases:</strong> Test Cases covering unit tests, stress tests, accuracy tests and boundary tests.  Test cases are automated when appropriate.</li>
                        </ul>
<!-- Component Production ends -->

<!-- Application begins -->
                        <h3>Application Assembly</h3>
                        <p>In this phase, a TopCoder application assembly team assembles the application using the component pieces.  The result of this phase is:</p>
						<ul>
                        <li><strong>Complete Application:</strong> Fully unit tested, documented and system tested implementation of the specification.</li>
                        </ul>
<!-- Application ends -->

<!-- Certification begins -->
                        <h3>Certification</h3>
                        <p>The purpose of this phase is to perform system testing for the application on a quality assurance environment at TopCoder.</p>
                        <ul>
						<li><strong>Certified solution:</strong> Solution certified on the required platform and technologies. Verify the Quality Assurance Plan has been properly implemented.</li>
						<li><strong>Certified performance:</strong> The solution will be certified to perform to the expectation of the user as defined by the deliverables from the Specification Phase.</li>
                        </ul>
<!-- Certification ends -->

<!-- Deployment begins -->
                        <h3>Deployment</h3>
                        <p>In this phase the fully functioning solution and all associated deliverables will be turned over to the client.  
                        An onsite TopCoder Project Manager and TopCoder Deployment Engineers will deploy the application on quality assurance servers.
                        </p><br /><br />
<!-- Deployment ends -->
                    </td>
                </tr>
            </table>

            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td colspan="2" class="bodyText" align="center">
                        <p class="learn_more"><strong><a href="http://www.topcoder.com/tc?module=Static&d1=about&d2=contactus">Contact us</a> today so we can get started developing your next application.</strong></p>
                    </td>
                </tr>
            </table>
            <p><br /></p>
        </div>
        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
        <jsp:include page="/includes/right.jsp" >
            <jsp:param name="level1" value="index" />
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
