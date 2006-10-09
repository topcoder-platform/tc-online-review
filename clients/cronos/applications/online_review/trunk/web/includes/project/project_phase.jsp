<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
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
							<td class="title" colspan="5">${group.name}</td>
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
									<td class="title" colspan="5">${group.name}</td>
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
										<td class="value" nowrap="nowrap"><tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="component" /></td>
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
											<td class="value" nowrap="nowrap">${reliability}%</td>
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
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="7">${group.name}</td>
								</tr>
								<tr>
									<td class="header" colspan="2" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="header" width="22%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.Date" /></td>
									<td class="headerC" width="14%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.AutoScreening" /></td>
									<td class="header" width="15%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.Screener" /></td>
									<td class="headerC" width="14%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ScreeningScore" /></td>
									<td class="headerC" width="15%" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ScreeningResult" /></td>
								</tr>
								<c:forEach items="${group.submissions}" var="submission" varStatus="submissionStatus">
									<tr class='${(submissionStatus.index % 2 == 0) ? "light" : "dark"}'>
										<c:set var="submitter" value="" />
										<c:forEach items="${group.submitters}" var="curSubmitter">
											<c:if test="${curSubmitter.id == submission.upload.owner}">
												<c:set var="submitter" value="${curSubmitter}" />
											</c:if>
										</c:forEach>
										<td class="value" width="10%" nowrap="nowrap">
<%--											<html:img id="Out1" class="Outline" border="0" page="/i/plus.gif" width="9" height="9" style="margin-right:5px;" title="View Previous Submissions"> --%>
											<c:set var="placement" value="" />
											<c:if test="${!(empty submitter)}">
												<c:set var="placement" value='${submitter.allProperties["Placement"]}' />
											</c:if>
											<c:if test="${!(empty placement)}">
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
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${submission.upload.id}" titleKey="viewProjectDetails.box.Submission.Download">${submission.id}</html:link>
											<c:if test="${!(empty submitter)}">
												(<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}' context="component" />)
											</c:if>
										</td>
										<c:if test="${isManager == true}">
											<td class="value" width="5%"><html:link page="/actions/DeleteSubmission.do?method=deleteSubmission&uid=${submission.upload.id}"><html:img srcKey="viewProjectDetails.box.Submission.icoTrash.img" altKey="viewProjectDetails.box.Submission.icoTrash.alt" border="0" styleClass="Outline" /></html:link></td>
										</c:if>
										<c:if test="${isManager != true}">
											<td class="value"><!-- @ --></td>
										</c:if>
										<td class="value" width="22%">${submission.modificationTimestamp}</td>
										<c:set var="scrTask" value="${group.screeningTasks[submissionStatus.index]}" />
										<c:if test="${empty scrTask}">
											<td class="valueC" width="14%"><html:img page="/i/clear.gif" width="8" height="10" /></td>
										</c:if>
										<c:if test="${!(empty scrTask)}">
											<c:set var="scrTaskStatus" value="${scrTask.screeningStatus.name}" />
											<c:choose>
												<c:when test='${scrTaskStatus == "Passed"}'>
													<td class="valueC" width="14%">
														<html:link page="/actions/ViewAutoScreening.do?method=viewAutoScreening&uid=${submission.upload.id}"><html:img
															srcKey="viewProjectDetails.box.Submission.icoPassed.img"
															altKey="viewProjectDetails.box.Submission.icoPassed.alt" /></html:link></td>
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
													<td class="valueC" width="14%"><html:img page="/i/clear.gif" width="8" height="10" /></td>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:set var="screener" value="" />
										<c:forEach items="${group.reviewers}" var="reviewer">
											<c:if test="${(empty reviewer.submission) && (empty screener)}">
												<c:set var="screener" value="${reviewer}" />
											</c:if>
											<c:if test="${!(empty reviewer.submission) && (reviewer.submission == submission.id)}">
												<c:set var="screener" value="${reviewer}" />
											</c:if>
										</c:forEach>
										<c:if test="${empty screener}">
											<td class="value" width="15%">not assigned</td>
										</c:if>
										<c:if test="${!(empty screener)}">
											<td class="value" width="15%"><tc-webtag:handle coderId='${screener.allProperties["External Reference ID"]}' context="component" /></td>
										</c:if>
										<c:set var="review" value="" />
										<c:forEach items="${group.screenings}" var="screening">
											<c:if test="${screening.submission == submission.id}">
												<c:set var="review" value="${screening}" />
											</c:if>
										</c:forEach>
										<c:if test="${empty review}">
											<c:if test="${isAllowedToPerformScreening == true}">
												<td class="valueC" width="14%" nowrap="nowrap">
													<b><html:link page="/actions/CreateScreening.do?method=createScreening&sid=${submission.id}"><bean:message
														key="viewProjectDetails.box.Screening.Submit" /></html:link></b></td>
											</c:if>
											<c:if test="${isAllowedToPerformScreening != true}">
												<td class="valueC" width="14%"><bean:message key="Pending" /></td>
											</c:if>
											<td class="valueC" width="15%"><bean:message key="NotAvailable" /></td>
										</c:if>
										<c:if test="${!(empty review)}">
											<c:if test="${review.committed == true}">
												<c:if test="${isAllowedToViewScreening == true}">
													<td class="valueC" width="14%">
														<html:link page="/actions/ViewScreening.do?method=viewScreening&rid=${review.id}">${review.score}</html:link></td>
												</c:if>
												<c:if test="${isAllowedToViewScreening != true}">
													<td class="valueC" width="14%">${review.score}</td>
												</c:if>
												<c:if test="${review.score >= passingMinimum}">
													<td class="valueC" width="15%"><bean:message key="viewProjectDetails.box.Screening.Passed" /></td>
												</c:if>
												<c:if test="${review.score < passingMinimum}">
													<td class="valueC" width="15%"><bean:message key="viewProjectDetails.box.Screening.Failed" /></td>
												</c:if>
											</c:if>
											<c:if test="${review.committed != true}">
												<c:if test="${isAllowedToPerformScreening == true}">
													<td class="valueC" width="14%" nowrap="nowrap">
														<b><html:link page="/actions/EditScreening.do?method=editScreening&rid=${review.id}"><bean:message
															key="viewProjectDetails.box.Screening.Submit" /></html:link></b></td>
												</c:if>
												<c:if test="${isAllowedToPerformScreening != true}">
													<td class="valueC" width="14%"><bean:message key="Pending" /></td>
												</c:if>
												<td class="valueC" width="15%"><bean:message key="NotAvailable" /></td>
											</c:if>
										</c:if>
									</tr>
								</c:forEach>
								<tr>
									<td class="lastRowTD" colspan="7"><!-- @ --></td>
								</tr>
							</table>
						</c:when>
						<c:when test='${group.appFunc == "VIEW_REVIEWS"}'>
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<c:set var="colSpan" value="${(fn:length(group.reviewers) * 2) + 2}" />
								<c:if test="${isAllowedToEditHisReviews != true}">
									<c:set var="colSpan" value="${colSpan + 1}" />
								</c:if>
								<tr>
									<td class="title" colspan="${colSpan}">${group.name}</td>
								</tr>
								<tr>
									<td class="value" colspan="${(isAllowedToEditHisReviews == true) ? 2 : 3}"><!-- @ --></td>
									<c:forEach items="${group.reviewers}" var="reviewer">
										<td class="valueC" colspan="2" nowrap="nowrap">
											<b><bean:message key='ResourceRole.${fn:replace(reviewer.resourceRole.name, " ", "")}' />:</b>
											<tc-webtag:handle coderId='${reviewer.allProperties["External Reference ID"]}' context="component" />
											<c:set var="testCase" value="" />
											<c:forEach items="${group.testCases}" var="curTestCase">
												<c:if test="${curTestCase.owner == reviewer.id}">
													<c:set var="testCase" value="${curTestCase}" />
												</c:if>
											</c:forEach>
											<c:if test="${!(empty testCase)}">
												<html:link page="/actions/DownloadTestCase.do?method=downloadTestCase&uid=${testCase.id}"
													titleKey="viewProjectDetails.box.Review.TestCase.hint"><bean:message
														key="viewProjectDetails.box.Review.TestCase" /></html:link>
												<c:if test="${isAllowedToUploadTC == true}">
													[
													<html:link page="/actions/UploadTestCase.do?method=uploadTestCase&pid=${project.id}"
														titleKey="viewProjectDetails.box.Review.TestCase.Update.hint"><bean:message
															key="viewProjectDetails.box.Review.TestCase.Update" /></html:link>
													]
												</c:if>
											</c:if>
											<c:if test="${(empty testCase) && (isAllowedToUploadTC == true)}">
												<html:link page="/actions/UploadTestCase.do?method=uploadTestCase&pid=${project.id}"
													titleKey="viewProjectDetails.box.Review.TestCase.Upload.hint"><bean:message
														key="viewProjectDetails.box.Review.TestCase.Upload" /></html:link>
											</c:if>
										</td>
									</c:forEach>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" width="7%"><bean:message key="viewProjectDetails.box.Review.Date" /></td>
									<c:if test="${isAllowedToEditHisReviews != true}">
										<td class="headerC" width="12%"><bean:message key="viewProjectDetails.box.Review.Score" /></td>
									</c:if>
									<c:forEach items="${group.reviewers}" var="reviewer">
										<td class="headerC" width="8%"><bean:message key="viewProjectDetails.box.Review.Score.short" /></td>
										<td class="headerC" width="12%"><bean:message key="viewProjectDetails.box.Review.Appeals" /></td>
									</c:forEach>
								</tr>
								<c:forEach items="${group.submissions}" var="submission" varStatus="submissionStatus">
									<tr class='${(submissionStatus.index % 2 == 0) ? "light" : "dark"}'>
										<c:set var="submitter" value="" />
										<c:forEach items="${group.submitters}" var="curSubmitter">
											<c:if test="${curSubmitter.id == submission.upload.owner}">
												<c:set var="submitter" value="${curSubmitter}" />
											</c:if>
										</c:forEach>
										<td class="value" nowrap="nowrap">
											<c:set var="placement" value="" />
											<c:if test="${!(empty submitter)}">
												<c:set var="placement" value='${submitter.allProperties["Placement"]}' />
											</c:if>
											<c:if test="${!(empty placement)}">
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
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${submission.upload.id}"
												titleKey="viewProjectDetails.box.Submission.Download">${submission.id}</html:link>
											<c:if test="${!(empty submitter)}">
												(<tc-webtag:handle coderId='${submitter.allProperties["External Reference ID"]}' context="component" />)
											</c:if>
										</td>
										<td class="valueC" width="7%">${group.reviewDates[submissionStatus.index]}</td>
										<c:if test="${isAllowedToEditHisReviews != true}">
											<c:if test="${!(empty submitter)}">
												<td class="valueC" width="12%"><html:link page="/actions/ViewCompositeScorecard.do?method=viewCompositeScorecard&sid=${submission.id}">${submitter.allProperties["Final Score"]}</html:link></td>
											</c:if>
											<c:if test="${empty submitter}">
												<td class="valueC" width="12%"><bean:message key="Incomplete" /></td>
											</c:if>
										</c:if>
										<c:forEach items="${group.reviews[submissionStatus.index]}" var="review">
											<c:if test="${empty review}">
												<c:if test="${isAllowedToEditHisReviews == true}">
													<td class="valueC" width="8%"><html:link
														page="/actions/CreateReview.do?method=createReview&sid=${submission.id}"><b><bean:message
														key="viewProjectDetails.box.Review.Submit" /></b></html:link></td>
												</c:if>
												<c:if test="${isAllowedToEditHisReviews != true}">
													<td class="valueC" width="8%"><bean:message key="Pending" /></td>
												</c:if>
											</c:if>
											<c:if test="${!(empty review)}">
												<c:if test="${review.committed == true}">
													<td class="valueC" width="8%"><html:link
														page="/actions/ViewReview.do?method=viewReview&rid=${review.id}">${review.score}</html:link></td>
												</c:if>
												<c:if test="${review.committed != true}">
													<c:if test="${isAllowedToEditHisReviews == true}">
														<td class="valueC" width="8%"><html:link
															page="/actions/EditReview.do?method=editReview&rid=${review.id}"><b><bean:message
															key="viewProjectDetails.box.Review.Submit" /></b></html:link></td>
													</c:if>
													<c:if test="${isAllowedToEditHisReviews != true}">
													<td class="valueC" width="8%"><bean:message key="Pending" /></td>
													</c:if>
												</c:if>
											</c:if>
											<td class="valueC" width="12%" nowrap="nowrap">[
												<html:link page="/actions/ViewReview.do?method=viewReview&rid=${review.id}">x</html:link> /
												<html:link page="/actions/ViewReview.do?method=viewReview&rid=${review.id}">y</html:link>
												]</td>
										</c:forEach>
									</tr>
								</c:forEach>
								<tr>
									<td class="lastRowTD" colspan="${colSpan}"><!-- @ --></td>
								</tr>
							</table>
						</c:when>
						<c:when test='${group.appFunc == "AGGREGATION"}'>
							<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="5">${group.name}</td>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Aggregation.Date" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Aggregation.Aggregation" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.AggregationReview.Date" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.AggregationReview.Review" /></td>
								</tr>
								<c:set var="winningSubmission" value="" />
								<c:forEach items="${group.submissions}" var="submission">
									<c:if test="${!(empty group.winner) && (group.winner.id == submission.upload.owner)}">
										<c:set var="winningSubmission" value="${submission}" />
									</c:if>
								</c:forEach>
								<c:if test="${!(empty winningSubmission)}">
									<tr class="light">
										<td class="value" nowrap="nowrap">
											<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" border="0" styleClass="Outline" />
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${winningSubmission.upload.id}"
												titleKey="viewProjectDetails.box.Submission.Download">${winningSubmission.upload.id}</html:link>
											(<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="component" />)
										</td>
										<c:if test="${!(empty group.aggregation)}">
											<c:if test="${group.aggregation.committed == true}">
												<td class="valueC">${group.aggregation.modificationTimestamp}</td>
												<td class="valueC" nowrap="nowrap"><html:link
													page="/actions/ViewAggregation.do?method=viewAggregation&rid=${group.aggregation.id}"><bean:message
													key="viewProjectDetails.box.Aggregation.ViewResults" /></html:link></td>
											</c:if>
											<c:if test="${group.aggregation.committed != true}">
												<td class="value"><!-- @ --></td>
												<c:if test="${isAllowedToPerformAggregation == true}">
													<td class="valueC" nowrap="nowrap"><html:link
														page="/actions/EditAggregation.do?method=editAggregation&rid=${group.aggregation.id}"><b><bean:message
														key="viewProjectDetails.box.Aggregation.Submit" /></b></html:link></td>
												</c:if>
												<c:if test="${isAllowedToPerformAggregation != true}">
													<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
												</c:if>
											</c:if>
											<c:if test="${group.aggregationReviewCommitted == true}">
												<td class="valueC">${group.aggregation.modificationTimestamp}</td>
												<td class="valueC" nowrap="nowrap"><html:link
													page="/actions/ViewAggregationReview.do?method=viewAggregationReview&rid=${group.aggregation.id}"><bean:message
													key="viewProjectDetails.box.Aggregation.ViewResults" /></html:link></td>
											</c:if>
											<c:if test="${group.aggregationReviewCommitted != true}">
												<td class="value"><!-- @ --></td>
												<c:if test="${isAllowedToPerformAggregationReview == true}">
													<td class="valueC" nowrap="nowrap"><html:link
														page="/actions/EditAggregationReview.do?method=editAggregationReview&rid=${group.aggregation.id}"><b><bean:message
														key="viewProjectDetails.box.AggregationReview.Submit" /></b></html:link></td>
												</c:if>
												<c:if test="${isAllowedToPerformAggregationReview != true}">
													<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
												</c:if>
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
									<td class="title" colspan="5">${group.name}</td>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalFix.Date" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalFix.Fix" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalReview.Date" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.FinalReview.Review" /></td>
								</tr>
								<c:set var="winningSubmission" value="" />
								<c:forEach items="${group.submissions}" var="submission">
									<c:if test="${!(empty group.winner) && (group.winner.id == submission.upload.owner)}">
										<c:set var="winningSubmission" value="${submission}" />
									</c:if>
								</c:forEach>
								<c:if test="${!(empty winningSubmission)}">
									<tr class="light">
										<td class="value" nowrap="nowrap">
											<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" border="0" styleClass="Outline" />
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${winningSubmission.upload.id}"
												titleKey="viewProjectDetails.box.Submission.Download">${winningSubmission.upload.id}</html:link>
											(<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="component" />)
										</td>
										<c:if test="${!(empty group.finalFix)}">
											<td class="valueC" nowrap="nowrap">${group.finalFix.modificationTimestamp}</td>
											<td class="valueC" nowrap="nowrap">
												<html:link page="/actions/DownloadFinalFix.do?method=downloadFinalFix&uid=${group.finalFix.id}"
													titleKey="viewProjectDetails.box.FinalFix.Download.alt"><bean:message
													key="viewProjectDetails.box.FinalFix.Download" /></html:link>
												<c:if test="${isAllowedToUploadFF == true}">
													[
													<html:link page="/actions/UploadFinalFix.do?method=uploadFinalFix&pid=${project.id}"
														titleKey="viewProjectDetails.box.FinalFix.Update.alt"><bean:message
														key="viewProjectDetails.box.FinalFix.Update" /></html:link>
													]
												</c:if>
											</td>
										</c:if>
										<c:if test="${empty group.finalFix}">
											<td class="value"><!-- @ --></td>
											<c:if test="${isAllowedToUploadFF == true}">
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/UploadFinalFix.do?method=uploadFinalFix&pid=${project.id}"><bean:message
														key="viewProjectDetails.box.FinalFix.Upload" /></html:link></td>
											</c:if>
											<c:if test="${isAllowedToUploadFF != true}">
												<td class="valueC"><bean:message key="Incomplete" /></td>
											</c:if>
										</c:if>
										<c:if test="${!(empty group.finalReview)}">
											<c:if test="${group.finalReview.committed == true}">
												<td class="valueC" nowrap="nowrap">${group.finalReview.modificationTimestamp}</td>
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/ViewFinalReview.do?method=viewFinalReview&rid=${group.finalReview.id}"><bean:message
														key="viewProjectDetails.box.FinalReview.ViewResults" /></html:link></td>
											</c:if>
											<c:if test="${group.finalReview.committed != true}">
												<td class="value"><!-- @ --></td>
												<c:if test="${isAllowedToPerformFinalReview == true}">
													<td class="valueC" nowrap="nowrap"><html:link
														page="/actions/EditFinalReview.do?method=editFinalReview&rid=${group.finalReview.id}"><b><bean:message
														key="viewProjectDetails.box.FinalReview.Submit" /></b></html:link></td>
												</c:if>
												<c:if test="${isAllowedToPerformFinalReview != true}">
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
									<td class="title" colspan="3">${group.name}</td>
								</tr>
								<tr>
									<td class="header" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Submission.ID" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Approval.Date" /></td>
									<td class="headerC" nowrap="nowrap"><bean:message key="viewProjectDetails.box.Approval.Approval" /></td>
								</tr>
								<c:set var="winningSubmission" value="" />
								<c:forEach items="${group.submissions}" var="submission">
									<c:if test="${!(empty group.winner) && (group.winner.id == submission.upload.owner)}">
										<c:set var="winningSubmission" value="${submission}" />
									</c:if>
								</c:forEach>
								<c:if test="${!(empty winningSubmission)}">
									<tr class="light">
										<td class="value" nowrap="nowrap">
											<html:img srcKey="viewProjectDetails.Submitter.icoWinner.img" altKey="viewProjectDetails.Submitter.icoWinner.alt" border="0" styleClass="Outline" />
											<html:link page="/actions/DownloadSubmission.do?method=downloadSubmission&uid=${winningSubmission.upload.id}"
												titleKey="viewProjectDetails.box.Submission.Download">${winningSubmission.upload.id}</html:link>
											(<tc-webtag:handle coderId='${group.winner.allProperties["External Reference ID"]}' context="component" />)
										</td>
										<c:if test="${!(empty group.approval)}">
											<c:if test="${group.approval.committed == true}">
												<td class="valueC" nowrap="nowrap">${group.approval.modificationTimestamp}</td>
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/ViewApproval.do?method=viewApproval&rid=${group.approval.id}"><bean:message
														key="viewProjectDetails.box.Approval.ViewResults" /></html:link></td>
											</c:if>
											<c:if test="${group.approval.committed != true}">
												<td class="value"><!-- @ --></td>
												<c:if test="${isAllowedToPerformApproval == true}">
													<td class="valueC" nowrap="nowrap">
														<html:link page="/actions/EditApproval.do?method=editApproval&rid=${group.approval.id}"><bean:message
															key="viewProjectDetails.box.Approval.Submit" /></html:link></td>
												</c:if>
												<c:if test="${isAllowedToPerformApproval != true}">
													<td class="valueC" nowrap="nowrap"><bean:message key="Pending" /></td>
												</c:if>
											</c:if>
										</c:if>
										<c:if test="${empty group.approval}">
											<td class="value"><!-- @ --></td>
											<c:if test="${isAllowedToPerformApproval == true}">
												<td class="valueC" nowrap="nowrap">
													<html:link page="/actions/CreateApproval.do?method=createApproval&sid=${winningSubmission.id}"><bean:message
														key="viewProjectDetails.box.Approval.Submit" /></html:link></td>
											</c:if>
											<c:if test="${isAllowedToPerformApproval != true}">
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
		if (aobject.blur) {
			aobject.blur();
		}
		return false;
	}
//-->
</script>
