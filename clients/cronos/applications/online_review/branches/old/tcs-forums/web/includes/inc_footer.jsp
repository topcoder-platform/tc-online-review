<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>

<!-- Footer Include Begins -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<!-- TopCoder Links Begins -->
    <tr>
		<td width="100%" class="footer">
			<A href="http://<%=ApplicationServer.SERVER_NAME%>/" class="footerLinks">Home</A>&#160;&#160;|&#160;&#160;
			<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=about&d2=index" class="footerLinks">About TopCoder</A>&#160;&#160;|&#160;&#160;
			<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=pressroom&d2=index" class="footerLinks">Press Room</A>&#160;&#160;|&#160;&#160;
			<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=about&d2=contactus" class="footerLinks">Contact Us</A>&#160;&#160;|&#160;&#160;
			<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=about&d2=privacy" class="footerLinks">Privacy</A>&#160;&#160;|&#160;&#160;
			<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=about&d2=terms" class="footerLinks">Terms</A>
		</td>
	<tr>
	<!-- TopCoder Links Ends -->

	<!-- Member Sites Begins -->
	<tr>
		<td width="100%" class="footer">
			<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc" class="footerLinks">Developer Center</A>&#160;&#160;|&#160;&#160;
			<A href="http://<%=ApplicationServer.CORP_SERVER_NAME%>/?module=Static&d1=corp&d2=index" class="footerLinks">Corporate Services</A>
		</td>
	<tr>
	<!-- Member Sites Ends -->

    <tr>
		<td height="4" class="footerStripe">
			<html:img src="/i/corp/clear.gif" width="10" height="4" border="0" />
		</td>
	</tr>
    <tr>
		<td width="100%" class="copyright">Copyright &#169; 2001-2005, TopCoder, Inc. All rights reserved.
		</td>
	</tr>
</table>
<!-- Footer Include Ends -->