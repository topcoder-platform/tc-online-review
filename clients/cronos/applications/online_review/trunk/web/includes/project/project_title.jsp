<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project tile.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<c:set var="projectName" value='${project.allProperties["Project Name"]}' />
<c:if test='${not empty project.allProperties["Project Version"]}'>
    <c:set var="projectName" value='${projectName} ${project.allProperties["Project Version"]}' />
</c:if>

<title>
    <c:if test="${empty param.thirdLevelPageKey}">
        <or:text key="global.title.level2"
            arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
            arg1="${projectName}" />
    </c:if>
    <c:if test="${not empty param.thirdLevelPageKey}">
        <or:text key="global.title.level3"
            arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
            arg1="${projectName}"
            arg2="${orfn:getMessage(pageContext, param.thirdLevelPageKey)}" />
    </c:if>
</title>