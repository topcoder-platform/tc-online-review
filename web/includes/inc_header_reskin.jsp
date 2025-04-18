<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    var scriptURL = '//uni-nav.topcoder.com/v1/tc-universal-nav.js';

    /*if (currEnv != prodEnv) {
        scriptURL = '//uni-nav.topcoder-dev.com/v1/tc-universal-nav-1.js';
    }*/

    !function(n,t,e,a,c,i,o){n['TcUnivNavConfig']=c,n[c]=n[c]||function(){
    (n[c].q=n[c].q??[]).push(arguments)},n[c].l=1*new Date();i=t.createElement(e),
    o=t.getElementsByTagName(e)[0];i.async=1;i.type="module";i.src=a;o.parentNode.insertBefore(i,o)
    }(window,document,"script",scriptURL,"tcUniNav");


    var signInUrl = "https://accounts-auth0.topcoder.com/?retUrl=https://software.topcoder.com/review";


    var registerUrl = "https://accounts-auth0.topcoder.com/?retUrl=https://software.topcoder.com/review&mode=signUp&regSource=onlinereview";

    var signOutUrl = "https://topcoder.com/logout";

    /*if (currEnv != prodEnv) {
        registerUrl = "https://accounts-auth0.topcoder-dev.com/?retUrl=https://software.topcoder-dev.com/review&mode=signUp&regSource=onlinereview";

        signInUrl = "https://accounts-auth0.topcoder-dev.com/?retUrl=https://software.topcoder-dev.com/review";
        signOutUrl = "https://topcoder-dev.com/logout";
    }*/

    tcUniNav('init', 'headerNav', {
        type: 'tool',
        toolName: 'Review',
        toolRoot: '/',
        user: 'auto',
        signOut() {
            window.location.replace(signOutUrl)
        },
        signIn() {
            window.location.replace(signInUrl)
        },
        signUp() {
            window.location.replace(registerUrl)
        }
    });
</script>

<c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
    <div class="webHeader__avatar">
        <tc-webtag:handle coderId="${orfn:getLoggedInUserId(pageContext.request)}" />
    </div>
</c:if>

<div id="headerNav"></div>
