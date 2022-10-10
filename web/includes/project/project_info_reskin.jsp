<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>


<div class="projectInfo">
    <h1 class="projectInfo__projectName">${project.allProperties["Project Name"]}</h1>
    <div class="projectInfo__info">
        <c:if test='${!(empty project.allProperties["Project Version"])}'>
            <p class="projectInfo__projectVersion"><or:text key="global.version" /> ${project.allProperties["Project Version"]}</p>
        </c:if>
        <p class="projectInfo__projectStatus"><or:text key="viewProjectDetails.ProjectStatus" /> ${projectStatus}</p>

        <div class="projectInfo__links">
            <!-- <c:if test="${isAllowedToCompleteAppeals}">
                <a class="projectInfo__link" href="<or:url value='/actions/EarlyAppeals?pid=${project.id}' />"><or:text key="viewProjectDetails.CompleteAppeals" /></a>
            </c:if>
            <c:if test="${isAllowedToResumeAppeals}">
                <a class="projectInfo__link" href="<or:url value='/actions/EarlyAppeals?pid=${project.id}' />"><or:text key="viewProjectDetails.ResumeAppeals" /></a>
            </c:if>
            <c:if test="${isAllowedToUnregister}">
                <a class="projectInfo__link" href="<or:url value='/actions/Unregister?pid=${project.id}' />"><or:text key="viewProjectDetails.Unregister" /></a>
            </c:if> -->
            <!-- <a class="projectInfo__link" href="<or:url value='/actions/ViewLateDeliverables?project_id=${project.id}' />"><or:text key="viewProjectDetails.LateDeliverables" /></a> -->
            <a class="projectInfo__link"
                href="${viewContestLink}"><or:text key="viewProjectDetails.ViewContest" /></a>
            <a class="projectInfo__link projectInfo__forumLink"
                href="${forumLink}"><or:text key="viewProjectDetails.DevelopmentForum" /></a>
            <c:if test="${isAllowedToContactPM}">
                <a class="projectInfo__link" href="<or:url value='/actions/ContactManager?pid=${project.id}' />"><or:text key="viewProjectDetails.ContactPM" /></a>
            </c:if>
        </div>
    </div>
</div>
