<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="resourceTabs" value="<%=com.cronos.onlinereview.util.ConfigHelper.getResourceTabs()%>" />

<c:if test="${isAllowedToViewResources}">
    <div class="projectDetails">
        <div class="projectDetails__sectionHeader">
            <div class="projectDetails__title">
                <or:text key="viewProjectDetails.box.Resources" />
            </div>
            <div class="projectDetails__accordion">
            </div>
        </div>

        <div class="projectDetails__sectionBody">
            <div class="projectDetails__tabs">
                <div class="projectDetails__tab projectDetails__tab--active"><a onclick="return activateResourcesTab([], this)" href="javascript:void(0)">All</a></div>
                <c:forEach items="${resourceTabs}" var="resTab">
                <div class="projectDetails__tab"><a onclick="return activateResourcesTab(${resTab.value}, this)" href="javascript:void(0)">${resTab.key}</a></div>
                </c:forEach>
            </div>

            <table class="resourcesTable" id="resources" width="100%" cellpadding="0" cellspacing="0">
                <thead class="resourcesTable__header">
                    <tr>
                        <th><or:text key="viewProjectDetails.Resource.Role" /></th>
                        <th><or:text key="viewProjectDetails.Resource.Handle" /></th>
                        <th><or:text key="viewProjectDetails.Resource.Email" /></th>
                        <c:if test="${isAllowedToViewAllPayment}">
                            <th><or:text key="viewProjectDetails.Resource.Payment" /></th>
                        </c:if>
                        <th><or:text key="viewProjectDetails.Resource.RegistrationDate" /></th>
                    </tr>
                </thead>
                <tbody class="resourcesTable__body">
                    <tr>
                        <td class="resourcesTable__empty" colspan="5" style="display:none;"></td>
                    </tr>
                <c:forEach items="${resources}" var="resource" varStatus="idxrResource">
                    <tr rel="${resource.resourceRole.id}">
                        <td nowrap="nowrap"><or:text key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' /></td>
                        <td nowrap="nowrap">
                            <tc-webtag:handle coderId="${users[idxrResource.index].id}" context="${orfn:getHandlerContext(pageContext.request)}" />
                        </td>
                                        <td nowrap="nowrap"> <a href="mailto:${users[idxrResource.index].email}">${users[idxrResource.index].email}</a> </td>
                        <c:if test="${isAllowedToViewAllPayment}">
                            <c:choose>
                                <c:when test='${not empty resourcePaymentsAmount[resource.id]}'>
                                    <td nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, resourcePaymentsAmount[resource.id])}</td>
                                </c:when>
                                <c:otherwise>
                                    <td nowrap="nowrap"><or:text key="NotAvailable" /></td>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <td nowrap="nowrap">
                            <fmt:parseDate pattern="MM.dd.yyyy hh:mm a" parseLocale="en_US" value="${resource.allProperties['Registration Date']}"
                            var="registrationDate"/>
                            <fmt:formatDate pattern="MM.dd.yyyy HH:mm z" value="${registrationDate}"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

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
            var liEles = aObject.parentNode.parentNode.getElementsByTagName("div");
            for (var i = 0; i < liEles.length; i++) {
                liEles[i].className = "projectDetails__tab";
            }
            aObject.parentNode.className = "projectDetails__tab projectDetails__tab--active";
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
                        tot++;
                    }
                }
            }

            let empty = document.getElementById("resources").querySelector('.resourcesTable__empty');
            if (tot) {
                empty.style.display = "none";
            } else {
                empty.style.display = "";
            }

            return false;
        }
    //-->
    </script>
</c:if>
