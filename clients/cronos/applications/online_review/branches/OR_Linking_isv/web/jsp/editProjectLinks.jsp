<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

    <head>
        <jsp:include page="/includes/project/project_title.jsp"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

        <!-- TopCoder CSS -->
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />"/>
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />"/>
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />"/>
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />"/>

        <!-- CSS and JS by Petar -->
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles2.css' />"/>
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/phasetabs.css' />"/>

        <script type="text/javascript" src="<html:rewrite href='/js/or/projectLinking.js' />"></script>
        <script type="text/javascript" src="<html:rewrite href='/js/or/jquery.js' />"></script>
    </head>

    <body>
    <div align="center">

        <div class="maxWidthBody" align="left">

            <jsp:include page="/includes/inc_header.jsp"/>

            <jsp:include page="/includes/project/project_tabs.jsp"/>
            <c:set var="project" value="${requestScope.project}"/>
            <div id="mainMiddleContent">
                <div class="clearfix"/>
                <html:form action="/actions/SaveProjectLinks" method="post">
                    <html:hidden property="method" value="saveProjectLinks" />
                    <html:hidden property="pid"/>
                    <div id="tabcontentcontainer">
                        <div id="contentTitle">
                            <h3><c:out value="${project.allProperties['Project Name']}"/>
                                <bean:message key="global.version.shortened"/><c:out
                                        value="${project.allProperties['Project Version']}"/>
                                - <bean:message key="editProjectLinks.ManageProjectLinks.title"/></h3>
                        </div>

                        <c:set var="projectLinks" value="${requestScope.projectLinks}"/>
                        <c:set var="projectLinkTypes" value="${requestScope.projectLinkTypes}"/>
                        <div id="tabExistLinks">
                            <table cellpadding="0" id="existLinks" class="tabLinks">
                                <tbody>
                                <tr>
                                    <td colspan="4" class="title">
                                        <bean:message key="editProjectLinks.EditProjectLinks.title"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="header"><bean:message
                                            key="editProjectLinks.tblHeader.LinkedProjectName"/></td>
                                    <td class="header"><bean:message key="editProjectLinks.tblHeader.LinkType"/></td>
                                    <td class="header"><bean:message key="editProjectLinks.tblHeader.Operation"/></td>
                                </tr>

                                <c:forEach items="${projectLinks}" var="projectLink" varStatus="index">
                                    <tr class="${index.index mod 2 ne 0 ? 'dark' : 'light'}">
                                        <td nowrap="nowrap" class="value">
                                            <c:out value="${projectLink.targetProjectName}"/>
                                            <bean:message key="global.version.shortened"/><c:out value="${projectLink.targetProjectVersion}"/>
                                            <input type="hidden" name="targetProjectId"
                                                   value="${projectLink.targetProjectId}"/>
                                            <input type="hidden" name="targetProjectName"
                                                   value="<c:out value="${projectLink.targetProjectName}"/> <bean:message key="global.version.shortened"/><c:out value="${projectLink.targetProjectVersion}"/>"/>
                                        </td>
                                        <td nowrap="nowrap" class="value">
                                            <select class="inputBox" name="linkTypeId">
                                                <c:forEach items="${projectLinkTypes}" var="linkType">
                                                    <c:set var="selected"
                                                           value="${linkType.id eq projectLink.type.id ? 'selected=\"selected\"' : ''}"/>
                                                    <option value="${linkType.id}" ${selected}>
                                                        <c:out value="${linkType.name}"/>
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td nowrap="nowrap" class="value">
                                            <html:img srcKey="editProjectLinks.btnDelete.img" border="0" style=""
                                                      onclick="deleteLink(this)" altKey="editProjectLinks.btnDelete.alt"/>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <tr>
                                    <td colspan="4" class="lastRowTD"><!-- @ --></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <c:set var="projects" value="${requestScope.projects}"/>
                        <div id="tabNewLinks">
                            <table cellpadding="0" id="newLinks" class="tabLinks">
                                <tbody>
                                <tr>
                                    <td colspan="6" class="title">
                                        <bean:message key="editProjectLinks.AddProjectLinks.title"/></td>
                                </tr>
                                <tr>
                                    <td class="header"><bean:message key="editProjectLinks.ProjectID.title"/></td>
                                    <td class="header"><bean:message key="editProjectLinks.SelectAProject.title"/></td>
                                    <td class="header"><bean:message key="editProjectLinks.tblHeader.LinkType"/></td>
                                    <td class="header"><bean:message key="editProjectLinks.tblHeader.Operation"/></td>
                                </tr>
                                <tr class="light">
                                    <td nowrap="nowrap" class="value">
                                        <input type="text" class="input" name="newTargetProjectId"
                                               id="newTargetProjectId" onchange="changeProjectById(this);"/></td>
                                    <td nowrap="nowrap" class="value">
                                        <select onchange="changeProject(this);" class="inputBox"
                                                name="newTargetProjectIdSelect" id="newTargetProjectIdSelect">
                                            <option value="-1">
                                                <bean:message key="editProjectLinks.SelectTargetProject.title"/>
                                            </option>
                                            <c:forEach items="${projects}" var="project">
                                                <option value="${project.id}">
                                                    <c:out value="${project.allProperties['Project Name']}"/>
                                                    <bean:message key="global.version.shortened"/><c:out
                                                        value="${project.allProperties['Project Version']}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td nowrap="nowrap" class="value">
                                        <select class="inputBox" name="newLinkTypeId" id="newLinkTypeId">
                                            <option selected="selected" value="-1">
                                                <bean:message key="editProjectLinks.SelectLinkType.title"/>
                                            </option>
                                            <c:forEach items="${projectLinkTypes}" var="linkType">
                                                <c:set var="selected"
                                                       value="${linkType.id eq projectLink.type.id ? 'selected=\"selected\"' : ''}"/>
                                                <option value="${linkType.id}" ${selected}>
                                                    <c:out value="${linkType.name}"/>
                                                </option>
                                            </c:forEach>
                                        </select></td>
                                    <td nowrap="nowrap" class="value">
                                        <html:img srcKey="editProjectLinks.btnAdd.img" border="0"
                                                  altKey="editProjectLinks.btnAdd.alt" style="" onclick="addLink()"/>
                                    </td>
                                </tr>

                                <tr>
                                    <td colspan="6" class="lastRowTD"><!-- @ --></td>
                                </tr>
                                </tbody>
                            </table>
                            <br/>
                        </div>

                        <div class="bottomButtonBar">
                            <html:image srcKey="editProjectLinks.btnSaveChanges.img" border="0"
                                        altKey="editProjectLinks.btnSaveChanges.alt"/>
                            <html:link
                                    page="/actions/ViewProjectDetails.do?method=viewProjectDetails&amp;pid=${project.id}">
                                <html:img srcKey="editProjectLinks.btnCancel.img" border="0"
                                          altKey="editProjectLinks.btnCancel.alt"/>
                            </html:link>
                        </div>

                    </div>
                </html:form>
            </div>

            <jsp:include page="/includes/inc_footer.jsp"/>
        </div>
    </div>

    </body>
</html:html>
