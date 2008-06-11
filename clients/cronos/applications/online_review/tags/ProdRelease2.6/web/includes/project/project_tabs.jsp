<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.shared.util.ApplicationServer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<%-- We need to have no spaces between tabs, that's why the formatting is weird --%>
<div id="mainTabs">
    <div style="float: left;">
    <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 1)}">
            <html:img src="/i/or/tab_my_open_projects_on.gif" width="119" height="36" border="0" imageName="tab1" />
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 1)}">
            <html:link page="/actions/ListProjects.do?method=listProjects&amp;scope=my" onmouseover="img_act('tab1')" onmouseout="img_inact('tab1')">
                <html:img src="/i/or/tab_my_open_projects.gif" width="119" height="36" border="0" imageName="tab1" />
            </html:link>
        </c:if>
    </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 2)}">
        <html:img src="/i/or/tab_all_open_projects_on.gif" width="119" height="36" imageName="tab2" />
    </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${(empty projectTabIndex) or (projectTabIndex != 2)}">
        <html:link page="/actions/ListProjects.do?method=listProjects&amp;scope=all" onmouseover="img_act('tab2')" onmouseout="img_inact('tab2')">
            <html:img src="/i/or/tab_all_open_projects.gif" width="119"height="36" border="0" imageName="tab2" />
        </html:link>
    </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${isAllowedToViewInactiveProjects}">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 4)}">
            <html:img src="/i/or/tab_inactive_projects_on.gif" width="119" height="36" border="0" imageName="tab4" />
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 4)}">
            <html:link page="/actions/ListProjects.do?method=listProjects&amp;scope=inactive" onmouseover="img_act('tab4')" onmouseout="img_inact('tab4')">
                <html:img src="/i/or/tab_inactive_projects.gif" width="119"height="36" border="0" imageName="tab4" />
            </html:link>
        </c:if>
    </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${isAllowedToCreateProject}">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 3)}">
            <html:img src="/i/or/tab_create_project_on.gif" width="119" height="36" border="0" imageName="tab3" />
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 3)}">
            <html:link page="/actions/NewProject.do?method=newProject" onmouseover="img_act('tab3')" onmouseout="img_inact('tab3')">
                <html:img src="/i/or/tab_create_project.gif" width="119" height="36" border="0" imageName="tab3" />
            </html:link>
        </c:if>
    </c:if>
    </div>
</div>
