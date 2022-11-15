<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright: Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: The user error page for the online review application.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="userError.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">
    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />

    <!-- JS from wireframes -->
    <script language="javascript" type="text/javascript" src="/js/or/popup.js"></script>
    <script language="javascript" type="text/javascript" src="/js/or/expand_collapse.js"></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css">
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"></script>
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

    <div class="content content">
        <div class="content__inner">
            <div class="error__projectName">
                <button type="button" class="back-btn" onclick="history.back()">
                    <i class="arrow-prev-icon"></i>
                </button>
                <h1 class="projectInfo__projectName">
                    ${project.allProperties['Project Name']}
                </h1>
            </div>
            <h2 class="attention">
                <or:text key="userError.Attention" />
            </h2>
            <div class="errorBanner">
             <p class="errorPrefix"><or:text key="userError.ErrorPrefix" />
                <span class="errorRed">${errorTitle}</span>
                <p class="errorReason"><or:text key="userError.Reason" />
                <span>${errorMessage}</span></p>
            </div>
             <div align="right">
                <a class="backToHome" href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                    <or:text key="btnBack.home"/>
                </a>
            </div>
        </div>
    </div>
<jsp:include page="/includes/inc_footer_reskin.jsp" />


</body>
</html>
