<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>

<!-- Footer Include Begins -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<!-- TopCoder Links Begins -->
	<tr>
		<td width="100%" class="footer">
			<a href="http://<%=ApplicationServer.SERVER_NAME%>/" class="footerLinks">Home</a>&#160;&#160;|&#160;&#160;<a
				href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=index" class="footerLinks">About
					TopCoder</a>&#160;&#160;|&#160;&#160;<a
				href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=pressroom&#x26;d2=index" class="footerLinks">Press
					Room</a>&#160;&#160;|&#160;&#160;<a
				href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=contactus" class="footerLinks">Contact
					Us</a>&#160;&#160;|&#160;&#160;<a
				href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=privacy"
				class="footerLinks">Privacy</a>&#160;&#160;|&#160;&#160;<a
				href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=terms" class="footerLinks">Terms</a></td>
	<tr>
	<!-- TopCoder Links Ends -->

	<!-- Member Sites Begins -->
	<tr>
		<td width="100%" class="footer">
			<a href="http://<%=ApplicationServer.SERVER_NAME%>/tc" class="footerLinks">Developer Center</a>&#160;&#160;|&#160;&#160;<a
				href="http://<%=ApplicationServer.CORP_SERVER_NAME%>/?module=Static&#x26;d1=corp&#x26;d2=index" class="footerLinks">Corporate
					Services</a>&#160;&#160;|&#160;&#160;<html:link
				page="/jsp/credits.jsp" styleClass="footerLinks">Credits</html:link></td>
	</tr>
	<!-- Member Sites Ends -->

	<tr>
		<td height="4" class="footerStripe"><html:img src="/i/corp/clear.gif" width="10" height="4" border="0" /></td>
	</tr>

	<tr>
		<td width="100%" class="copyright">Copyright &#169; 2001-2007, TopCoder, Inc. All rights reserved.</td>
	</tr>
</table>
<!-- Footer Include Ends -->