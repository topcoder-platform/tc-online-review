<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<div class="projectDetails">
    <div class="projectDetails__sectionHeader">
        <div class="projectDetails__title">
            <or:text key="viewProjectDetails.box.MyInfo" />
        </div>
        <div class="projectDetails__accordion">
        </div>
    </div>

    <div class="projectDetails__sectionBody">
        <table class="myInfoTable" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
            <thead class="myInfoTable__header">
                <tr>
                    <th><or:text key="viewProjectDetails.Role" /></th>
                    <th><or:text key="viewProjectDetails.box.OutstandingDeliverabes" /></th>
                </tr>
            </thead>
            <tbody class="myInfoTable__body">
                <tr>
                    <td align="left">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tbody class="roles__info">
                                <c:forEach items="${requestScope.myPayment}" var="rolePayment">
                                    <c:set var="roleName" value="${rolePayment.key.name}"/>
                                    <tr>
                                        <td nowrap="nowrap">
                                            <c:out value="${roleName}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </td>

                    <td nowrap="nowrap">
                        <c:if test="${not empty outstandingDeliverables}">
                            <c:forEach items="${outstandingDeliverables}" var="deliverable" varStatus="deliverableStatus">
                                <div class="myInfoTable__deliverableItem">
                                    <c:set var="devrStatus" value="${outstandingDeliverableStatuses[deliverableStatus.index]}" />
                                    <c:choose>
                                        <c:when test="${deliverable.complete}">
                                            <img src="/i/or/icon_authorization.gif" alt="<or:text key='global.Completed' />" class="Outline" />
                                        </c:when>
                                        <c:when test="${devrStatus == 1}">
                                            <img src="/i/or/icon_notification.gif" alt="<or:text key='global.DeadlineNear' />" class="Outline" />
                                        </c:when>
                                        <c:when test="${devrStatus == 2}">
                                            <img src="/i/or/icon_warning.gif" alt="<or:text key='global.Late' />" class="Outline" />
                                        </c:when>
                                    </c:choose>
                                    ${orfn:displayDate(pageContext.request, outstandingDeliverableDates[deliverableStatus.index])}
                                    <c:if test="${not empty outstandingDeliverableUserIds[deliverableStatus.index]}">
                                        <tc-webtag:handle coderId="${outstandingDeliverableUserIds[deliverableStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" />:
                                    </c:if>
                                    <or:text key='Deliverable.${fn:replace(deliverable.name, " ", "")}' />
                                    <c:if test="${not empty deliverable.submission}">
                                        ${deliverable.submission}
                                        <c:if test="${(isManager) && (not empty outstandingDeliverableSubmissionUserIds[deliverableStatus.index])}">
                                            (<tc-webtag:handle coderId="${outstandingDeliverableSubmissionUserIds[deliverableStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" />)
                                    </c:if>
                                    </c:if>
                            </div>
                            </c:forEach>
                        </c:if>

                        <c:if test="${(empty outstandingDeliverables) and (not unrespondedLateDeliverables)}">
                            <or:text key="viewProjectDetails.NoOutstandingDeliverables" />
                        </c:if>

                        <c:if test="${unrespondedLateDeliverables}">
                        There are unresponded late deliverables! See the list <a href="<or:url value='/actions/${unrespondedLateDeliverablesLink}' />">here</a>.
                        </c:if>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
