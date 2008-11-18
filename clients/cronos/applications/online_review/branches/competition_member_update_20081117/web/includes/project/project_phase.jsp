<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
	<c:set var="submBoxIdx" value="0" />
	<a name="tabs"></a>
	<div id="tabcontentcontainer">
		<c:forEach items="${phaseGroups}" var="group" varStatus="groupStatus">
			<div id="sc${groupStatus.index + 1}" style='display:${(groupStatus.index == activeTabIdx) ? "block" : "none"};'>
				<ul id="tablist">
					<c:forEach items="${phaseGroups}" var="innerGroup" varStatus="innerGroupStatus">
						<li ${(groupStatus.index == innerGroupStatus.index) ? "id='current'" : ""}><a href="javascript:void(0)"
							onClick="return activateTab('sc${innerGroupStatus.index + 1}', this)">${innerGroup.name}</a></li>
					</c:forEach>
				</ul>
				<div style="clear:both;"></div>
				<c:if test="${group.phaseOpen != true}">
					<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
						<tr>
							<td class="title" colspan="5">${group.tableName}</td>
						</tr>
						<tr class="light">
							<td class="valueC" nowrap="nowrap"><bean:message key="viewProjectDetails.Tabs.PhaseNotOpen" /></td>
						</tr>
						<tr>
							<td class="lastRowTD"><!-- @ --></td>
						</tr>
					</table>
				</c:if>
				<c:if test="${group.phaseOpen == true}">
					<c:choose>
						<c:when test='${group.appFunc == "VIEW_REGISTRANTS"}'>
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="5">${group.tableName}</td>
								</tr>
								<tr>
									<td class="header"><bean:message key="viewProjectDetails.box.Registration.Handle" /></td>
									<td class="header"><bean:message key="viewProjectDetails.box.Registration.Email" /></td>
									<td class="header"><bean:message key="viewProjectDetails.box.Registration.Reliability" /></td>
									<td class="header"><bean:message key="viewProjectDetails.box.Registration.Rating" /></td>
									<td class="headerC"><bean:message key="viewProjectDetails.box.Registration.RegistrationDate" /></td>
								</tr>
								<c:forEach items="${group.submitters}" var="resource" varStatus="resourceStatus">
									<c:set var="registrantEmail" value="${group.registantsEmails[resourceStatus.index]}" />
									<tr class='${(resourceStatus.index % 2 == 0) ? "light" : "dark"}'>
										<td class="value" nowrap="nowrap"><tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" /></td>
										<c:if test="${not empty registrantEmail}">
											<td class="value" nowrap="nowrap"><a href="mailto:${registrantEmail}">${registrantEmail}</a></td>
										</c:if>
										<c:if test="${empty registrantEmail}">
											<td class="value" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Registration.EmailUnknown" /></td>
										</c:if>
										<c:set var="reliability" value='${resource.allProperties["Reliability"]}' />
										<c:if test="${empty reliability}">
											<td class="value" nowrap="nowrap"><bean:message key="NotAvailable" /></td>
										</c:if>
										<c:if test="${!(empty reliability)}">
											<td class="value" nowrap="nowrap">${reliability}</td>
										</c:if>
										<c:set var="rating" value='${resource.allProperties["Rating"]}' />
										<c:choose>
											<c:when test="${empty rating}">
												<c:set var="ratingColor" value="coderTextBlack" />
											</c:when>
										</c:choose>
										<c:if test="${empty rating}">
											<td class="value" nowrap="nowrap"><bean:message key="NotRated" /></td>
										</c:if>
										<c:if test="${!(empty rating)}">
											<td class="value" nowrap="nowrap"><span class="${ratingColor}">${rating}</span></td>
										</c:if>
										<td class="valueC" nowrap="nowrap">${resource.allProperties["Registration Date"]}</td>
									</tr>
								</c:forEach>
								<tr>
									<td class="lastRowTD" colspan="5"><!-- @ --></td>
								</tr>
							</table>
						</c:when>
						<c:when test='${group.appFunc == "VIEW_SUBMISSIONS"}'>
							<table id="Submissions${submBoxIdx}" class="scorecard" width="100%" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td class="title" colspan="7">${group.tableName}</td>
								</tr>
								<tr>
									<td class="header" colspan="2" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="header" width="22%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.Date" arg0="${group.groupIndex}" /></td>
									<td class="headerC" width="14%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.AutoScreening" /></td>
									<td class="header" width="15%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.Screener" /></td>
									<td class="headerC" width="14%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ScreeningScore" arg0="${group.groupIndex}" /></td>
									<td class="headerC" width="15%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ScreeningResult" arg0="${group.groupIndex}" /></td>
								</tr>
								<c:set var="prevSubm" value="${group.pastSubmissions}" />
								<c:set var="prevSubmissions" value="" />
								<c:forEach items="${group.submissions}" var="submission" varStatus="submissionStatus">
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
									<tr class='${(submissionStatus.index % 2 == 0) ? "light" : "dark"}'>
										<td class="value" width="10%" nowrap="nowrap">
											<c:if test="${not empty prevSubmissions}">
												<a id="PrevSubm${submBoxIdx}_${submissionStatus.index}_plus" href="javascript:void(0)" onClick='return expandSubmissions(${submBoxIdx}, ${submissionStatus.index}, this)'><html:img
														styleClass="Outline" border="0" srcKey="viewProjectDetails.box.Submission.icoShowMore.img" altKey="viewProjectDetails.box.Submission.icoShowMore.alt" /></a><a
													id="PrevSubm${submBoxIdx}_${submissionStatus.index}_minus" href="javascript:void(0)" onClick='return collapseSubmissions(${submBoxIdx}, ${submissionStatus.index}, this)' style="display:none;"><html:img
														styleClass="Outline" border="0" srcKey="viewProjectDetails.box.Submission.icoShowLess.img" altKey="viewProjectDetails.box.Submission.icoShowLess.alt" /></a>
											</c:if>
											<c:if test="${(empty prevSubmissions) and (not empty prevSubm)}">
												<html:img styleClass="Outline" border="0" srcKey="viewProjectDetails.box.Submission.icoShowMore.img" style="visibility:hidden;" />
											</c:if>
											<c:set var="placement" value="" />
											<c:if test="${not empty submission}">
												<c:set var="placement" value='${submission.placement}' />
											</c:if>
											<c:set var="failedReview" value="${(submissionStatusName == 'Failed Screening') or (submissionStatusName == 'Failed Review')}" />
											<c:if test="${(not empty placement) and (not failedReview)}">
												<c:choose>
													<c:when test="${placement == 1}">
														<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" styleClass="Outline" border="0" />
													</c:when>
													<c:when test="${placement == 2}">
														<html:img srcKey="viewProjectDetails.Submitter.icoRunnerUp.img" altKey="viewProjectDetails.Submitter.icoRunnerUp.alt" styleClass="Outline" border="0" />
													</c:when>
													<c:otherwise>
														<html:img srcKey="viewProjectDetails.Submitter.icoOther.img" alt="${placement} Place" styleClass="Outline" border="0" />
													</c:otherwise>
												</c:choose>
											</c:if>
											<c:if test="${failedReview}">
												<c:set var="failureKeyName" value='SubmissionStatus.${fn:replace(submissionStatusName, " ", "")}' />
												<c:if test="${empty placement}">
													<html:img srcKey="viewProjectDetails.box.Submission.icoFailed.img" altKey="${failureKeyName}" border="0" />
												</c:if>
												<c:if test="${not empty placement}">
													<c:set var="placeStr" value="${orfn:getMessage(pageContext, failureKeyName)} (Place ${placement})" />
													<html:img srcKey="viewProjectDetails.box.Submission.icoFailed.img" alt="${placeStr}" border="0" />
												</c:if>
											</c:if>
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${submission.upload.id}" titleKey="viewProjectDetails.box.Submission.Download">${submission.id}</html:link>
											<c:if test="${not empty submitter}">
												(<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
											</c:if>
										</td>
										<c:if test="${isManager}">
											<td class="value" width="5%"><html:link page="/actions/DeleteSubmission.do?method=deleteSubmission&uid=${submission.upload.id}"><html:img srcKey="viewProjectDetails.box.Submission.icoTrash.img" altKey="viewProjectDetails.box.Submission.icoTrash.alt" border="0" styleClass="Outline" /></html:link></td>
										</c:if>
										<c:if test="${not isManager}">
											<td class="value"><!-- @ --></td>
										</c:if>
										<td class="value" width="22%">${orfn:displayDate(pageContext.request, submission.upload.creationTimestamp)}</td>
										<c:set var="scrTask" value="${group.screeningTasks[submissionStatus.index]}" />
										<c:if test="${empty scrTask}">
											<td class="valueC" width="14%"><html:img src="/i/clear.gif" width="8" height="10" /></td>
										</c:if>
										<c:if test="${not empty scrTask}">
											<c:set var="scrTaskStatus" value="${scrTask.screeningStatus.name}" />
											<c:choose>
												<c:when test='${scrTaskStatus == "Passed"}'>
													<td class="valueC" width="14%">
														<html:img
															srcKey="viewProjectDetails.box.Submission.icoPassed.img"
															altKey="viewProjectDetails.box.Submission.icoPassed.alt" /></td>
												</c:when>
												<c:when test='${scrTaskStatus == "Passed with Warning"}'>
													<td class="valueC" width="14%">
														<html:link page="/actions/ViewAutoScreening.do?method=viewAutoScreening&uid=${submission.upload.id}"><html:img
															srcKey="viewProjectDetails.box.Submission.icoPassedWW.img"
															altKey="viewProjectDetails.box.Submission.icoPassedWW.alt" /></html:link></td>
												</c:when>
												<c:when test='${scrTaskStatus == "Failed"}'>
													<td class="valueC" width="14%">
														<html:link page="/actions/ViewAutoScreening.do?method=viewAutoScreening&uid=${submission.upload.id}"><html:img
															srcKey="viewProjectDetails.box.Submission.icoFailed.img"
															altKey="viewProjectDetails.box.Submission.icoFailed.alt" /></html:link></td>
												</c:when>
												<c:otherwise>
													<td class="valueC" width="14%"><html:img src="/i/clear.gif" width="8" height="10" /></td>
												</c:otherwise>
											</c:choose>
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
											<td class="value" width="15%"><bean:message key="viewProjectDetails.box.Screening.ScreenerNotAssigned" /></td>
										</c:if>
										<c:if test="${not empty screener}">
											<td class="value" width="15%"><tc-webtag:handle coderId='${screener.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" /></td>
										</c:if>
										<c:set var="review" value="" />
										<c:forEach items="${group.screenings}" var="screening">
											<c:if test="${screening.submission == submission.id}">
												<c:set var="review" value="${screening}" />
											</c:if>
										</c:forEach>
										<c:if test="${empty review}">
											<c:if test="${isAllowedToPerformScreening}">
												<td class="valueC" width="14%" nowrap="nowrap">
													<b><html:link page="/actions/CreateScreening.do?method=createScreening&sid=${submission.id}"><bean:message
														key="viewProjectDetails.box.Screening.Submit" /></html:link></b></td>
											</c:if>
											<c:if test="${not isAllowedToPerformScreening}">
												<td class="valueC" width="14%"><bean:message key="Pending" /></td>
											</c:if>
											<td class="valueC" width="15%"><bean:message key="NotAvailable" /></td>
										</c:if>
										<c:if test="${not empty review}">
											<c:if test="${review.committed}">
												<c:if test="${isAllowedToViewScreening}">
													<td class="valueC" width="14%">
														<html:link page="/actions/ViewScreening.do?method=viewScreening&rid=${review.id}">${orfn:displayScore(pageContext.request, review.score)}</html:link></td>
												</c:if>
												<c:if test="${not isAllowedToViewScreening}">
													<td class="valueC" width="14%">${orfn:displayScore(pageContext.request, review.score)}</td>
												</c:if>
												<c:if test="${review.score >= passingMinimum}">
													<td class="valueC" width="15%"><bean:message key="viewProjectDetails.box.Screening.Passed" /></td>
												</c:if>
												<c:if test="${review.score < passingMinimum}">
													<td class="valueC" width="15%"><bean:message key="viewProjectDetails.box.Screening.Failed" /></td>
												</c:if>
											</c:if>
											<c:if test="${not review.committed}">
												<c:if test="${isAllowedToPerformScreening}">
													<td class="valueC" width="14%" nowrap="nowrap">
														<b><html:link page="/actions/EditScreening.do?method=editScreening&rid=${review.id}"><bean:message
															key="viewProjectDetails.box.Screening.Submit" /></html:link></b></td>
												</c:if>
												<c:if test="${not isAllowedToPerformScreening}">
													<td class="valueC" width="14%"><bean:message key="Pending" /></td>
												</c:if>
												<td class="valueC" width="15%"><bean:message key="NotAvailable" /></td>
											</c:if>
										</c:if>
									</tr>
									<c:forEach items="${prevSubmissions}" var="pastSubmission" varStatus="pastSubmissionStatus">
										<tr id="PrevSubm${submBoxIdx}_${submissionStatus.index}" class='${(submissionStatus.index % 2 == 0) ? "light" : "dark"}' style="display:none;">
											<td class="value" colspan="2" nowrap="nowrap">
												<html:img border="0" srcKey="viewProjectDetails.box.Submission.icoShowMore.img" styleClass="Outline" style="visibility:hidden;" />
												<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&amp;uid=${pastSubmission.id}">
													<bean:message key="viewProjectDetails.box.Submission.Previous.UploadID" />
													${pastSubmission.id}</html:link></td>
											<td class="value" width="22%">${orfn:displayDate(pageContext.request, pastSubmission.creationTimestamp)}</td>
											<td class="value" width="14%"><!-- @ --></td>
											<td class="value" width="15%"><!-- @ --></td>
											<td class="value" width="14%"><!-- @ --></td>
											<td class="value" width="15%"><!-- @ --></td>
										</tr>
									</c:forEach>
								</c:forEach>
								<tr>
									<td class="lastRowTD" colspan="7"><!-- @ --></td>
								</tr>
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
						<c:when test='${group.appFunc == "VIEW_REVIEWS"}'>
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<c:set var="colSpan" value="${(fn:length(group.reviewers) * 2) + 2}" />
								<c:if test="${not isAllowedToEditHisReviews}">
									<c:set var="colSpan" value="${colSpan + 1}" />
								</c:if>
								<tr>
									<td class="title" colspan="${colSpan}">${group.tableName}</td>
								</tr>
								<tr>
									<td class="value" colspan="${(isAllowedToEditHisReviews) ? 2 : 3}"><!-- @ --></td>
									<c:forEach items="${group.reviewers}" var="reviewer">
										<td class="valueC" colspan="2" nowrap="nowrap">
											<b><bean:message key='ResourceRole.${fn:replace(reviewer.resourceRole.name, " ", "")}' />:</b>
											<tc-webtag:handle coderId='${reviewer.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />
											<c:set var="testCase" value="" />
											<c:forEach items="${group.testCases}" var="curTestCase">
												<c:if test="${curTestCase.owner == reviewer.id}">
													<c:set var="testCase" value="${curTestCase}" />
												</c:if>
											</c:forEach>
											<c:if test="${not empty testCase}">
												<html:link page="/actions/DownloadTestCase.do?method=downloadTestCase&uid=${testCase.id}"
													titleKey="viewProjectDetails.box.Review.TestCase.hint"><bean:message
														key="viewProjectDetails.box.Review.TestCase" /></html:link>
												<c:if test="${isAllowedToUploadTC and group.uploadingTestcasesAllowed}">
													[
													<html:link page="/actions/UploadTestCase.do?method=uploadTestCase&pid=${project.id}"
														titleKey="viewProjectDetails.box.Review.TestCase.Update.hint"><bean:message
															key="viewProjectDetails.box.Review.TestCase.Update" /></html:link>
													]
												</c:if>
											</c:if>
											<c:if test="${(empty testCase) and isAllowedToUploadTC and group.uploadingTestcasesAllowed}"><%-- and group.uploadingTestcasesAllowed --%>
												<html:link page="/actions/UploadTestCase.do?method=uploadTestCase&pid=${project.id}"
													titleKey="viewProjectDetails.box.Review.TestCase.Upload.hint"><bean:message
														key="viewProjectDetails.box.Review.TestCase.Upload" /></html:link>
											</c:if>
										</td>
									</c:forEach>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" width="12%"><bean:message key="viewProjectDetails.box.Review.Date" arg0="${group.groupIndex}" /></td>
									<c:if test="${isAllowedToEditHisReviews != true}">
										<td class="headerC" width="8%"><bean:message key="viewProjectDetails.box.Review.Score" arg0="${group.groupIndex}" /></td>
									</c:if>
									<c:forEach items="${group.reviewers}" var="reviewer">
										<td class="headerC" width="8%"><bean:message key="viewProjectDetails.box.Review.Score.short" /></td>
										<td class="headerC" width="8%"><bean:message key="viewProjectDetails.box.Review.Appeals" /></td>
									</c:forEach>
								</tr>
								<c:set var="submissionIdx" value="0" />
								<c:forEach items="${group.submissions}" var="submission" varStatus="submissionStatus">
									<c:set var="submissionStatusName" value="${submission.submissionStatus.name}" />
									<c:if test='${(submissionStatusName != "Failed Screening") && (submissionStatusName != "Deleted")}'>
										<tr class='${(submissionIdx % 2 == 0) ? "light" : "dark"}'>
											<c:set var="submitter" value="" />
											<c:forEach items="${group.submitters}" var="curSubmitter">
												<c:if test="${curSubmitter.id == submission.upload.owner}">
													<c:set var="submitter" value="${curSubmitter}" />
												</c:if>
											</c:forEach>
											<td class="value" nowrap="nowrap">
												<c:set var="placement" value="" />
												<c:if test="${not empty submission}">
													<c:set var="placement" value='${submission.placement}' />
												</c:if>
												<c:set var="failedReview" value="${(submissionStatusName == 'Failed Screening') or (submissionStatusName == 'Failed Review')}" />
												<c:if test="${(not empty placement) and (not failedReview)}">
													<c:choose>
														<c:when test="${placement == 1}">
															<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" styleClass="Outline" border="0" />
														</c:when>
														<c:when test="${placement == 2}">
															<html:img srcKey="viewProjectDetails.Submitter.icoRunnerUp.img" altKey="viewProjectDetails.Submitter.icoRunnerUp.alt" styleClass="Outline" border="0" />
														</c:when>
														<c:otherwise>
															<html:img srcKey="viewProjectDetails.Submitter.icoOther.img" alt="${placement} Place" styleClass="Outline" border="0" />
														</c:otherwise>
													</c:choose>
												</c:if>
												<c:if test="${failedReview}">
													<c:set var="failureKeyName" value='SubmissionStatus.${fn:replace(submissionStatusName, " ", "")}' />
													<c:if test="${empty placement}">
														<html:img srcKey="viewProjectDetails.box.Submission.icoFailed.img" altKey="${failureKeyName}" border="0" />
													</c:if>
													<c:if test="${not empty placement}">
														<c:set var="placeStr" value="${orfn:getMessage(pageContext, failureKeyName)} (Place ${placement})" />
														<html:img srcKey="viewProjectDetails.box.Submission.icoFailed.img" alt="${placeStr}" border="0" />
													</c:if>
												</c:if>
												<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${submission.upload.id}"
													titleKey="viewProjectDetails.box.Submission.Download">${submission.id}</html:link>
												<c:if test="${not empty submitter}">
													(<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
												</c:if>
											</td>
											<td class="valueC" width="12%">${orfn:displayDateBr(pageContext.request, group.reviewDates[submissionStatus.index])}</td>
											<c:if test="${not isAllowedToEditHisReviews}">
												<c:if test="${not empty submission}">
													<c:set var="finalScore" value='${submission.finalScore}' />
												</c:if>
												<c:if test="${not empty finalScore}">
													<td class="valueC" width="8%"><html:link page="/actions/ViewCompositeScorecard.do?method=viewCompositeScorecard&sid=${submission.id}">${orfn:displayScore(pageContext.request, finalScore)}</html:link></td>
												</c:if>
												<c:if test="${empty finalScore}">
													<td class="valueC" width="8%"><bean:message key="Incomplete" /></td>
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
														<td class="valueC" width="8%" nowrap="nowrap"><html:link
															page="/actions/CreateReview.do?method=createReview&sid=${submission.id}"><b><bean:message
															key="viewProjectDetails.box.Review.Submit" /></b></html:link></td>
														<td class="valueC"><bean:message key="NotAvailable" /></td>
													</c:if>
													<c:if test="${(not isAllowedToEditHisReviews) || (not group.displayReviewLinks)}">
														<td class="valueC" width="8%"><bean:message key="NotAvailable" /></td>
														<td class="valueC"><bean:message key="NotAvailable" /></td>
													</c:if>
												</c:if>
												<c:if test="${(not empty review) && group.displayReviewLinks}">
													<c:if test="${review.committed}">
														<td class="valueC" width="8%"><html:link
															page="/actions/ViewReview.do?method=viewReview&rid=${review.id}">${orfn:displayScore(pageContext.request, review.score)}</html:link></td>
													</c:if>
													<c:if test="${not review.committed}">
														<c:if test="${isAllowedToEditHisReviews}">
															<td class="valueC" width="8%"><html:link
																page="/actions/EditReview.do?method=editReview&rid=${review.id}"><b><bean:message
																key="viewProjectDetails.box.Review.Submit" /></b></html:link></td>
														</c:if>
														<c:if test="${not isAllowedToEditHisReviews}">
															<td class="valueC" width="8%"><bean:message key="Pending" /></td>
														</c:if>
													</c:if>
													<c:if test="${group.appealsPhaseOpened}">
														<td class="valueC" nowrap="nowrap">[
															<html:link page="/actions/ViewReview.do?method=viewReview&rid=${review.id}"
																titleKey="viewProjectDetails.box.Review.Appeals.Unresolved">${unresolvedAppeals[reviewStatus.index]}</html:link> /
															<html:link page="/actions/ViewReview.do?method=viewReview&rid=${review.id}"
																titleKey="viewProjectDetails.box.Review.Appeals.Total">${totalAppeals[reviewStatus.index]}</html:link>
														]</td>
													</c:if>
													<c:if test="${not group.appealsPhaseOpened}">
														<td class="valueC"><bean:message key="NotAvailable" /></td>
													</c:if>
												</c:if>
											</c:forEach>
										</tr>
										<c:set var="submissionIdx" value="${submissionIdx + 1}" />
									</c:if>
								</c:forEach>
								<tr>
									<td class="lastRowTD" colspan="${colSpan}"><!-- @ --></td>
								</tr>
							</table>
						</c:when>
						<c:when test='${group.appFunc == "AGGREGATION"}'>
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="5">${group.tableName}</td>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Aggregation.Date" arg0="${group.groupIndex}" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Aggregation.Aggregation" arg0="${group.groupIndex}" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.AggregationReview.Date" arg0="${group.groupIndex}" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.AggregationReview.Review" arg0="${group.groupIndex}" /></td>
								</tr>
								<c:set var="winningSubmission" value="" />
								<c:forEach items="${group.submissions}" var="submission">
									<c:if test="${(not empty group.winner) and (group.winner.id == submission.upload.owner)}">
										<c:set var="winningSubmission" value="${submission}" />
									</c:if>
								</c:forEach>
								<c:if test="${not empty winningSubmission}">
									<tr class="light">
										<td class="value" nowrap="nowrap">
											<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" border="0" styleClass="Outline" />
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${winningSubmission.upload.id}"
												titleKey="viewProjectDetails.box.Submission.Download">${winningSubmission.id}</html:link>
											(<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
										</td>
										<c:if test="${not empty group.aggregation}">
											<c:if test="${group.aggregation.committed}">
												<td class="valueC" nowrap="nowrap">${orfn:displayDate(pageContext.request, group.aggregation.modificationTimestamp)}</td>
												<td class="valueC" nowrap="nowrap"><html:link
													page="/actions/ViewAggregation.do?method=viewAggregation&rid=${group.aggregation.id}"><bean:message
													key="viewProjectDetails.box.Aggregation.ViewResults" /></html:link></td>
											</c:if>
											<c:if test="${not group.aggregation.committed}">
												<td class="value"><!-- @ --></td>
												<c:if test="${isAllowedToPerformAggregation}">
													<td class="valueC" nowrap="nowrap"><html:link
														page="/actions/EditAggregation.do?method=editAggregation&rid=${group.aggregation.id}"><b><bean:message
														key="viewProjectDetails.box.Aggregation.Submit" /></b></html:link></td>
												</c:if>
												<c:if test="${not isAllowedToPerformAggregation}">
													<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
												</c:if>
											</c:if>
											<c:if test="${group.displayAggregationReviewLink}">
												<c:if test="${group.aggregationReviewCommitted}">
													<td class="valueC" nowrap="nowrap">${orfn:displayDate(pageContext.request, group.aggregation.modificationTimestamp)}</td>
													<td class="valueC" nowrap="nowrap"><html:link
														page="/actions/ViewAggregationReview.do?method=viewAggregationReview&rid=${group.aggregation.id}"><bean:message
														key="viewProjectDetails.box.Aggregation.ViewResults" /></html:link></td>
												</c:if>
												<c:if test="${not group.aggregationReviewCommitted}">
													<td class="value"><!-- @ --></td>
													<c:if test="${group.aggregation.committed and isAllowedToPerformAggregationReview}">
														<c:if test="${isSubmitter}">
															<c:set var="aggrRevKey" value="Comment" />
														</c:if>
														<c:if test="${not isSubmitter}">
															<c:set var="aggrRevKey" value="Approval" />
														</c:if>
														<td class="valueC" nowrap="nowrap"><html:link
															page="/actions/EditAggregationReview.do?method=editAggregationReview&rid=${group.aggregation.id}"><b><bean:message
															key="viewProjectDetails.box.AggregationReview.Submit${aggrRevKey}" /></b></html:link></td>
													</c:if>
													<c:if test="${not (group.aggregation.committed and isAllowedToPerformAggregationReview)}">
														<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
													</c:if>
												</c:if>
											</c:if>
											<c:if test="${not group.displayAggregationReviewLink}">
												<td class="value"><!-- @ --></td>
												<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
											</c:if>
										</c:if>
										<c:if test="${empty group.aggregation}">
											<td class="valueC"><!-- @ --></td>
											<td class="valueC"><bean:message key="NotAvailable" /></td>
											<td class="valueC"><!-- @ --></td>
											<td class="valueC"><bean:message key="NotAvailable" /></td>
										</c:if>
									</tr>
								</c:if>
								<tr>
									<td class="lastRowTD" colspan="5"><!-- @ --></td>
								</tr>
							</table>
						</c:when>
						<c:when test='${group.appFunc == "FINAL_FIX"}'>
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="5">${group.tableName}</td>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalFix.Date" arg0="${group.groupIndex}" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalFix.Fix" arg0="${group.groupIndex}" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalReview.Date" arg0="${group.groupIndex}" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalReview.Review" arg0="${group.groupIndex}" /></td>
								</tr>
								<c:set var="winningSubmission" value="" />
								<c:forEach items="${group.submissions}" var="submission">
									<c:if test="${(not empty group.winner) && (group.winner.id == submission.upload.owner)}">
										<c:set var="winningSubmission" value="${submission}" />
									</c:if>
								</c:forEach>
								<c:if test="${not empty winningSubmission}">
									<tr class="light">
										<td class="value" nowrap="nowrap">
											<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" border="0" styleClass="Outline" />
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${winningSubmission.upload.id}"
												titleKey="viewProjectDetails.box.Submission.Download">${winningSubmission.id}</html:link>
											(<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
										</td>
										<c:if test="${not empty group.finalFix}">
											<td class="valueC" nowrap="nowrap">${orfn:displayDate(pageContext.request, group.finalFix.modificationTimestamp)}</td>
											<td class="valueC" nowrap="nowrap">
												<html:link page="/actions/DownloadFinalFix.do?method=downloadFinalFix&uid=${group.finalFix.id}"
													titleKey="viewProjectDetails.box.FinalFix.Download.alt"><bean:message
													key="viewProjectDetails.box.FinalFix.Download" /></html:link>
											</td>
										</c:if>
										<c:if test="${empty group.finalFix}">
											<td class="value"><!-- @ --></td>
											<c:if test="${isAllowedToUploadFF}">
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/UploadFinalFix.do?method=uploadFinalFix&pid=${project.id}"><bean:message
														key="viewProjectDetails.box.FinalFix.Upload" /></html:link></td>
											</c:if>
											<c:if test="${not isAllowedToUploadFF}">
												<td class="valueC"><bean:message key="Incomplete" /></td>
											</c:if>
										</c:if>
										<c:if test="${not empty group.finalReview}">
											<c:if test="${group.finalReview.committed}">
												<td class="valueC" nowrap="nowrap">${orfn:displayDate(pageContext.request, group.finalReview.modificationTimestamp)}</td>
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/ViewFinalReview.do?method=viewFinalReview&rid=${group.finalReview.id}"><bean:message
														key="viewProjectDetails.box.FinalReview.ViewResults" /></html:link></td>
											</c:if>
											<c:if test="${not group.finalReview.committed}">
												<td class="value"><!-- @ --></td>
												<c:if test="${isAllowedToPerformFinalReview}">
													<td class="valueC" nowrap="nowrap"><html:link
														page="/actions/EditFinalReview.do?method=editFinalReview&rid=${group.finalReview.id}"><b><bean:message
														key="viewProjectDetails.box.FinalReview.Submit" /></b></html:link></td>
												</c:if>
												<c:if test="${not isAllowedToPerformFinalReview}">
													<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
												</c:if>
											</c:if>
										</c:if>
										<c:if test="${empty group.finalReview}">
											<td class="value"><!-- @ --></td>
											<td class="valueC" nowrap="nowrap"><bean:message key="NotAvailable" /></td>
										</c:if>
									</tr>
								</c:if>
								<tr>
									<td class="lastRowTD" colspan="5"><!-- @ --></td>
								</tr>
							</table>
						</c:when>
						<c:when test='${group.appFunc == "APPROVAL"}'>
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="3">${group.tableName}</td>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Approval.Date" arg0="${group.groupIndex}" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Approval.Approval" arg0="${group.groupIndex}" /></td>
								</tr>
								<c:set var="winningSubmission" value="" />
								<c:forEach items="${group.submissions}" var="submission">
									<c:if test="${(not empty group.winner) and (group.winner.id == submission.upload.owner)}">
										<c:set var="winningSubmission" value="${submission}" />
									</c:if>
								</c:forEach>
								<c:if test="${not empty winningSubmission}">
									<tr class="light">
										<td class="value" nowrap="nowrap">
											<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" border="0" styleClass="Outline" />
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${winningSubmission.upload.id}"
												titleKey="viewProjectDetails.box.Submission.Download">${winningSubmission.id}</html:link>
											(<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
										</td>
										<c:if test="${not empty group.approval}">
											<c:if test="${group.approval.committed}">
												<td class="valueC" nowrap="nowrap">${orfn:displayDate(pageContext.request, group.approval.modificationTimestamp)}</td>
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/ViewApproval.do?method=viewApproval&rid=${group.approval.id}"><bean:message
														key="viewProjectDetails.box.Approval.ViewResults" /></html:link></td>
											</c:if>
											<c:if test="${not group.approval.committed}">
												<td class="value"><!-- @ --></td>
												<c:if test="${isAllowedToPerformApproval}">
													<td class="valueC" nowrap="nowrap">
														<html:link page="/actions/EditApproval.do?method=editApproval&rid=${group.approval.id}"><bean:message
															key="viewProjectDetails.box.Approval.Submit" /></html:link></td>
												</c:if>
												<c:if test="${not isAllowedToPerformApproval}">
													<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
												</c:if>
											</c:if>
										</c:if>
										<c:if test="${empty group.approval}">
											<td class="value"><!-- @ --></td>
											<c:if test="${isAllowedToPerformApproval}">
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/CreateApproval.do?method=createApproval&sid=${winningSubmission.id}"><bean:message
														key="viewProjectDetails.box.Approval.Submit" /></html:link></td>
											</c:if>
											<c:if test="${not isAllowedToPerformApproval}">
												<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
											</c:if>
										</c:if>
									</tr>
								</c:if>
								<tr>
									<td class="lastRowTD" colspan="3"><!-- @ --></td>
								</tr>
							</table>
						</c:when>
					</c:choose>
				</c:if>
			</div>
		</c:forEach>
	</div><br />

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
