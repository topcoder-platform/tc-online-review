<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays footer for all pages in online review application.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>

<div align="center" style="margin: 20px;">
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/" class="footerLinks">Home</a>&#160;&#160;|&#160;&#160;<a
                href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=index" class="footerLinks">About
                    TopCoder</a>&#160;&#160;|&#160;&#160;<a
                href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=pressroom&#x26;d2=index" class="footerLinks">Press
                    Room</a>&#160;&#160;|&#160;&#160;<a
                href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=contactus" class="footerLinks">Contact
                    Us</a>&#160;&#160;|&#160;&#160;<a
                href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=privacy"
                class="footerLinks">Privacy</a>&#160;&#160;|&#160;&#160;<a
                href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&#x26;d1=about&#x26;d2=terms" class="footerLinks">Terms</a>

            <br />

            <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc" class="footerLinks">Developer Center</a>&#160;&#160;|&#160;&#160;<a
                href="http://<%=ApplicationServer.CORP_SERVER_NAME%>/?module=Static&#x26;d1=corp&#x26;d2=index" class="footerLinks">Corporate
                    Services</a>&#160;&#160;|&#160;&#160;<a href="<or:url value='/actions/Credits' />" class="footerLinks">Credits</a>

            <br /><br />

            <span class="copyright">Copyright TopCoder, Inc. 2001-<script type="text/javascript">d=new Date();document.write(d.getFullYear());</script></span>
</div>

<%-- Analytics --%>

<script>
  !function(){var analytics=window.analytics=window.analytics||[];if(!analytics.initialize)if(analytics.invoked)window.console&&console.error&&console.error("Segment snippet included twice.");else{analytics.invoked=!0;analytics.methods=["trackSubmit","trackClick","trackLink","trackForm","pageview","identify","reset","group","track","ready","alias","debug","page","once","off","on"];analytics.factory=function(t){return function(){var e=Array.prototype.slice.call(arguments);e.unshift(t);analytics.push(e);return analytics}};for(var t=0;t<analytics.methods.length;t++){var e=analytics.methods[t];analytics[e]=analytics.factory(e)}analytics.load=function(t){var e=document.createElement("script");e.type="text/javascript";e.async=!0;e.src=("https:"===document.location.protocol?"https://":"http://")+"cdn.segment.com/analytics.js/v1/"+t+"/analytics.min.js";var n=document.getElementsByTagName("script")[0];n.parentNode.insertBefore(e,n)};analytics.SNIPPET_VERSION="4.0.0";
  analytics.load("CSmkPqh4LsMPA5TVTxQSx98M7OvbuUQl");
  analytics.page();
  }}();
</script>


<script>
analytics.identify('', {
  <%--name: '<tc-webtag:handle coderId="${orfn:getLoggedInUserId(pageContext.request)}" />' --%>
  id: '${orfn:getLoggedInUserId(pageContext.request)}'
});
</script>

<script>
function prepareFrame() {
	var ifrm = document.createElement("iframe");
	ifrm.setAttribute("src", "<%=com.cronos.onlinereview.util.ConfigHelper.getNewAuthUrl()%>");
	ifrm.style.width = "0px";
	ifrm.style.height = "0px";
	document.body.appendChild(ifrm);
}
window.onload = prepareFrame;
</script>

