<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

	<c:set var="projectName" value='${project.allProperties["Project Name"]}' />
	<c:if test='${not empty project.allProperties["Project Version"]}'>
		<c:set var="projectName" value='${projectName} ${project.allProperties["Project Version"]}' />
	</c:if>
	<title><bean:message key="global.title.level${(empty param.thirdLevelPageKey) ? '2' : '3'}"
		arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
		arg1="${projectName}"
		arg2="${orfn:getMessage(pageContext, param.thirdLevelPageKey)}" /></title>
	