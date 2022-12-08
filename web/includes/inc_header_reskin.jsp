<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>

<jsp:useBean id="sessionInfo" class="com.topcoder.onlinereview.component.webcommon.SessionInfo" scope="request" />
<c:set var="userId" value="${orfn:getLoggedInUserId(pageContext.request)}"/>

<script language="JavaScript" type="text/javascript" src="/js/tcscript.js"><!-- @ --></script>

<script type="text/javascript">
    var currEnv = '<%=ApplicationServer.ENVIRONMENT%>';
    var prodEnv = '<%=ApplicationServer.PROD%>';
    var scriptURL = '//uni-nav.topcoder-dev.com/v1/tc-universal-nav.js';

    if (currEnv === prodEnv) {
        scriptURL = '//uni-nav.topcoder.com/v1/tc-universal-nav-1.js';
    }

    !function(n,t,e,a,c,i,o){n['TcUnivNavConfig']=c,n[c]=n[c]||function(){
    (n[c].q=n[c].q??[]).push(arguments)},n[c].l=1*new Date();i=t.createElement(e),
    o=t.getElementsByTagName(e)[0];i.async=1;i.type="module";i.src=a;o.parentNode.insertBefore(i,o)
    }(window,document,"script",scriptURL,"tcUniNav");

    var userId = `${userId}`;
    var handle = "${userHandle}";
    var initials = handle ? handle.substr(0, 2).toUpperCase() : '';

    var user = {
        userId,
        initials,
        handle
    };

    tcUniNav('init', 'headerNav', {
        type: 'tool',
        toolName: 'Online Review',
        user,
        signOut() {
            window.location.replace("http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Logout")
        }
    });
</script>

<c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
    <div class="webHeader__avatar">
        <tc-webtag:handle coderId="${orfn:getLoggedInUserId(pageContext.request)}" />
    </div>
</c:if>

<div id="headerNav"></div>
