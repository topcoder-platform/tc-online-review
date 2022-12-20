<%@page import="com.topcoder.onlinereview.component.webcommon.ApplicationServer"%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="submBoxIdx" value="0" />

<div class="projectDetails">
    <c:if test="${!param.showTitle}">
        <div class="projectDetails__sectionHeader">
                <div class="projectDetails__title">
                    Phases
                </div>
                <div class="projectDetails__accordion">
                </div>
        </div>
    </c:if>
    <div class="projectDetails__sectionBody">
    <a name="tabs"></a>
    <div id="tabcontentcontainer">
        <c:forEach items="${phaseGroups}" var="group" varStatus="groupStatus">
            <div id="sc${groupStatus.index + 1}" style='display:${(groupStatus.index == activeTabIdx) ? "block" : "none"};'>
                <div class="projectDetails__tabs">
                    <c:forEach items="${phaseGroups}" var="innerGroup" varStatus="innerGroupStatus">
                      <c:if test="${innerGroup.visible == true}">
                        <div ${(groupStatus.index == innerGroupStatus.index) ? "class='projectDetails__tab projectDetails__tab--active'" : "class='projectDetails__tab'"}><a href="javascript:void(0)"
                            onClick="return activateTab('sc${innerGroupStatus.index + 1}', this)">${innerGroup.name}</a></div>
                      </c:if>
                    </c:forEach>
                </div>
                <c:if test="${group.phaseOpen != true}">
                    <table class="phasesTable" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                        <tbody class="phasesTable__body phasesTable__body--center">
                            <tr>
                                <td nowrap="nowrap"><or:text key="viewProjectDetails.Tabs.PhaseNotOpen" /></td>
                            </tr>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${group.phaseOpen == true}">
                    <c:choose>
                        <c:when test='${group.appFunc == "VIEW_REGISTRANTS"}'>
                            <table class="phasesTable phasesTable__registrants" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th><or:text key="viewProjectDetails.box.Registration.Handle" /></th>
                                        <th><or:text key="viewProjectDetails.box.Registration.Email" /></th>
                                        <th><or:text key="viewProjectDetails.box.Registration.Reliability" /></th>
                                        <th><or:text key="viewProjectDetails.box.Registration.Rating" /></th>
                                        <th><or:text key="viewProjectDetails.box.Registration.RegistrationDate" /></th>
                                    </tr>
                                </thead>
                                <tbody  class="phasesTable__body">
                                    <c:if test="${empty group.submitters}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="5"></td>
                                        </tr>
                                    </c:if>
                                    <c:forEach items="${group.submitters}" var="resource" varStatus="resourceStatus">
                                        <c:set var="registrantEmail" value="${group.registrantsEmails[resourceStatus.index]}" />
                                        <tr>
                                            <td nowrap="nowrap"><tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" /></td>
                                            <c:if test="${not empty registrantEmail}">
                                                <td nowrap="nowrap"><a href="mailto:${registrantEmail}">${registrantEmail}</a></td>
                                            </c:if>
                                            <c:if test="${empty registrantEmail}">
                                                <td nowrap="nowrap"><or:text key="viewProjectDetails.box.Registration.EmailUnknown" /></td>
                                            </c:if>
                                            <c:set var="reliability" value='${resource.allProperties["Reliability"]}' />
                                            <c:if test="${empty reliability}">
                                                <td nowrap="nowrap"><or:text key="NotAvailable" /></td>
                                            </c:if>
                                            <c:if test="${!(empty reliability)}">
                                                <td nowrap="nowrap">${reliability}</td>
                                            </c:if>
                                            <c:set var="rating" value='${resource.allProperties["Rating"]}' />
                                            <c:choose>
                                                <c:when test="${empty rating}">
                                                    <c:set var="ratingColor" value="coderTextBlack" />
                                                </c:when>
                                            </c:choose>
                                            <c:if test="${empty rating}">
                                                <td nowrap="nowrap"><or:text key="NotRated" /></td>
                                            </c:if>
                                            <c:if test="${!(empty rating)}">
                                                <td nowrap="nowrap"><span class="${ratingColor}">${rating}</span></td>
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
                        </c:when>
                        <c:when test='${group.appFunc == "VIEW_SUBMISSIONS"}'>
                            <table id="Submissions${submBoxIdx}" class="phasesTable phasesTable__submissions" cellpadding="0" cellspacing="0" border="0">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th colspan="2" nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.Date" arg0="${group.groupIndex}" /></th>
                                        <c:if test="${isThurgood}">
                                            <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.Thurgood" /></th>
                                        </c:if>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.Screener" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ScreeningScore" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ScreeningResult" arg0="${group.groupIndex}" /></th>
                                    </tr>
                                </thead>
                                <tbody class="phasesTable__body">
                                    <c:if test="${empty group.submissions}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="<c:out value="${isThurgood ? 7: 6}"/>"></td>
                                        </tr>
                                    </c:if>
                                    <c:set var="prevSubm" value="${group.pastSubmissions}" />
                                    <c:set var="prevSubmissions" value="" />
                                    <c:set var="submissionIdx" value="0" />
                                    <c:forEach items="${group.submissions}" var="submission" varStatus="submissionStatus">
                                        <c:set var="underIterativeReview" value="false"/>
                                        <c:forEach items="${phaseGroups}" var="grp">
                                            <c:if test='${grp.appFunc == "ITERATIVEREVIEW" and grp.iterativeReviewSubmission.id eq submission.id}'>
                                                <c:set var="underIterativeReview" value="true"/>
                                            </c:if>
                                        </c:forEach>
                                        <c:set var="submitter" value="" />
                                        <c:set var="submissionStatusName" value="${submission.submissionStatus.name}" />
                                        <c:forEach items="${group.submitters}" var="curSubmitter">
                                            <c:if test="${curSubmitter.id == submission.upload.owner}">
                                                <c:set var="submitter" value="${curSubmitter}" />
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${not empty prevSubm}">
                                            <c:set var="prevSubmissions" value="${prevSubm[submissionStatus.index]}" />
                                        </c:if>
                                        <c:if test='${(submissionStatusName != "Deleted")}'>
                                            <tr>
                                            <td nowrap="nowrap">
                                            <c:set var="placement" value="" />
                                            <c:if test="${not empty submission}">
                                                <c:set var="placement" value='${submission.placement}' />
                                            </c:if>
                                            <c:set var="failedReview" value="${(submissionStatusName == 'Failed Screening') or (submissionStatusName == 'Failed Review')}" />
                                            <c:if test="${(not empty placement) and (not failedReview)}">
                                                <c:choose>
                                                    <c:when test="${placement == 1}">
                                                        <span class="phasesTable__placements phasesTable__placements--first">1st</span>
                                                    </c:when>
                                                    <c:when test="${placement == 2}">
                                                        <span class="phasesTable__placements phasesTable__placements--second">2nd</span>
                                                    </c:when>
                                                    <c:when test="${placement == 3}">
                                                        <span class="phasesTable__placements phasesTable__placements--third">3rd</span>
                                                    </c:when>
                                                    <c:when test="${placement == 4}">
                                                        <span class="phasesTable__placements phasesTable__placements--forth">4th</span>
                                                    </c:when>
                                                    <c:when test="${placement == 5}">
                                                        <span class="phasesTable__placements phasesTable__placements--fifth">5th</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="phasesTable__placements">&nbsp;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <c:if test="${failedReview}">
                                                <c:set var="failureKeyName" value='SubmissionStatus.${fn:replace(submissionStatusName, " ", "")}' />
                                                <c:if test="${empty placement}">
                                                    <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="<or:text key='${failureKeyName}' />"></span>
                                                </c:if>
                                                <c:if test="${not empty placement}">
                                                    <c:set var="placeStr" value="${orfn:getMessage(pageContext, failureKeyName)} (Place ${placement})" />
                                                    <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="${placeStr}"></span>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${not downloadCurrentIterativeReview || underIterativeReview}">
                                                <c:if test="${project.projectCategory.projectType.id ne 3}">
                                                    <c:if test="${group.readyToDownload[submissionStatus.index]}">
                                                        <a href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                    <c:if test="${not group.readyToDownload[submissionStatus.index]}">
                                                        <a style="pointer-events:none" href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${project.projectCategory.projectType.id eq 3}">
                                                    <c:if test="${group.readyToDownload[submissionStatus.index]}">
                                                        <a href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                    <c:if test="${not group.readyToDownload[submissionStatus.index]}">
                                                        <a style="pointer-events:none" href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${not empty submitter}">
                                                    (<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                </c:if>
                                            </c:if>
                                            <c:if test="${downloadCurrentIterativeReview && not underIterativeReview}">
                                                ${submission.id}
                                                <c:if test="${not empty submitter}">
                                                    (<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                </c:if>
                                            </c:if>
                                            </td>
                                            <td><!-- @ --></td>
                                            <td>${orfn:displayDate(pageContext.request, submission.upload.creationTimestamp)}</td>
                                            <c:if test="${isThurgood}">
                                                <td>
                                                <c:if test="${not empty submission.thurgoodJobId}">
                                                    <a href="${fn:replace(thurgoodBaseUIURL, '{job_id}', submission.thurgoodJobId)}"><or:text key="viewProjectDetails.box.Submission.ThurgoodLink" /></a>
                                                </c:if>
                                                <c:if test="${empty submission.thurgoodJobId}">
                                                    <or:text key="viewProjectDetails.box.Submission.ThurgoodNotAvailable" />
                                                </c:if>
                                                </td>
                                            </c:if>
                                            <c:set var="screener" value="" />
                                            <c:forEach items="${group.reviewers}" var="reviewer">
                                                <c:if test="${(empty reviewer.submissions) and (empty screener)}">
                                                    <c:set var="screener" value="${reviewer}" />
                                                </c:if>
                                                <c:if test="${(not empty reviewer.submissions) and (reviewer.submissions[0] == submission.id)}">
                                                    <c:set var="screener" value="${reviewer}" />
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${empty screener}">
                                                <td><or:text key="viewProjectDetails.box.Screening.ScreenerNotAssigned" /></td>
                                            </c:if>
                                            <c:if test="${not empty screener}">
                                                <td><tc-webtag:handle coderId='${screener.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" /></td>
                                            </c:if>
                                            <c:set var="review" value="" />
                                            <c:forEach items="${group.screenings}" var="screening">
                                                <c:if test="${screening.submission == submission.id}">
                                                    <c:set var="review" value="${screening}" />
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${empty review}">
                                                <c:if test="${isAllowedToPerformScreening}">
                                                    <td nowrap="nowrap">
                                                    <b><a href="<or:url value='/actions/CreateScreening?sid=${submission.id}' />"><or:text
                                                        key="viewProjectDetails.box.Screening.Submit" /></a></b></td>
                                                </c:if>
                                                <c:if test="${not isAllowedToPerformScreening}">
                                                    <td><or:text key="Pending" /></td>
                                                </c:if>
                                                <td><or:text key="NotAvailable" /></td>
                                            </c:if>
                                            <c:if test="${not empty review}">
                                                <c:if test="${review.committed}">
                                                    <c:if test="${isAllowedToViewScreening}">
                                                        <td>
                                                        <a href="<or:url value='/actions/ViewScreening?rid=${review.id}' />">${orfn:displayScore(pageContext.request, review.score)}</a></td>
                                                    </c:if>
                                                    <c:if test="${not isAllowedToViewScreening}">
                                                        <td>${orfn:displayScore(pageContext.request, review.score)}</td>
                                                    </c:if>
                                                    <td>
                                                    <c:if test="${group.screeningPhaseStatus == 3}">
                                                        <c:if test="${submission.submissionStatus.name ne 'Failed Screening'}">
                                                            <or:text key="viewProjectDetails.box.Screening.Passed" />
                                                        </c:if>
                                                        <c:if test="${submission.submissionStatus.name eq 'Failed Screening'}">
                                                            <or:text key="viewProjectDetails.box.Screening.Failed" />
                                                        </c:if>
                                                        <c:if test="${isAllowedToAdvanceSubmissionWithFailedScreening and submission.submissionStatus.name eq 'Failed Screening'}">
                                                            (<a href="<or:url value='/actions/AdvanceFailedScreeningSubmission?uid=${submission.upload.id}' />"><or:text key="viewProjectDetails.box.Screening.Advance" /></a>)
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${group.screeningPhaseStatus != 3}">
                                                        <or:text key="NotAvailable" />
                                                    </c:if>
                                                    </td>
                                                </c:if>
                                                <c:if test="${not review.committed}">
                                                    <c:if test="${isAllowedToPerformScreening}">
                                                        <td nowrap="nowrap">
                                                        <b><a href="<or:url value='/actions/EditScreening?rid=${review.id}' />"><or:text
                                                            key="viewProjectDetails.box.Screening.Submit" /></a></b></td>
                                                    </c:if>
                                                    <c:if test="${not isAllowedToPerformScreening}">
                                                        <td><or:text key="Pending" /></td>
                                                    </c:if>
                                                    <td><or:text key="NotAvailable" /></td>
                                                </c:if>
                                            </c:if>
                                            </tr>
                                            <c:set var="submissionIdx" value="${submissionIdx + 1}" />
                                        </c:if>
                                        <c:forEach items="${prevSubmissions}" var="pastSubmission" varStatus="pastSubmissionStatus">
                                            <tr id="PrevSubm${submBoxIdx}_${submissionStatus.index}" style="display:none;">
                                                <td colspan="2" nowrap="nowrap">
                                                        <img border="0" src="<or:text key='viewProjectDetails.box.Submission.icoShowMore.img' />" class="Outline" style="display:none;" />
                                                        <c:if test="${project.projectCategory.projectType.id ne 3}">
                                                            <a href="<or:url value='/actions/DownloadContestSubmission?uid=${pastSubmission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Previous.UploadID' />">${pastSubmission.id}</a>
                                                        </c:if>
                                                        <c:if test="${project.projectCategory.projectType.id eq 3}">
                                                            <a href="<or:url value='/actions/DownloadContestSubmission?uid=${pastSubmission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Previous.UploadID' />">${pastSubmission.id}</a>
                                                        </c:if>
                                                </td>
                                                <td>${orfn:displayDate(pageContext.request, pastSubmission.creationTimestamp)}</td>
                                                <c:if test="${isThurgood}">
                                                    <td><!-- @ -->
                                                    </td>
                                                </c:if>
                                                <td><!-- @ --></td>
                                                <td><!-- @ --></td>
                                                <td><!-- @ --></td>
                                            </tr>
                                        </c:forEach>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <c:if test="${(not empty prevSubm) && (submBoxIdx == 0)}">
<script language="JavaScript" type="text/javascript">
    function expandSubmissions(iBoxIdx, iLinkIdx, aObject) {
        return expcollSubmissions(iBoxIdx, iLinkIdx, aObject, "none", "inline", "");
    }
    function collapseSubmissions(iBoxIdx, iLinkIdx, aObject) {
        return expcollSubmissions(iBoxIdx, iLinkIdx, aObject, "inline", "none", "none");
    }

    function expcollSubmissions(iBoxIdx, iLinkIdx, aObject, strStyle1, strStyle2, strStyle3) {
        var strRowGroup = "PrevSubm" + iBoxIdx + "_" + iLinkIdx;
        var imgPlus = document.getElementById(strRowGroup + "_plus");
        var imgMinus = document.getElementById(strRowGroup + "_minus");
        var table = document.getElementById("Submissions" + iBoxIdx);

        if (imgPlus) {
            imgPlus.style.display = strStyle1;
        }
        if (imgMinus) {
            imgMinus.style.display = strStyle2;
        }
        if (table) {
            var rows = table.getElementsByTagName("tr");
            for (var i = 0; i < rows.length; ++i) {
                var row = table.rows[i];
                if (row.id == strRowGroup) {
                    row.style.display = strStyle3;
                }
            }
        }

        if (aObject.blur) {
            aObject.blur();
        }
        return false;
    }
</script>
                            </c:if>
                            <c:set var="submBoxIdx" value="${submBoxIdx + 1}" />
                        </c:when>
                        <c:when test='${(group.appFunc == "VIEW_REVIEWS") and (group.visible == true)}'>
                            <table class="phasesTable phasesTable__review" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <c:set var="colSpan" value="${(fn:length(group.reviewers) * 2) + 2}" />
                                <c:if test="${not isAllowedToEditHisReviews}">
                                    <c:set var="colSpan" value="${colSpan + 1}" />
                                </c:if>
                                <tr class="reviewers__section">
                                    <td class="value" colspan="${(isAllowedToEditHisReviews) ? 2 : 3}"><!-- @ --></td>
                                    <c:forEach items="${group.reviewers}" var="reviewer">
                                        <td class="reviewer" colspan="2" nowrap="nowrap">
                                            <or:text key='ResourceRole.${fn:replace(reviewer.resourceRole.name, " ", "")}' />:
                                            <tc-webtag:handle coderId='${reviewer.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />
                                            <c:set var="testCase" value="" />
                                            <c:forEach items="${group.testCases}" var="curTestCase">
                                                <c:if test="${curTestCase.owner == reviewer.id}">
                                                    <c:set var="testCase" value="${curTestCase}" />
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${not empty testCase}">
                                                <a href="<or:url value='/actions/DownloadTestCase?uid=${testCase.id}' />"
                                                    title="<or:text key='viewProjectDetails.box.Review.TestCase.hint' />"><or:text
                                                        key="viewProjectDetails.box.Review.TestCase" /></a>
                                                <c:if test="${isAllowedToUploadTC and group.uploadingTestcasesAllowed}">
                                                    [
                                                    <a href="<or:url value='/actions/UploadTestCase?pid=${project.id}' />"
                                                        title="<or:text key='viewProjectDetails.box.Review.TestCase.Update.hint' />"><or:text
                                                            key="viewProjectDetails.box.Review.TestCase.Update" /></a>
                                                    ]
                                                </c:if>
                                            </c:if>
                                            <c:if test="${(empty testCase) and isAllowedToUploadTC and group.uploadingTestcasesAllowed}"><%-- and group.uploadingTestcasesAllowed --%>
                                                <a href="<or:url value='/actions/UploadTestCase?pid=${project.id}' />"
                                                    title="<or:text key='viewProjectDetails.box.Review.TestCase.Upload.hint' />"><or:text
                                                        key="viewProjectDetails.box.Review.TestCase.Upload" /></a>
                                            </c:if>
                                        </td>
                                    </c:forEach>
                                </tr>
                                <tr class="phasesTable__header">
                                    <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                    <th><or:text key="viewProjectDetails.box.Review.Date" arg0="${group.groupIndex}" /></th>
                                    <c:if test="${isAllowedToEditHisReviews != true}">
                                        <th><or:text key="viewProjectDetails.box.Review.Score" arg0="${group.groupIndex}" /></th>
                                    </c:if>
                                    <c:forEach items="${group.reviewers}" var="reviewer">
                                        <th><or:text key="viewProjectDetails.box.Review.Score.short" /></th>
                                        <th><or:text key="viewProjectDetails.box.Review.Appeals" /></th>
                                    </c:forEach>
                                </tr>
                                <tbody class="phasesTable__body">
                                    <c:if test="${empty group.submissions}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="${colSpan}"></td>
                                        </tr>
                                    </c:if>
                                    <c:set var="submissionIdx" value="0" />
                                    <c:forEach items="${group.submissions}" var="submission" varStatus="submissionStatus">
                                        <c:set var="submissionStatusName" value="${submission.submissionStatus.name}" />
                                        <c:if test='${(submissionStatusName != "Failed Screening") && (submissionStatusName != "Deleted")}'>
                                            <tr>
                                                <c:set var="submitter" value="" />
                                                <c:forEach items="${group.submitters}" var="curSubmitter">
                                                    <c:if test="${curSubmitter.id == submission.upload.owner}">
                                                        <c:set var="submitter" value="${curSubmitter}" />
                                                    </c:if>
                                                </c:forEach>
                                                <td nowrap="nowrap">
                                                    <c:set var="placement" value="" />
                                                    <c:if test="${not empty submission}">
                                                        <c:set var="placement" value='${submission.placement}' />
                                                    </c:if>
                                                    <c:set var="failedReview" value="${(submissionStatusName == 'Failed Screening') or (submissionStatusName == 'Failed Review')}" />
                                                    <c:if test="${(not empty placement) and (not failedReview)}">
                                                        <c:choose>
                                                            <c:when test="${placement == 1}">
                                                                <span class="phasesTable__placements phasesTable__placements--first">1st</span>
                                                            </c:when>
                                                            <c:when test="${placement == 2}">
                                                                <span class="phasesTable__placements phasesTable__placements--second">2nd</span>
                                                            </c:when>
                                                            <c:when test="${placement == 3}">
                                                                <span class="phasesTable__placements phasesTable__placements--third">3rd</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="phasesTable__placements">&nbsp;</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                    <c:if test="${failedReview}">
                                                        <c:set var="failureKeyName" value='SubmissionStatus.${fn:replace(submissionStatusName, " ", "")}' />
                                                        <c:if test="${empty placement}">
                                                            <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="<or:text key='${failureKeyName}' />"></span>
                                                        </c:if>
                                                        <c:if test="${not empty placement}">
                                                            <c:set var="placeStr" value="${orfn:getMessage(pageContext, failureKeyName)} (Place ${placement})" />
                                                            <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="${placeStr}"></span>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${project.projectCategory.projectType.id ne 3}">
                                                        <c:if test="${group.readyToDownload[submissionStatus.index]}">
                                                        <a href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                        </c:if>
                                                        <c:if test="${not group.readyToDownload[submissionStatus.index]}">
                                                        <a style="pointer-events:none" href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${project.projectCategory.projectType.id eq 3}">
                                                        <c:if test="${group.readyToDownload[submissionStatus.index]}">
                                                        <a href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                        </c:if>
                                                        <c:if test="${not group.readyToDownload[submissionStatus.index]}">
                                                        <a style="pointer-events:none" href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${not empty submitter}">
                                                        (<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                    </c:if>
                                                </td>
                                                <td>${orfn:displayDate(pageContext.request, group.reviewDates[submissionStatus.index])}</td>
                                                <c:if test="${not isAllowedToEditHisReviews}">
                                                    <c:if test="${not empty submission}">
                                                        <c:set var="finalScore" value='${submission.finalScore}' />
                                                    </c:if>
                                                    <c:if test="${not empty finalScore}">
                                                        <td><a href="<or:url value='/actions/ViewCompositeScorecard?sid=${submission.id}' />">${orfn:displayScore(pageContext.request, finalScore)}</a></td>
                                                    </c:if>
                                                    <c:if test="${empty finalScore}">
                                                        <td><or:text key="Incomplete" /></td>
                                                    </c:if>
                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${group.appealsPhaseOpened}">
                                                        <c:set var="totalAppeals" value="${group.totalAppealsCounts[submissionStatus.index]}" />
                                                        <c:set var="unresolvedAppeals" value="${group.unresolvedAppealsCounts[submissionStatus.index]}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="totalAppeals" value="" />
                                                        <c:set var="unresolvedAppeals" value="" />
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:forEach items="${group.reviews[submissionStatus.index]}" var="review" varStatus="reviewStatus">
                                                    <c:if test="${(empty review) or (not group.displayReviewLinks)}">
                                                        <c:if test="${isAllowedToEditHisReviews && group.displayReviewLinks}">
                                                            <td nowrap="nowrap"><a href="<or:url value='/actions/CreateReview?sid=${submission.id}' />"><b><or:text
                                                                key="viewProjectDetails.box.Review.Submit" /></b></a></td>
                                                            <td><or:text key="NotAvailable" /></td>
                                                        </c:if>
                                                        <c:if test="${(not isAllowedToEditHisReviews) || (not group.displayReviewLinks)}">
                                                            <td><or:text key="NotAvailable" /></td>
                                                            <td><or:text key="NotAvailable" /></td>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${(not empty review) && group.displayReviewLinks}">
                                                        <c:if test="${review.committed}">
                                                            <td><a href="<or:url value='/actions/ViewReview?rid=${review.id}' />">${orfn:displayScore(pageContext.request, review.score)}</a></td>
                                                        </c:if>
                                                        <c:if test="${not review.committed}">
                                                            <c:if test="${isAllowedToEditHisReviews}">
                                                                <td><a href="<or:url value='/actions/EditReview?rid=${review.id}' />"><b><or:text
                                                                    key="viewProjectDetails.box.Review.Submit" /></b></a></td>
                                                            </c:if>
                                                            <c:if test="${not isAllowedToEditHisReviews}">
                                                                <td><or:text key="Pending" /></td>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${group.appealsPhaseOpened}">
                                                            <td nowrap="nowrap">[
                                                                <a href="<or:url value='/actions/ViewReview?rid=${review.id}' />"
                                                                    title="<or:text key='viewProjectDetails.box.Review.Appeals.Unresolved' />">${unresolvedAppeals[reviewStatus.index]}</a> /
                                                                <a href="<or:url value='/actions/ViewReview?rid=${review.id}' />"
                                                                    title="<or:text key='viewProjectDetails.box.Review.Appeals.Total' />">${totalAppeals[reviewStatus.index]}</a>
                                                            ]</td>
                                                        </c:if>
                                                        <c:if test="${not group.appealsPhaseOpened}">
                                                            <td><or:text key="NotAvailable" /></td>
                                                        </c:if>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            <c:set var="submissionIdx" value="${submissionIdx + 1}" />
                                        </c:if>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:when test='${group.appFunc == "AGGREGATION"}'>
                            <table class="phasesTable" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Aggregation.Date" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Aggregation.Aggregation" arg0="${group.groupIndex}" /></th>
                                    </tr>
                                </thead>
                                <tbody class="phasesTable__body">
                                    <c:set var="winningSubmission" value="" />
                                    <c:forEach items="${group.submissions}" var="submission">
                                        <c:if test="${(not empty group.winner) and (group.winner.id == submission.upload.owner)}">
                                            <c:set var="winningSubmission" value="${submission}" />
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty winningSubmission}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="3"></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${not empty winningSubmission}">
                                        <tr>
                                            <td nowrap="nowrap">
                                                <span class="phasesTable__placements phasesTable__placements--first">1st</span>
                                                <a href="<or:url value='/actions/DownloadContestSubmission?sid=${winningSubmission.id}' />"
                                                    title="<or:text key='viewProjectDetails.box.Submission.Download' />">${winningSubmission.id}</a>
                                                (<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                            </td>
                                            <c:if test="${not empty group.aggregation}">
                                                <c:if test="${group.aggregation.committed}">
                                                    <td nowrap="nowrap">${orfn:displayDate(pageContext.request, group.aggregation.modificationTimestamp)}</td>
                                                    <td nowrap="nowrap"><a href="<or:url value='/actions/ViewAggregation?rid=${group.aggregation.id}' />"><or:text
                                                        key="viewProjectDetails.box.Aggregation.ViewResults" /></a></td>
                                                </c:if>
                                                <c:if test="${not group.aggregation.committed}">
                                                    <td><!-- @ --></td>
                                                    <c:if test="${isAllowedToPerformAggregation}">
                                                        <td nowrap="nowrap"><a href="<or:url value='/actions/EditAggregation?rid=${group.aggregation.id}' />"><b><or:text
                                                            key="viewProjectDetails.box.Aggregation.Submit" /></b></a></td>
                                                    </c:if>
                                                    <c:if test="${not isAllowedToPerformAggregation}">
                                                        <td nowrap="nowrap"><or:text key="Pending" /></td>
                                                    </c:if>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${empty group.aggregation}">
                                                <td><!-- @ --></td>
                                                <td><or:text key="NotAvailable" /></td>
                                            </c:if>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </c:when>
                        <c:when test='${group.appFunc == "FINAL_FIX"}'>
                            <table class="phasesTable" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.FinalFix.Date" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.FinalFix.Fix" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.FinalReview.Date" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.FinalReview.Review" arg0="${group.groupIndex}" /></th>
                                    </tr>
                                </thead>
                                <tbody class="phasesTable__body">
                                    <c:set var="winningSubmission" value="" />
                                    <c:forEach items="${group.submissions}" var="submission">
                                        <c:if test="${(not empty group.winner) && (group.winner.id == submission.upload.owner)}">
                                            <c:set var="winningSubmission" value="${submission}" />
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty winningSubmission}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="6"></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${not empty winningSubmission}">
                                        <tr>
                                            <td nowrap="nowrap">
                                                <span class="phasesTable__placements phasesTable__placements--first">1st</span>
                                                <a href="<or:url value='/actions/DownloadContestSubmission?sid=${winningSubmission.id}' />"
                                                    title="<or:text key='viewProjectDetails.box.Submission.Download' />">${winningSubmission.id}</a>
                                                (<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                            </td>
                                            <c:if test="${not empty group.finalFix}">
                                                <td nowrap="nowrap">${orfn:displayDate(pageContext.request, group.finalFix.creationTimestamp)}</td>
                                                <td nowrap="nowrap">
                                                    <a href="<or:url value='/actions/DownloadFinalFix?uid=${group.finalFix.id}' />"
                                                        title="<or:text key='viewProjectDetails.box.FinalFix.Download.alt' />"><or:text
                                                        key="viewProjectDetails.box.FinalFix.Download" /></a>
                                                </td>
                                            </c:if>
                                            <c:if test="${empty group.finalFix}">
                                                <td><!-- @ --></td>
                                                <c:if test="${isAllowedToUploadFF}">
                                                    <td nowrap="nowrap">
                                                        <a href="${viewContestLink}"><or:text
                                                            key="viewProjectDetails.box.FinalFix.Upload" /></a></td>
                                                </c:if>
                                                <c:if test="${not isAllowedToUploadFF}">
                                                    <td><or:text key="Incomplete" /></td>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${not empty group.finalReview}">
                                                <c:if test="${group.finalReview.committed}">
                                                    <td nowrap="nowrap">${orfn:displayDate(pageContext.request, group.finalReview.modificationTimestamp)}</td>
                                                    <td nowrap="nowrap">
                                                        <a href="<or:url value='/actions/ViewFinalReview?rid=${group.finalReview.id}' />"><or:text
                                                            key="viewProjectDetails.box.FinalReview.ViewResults" /></a></td>
                                                </c:if>
                                                <c:if test="${not group.finalReview.committed}">
                                                    <td><!-- @ --></td>
                                                    <c:if test="${isAllowedToPerformFinalReview}">
                                                        <td nowrap="nowrap"><a href="<or:url value='/actions/EditFinalReview?rid=${group.finalReview.id}' />"><b><or:text
                                                            key="viewProjectDetails.box.FinalReview.Submit" /></b></a></td>
                                                    </c:if>
                                                    <c:if test="${not isAllowedToPerformFinalReview}">
                                                        <td nowrap="nowrap"><or:text key="Pending" /></td>
                                                    </c:if>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${empty group.finalReview}">
                                                <td><!-- @ --></td>
                                                <td nowrap="nowrap"><or:text key="NotAvailable" /></td>
                                            </c:if>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </c:when>
                        <c:when test='${group.appFunc == "APPROVAL"}'>
                            <table class="phasesTable" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.FinalFix.Date" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.FinalFix.Fix" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Approval.Reviewer" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Approval.Date" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Approval.Approval" arg0="${group.groupIndex}" /></th>
                                    </tr>
                                </thead>
                                <tbody class="phasesTable__body">
                                    <c:set var="winningSubmission" value="" />
                                    <c:forEach items="${group.submissions}" var="submission">
                                        <c:if test="${(not empty group.winner) and (group.winner.id == submission.upload.owner)}">
                                            <c:set var="winningSubmission" value="${submission}" />
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty winningSubmission}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="6"></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${not empty winningSubmission}">
                                        <c:forEach items="${group.approvalReviewers}" var="reviewer">
                                            <c:set var="isReviewerCurrentUser"
                                                   value="${reviewer.allProperties['External Reference ID'] eq orfn:getLoggedInUserId(pageContext.request)}"/>
                                            <c:set var="approval" value="${null}"/>
                                            <c:forEach items="${group.approval}" var="review">
                                                <c:if test="${review.author eq reviewer.id}">
                                                    <c:set var="approval" value="${review}"/>
                                                </c:if>
                                            </c:forEach>
                                            <tr>
                                                <td nowrap="nowrap">
                                                    <span class="phasesTable__placements phasesTable__placements--first">1st</span>
                                                    <a href="<or:url value='/actions/DownloadContestSubmission?sid=${winningSubmission.id}' />"
                                                            title="<or:text key='viewProjectDetails.box.Submission.Download' />">${winningSubmission.id}</a>
                                                    (<tc-webtag:handle
                                                        coderId='${group.winner.allProperties["External Reference ID"]}'
                                                        context="${orfn:getHandlerContext(pageContext.request)}"/>)
                                                </td>
                                                <td nowrap="nowrap">${orfn:displayDate(pageContext.request, group.finalFix.modificationTimestamp)}</td>
                                                <td nowrap="nowrap">
                                                    <c:if test="${not empty group.finalFix}">
                                                        <a href="<or:url value='/actions/DownloadFinalFix?uid=${group.finalFix.id}' />"
                                                            title="<or:text key='viewProjectDetails.box.FinalFix.Download.alt' />">
                                                                <or:text key="viewProjectDetails.box.FinalFix.Download"/>
                                                        </a>
                                                    </c:if>
                                                </td>
                                                <td nowrap="nowrap">
                                                    <tc-webtag:handle
                                                        coderId='${reviewer.allProperties["External Reference ID"]}'
                                                        context="${orfn:getHandlerContext(pageContext.request)}"/>
                                                </td>
                                                <c:choose>
                                                    <c:when test="${approval eq null}">
                                                        <td><!-- @ --></td>
                                                        <c:choose>
                                                            <c:when test="${isAllowedToPerformApproval and isReviewerCurrentUser and group.approvalPhaseStatus == 2}">
                                                                <td nowrap="nowrap">
                                                                    <a href="<or:url value='/actions/CreateApproval?sid=${winningSubmission.id}' />">
                                                                        <or:text key="viewProjectDetails.box.Approval.Submit"/></a>
                                                                </td>
                                                            </c:when>
                                                            <c:when test="${group.approvalPhaseStatus == 2}">
                                                                <td nowrap="nowrap">
                                                                    <or:text key="Pending"/></td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td nowrap="nowrap">
                                                                    <or:text key="NotAvailable"/></td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:when test="${approval.committed}">
                                                        <td
                                                            nowrap="nowrap">${orfn:displayDate(pageContext.request, approval.modificationTimestamp)}</td>
                                                        <td nowrap="nowrap">
                                                            <a href="<or:url value='/actions/ViewApproval?rid=${approval.id}' />"><or:text
                                                                    key="viewProjectDetails.box.Approval.ViewResults"/></a></td>
                                                    </c:when>
                                                    <c:when test="${not approval.committed}">
                                                        <td><!-- @ --></td>
                                                        <c:choose>
                                                            <c:when test="${isAllowedToPerformApproval and isReviewerCurrentUser and group.approvalPhaseStatus == 2}">
                                                                <td nowrap="nowrap">
                                                                <a href="<or:url value='/actions/EditApproval?rid=${approval.id}' />">
                                                                    <or:text key="viewProjectDetails.box.Approval.Submit"/></a>
                                                                </td>
                                                            </c:when>
                                                            <c:when test="${group.approvalPhaseStatus == 2}">
                                                                <td nowrap="nowrap">
                                                                    <or:text key="Pending"/></td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td nowrap="nowrap">
                                                                    <or:text key="NotAvailable"/></td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                </c:choose>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                        </c:when>
                        <c:when test='${group.appFunc == "POSTMORTEM"}'>
                            <table class="phasesTable" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Post-Mortem.Reviewer.ID" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Post-Mortem.Date" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Post-Mortem.Post-Mortem" arg0="${group.groupIndex}" /></th>
                                    </tr>
                                </thead>
                                <tbody class="phasesTable__body">
                                    <c:if test="${empty group.postMortemReviews}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="3"></td>
                                        </tr>
                                    </c:if>
                                    <c:forEach items="${group.postMortemReviewers}" var="reviewer" varStatus="index">
                                    <c:set var="isReviewerCurrentUser"
                                           value="${reviewer.allProperties['External Reference ID'] eq orfn:getLoggedInUserId(pageContext.request)}"/>
                                    <c:set var="review" value="${null}"/>
                                    <c:forEach items="${group.postMortemReviews}" var="postMortemReview">
                                        <c:if test="${postMortemReview.author eq reviewer.id}">
                                            <c:set var="review" value="${postMortemReview}"/>
                                        </c:if>
                                    </c:forEach>

                                    <tr>
                                        <td nowrap="nowrap">
                                            <tc-webtag:handle coderId='${reviewer.allProperties["External Reference ID"]}'
                                                              context="${orfn:getHandlerContext(pageContext.request)}"/>
                                        </td>
                                        <c:choose>
                                            <c:when test="${review eq null}">
                                                <td><!-- @ --></td>
                                                <c:choose>
                                                    <c:when test="${isAllowedToPerformPortMortemReview and isReviewerCurrentUser}">
                                                        <td nowrap="nowrap">
                                                            <a href="<or:url value='/actions/CreatePostMortem?pid=${project.id}' />">
                                                                <or:text key="viewProjectDetails.box.Post-Mortem.Submit"/></a>
                                                        </td>
                                                    </c:when>
                                                    <c:when test="${group.postMortemPhaseStatus == 2}">
                                                        <td nowrap="nowrap">
                                                            <or:text key="Pending"/></td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td nowrap="nowrap">
                                                            <or:text key="NotAvailable"/></td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:when test="${review.committed}">
                                                <td nowrap="nowrap">${orfn:displayDate(pageContext.request, review.modificationTimestamp)}</td>
                                                <td nowrap="nowrap">
                                                    <a href="<or:url value='/actions/ViewPostMortem?rid=${review.id}' />">
                                                        <or:text key="viewProjectDetails.box.Post-Mortem.ViewResults"/>
                                                    </a>
                                                </td>
                                            </c:when>
                                            <c:when test="${not review.committed}">
                                                <td><!-- @ --></td>
                                                <c:choose>
                                                    <c:when test="${isAllowedToPerformPortMortemReview and isReviewerCurrentUser}">
                                                        <td nowrap="nowrap">
                                                            <a href="<or:url value='/actions/EditPostMortem?rid=${review.id}' />">
                                                                <or:text key="viewProjectDetails.box.Post-Mortem.Submit"/>
                                                            </a>
                                                        </td>
                                                    </c:when>
                                                    <c:when test="${group.postMortemPhaseStatus == 2}">
                                                        <td nowrap="nowrap">
                                                            <or:text key="Pending"/></td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td nowrap="nowrap">
                                                            <or:text key="NotAvailable"/></td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                        </c:choose>
                                    </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:when test='${group.appFunc == "SPEC_REVIEW"}'>
                            <table class="phasesTable phasesTable__specification" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Specification.Date" arg0="${group.groupIndex}" /></td>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.SpecificationReview.Date" arg0="${group.groupIndex}" /></td>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.SpecificationReview.Review" arg0="${group.groupIndex}" /></td>
                                    </tr>
                                </thead>
                                <tbody class="phasesTable__body">
                                    <c:if test="${empty group.specificationSubmission}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="4"></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${not empty group.specificationSubmission}">
                                        <tr>
                                            <td nowrap="nowrap">
                                                <c:if test="${group.specificationReadyToDownload}">
                                                <a href="<or:url value='/actions/DownloadSpecificationSubmission?uid=${group.specificationSubmission.upload.id}' />"
                                                    title="<or:text key='viewProjectDetails.box.Specification.Download' />">${group.specificationSubmission.id}</a>
                                                </c:if>
                                                <c:if test="${not group.specificationReadyToDownload}">
                                                <a style="pointer-events:none" href="<or:url value='/actions/DownloadSpecificationSubmission?uid=${group.specificationSubmission.upload.id}' />"
                                                    title="<or:text key='viewProjectDetails.box.Specification.Download' />">${group.specificationSubmission.id}</a>
                                                </c:if>
                                                (<tc-webtag:handle coderId='${group.specificationSubmitter.allProperties["External Reference ID"]}'
                                                                   context="${orfn:getHandlerContext(pageContext.request)}" />)
                                            </td>
                                            <td
                                                nowrap="nowrap">${orfn:displayDate(pageContext.request, group.specificationSubmission.upload.creationTimestamp)}</td>
                                            <c:choose>
                                                <c:when test="${not empty group.specificationReview}">
                                                    <c:choose>
                                                        <c:when test="${group.specificationReview.committed}">
                                                            <td nowrap="nowrap">
                                                               ${orfn:displayDate(pageContext.request, group.specificationReview.modificationTimestamp)}
                                                            </td>
                                                            <td nowrap="nowrap">
                                                                <a href="<or:url value='/actions/ViewSpecificationReview?rid=${group.specificationReview.id}' />">
                                                                    <or:text key="viewProjectDetails.box.SpecificationReview.ViewResults"/></a>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td><!-- @ --></td>
                                                            <c:choose>
                                                                <c:when test="${isAllowedToPerformSpecReview}">
                                                                    <td nowrap="nowrap">
                                                                        <a href="<or:url value='/actions/EditSpecificationReview?rid=${group.specificationReview.id}' />">
                                                                        <b><or:text key="viewProjectDetails.box.SpecificationReview.Submit"/></b></a>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td nowrap="nowrap"><or:text key="Pending"/></td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <td><!-- @ --></td>
                                                    <td nowrap="nowrap">
                                                    <c:choose>
                                                        <c:when test="${isAllowedToPerformSpecReview}">
                                                            <a href="<or:url value='/actions/CreateSpecificationReview?sid=${group.specificationSubmission.id}' />">
                                                            <b><or:text key="viewProjectDetails.box.SpecificationReview.Submit"/></b></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <or:text key="Pending"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </c:when>
                        <c:when test='${group.appFunc == "CHECKPOINT"}'>
                            <table id="Submissions${submBoxIdx}" class="phasesTable phasesTable__checkpoint" cellpadding="0" cellspacing="0" border="0">
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.Date" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.Screener" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ScreeningScore" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ScreeningResult" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.Reviewer" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ReviewScore" arg0="${group.groupIndex}" /></th>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ReviewResult" arg0="${group.groupIndex}" /></th>
                                    </tr>
                                </thead>
                                <tbody class="phasesTable__body">
                                    <c:if test="${empty group.checkpointSubmissions}">
                                        <tr>
                                            <td class="phasesTable__empty" colspan="11"></td>
                                        </tr>
                                    </c:if>
                                    <c:set var="prevCheckpointSubm" value="${group.pastCheckpointSubmissions}" />
                                    <c:set var="prevCheckpointSubmissions" value="" />
                                    <c:forEach items="${group.checkpointSubmissions}" var="submission" varStatus="submissionStatus">
                                        <c:set var="submitter" value="" />
                                        <c:set var="submissionStatusName" value="${submission.submissionStatus.name}" />
                                        <c:forEach items="${group.submitters}" var="curSubmitter">
                                            <c:if test="${curSubmitter.id == submission.upload.owner}">
                                                <c:set var="submitter" value="${curSubmitter}" />
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${not empty prevCheckpointSubm}">
                                            <c:set var="prevCheckpointSubmissions" value="${prevCheckpointSubm[submissionStatus.index]}" />
                                        </c:if>
                                        <tr>
                                            <%-- Checkpoint Submission ID --%>
                                            <td nowrap="nowrap">
                                                <c:set var="placement" value="" />
                                                <c:if test="${not empty submission}">
                                                    <c:set var="placement" value='${submission.placement}' />
                                                </c:if>
                                                <c:set var="failedReview"
                                                       value="${(submissionStatusName == 'Failed Checkpoint Screening') or (submissionStatusName == 'Failed Checkpoint Review')}" />
                                                <c:if test="${(not empty placement) and (not failedReview)}">
                                                    <c:choose>
                                                        <c:when test="${placement == 1}">
                                                            <span class="phasesTable__placements phasesTable__placements--first">1st</span>
                                                        </c:when>
                                                        <c:when test="${placement == 2}">
                                                            <span class="phasesTable__placements phasesTable__placements--second">2nd</span>
                                                        </c:when>
                                                        <c:when test="${placement == 3}">
                                                            <span class="phasesTable__placements phasesTable__placements--third">3rd</span>
                                                        </c:when>
                                                        <c:when test="${placement == 4}">
                                                            <span class="phasesTable__placements phasesTable__placements--forth">4th</span>
                                                        </c:when>
                                                        <c:when test="${placement == 5}">
                                                            <span class="phasesTable__placements phasesTable__placements--fifth">5th</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="phasesTable__placements">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                                <c:if test="${failedReview}">
                                                    <c:set var="failureKeyName" value='SubmissionStatus.${fn:replace(submissionStatusName, " ", "")}' />
                                                    <c:if test="${empty placement}">
                                                            <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="<or:text key='${failureKeyName}' />"></span>
                                                    </c:if>
                                                    <c:if test="${not empty placement}">
                                                        <c:set var="placeStr" value="${orfn:getMessage(pageContext, failureKeyName)} (Place ${placement})" />
                                                        <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="${placeStr}"></span>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${project.projectCategory.projectType.id ne 3}">
                                                    <c:if test="${group.checkpointReadyToDownload[submissionStatus.index]}">
                                                    <a href="<or:url value='/actions/DownloadCheckpointSubmission?uid=${submission.upload.id}' />"
                                                           title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                    <c:if test="${not group.checkpointReadyToDownload[submissionStatus.index]}">
                                                    <a style="pointer-events:none" href="<or:url value='/actions/DownloadCheckpointSubmission?uid=${submission.upload.id}' />"
                                                           title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${project.projectCategory.projectType.id eq 3}">
                                                    <c:if test="${group.checkpointReadyToDownload[submissionStatus.index]}">
                                                    <a href="<or:url value='/actions/DownloadCheckpointSubmission?uid=${submission.upload.id}' />"
                                                           title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                    <c:if test="${not group.checkpointReadyToDownload[submissionStatus.index]}">
                                                    <a style="pointer-events:none" href="<or:url value='/actions/DownloadCheckpointSubmission?uid=${submission.upload.id}' />"
                                                           title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${not empty submitter}">
                                                    (<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}'
                                                                       context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                </c:if>

                                                <%-- Delete Checkpoint Submission --%>
                                                <c:choose>
                                                   <c:when test="${isManager and not group.checkpointReviewFinished}">
                                                       <a href="<or:url value='/actions/DeleteSubmission?uid=${submission.upload.id}' />">
                                                           <img src="<or:text key='viewProjectDetails.box.Submission.icoTrash.img' />"
                                                                     alt="<or:text key='viewProjectDetails.box.Submission.icoTrash.alt' />"
                                                                     border="0" class="Outline" />
                                                       </a>
                                                   </c:when>
                                                   <c:otherwise><!-- @ --></c:otherwise>
                                                </c:choose>
                                            </td>

                                            <%-- Checkpoint Submission Date --%>
                                            <td>
                                                ${orfn:displayDate(pageContext.request, submission.upload.creationTimestamp)}
                                            </td>

                                            <%-- Checkpoint Screener --%>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${empty group.checkpointScreener}">
                                                            <or:text
                                                                    key="viewProjectDetails.box.Screening.ScreenerNotAssigned"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tc-webtag:handle
                                                                    coderId='${group.checkpointScreener.allProperties["External Reference ID"]}'
                                                                    context="${orfn:getHandlerContext(pageContext.request)}"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                            <%-- Checkpoint Screening Score --%>
                                            <c:set var="review" value="" />
                                            <c:forEach items="${group.checkpointScreeningReviews}" var="screening">
                                                <c:if test="${screening.submission == submission.id}">
                                                    <c:set var="review" value="${screening}" />
                                                </c:if>
                                            </c:forEach>

                                            <td nowrap="nowrap">
                                                <c:choose>
                                                    <c:when test="${empty review}">
                                                        <c:choose>
                                                            <c:when test="${isAllowedToPerformCheckpointScreening}">
                                                                <b><a href="<or:url value='/actions/CreateCheckpointScreening?sid=${submission.id}' />">
                                                                    <or:text
                                                                            key="viewProjectDetails.box.Screening.Submit"/></a></b>
                                                            </c:when>
                                                            <c:otherwise><or:text key="Pending"/></c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${review.committed}">
                                                                <c:choose>
                                                                    <c:when test="${isAllowedToViewCheckpointScreening}">
                                                                        <a href="<or:url value='/actions/ViewCheckpointScreening?rid=${review.id}' />">
                                                                            ${orfn:displayScore(pageContext.request, review.score)}</a>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${orfn:displayScore(pageContext.request, review.score)}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:choose>
                                                                    <c:when test="${isAllowedToPerformCheckpointScreening}">
                                                                        <b><a href="<or:url value='/actions/EditCheckpointScreening?rid=${review.id}' />">
                                                                            <or:text
                                                                                    key="viewProjectDetails.box.Screening.Submit"/></a></b>
                                                                    </c:when>
                                                                    <c:otherwise><or:text key="Pending"/></c:otherwise>
                                                                </c:choose>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <c:set var="failedScreening" value="${(submissionStatusName == 'Failed Checkpoint Screening')}" />
                                            <%-- Checkpoint Screening Results --%>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty review and review.committed and group.checkpointScreeningPhaseStatus == 3}">
                                                        <c:choose>
                                                            <c:when test="${not failedScreening}">
                                                                <or:text key="viewProjectDetails.box.Screening.Passed"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <or:text key="viewProjectDetails.box.Screening.Failed"/>
                                                                <c:if test="${isAllowedToAdvanceSubmissionWithFailedScreening and submission.submissionStatus.name eq 'Failed Checkpoint Screening'}">
                                                                    (<a href="<or:url value='/actions/AdvanceFailedCheckpointScreeningSubmission?uid=${submission.upload.id}' />"><or:text key="viewProjectDetails.box.Screening.Advance" /></a>)
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise><or:text key="NotAvailable" /></c:otherwise>
                                                </c:choose>
                                            </td>


                                                <%-- Checkpoint Reviewer --%>
                                                <td>
                                                    <c:if test="${not failedScreening}">
                                                        <c:choose>
                                                            <c:when test="${empty group.checkpointReviewer}">
                                                                <or:text
                                                                        key="viewProjectDetails.box.Checkpoint.ReviewerNotAssigned"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <tc-webtag:handle
                                                                        coderId='${group.checkpointReviewer.allProperties["External Reference ID"]}'
                                                                        context="${orfn:getHandlerContext(pageContext.request)}"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </td>

                                                <%-- Checkpoint Review Score --%>
                                                <c:set var="review" value="" />
                                                <c:forEach items="${group.checkpointReviews}" var="item">
                                                    <c:if test="${item.submission == submission.id}">
                                                        <c:set var="review" value="${item}" />
                                                    </c:if>
                                                </c:forEach>

                                                <td nowrap="nowrap">
                                                    <c:if test="${not failedScreening}">
                                                    <c:choose>
                                                        <c:when test="${empty review}">
                                                            <c:choose>
                                                                <c:when test="${isAllowedToPerformCheckpointReview}">
                                                                    <b><a href="<or:url value='/actions/CreateCheckpointReview?sid=${submission.id}' />">
                                                                        <or:text
                                                                                key="viewProjectDetails.box.Screening.Submit"/></a></b>
                                                                </c:when>
                                                                <c:otherwise><or:text key="Pending"/></c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${review.committed}">
                                                                    <c:choose>
                                                                        <c:when test="${isSubmitter and not group.checkpointReviewFinished}">
                                                                            <or:text key="Pending"/>
                                                                        </c:when>
                                                                        <c:when test="${isAllowedToViewCheckpointReview}">
                                                                            <a href="<or:url value='/actions/ViewCheckpointReview?rid=${review.id}' />">
                                                                                ${orfn:displayScore(pageContext.request, review.score)}</a>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            ${orfn:displayScore(pageContext.request, review.score)}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when test="${isAllowedToPerformCheckpointReview}">
                                                                            <b><a href="<or:url value='/actions/EditCheckpointReview?rid=${review.id}' />">
                                                                                <or:text
                                                                                        key="viewProjectDetails.box.Screening.Submit"/></a></b>
                                                                        </c:when>
                                                                        <c:otherwise><or:text key="Pending"/></c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </c:if>
                                                </td>

                                                <%-- Checkpoint Review Results --%>
                                                <td>
                                                    <c:if test="${not failedScreening}">
                                                        <c:choose>
                                                            <c:when test="${not empty review and review.committed}">
                                                                <c:choose>
                                                                    <c:when test="${isSubmitter and not group.checkpointReviewFinished}">
                                                                        <!--@-->
                                                                    </c:when>
                                                                    <c:when test="${not failedReview}">
                                                                        <or:text key="viewProjectDetails.box.CheckpointReview.Passed"/>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <or:text key="viewProjectDetails.box.CheckpointReview.Failed"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:otherwise><!--@--></c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                    <c:if test="${not empty prevCheckpointSubmissions}">
                                                        <a id="PrevSubm${submBoxIdx}_${submissionStatus.index}_plus" href="javascript:void(0)" onClick='return expandSubmissions(${submBoxIdx}, ${submissionStatus.index}, this)'><img
                                                                class="Outline checkPoint__chevron" border="0" src="<or:text key='viewProjectDetails.box.Submission.icoShowMore.img' />" alt="<or:text key='viewProjectDetails.box.Submission.icoShowMore.alt' />" /></a><a
                                                            id="PrevSubm${submBoxIdx}_${submissionStatus.index}_minus" href="javascript:void(0)" onClick='return collapseSubmissions(${submBoxIdx}, ${submissionStatus.index}, this)' style="display:none;"><img
                                                                class="Outline checkPoint__chevron" border="0" src="<or:text key='viewProjectDetails.box.Submission.icoShowLess.img' />" alt="<or:text key='viewProjectDetails.box.Submission.icoShowLess.alt' />" /></a>
                                                    </c:if>
                                                    <c:if test="${(empty prevCheckpointSubmissions) and (not empty prevCheckpointSubm)}">
                                                        <img class="Outline" border="0" src="<or:text key='viewProjectDetails.box.Submission.icoShowMore.img' />" style="display:none;" />
                                                    </c:if>
                                                </td>
                                        </tr>


                                        <c:forEach items="${prevCheckpointSubmissions}" var="pastSubmission" varStatus="pastSubmissionStatus">
                                            <tr id="PrevSubm${submBoxIdx}_${submissionStatus.index}" style="display:none;">
                                                <td colspan="1" nowrap="nowrap">
                                                    <img border="0" src="<or:text key='viewProjectDetails.box.Submission.icoShowMore.img' />" class="Outline" style="display:none;" />
                                                    <c:if test="${(not empty placement) and (not failedReview)}">
                                                        <span class="phasesTable__placements">&nbsp;</span>
                                                    </c:if>
                                                    <c:if test="${project.projectCategory.projectType.id ne 3}">
                                                            <a href="<or:url value='/actions/DownloadContestSubmission?uid=${pastSubmission.id}' />"
                                                                title="<or:text key='viewProjectDetails.box.Submission.Previous.UploadID' />">${pastSubmission.id}</a>
                                                    </c:if>
                                                    <c:if test="${project.projectCategory.projectType.id eq 3}">
                                                            <!-- <a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/?module=DownloadSubmission&sbmid=${pastSubmission.id}&sbt=original" title="<or:text key='viewProjectDetails.box.Submission.Previous.UploadID' />">${pastSubmission.id}</a> -->
                                                            <a href="<or:url value='/actions/DownloadContestSubmission?uid=${pastSubmission.id}' />"
                                                                title="<or:text key='viewProjectDetails.box.Submission.Previous.UploadID' />">${pastSubmission.id}</a>
                                                    </c:if>
                                                </td>
                                                <td>${orfn:displayDate(pageContext.request, pastSubmission.creationTimestamp)}</td>
                                                <td><!-- @ --></td>
                                                <td><!-- @ --></td>
                                                <td><!-- @ --></td>
                                                <td><!-- @ --></td>
                                                <td><!-- @ --></td>
                                                <td><!-- @ --></td>
                                            </tr>
                                        </c:forEach>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <c:if test="${(not empty prevCheckpointSubm) && (submBoxIdx == 0)}">
<script language="JavaScript" type="text/javascript">
    function expandSubmissions(iBoxIdx, iLinkIdx, aObject) {
        return expcollSubmissions(iBoxIdx, iLinkIdx, aObject, "none", "inline", "");
    }
    function collapseSubmissions(iBoxIdx, iLinkIdx, aObject) {
        return expcollSubmissions(iBoxIdx, iLinkIdx, aObject, "inline", "none", "none");
    }

    function expcollSubmissions(iBoxIdx, iLinkIdx, aObject, strStyle1, strStyle2, strStyle3) {
        var strRowGroup = "PrevSubm" + iBoxIdx + "_" + iLinkIdx;
        var imgPlus = document.getElementById(strRowGroup + "_plus");
        var imgMinus = document.getElementById(strRowGroup + "_minus");
        var table = document.getElementById("Submissions" + iBoxIdx);

        if (imgPlus) {
            imgPlus.style.display = strStyle1;
        }
        if (imgMinus) {
            imgMinus.style.display = strStyle2;
        }
        if (table) {
            var rows = table.getElementsByTagName("tr");
            for (var i = 0; i < rows.length; ++i) {
                var row = table.rows[i];
                if (row.id == strRowGroup) {
                    row.style.display = strStyle3;
                }
            }
        }

        if (aObject.blur) {
            aObject.blur();
        }
        return false;
    }
</script>
                            </c:if>
                            <c:set var="submBoxIdx" value="${submBoxIdx + 1}" />
                        </c:when><c:when test='${group.appFunc == "ITERATIVEREVIEW"}'>
                            <table class="phasesTable" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <c:set var="colSpan" value="${fn:length(group.iterativeReviewers) + 2}" />

                                <c:set var="canEditIterativeReviewForThisPhase" value="${isAllowedToEditHisIterativeReview}" />
                                <c:if test="${group.iterativeReviewPhase.phaseStatus.name eq 'Closed'}">
                                    <c:set var="canEditIterativeReviewForThisPhase" value="false" />
                                </c:if>

                                <c:if test="${not canEditIterativeReviewForThisPhase}">
                                    <c:set var="colSpan" value="${colSpan + 1}" />
                                </c:if>
                                <tr>
                                    <td colspan="${(canEditIterativeReviewForThisPhase) ? 2 : 3}"><!-- @ --></td>
                                    <c:forEach items="${group.iterativeReviewers}" var="reviewer">
                                        <td nowrap="nowrap">
                                            <b><or:text key='ResourceRole.${fn:replace(reviewer.resourceRole.name, " ", "")}' />:</b>
                                            <tc-webtag:handle coderId='${reviewer.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />
                                        </td>
                                    </c:forEach>
                                </tr>
                                <thead class="phasesTable__header">
                                    <tr>
                                        <th nowrap="nowrap"><or:text key="viewProjectDetails.box.Submission.ID" /></th>
                                        <th><or:text key="viewProjectDetails.box.Review.Date" arg0="${group.groupIndex}" /></th>
                                        <c:if test="${canEditIterativeReviewForThisPhase != true}">
                                            <th><or:text key="viewProjectDetails.box.Review.Score" arg0="${group.groupIndex}" /></th>
                                        </c:if>
                                        <c:forEach items="${group.iterativeReviewers}" var="reviewer">
                                            <th><or:text key="viewProjectDetails.box.Review.Score.short" /></th>
                                        </c:forEach>
                                    </tr>
                                </thead>
                                <c:if test="${empty group.iterativeReviewSubmission}">
                                <tbody class="phasesTable__body">
                                    <tr>
                                        <td class="phasesTable__empty" colspan="${colSpan}"></td>
                                    </tr>
                                </tbody>
                                </c:if>
                                <c:if test="${group.iterativeReviewSubmission ne null}">
                                <tbody class="phasesTable__body">
                                    <c:set var="submission" value="${group.iterativeReviewSubmission}"/>
                                    <c:set var="submissionStatusName" value="${submission.submissionStatus.name}" />
                                    <tr>
                                        <td nowrap="nowrap">
                                            <c:set var="placement" value="${submission.placement}" />
                                            <c:set var="failedReview" value="${(submissionStatusName == 'Failed Screening') or (submissionStatusName == 'Failed Review')}" />
                                            <c:if test="${(not empty placement) and (not failedReview)}">
                                                <c:choose>
                                                    <c:when test="${placement == 1}">
                                                        <span class="phasesTable__placements phasesTable__placements--first">1st</span>
                                                    </c:when>
                                                    <c:when test="${placement == 2}">
                                                        <span class="phasesTable__placements phasesTable__placements--second">2nd</span>
                                                    </c:when>
                                                    <c:when test="${placement == 3}">
                                                        <span class="phasesTable__placements phasesTable__placements--third">3rd</span>
                                                    </c:when>
                                                    <c:when test="${placement == 4}">
                                                        <span class="phasesTable__placements phasesTable__placements--forth">4th</span>
                                                    </c:when>
                                                    <c:when test="${placement == 5}">
                                                        <span class="phasesTable__placements phasesTable__placements--fifth">5th</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="phasesTable__placements">&nbsp;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <c:if test="${failedReview}">
                                                <c:set var="failureKeyName" value='SubmissionStatus.${fn:replace(submissionStatusName, " ", "")}' />
                                                <c:if test="${empty placement}">
                                                    <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="<or:text key='${failureKeyName}' />"></span>
                                                </c:if>
                                                <c:if test="${not empty placement}">
                                                    <c:set var="placeStr" value="${orfn:getMessage(pageContext, failureKeyName)} (Place ${placement})" />
                                                    <span class="phasesTable__placements phasesTable__placements--failed"><img src="/i/reskin/cross.svg" alt="${placeStr}"></span>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${project.projectCategory.projectType.id ne 3}">
                                                <c:if test="${group.iterativeReadyToDownload}">
                                                <a href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                </c:if>
                                                <c:if test="${not group.iterativeReadyToDownload}">
                                                <a style="pointer-events:none" href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${project.projectCategory.projectType.id eq 3}">
                                                <!-- <a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/?module=DownloadSubmission&sbmid=${submission.id}&sbt=original" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a> -->
                                                <c:if test="${group.iterativeReadyToDownload}">
                                                <a href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                </c:if>
                                                <c:if test="${not group.iterativeReadyToDownload}">
                                                <a style="pointer-events:none" href="<or:url value='/actions/DownloadContestSubmission?sid=${submission.id}' />" title="<or:text key='viewProjectDetails.box.Submission.Download' />">${submission.id}</a>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${isManager || group.iterativeReviewPhase.phaseStatus.name eq 'Closed'}">
                                                (<tc-webtag:handle coderId='${group.iterativeReviewSubmitter.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                            </c:if>
                                        </td>
                                        <td>${orfn:displayDateBr(pageContext.request, group.reviewDates[0])}</td>
                                        <c:if test="${not canEditIterativeReviewForThisPhase}">
                                            <c:if test="${not empty submission}">
                                                <c:set var="finalScore" value='${submission.finalScore}' />
                                            </c:if>
                                            <c:if test="${not empty finalScore}">
                                                <td><a href="<or:url value='/actions/ViewCompositeScorecard?sid=${submission.id}&phid=${group.iterativeReviewPhase.id}' />">${orfn:displayScore(pageContext.request, finalScore)}</a></td>
                                            </c:if>
                                            <c:if test="${empty finalScore}">
                                                <td><or:text key="Incomplete" /></td>
                                            </c:if>
                                        </c:if>
                                        <c:forEach items="${group.iterativeReviewReviews}" var="review" varStatus="reviewStatus">
                                            <c:if test="${(empty review) or (not group.displayReviewLinks)}">
                                                <c:if test="${canEditIterativeReviewForThisPhase && group.displayReviewLinks}">
                                                    <td nowrap="nowrap"><a href="<or:url value='/actions/CreateIterativeReview?sid=${submission.id}' />"><b><or:text
                                                        key="viewProjectDetails.box.IterativeReview.Submit" /></b></a></td>
                                                </c:if>
                                                <c:if test="${(not canEditIterativeReviewForThisPhase) || (not group.displayReviewLinks)}">
                                                    <td><or:text key="NotAvailable" /></td>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${(not empty review) && group.displayReviewLinks}">
                                                <c:if test="${review.committed}">
                                                    <td><a href="<or:url value='/actions/ViewIterativeReview?rid=${review.id}' />">${orfn:displayScore(pageContext.request, review.score)}</a></td>
                                                </c:if>
                                                <c:if test="${not review.committed}">
                                                    <c:if test="${canEditIterativeReviewForThisPhase}">
                                                        <td><a href="<or:url value='/actions/EditIterativeReview?rid=${review.id}' />"><b><or:text
                                                            key="viewProjectDetails.box.Review.Submit" /></b></a></td>
                                                    </c:if>
                                                    <c:if test="${not canEditIterativeReviewForThisPhase}">
                                                        <td><or:text key="Pending" /></td>
                                                    </c:if>
                                                </c:if>
                                            </c:if>
                                        </c:forEach>
                                    </tr>
                                </tbody>
                                </c:if>
                            </table>
                        </c:when>
                    </c:choose>
                </c:if>
            </div>
        </c:forEach>
    </div>
    </div>
</div>
<script language="JavaScript" type="text/javascript">
<!--
    // A reference to the previously active tab
    <c:if test="${(not empty activeTabIdx) && (activeTabIdx != -1)}">
    var previousActiveTab = document.getElementById("sc${activeTabIdx + 1}");
    </c:if>
    <c:if test="${(empty activeTabIdx) || (activeTabIdx == -1)}">
    var previousActiveTab = null;
    </c:if>

    /*
     * This function will deactivate the previously active tab (if there was any),
     * and activate the new one.
     */
    function activateTab(tabId, aObject) {
        var tabToActivate = document.getElementById(tabId);
        if (tabToActivate == null) {
            return false;
        }
        // Deactivate the previously active tab
        if (previousActiveTab != null) {
            previousActiveTab.style.display = "none";
        }
        // Activate the new one and update the reference to the previously active tab
        tabToActivate.style.display = "block";
        previousActiveTab = tabToActivate;
        // Remove focus from the link that triggered the activation
        if (aObject.blur) {
            aObject.blur();
        }
        return false;
    }
//-->
</script>
