<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright: Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: The projects listing page for the online review application.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <c:choose>
        <c:when test="${projectTabIndex == 1}">
            <c:set var="listKeyName" value="listProjects.title.MyProjects" />
        </c:when>
        <c:when test="${projectTabIndex == 4}">
            <c:set var="listKeyName" value="listProjects.title.DraftProjects" />
        </c:when>
        <c:otherwise>
            <c:set var="listKeyName" value="listProjects.title.AllProjects" />
        </c:otherwise>
    </c:choose>
    <title><or:text key="global.title.level2"
        arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
        arg1='${orfn:getMessage(pageContext, listKeyName)}' /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/project_list.css" />

    <!-- CSS and JS from wireframes -->
    <script language="javascript" type="text/javascript" src="/js/or/expand_collapse.js"><!-- @ --></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
            let avatar = document.querySelector('.webHeader__avatar a');
            let avatarImage = document.createElement('div');
            avatarImage.className = "webHeader__avatarImage";
            let twoChar = avatar.text.substring(0, 2);
            avatarImage.innerText = twoChar;
            avatar.innerHTML = avatarImage.outerHTML;
        });
    </script>
</head>

<body>

<jsp:include page="/includes/inc_header_reskin.jsp" />

<jsp:include page="/includes/project/project_tabs_reskin.jsp" />

<div class="content">
    <div class="content__inner">
        <jsp:include page="/includes/project/project_tab_info_reskin.jsp" />
        <div class="divider"></div>
        <jsp:include page="/includes/project/project_list_reskin.jsp" />
    </div>
</div>

<jsp:include page="/includes/inc_footer_reskin.jsp" />

</body>
</html>
