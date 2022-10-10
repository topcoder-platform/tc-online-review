<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<div class="mainTabs">
    <div class="mainTabs__inner">
        <div class="mainTabs__item">
        <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
            <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 1)}">
                <strong>My Open Projects</strong>
            </c:if>
            <c:if test="${(empty projectTabIndex) or (projectTabIndex != 1)}">
                <a href="<or:url value='/actions/ListProjects?scope=my' />">
                    My Open Projects
                </a>
            </c:if>
        </c:if>
        </div>

        <div class="mainTabs__item">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 2)}">
            <strong>All Open Projects</strong>
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 2)}">
            <a href="<or:url value='/actions/ListProjects?scope=all' />">
                All Open Projects
            </a>
        </c:if>
        </div>

        <div class="mainTabs__item">
        <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
            <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 5)}">
                <strong>Late Deliverables</strong>
            </c:if>
            <c:if test="${(empty projectTabIndex) or (projectTabIndex != 5)}">
                <a href="<or:url value='/actions/ViewLateDeliverables' />">
                    Late Deliverables
                </a>
            </c:if>
        </c:if>
        </div>
    </div>
</div>
