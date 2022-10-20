<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<div class="projectDetails">
    <div class="projectDetails__sectionHeader">
        <div class="projectDetails__title">
            <or:text key="viewProjectDetails.ProjectDetails" />
        </div>
        <div class="projectDetails__accordion">
        </div>
    </div>
    
    <div class="projectDetails__sectionBody">
        <div class="projectDetailsGrid">
            <c:if test="${isAllowedToViewSVNLink}">
                <div class="item">
                    <div class="head"><or:text key="viewProjectDetails.SVNModule" /></div>
                    <div class="body"><a href='${fn:escapeXml(project.allProperties["SVN Module"])}'>${orfn:htmlEncode(project.allProperties["SVN Module"])}</a></div>
                </div>
            </c:if>
            <div class="item">
                <div class="head">Type: </div>
                <div class="body">${projectType}</div>
            </div>
            <div class="item">
                <div class="head">Category: </div>
                <div class="body">${projectCategory}</div>
            </div>
            <c:if test="${isAllowedToViewAutopilotStatus}">
            <div class="item">
                <c:set var="autopilotStatus" value="${project.allProperties['Autopilot Option']}" />
                <c:if test='${autopilotStatus != "On" and autopilotStatus != "Off"}'>
                    <c:set var="autopilotStatus" value="Off" />
                </c:if>
                <div class="head"><or:text key="viewProjectDetails.AutopilotStatus" /> </div>
                <div class="body"><or:text key="global.option.${autopilotStatus}" /></div>
            </div>
            </c:if>
            <c:forEach items="${scorecardTemplates}" var="scorecard" varStatus="index">
                <div class="item">
                    <div class="head"><or:text key='ScorecardType.${fn:replace(scorecard.scorecardType.name, " ", "")}.scorecard' />: </div>
                    <div class="body"><a href="${requestScope.scorecardLinks[index.index]}">
                        ${orfn:htmlEncode(scorecard.name)}
                        <or:text key="global.version.shortened"/>${orfn:htmlEncode(scorecard.version)}
                        </a></div>
                </div>
            </c:forEach>
            <c:if test="${isAdmin}">
                <div class="item">
                    <div class="head"><or:text key="viewProjectDetails.BillingProject" /> </div>
                    <div class="body">${billingProject}</div>
                </div>
            </c:if>
            <c:if test="${not empty requestScope.cockpitProject and requestScope.isAllowedToViewCockpitProjectName}">
                <div class="item">
                    <div class="head"><or:text key="viewProjectDetails.CockpitProject" /></div>
                    <div class="body"><a href="${requestScope.cockpitProjectLink}">${requestScope.cockpitProject}</a></div>
                </div>
            </c:if>
            <div class="item">
                <div class="head"><or:text key="viewProjectDetails.ProjectStatus" /></div>
                <div class="body">${projectStatus}</div>
            </div>
        </div>
    </div>
</div>
