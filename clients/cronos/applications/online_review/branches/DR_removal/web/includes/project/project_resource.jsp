<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2011 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the content of resources on Project Details screen.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="resourceTabs" value="<%=com.cronos.onlinereview.util.ConfigHelper.getResourceTabs()%>" />
<c:if test="${isAllowedToViewResources}">
    <div>
        <ul id="tablist">
            <li class="current"><a onclick="return activateResourcesTab([], this)" href="javascript:void(0)">All</a></li>
            <c:forEach items="${resourceTabs}" var="resTab">
            <li><a onclick="return activateResourcesTab(${resTab.value}, this)" href="javascript:void(0)">${resTab.key}</a></li>
            </c:forEach>
        </ul>
        <div style="clear:both;"></div>
    </div>

        <table id="resources" class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
        <tr>
            <td class="title" colspan='${(isAllowedToViewAllPayment) ? "5" : "4"}'><or:text key="viewProjectDetails.box.Resources" /></td>
        </tr>
        <tr>
            <td class="header"><or:text key="viewProjectDetails.Resource.Role" /></td>
            <td class="header"><or:text key="viewProjectDetails.Resource.Handle" /></td>
            <td class="header"><or:text key="viewProjectDetails.Resource.Email" /></td>
            <c:if test="${isAllowedToViewAllPayment}">
                <td class="header" nowrap="nowrap"><or:text key="viewProjectDetails.Resource.Payment" /></td>
            </c:if>
            <td class="headerC" nowrap="nowrap"><or:text key="viewProjectDetails.Resource.RegistrationDate" /></td>
        </tr>
        <c:forEach items="${resources}" var="resource" varStatus="idxrResource">
            <tr class='${(idxrResource.index % 2 == 0) ? "light" : "dark"}' rel="${resource.resourceRole.id}">
                <td class="value" nowrap="nowrap"><or:text key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}.bold' /></td>
                <td class="value" nowrap="nowrap">
                    <tc-webtag:handle coderId="${users[idxrResource.index].id}" context="${orfn:getHandlerContext(pageContext.request)}" />
                </td>
                                <td class="value" nowrap="nowrap"> <a href="mailto:${users[idxrResource.index].email}">${users[idxrResource.index].email}</a> </td>
                <c:if test="${isAllowedToViewAllPayment}">
                    <c:choose>
                        <c:when test='${not empty resourcePaymentsAmount[resource.id]}'>
                            <td class="value" nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, resourcePaymentsAmount[resource.id])}</td>
                        </c:when>
                        <c:otherwise>
                            <td class="value" nowrap="nowrap"><or:text key="NotAvailable" /></td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <td class="valueC" nowrap="nowrap">
                    <fmt:parseDate pattern="MM.dd.yyyy hh:mm a" parseLocale="en_US" value="${resource.allProperties['Registration Date']}"
                    var="registrationDate"/>
                    <fmt:formatDate pattern="MM.dd.yyyy HH:mm z" value="${registrationDate}"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td class="lastRowTD" colspan='${(isAllowedToViewAllPayment) ? "5" : "4"}'><!-- @ --></td>
        </tr>
    </table><br />

    <script language="JavaScript" type="text/javascript">
    <!--
        /*
         * Checks whether an array contains a specified element.
         */
        function contains(arr, val) {
            for (var i = 0; i < arr.length; i++) {
                if (val == arr[i]) {
                    return true;
                }
            }
            return false;
        }
        /*
         * This function will deactivate the previously active tab (if there was any),
         * and activate the new one. Also it will filter the resources based on the allowed roles.
         */
        function activateResourcesTab(resourceIds, aObject) {
            var liEles = aObject.parentNode.parentNode.getElementsByTagName("li");
            for (var i = 0; i < liEles.length; i++) {
                liEles[i].className = "";
            }
            aObject.parentNode.className = "current";
            // Remove focus from the link that triggered the activation
            if (aObject.blur) {
                aObject.blur();
            }

            var trs = document.getElementById("resources").getElementsByTagName("tr");
            var tot = 0;
            for (var i = 0; i < trs.length; i++) {
                var rid = trs[i].getAttribute("rel");
                if (rid) {
                    if (resourceIds.length > 0 && !contains(resourceIds, parseInt(rid))) {
                        trs[i].style.display = "none";
                    } else {
                        trs[i].style.display = "";
                        trs[i].className = (tot % 2 == 0) ? "light" : "dark";
                        tot++;
                    }
                }
            }
            return false;
        }
    //-->
    </script>
</c:if>
