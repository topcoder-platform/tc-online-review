<%@ page import="com.topcoder.shared.util.ApplicationServer,
                 com.topcoder.web.common.BaseServlet, com.topcoder.web.common.SessionInfo" %>

<%
    SessionInfo sessionInfo = (SessionInfo)request.getAttribute(BaseServlet.SESSION_INFO_KEY);
    String level1 = request.getParameter("level1")==null?"competition":request.getParameter("level1");
    String handle = null;
    if (sessionInfo != null)
    {
        handle = sessionInfo.getHandle();
    }

%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tbody>
    <tr>
        <td width="100%" class="footer">
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/" class="footerLinks">Home</a>&#160;&#160;|&#160;&#160;
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/aboutus/" class="footerLinks">About TopCoder</a>&#160;&#160;|&#160;&#160;
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/aboutus/news/" class="footerLinks">Press Room</a>&#160;&#160;|&#160;&#160;
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/help/template-help/" class="footerLinks">Contact Us</a>&#160;&#160;|&#160;&#160;
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/aboutus/careers/" class="footerLinks">Careers</a>&#160;&#160;|&#160;&#160;
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=privacy" class="footerLinks">Privacy</a>&#160;&#160;|&#160;&#160;
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=terms" class="footerLinks">Terms</a>
            <br />
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc" class="footerLinks">Competitions</a>&#160;&#160;|&#160;&#160;
            <a href="https://<%=ApplicationServer.SERVER_NAME%>/direct/" class="footerLinks">Cockpit</a>        </td>
    </tr>
    <tr>
      <td width="100%" class="copyright">Copyright TopCoder, Inc. 2001-<script type="text/javascript">d=new Date();document.write(d.getFullYear());</script></td></tr>
</tbody>
</table>

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

