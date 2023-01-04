<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project links edition page.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css" />
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_edit_project_links.js"><!-- @ --></script>

    <script type="text/javascript">
        function updateForumLink(projectId) {
            return fetch("<%=com.cronos.onlinereview.util.ConfigHelper.getChallengeByLegacyIdUrlV5()%>" + projectId)
                .then((response) => response.json())
                .then((data) => {
                    let with_forum = data.filter(item => 'discussions' in item);
                    let id = with_forum?.[0]?.id;
                    if (id !== undefined) {
                        let forumLinkEl = document.querySelector('.projectInfo__forumLink');
                        return forumLinkEl.href = "https://<%=ApplicationServer.FORUMS_SERVER_NAME%>/categories/" + id;
                    }
                });
        }
        document.addEventListener("DOMContentLoaded", function(){
            let projectId = ${project.id};
            updateForumLink(projectId);

            for (const dropdown of document.querySelectorAll(".custom-select-wrapper")) {
                dropdown.addEventListener('click', function () {
                    this.querySelector('.custom-select').classList.toggle('open');
                });
            }

            for (const options of document.querySelectorAll('.custom-options')) {
                let selected = options.querySelector('.custom-option[selected]');
                if (!selected) {
                    let option = options.querySelector('.custom-option');
                    option.selected = true;
                    option.classList.add('selected');
                } else {
                    selected.classList.add('selected');
                }
            }

            for (const option of document.querySelectorAll(".custom-option")) {
                const input = option.closest('.editProjectLink__input').querySelector('input');
                const selectedSpan = option.closest('.custom-select').querySelector('.custom-select__trigger span');
                if (option.classList.contains('selected')) {
                    const currentSelected = option.textContent;
                    if (selectedSpan.textContent == '') {
                        selectedSpan.textContent = currentSelected;
                    }
                    input.value = option.getAttribute('data-value');
                }
                option.addEventListener('click', function (e) {
                    if (!this.classList.contains('selected')) {
                        this.parentNode.querySelector('.custom-option.selected').classList.remove('selected');
                        this.classList.add('selected');
                        selectedSpan.textContent = this.textContent;
                        input.value = this.getAttribute('data-value');
                    }
                });
            }

            window.addEventListener('click', function (e) {
                for (const select of document.querySelectorAll('.custom-select')) {
                    if (!select.contains(e.target)) {
                        select.classList.remove('open');
                    }
                }
            });
        });
    </script>
    <script language="JavaScript" type="text/javascript">
        /**
         * Initializes some parameters.
         */
        function initParameters() {
             // Initiates the lastLinkIndex
           lastLinkIndex = ${fn:length(projectLinkForm.map['link_dest_id']) - 1};

           // Initiates all possible options
           projectOptions = new Array();
           // Initiates option values
           projectOptions.push(new Option('${orfn:getMessage(pageContext, "editProjectLinks.projectTypes.SelectProject")}','-1'));
         <c:forEach items="${allProjects}" var="projectElement">
         <c:set var="str" value="${projectElement.allProperties[\"Project Name\"]} v${projectElement.allProperties[\"Project Version\"]} (${projectElement.projectCategory.name}) (${projectElement.projectStatus.name})"/>
         <c:set var="repl" value="${fn:replace(str, \"'\", \"\")}"/>
            <c:if test="${projectElement.id ne project.id}">
                projectOptions.push(new Option('${repl}','${projectElement.id}'));
            </c:if>
         </c:forEach>

      }

      /**
       * Callback function. It is called after page loading.
       */
        function onLoad() {
               initParameters();

             // reset drop downs
             resetDropDowns();
        }
    </script>
        <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
            let avatar = document.querySelector('.webHeader__avatar a');
            let avatarImage = document.createElement('div');
            avatarImage.className = "webHeader__avatarImage";
            let twoChar = avatar.text.substring(0, 2);
            avatarImage.innerText = twoChar;
            avatar.innerHTML = avatarImage.outerHTML;
        });
    </script>

</head>
<body onload="onLoad();">
    <jsp:include page="/includes/inc_header_reskin.jsp" />
    <jsp:include page="/includes/project/project_tabs_reskin.jsp" />

    <div class="content">
        <div class="content__inner">
            <div class="editProjectLink__header">
                <button type="button" class="back-btn edit-back-btn" onclick="history.back()">
                    <i class="arrow-prev-icon"></i>
                </button>
    <div class="content content">
        <div class="content__inner">
            <div class="editProjectLink__header">
                <button type="button" class="back-btn edit-back-btn" onclick="history.back()">
                    <i class="arrow-prev-icon"></i>
                </button>

                <jsp:include page="/includes/project/project_info_reskin.jsp" />
            </div>
            <div class="divider"></div>
            <div class="editProjectLink">
                <div class="editProjectLink__sectionHeader">
                    <div class="editProjectLink__title">
                        <or:text key="editProjectLinks.box.editLinks" />
                    </div>
                </div>
                <div class="projectDetails__sectionBody">
                    <div id="tabcontentcontainer">
                        <s:form action="SaveProjectLinks" onsubmit="return validate_form(this, true);" namespace="/actions" style="margin-bottom: 0;">
                        <input type="hidden" name="pid"  value="<or:fieldvalue field='pid' />" />

                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table cellpadding="0" cellspacing="0" border="0">
                            <tr><td width="16"><!-- @ --></td><td><!-- @ --></td></tr>
                            <tr>
                                <td colspan="2"><span style="color:red;"><or:text key="Error.saveReview.ValidationFailed" /></span></td>
                            </tr>
                            <s:actionerror escape="false" />
                            </table><br />
                        </c:if>
                            <div id="tabNewLinks">
                                <table width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;" id="newLinks" class="projectLinkTable">
                                    <thead class="projectLinkTable__header">
                                        <tr>
                                            <th><or:text key="editProjectLinks.editLink.LinkType" /></th>
                                            <th><or:text key="editProjectLinks.editLink.SelectProject" /></th>

                                            <th><or:text key="editProjectLinks.editLink.Operation" /></th>
                                        </tr>
                                    </thead>
                                    <tbody class="projectLinkTable__body">
                                    <c:forEach var="linkIdx" varStatus="linkStatus" begin="0" end="${fn:length(projectLinkForm.map['link_dest_id']) - 1}">
                                    <tr class='${(linkStatus.index % 2 == 0) ? "light" : "dark"}'>
                                        <td width="18%">
                                            <div class="editProjectLink__input">
                                                <input type="hidden" name="link_type_id[${linkIdx}]">
                                                <div class="custom-select-wrapper">
                                                    <div class="custom-select grey">
                                                        <div class="custom-select__trigger greyText"><span><or:text key="editProjectLinks.projectTypes.SelectType" /></span>
                                                            <div class="arrow"></div>
                                                        </div>
                                                        <div class="custom-options">
                                                            <c:set var="OR_FIELD_TO_SELECT" value="link_type_id[${linkIdx}]"/>
                                                            <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="editProjectLinks.projectTypes.SelectType" def=""/></span>
                                                            <c:forEach items="${projectLinkTypes}" var="projectLinkType">
                                                                <span class="custom-option custom-option-grey" data-value="${projectLinkType.id}" <or:selected value="${projectLinkType.id}"/>>${projectLinkType.name}</span>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div name="project_link_validation_msg" class="error" style="display:none"></div>
                                        </td>
                                        <td nowrap="nowrap" width="73%">
                                            <div class="editProjectLink__input">
                                                <input type="hidden" name="link_dest_id[${linkIdx}]">
                                                <div class="custom-select-wrapper">
                                                    <div class="custom-select grey">
                                                        <div class="custom-select__trigger greyText"><span><or:text key="editProjectLinks.projectTypes.SelectType" /></span>
                                                            <div class="arrow"></div>
                                                        </div>
                                                        <div class="custom-options">
                                                            <c:set var="OR_FIELD_TO_SELECT" value="link_dest_id[${linkIdx}]"/>
                                                            <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="editProjectLinks.projectTypes.SelectType" def=""/></span>
                                                            <c:forEach items="${allProjects}" var="projectElement">
                                                                <c:if test="${projectElement.id ne project.id}">
                                                                    <span class="custom-option custom-option-grey" data-value="${projectElement.id}" <or:selected value="${projectElement.id}"/>>${projectElement.allProperties["Project Name"]} v${projectElement.allProperties["Project Version"]} (${projectElement.projectCategory.name})</span>
                                                                </c:if>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div name="project_link_validation_msg" class="error" style="display:none"></div>
                                        </td>

                                        <td nowrap="nowrap">
                                            <c:if test="${linkIdx eq 0}">
                                                <a class="editProjectLink__add" onclick="javascript:newProjectLink();"><or:text key='editProjectLinks.btnAdd.alt' /></a>
                                            </c:if>
                                            <img src="<or:text key='editProjectLinks.btnDelete.img' />"
                                                    style="cursor:hand;${(linkIdx eq 0) ? 'display: none;' : ''}" border="0"
                                                    onclick="deleteProjectLink(this.parentNode.parentNode);"
                                                    alt="<or:text key='editProjectLinks.btnDelete.alt' />" />
                                            <input type="hidden" name="link_action[${linkIdx}]"  value="<or:fieldvalue field='link_action[${linkIdx}]' />" />
                                        </td>
                                    </tr>
                                </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </s:form>
                    </div>
                </div>
            </div>
            <div class="saveChanges__button">
                <button id="saveChanges" form="SaveProjectLinks" value="Submit" class="saveChanges__save"><or:text key='btnSaveChanges.alt' /></button>
                <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />" class="saveChanges__cancel"><or:text key='btnCancel.alt' /></a>
            </div>
        </div>
    </div>
    <jsp:include page="/includes/inc_footer_reskin.jsp" />

</body>
<script type="text/javascript">
    var editLinkForm = document.querySelector('form');
    editLinkForm.addEventListener('submit', function() {
        document.getElementById("saveChanges").disabled = true;
    }, false);
</script>
</html>
