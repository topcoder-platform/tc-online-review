<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays footer for all pages in online review application.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
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

<%-- KissMetrics Analytics --%>
<script type="text/javascript">var _kmq = _kmq || [];
  var _kmk = _kmk || 'aa23cd43c455ef33b6a0df3de81a79af9ea30f75';
  function _kms(u){
    setTimeout(function(){
      var d = document, f = d.getElementsByTagName('script')[0],
      s = d.createElement('script');
      s.type = 'text/javascript'; s.async = true; s.src = u;
      f.parentNode.insertBefore(s, f);
    }, 1);
  }
  _kms('//i.kissmetrics.com/i.js');
  _kms('//scripts.kissmetrics.com/' + _kmk + '.2.js');
</script>


