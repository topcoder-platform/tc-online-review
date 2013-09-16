<%@ taglib prefix="html" uri="/tags/struts-html" %>
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
                    Services</a>&#160;&#160;|&#160;&#160;<html:link
                page="/jsp/credits.jsp" styleClass="footerLinks">Credits</html:link>

            <br /><br />

            <span class="copyright">Copyright TopCoder, Inc. 2001-<script type="text/javascript">d=new Date();document.write(d.getFullYear());</script></span>
</div>

<%-- Analytics --%>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-6340959-1']);
  _gaq.push(['_setDomainName', '.topcoder.com']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>

<!-- Start of HubSpot Logging Code  -->
<script type="text/javascript" language="javascript">
var hs_portalid=17680;
var hs_salog_version = "2.00";
var hs_ppa = "topcoder.app101.hubspot.com";
document.write(unescape("%3Cscript src='" + document.location.protocol + "//" + hs_ppa + "/salog.js.aspx' type='text/javascript'%3E%3C/script%3E"));
</script>
<!-- End of HubSpot Logging Code -->

