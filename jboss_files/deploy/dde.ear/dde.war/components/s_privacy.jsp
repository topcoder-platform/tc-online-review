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
	String page_name = "s_privacy.jsp";
	String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>TopCoder Software</title>
	<jsp:include page="/includes/header-files.jsp" />
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
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

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr><td width="100%" height="2" bgcolor="#000000"><img src="/images/clear.gif" alt="" width="10" height="2" border="0" /></td></tr>
</table>
<!-- breadcrumb ends -->

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
		<td width="100%">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
				<tr><td class="normal"><img src="/images/hd_privacy_policy.png" alt="Privacy Policy" border="0" /></td></tr>
				<tr><td class="normal">
						<p>&nbsp;</p>

						<p>Because we value your contributions to, and use of, our website, TopCoder Software is committed to protecting the privacy of our users. We want to provide a safe, secure user experience. We will use reasonable commercial efforts to ensure that the information you submit to us remains private. Your personal information is immediately transferred from our web server to behind our firewall, where it remains isolated from a direct connection to the Internet.</p>

						<p>Our security and Privacy Statement are periodically reviewed and enhanced as necessary. However, you should understand that "perfect security" does not exist on the Internet. The information that you submit to us is used only for the purposes as set forth herein, as required by law, or when we believe in good faith that disclosure is legally required to protect our rights. We will not use your personal information for any other purpose other than as set out here without first obtaining your permission. The following reflects our commitment to you.</p>

						<p><strong>Information About All TopCoder Software Visitors</strong><br />
								We gather information about all of our users collectively, including (a) what area users visit most frequently, (b) their Internet domain and their computer's IP address, (c) the type of browser and operating system they use and (d) if the user linked to our Web site from another Web site, the address of that other Web site. This information does not in any way personally identify a user; we only use this data anonymously and in the aggregate. We use this information to analyze trends and statistics to help us improve our services, plan site enhancements and measure overall site effectiveness. From time to time we may reveal general, anonymous, aggregated statistical information about our site, services and users, such as the number of visits and users and what site features they use.</p>

						<p><strong>Information About You Specifically</strong><br />
						If you choose to register for access to the TopCoder Software website in order to test our software components or to participate in collaborative forums, you will be required to provide us with additional information about yourself, including name, address and contact information. From time to time, we may use the contact information to contact and/or alert registered users of new components and communicate news, events and reminders. If you do not want to be alerted to any of these items, send us an e-mail asking to be removed from this notification listing to service@topcodersoftware.com.</p>

						<p>Only TopCoder Software will have access to view your contact information.</p>

						<p>The personal information you provide must be accurate and complete and all registration details (where applicable) must contain your real name, address and other requested details. You are solely responsible for your personal information and we may take any action with respect to your personal information we deem necessary or appropriate if we believe it may cause us to suffer any loss, liability or commercial damage.</p>

						<p><strong>Collaboration Discussion Forums</strong><br />
						Collaborative discussion rooms make forum style discussion topics available to our registered users. The purpose of this forum is to allow registered users to collaborate on their software needs. The content in the collaborative discussion groups will be reviewed by TopCoder Software to determine the need for certain software components.  These forums will identify TopCoder Software users by their email addresses. Those simply viewing the discussions may do so without identifying themselves, but those who wish to contribute must be users and must register. Please remember that any information you disclose in these areas becomes public information. Accordingly, you should always exercise caution when deciding to disclose any personal information.</p>

						<p><strong>Cookies</strong><br />
						To enhance your experience at TopCoder Software, we may use a feature on your Internet browser called a "cookie". As you may already know, cookies are small files that your Web browser places on your computer's hard drive. We use cookies for remembering user names and passwords and preferences, tracking click streams, and for load balancing. Because of our use of cookies, we can deliver faster service, consistent, updated results, and a more personalized site experience. You have the option of setting your browser to reject cookies. However, doing this may hinder performance and negatively impact your experience on our site.</p>

						<p><strong>Feedback</strong><br />
						TopCoder Software may ask you for feedback and comments about the site and we encourage your participation. Any feedback that is submitted becomes the property of TopCoder Software. We may use this feedback, such as success stories or component ideas, for marketing purposes, or to contact you for further feedback.</p>

						<p><strong>Links to Other Sites</strong><br />
						We may provide links to other Web sites that we believe can offer you useful information. However, we are not responsible for the privacy policies of other Web sites, including those sites on which our logo or other information supplied by us appears. We suggest that you access these sites' online policies regarding their data collection.</p>

						<p><strong>Access to Your Information</strong><br />
						TopCoder Software will provide you with a reasonable opportunity to review the information that it has gathered about you, as well as a reasonable opportunity to correct any erroneous information.</p>

						<p><strong>Privacy Commitment Changes</strong><br />
						If we decide to change our privacy commitment for TopCoder Software, we will post those changes here so that you will always know what information we gather, how we might use that information, and whether we will disclose it to anyone. In the event that we make any significant changes to our Privacy Statement, we will also provide you with the opportunity to consent to different uses of your personal information. If, at any time, you have questions or concerns about TopCoder Software's privacy commitment, please feel free to e-mail us at service@topcodersoftware.com.</p>

						<p>Thank you for visiting our site.</p>
					</td>
				</tr>
<!-- Use the following two lines only when user has clicked on Privacy on Link on Terms & Conditions page -->
				<tr><td height="20"><img src="/images/clear.gif" alt="" width="10" height="20" border="0" /></td></tr>
				<tr><td class="normalCenter"><strong><a href="javascript:history.back()">&lt; Go back</a></strong></td></tr>

				<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
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
