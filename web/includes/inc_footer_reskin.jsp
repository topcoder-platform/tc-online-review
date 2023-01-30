<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ page import="com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>

<script language="JavaScript" type="text/javascript" src="/js/or/validation.js"><!-- @ --></script>
<script language="JavaScript" type="text/javascript" src="/js/toasts.js"><!-- @ --></script>

<script type="text/javascript">
  document.addEventListener("DOMContentLoaded", function() {
    tcUniNav('init', 'footerNav', {
        type: 'footer',
    });
  });
</script>

<div id="footerNav"></div>

<div class="loading-spinner">
    <div class="spinner"></div>
</div>

<div id="toast"></div>

<jsp:include page="/includes/privacyPolicyModal.jsp" />
<jsp:include page="/includes/termsModal.jsp" />
<jsp:include page="/includes/supportModal.jsp" />

<%-- Analytics --%>

<script>
  !function(){var analytics=window.analytics=window.analytics||[];if(!analytics.initialize)if(analytics.invoked)window.console&&console.error&&console.error("Segment snippet included twice.");else{analytics.invoked=!0;analytics.methods=["trackSubmit","trackClick","trackLink","trackForm","pageview","identify","reset","group","track","ready","alias","debug","page","once","off","on","addSourceMiddleware","addIntegrationMiddleware","setAnonymousId","addDestinationMiddleware"];analytics.factory=function(e){return function(){var t=Array.prototype.slice.call(arguments);t.unshift(e);analytics.push(t);return analytics}};for(var e=0;e<analytics.methods.length;e++){var key=analytics.methods[e];analytics[key]=analytics.factory(key)}analytics.load=function(key,e){var t=document.createElement("script");t.type="text/javascript";t.async=!0;t.src="https://cdn.segment.com/analytics.js/v1/" + key + "/analytics.min.js";var n=document.getElementsByTagName("script")[0];n.parentNode.insertBefore(t,n);analytics._loadOptions=e};analytics.SNIPPET_VERSION="4.13.1";
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

