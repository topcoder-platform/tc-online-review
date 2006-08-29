<%@ page language="java" isELIgnored="false" %>
<%
	int phaseIndex = ((Integer)request.getAttribute("phaseIndex")).intValue();
	String role = (String)request.getAttribute("role");

	if (phaseIndex == 1) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title">
							<b>My Role</b></td><td class="title"><b>My Deliverables</b></td><td class="title"><b>Outstanding Deliverables:</b>
						</td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Approval</b></td>
						<td class="value" width="40%" align="left" nowrap>There are no outstanding deliverables.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("submitter")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Submitter (<a href="#" class="coderTextYellow">henryouly</a>)</b><br>
							<b>Payment:</b> N/A
						</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Submission</b></td>
						<td class="value" width="40%" align="left" nowrap>There are no outstanding deliverables.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
	} else if (phaseIndex == 2) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title">
							<b>My Role</b></td><td class="title"><b>My Deliverables</b></td><td class="title"><b>Outstanding Deliverables:</b>
						</td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Approval</b></td>
						<td class="value" width="40%" align="left" nowrap>There are no outstanding deliverables.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("submitter")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left" nowrap><b>Submitter (<a href="#" class="coderTextYellow">henryouly</a>)</b><br>
							<b>Payment:</b> N/A
						</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8"> 00.00.0000 00:00 AM<b>Submission</b>
						</td>
						<td class="value" width="40%" align="left" nowrap>There are no outstanding deliverables.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("screener")) {
			String time = (String) request.getAttribute("time");
			if (time.equalsIgnoreCase("during")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Screener</b><br><b>Payment:</b> $500 (not paid)</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8"> 00.00.0000 00:00 AM <a href="../jsp/pc_add_screening_scorecard.jsp?role=screener&projectTabIndex=0"><b>Screening Scorecard</b></a>
						</td>
						<td class="value" width="40%" align="left" nowrap>00.00.0000 00:00 AM Screening Scorecard</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
			} else {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Screener</b><br>
							<b>Payment:</b> $500 (not paid)
						</td>
						<td class="value" width="45%" align="left" nowrap>
							00.00.0000 00:00 AM <!--<a href="pc-screener-screening_scorecard.jsp">--><b>Screening Scorecard</b><!--</a>-->
						</td>
						<td class="value" width="40%" align="left" nowrap>00.00.0000 00:00 AM Screening Scorecard</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
			}
		}
	} else if (phaseIndex == 3) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Approval</b></td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextGreen">qiucx0161:</a> Review 15655112 (<a href="#" class="coderTextYellow">mayi</a>)<br>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextYellow">mgmg:</a> Review 15655112 (<a href="#" class="coderTextYellow">mayi</a>)
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("submitter")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left" nowrap><b>Submitter (<a href="#" class="coderTextYellow">henryouly</a>)</b><br>
							<b>Payment:</b> N/A
						</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8"> 00.00.0000 00:00 AM <a href="pc_upload_submission.jsp?role=submitter&projectTabIndex=0"><b>Submission</b></a>
						</td>
						<td class="value" width="40%" align="left" nowrap>There are no outstanding deliverables.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("reviewer")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Reviewer - Stress</b><br><b>Payment:</b> $500 (not paid)</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" width="10" height="8" class="Outline" alt="Completed"> 00.00.0000 00:00 AM&nbsp; <b> Test Case Upload - Stress</b>
							[ <a href="pc_upload_testcase.jsp?role=reviewer&projectTabIndex=0" title="Upload New Test Case">Update </a>]<br>
							<img border="0" src="../i/icon_authorization.gif" width="10" height="8" class="Outline" alt="Completed"> 00.00.0000 00:00 AM&nbsp; <b>Review Scorecard 11889718</b><br>
							<img border="0" src="../i/icon_authorization.gif" width="10" height="8" class="Outline" alt="Completed"> 00.00.0000 00:00 AM&nbsp; <b>Review Scorecard 14818660</b><br>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline Near" width="10" height="10">
							00.00.0000 00:00 AM&nbsp; <b><a href="pc_edit_scorecard.jsp?role=reviewer&projectTabIndex=0">
							Review Scorecard 15655112</a></b><br>
						</td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <b><a href="#" class="coderTextGreen">qiucx0161</a>:</b> Review 15655112 (<a href="#" class="coderTextYellow">mayi</a>)<br>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <b><a href="#" class="coderTextYellow">mgmg</a>:</b> Review 15655112 (<a href="#" class="coderTextYellow">mayi</a>)
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
%>
<%
	} else if (phaseIndex == 4) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Approval</b></td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextGreen">qiucx0161:</a> Review 15655112 (<a href="#" class="coderTextYellow">mayi</a>)<br>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextYellow">mgmg:</a> Review 15655112 (<a href="#" class="coderTextYellow">mayi</a>)
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
	    } else if (role.equalsIgnoreCase("public")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Public</b></td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM  Approval Scorecard
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="2"></td>
					</tr>
				</table>
<%
	    } else if (role.equalsIgnoreCase("submitter")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Submitter (</b><a href="#" class="coderTextYellow">henryouly</a><b>)</b><br>
							<b>Payment:</b> N/A
						</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8"> 00.00.0000 00:00 AM <b>Submission</b>
						</td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10">
							00.00.0000 00:00 AM <b>Review - Stress </b>(<a href="#" class="coderTextGreen">qiucx0161</a>)<b><br>
							</b>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10">
							00.00.0000 00:00 AM <b>Review - Failure </b>(<a href="#" class="coderTextYellow">mgmg</a>)
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
%>
<%
	} else if (phaseIndex == 5) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title">
							<b>My Role</b></td><td class="title"><b>My Deliverables</b></td><td class="title"><b>Outstanding Deliverables:</b>
						</td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Approval</b></td>
						<td class="value" width="40%" align="left" nowrap>There are no outstanding deliverables.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("submitter")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Submitter (</b><a href="#" class="coderTextYellow">henryouly</a><b>)</b><br>
							<b>Payment:</b> N/A
						</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8"> 00.00.0000 00:00 AM <b>Submission</b>
						</td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10">
							00.00.0000 00:00 AM <b>Review - Stress </b>(<a href="#" class="coderTextGreen">qiucx0161</a>)<b><br>
							</b>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10">
							00.00.0000 00:00 AM <b>Review - Failure </b>(<a href="#" class="coderTextYellow">mgmg</a>)
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
%>
<%
	} else if (phaseIndex == 6) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Approval</b></td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_warning.gif" class="Outline" alt="Late" width="10" height="10"> 00.00.00 00:00 AM<a href="#" class="coderTextYellow"> henryouly</a>: Final Fix
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("submitter")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Submitter (</b><a href="#" class="coderTextYellow">henryouly</a><b>)</b><br>
							<b>Payment:</b> $2000 (not paid)
						</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8"> 00.00.0000 00:00 AM Submission<br>
							<img border="0" src="../i/icon_warning.gif" class="Outline" alt="Late" width="10" height="10"> 00.00.0000 00:00 AM <b>
							<a href="pc_aggregation_results2.jsp?role=submitter&projectTabIndex=0">Approve Aggregation</a></b><br>
							<img border="0" src="../i/icon_warning.gif" class="Outline" alt="Late" width="10" height="10"> 00.00.0000 00:00 AM <a href="pc_upload_finalfix.jsp?role=submitter&projectTabIndex=0"><b>Final Fix</b></a>
						</td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_warning.gif" class="Outline" alt="Late" width="10" height="10"> 00.00.0000 00:00 AM <b> Approve Aggregation (</b><a href="#" class="coderTextYellow">henryouly</a><b>)<br></b>
							<img border="0" src="../i/icon_warning.gif" class="Outline" alt="Late" width="10" height="10"> 00.00.0000 00:00 AM <b>Final Fix</b>
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("aggregator")) {
%>
				<table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
					<tr>
						<td class="title">My Role</td>
						<td class="title">My Deliverables</td>
						<td class="title">Outstanding Deliverables:</td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left" nowrap><b>Aggregator<br>Payment: </b>$500 (not paid)</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_authorization.gif" class="star"alt="Complete" width="10" height="8"> 00.00.0000 00:00 AM <b>Aggregation Scorecard</b>
						</td>
						<td class="value" width="40%" align="left" nowrap>There are no outstanding deliverables.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
%>
<%
	} else if (phaseIndex == 7) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>00.00.0000 00:00 AM <b>Approval</b></td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextOrange">ivern</a>: Approval
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("finalReviewer")) {
%>
				<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left" nowrap><b>Final Reviewer<br>Payment: </b>$500 (not paid)</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.0000 00:00 AM  <a href="#"><b>Final Review 11889718</b></a>
						</td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.0000 00:00 AM  <b>Final Review 11889718</b>
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("observer")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Observer</b></td>
						<td class="value" width="45%" align="left" nowrap>You have no deliverables for this project.</td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextOrange">ivern</a>: Approval Scorecard
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
%>
<%
	} else if ((phaseIndex == 8) || (phaseIndex == 9)) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.0000 00:00 AM <a href="pc-approver-approval_scorecard.jsp"><b>Approval</b></a>
						</td>
						<td class="value" width="40%" align="left" nowrap><img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM<b> </b><a href="#" class="coderTextOrange">ivern</a>: Approval
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("approver")) {
%>
				<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>
							00.00.0000 00:00 AM  <a href="pc-approver-approval_scorecard.jsp"><b>Approval Scorecard 11889718</b></a>
						</td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM  Approval Scorecard
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("observer")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Observer</b></td>
						<td class="value" width="45%" align="left" nowrap>You have no deliverables for this project.</td>
						<td class="value" width="40%" align="left" nowrap>&nbsp;</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("public")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title">&nbsp<!--<b>My Deliverables</b>--></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Public</b></td>
						<td class="value" width="45%" align="left" nowrap><!--   --></td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextOrange">ivern</a>: Approval Scorecard</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		} else if (role.equalsIgnoreCase("designer")) {
%>
				<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title">&nbsp<!--<b>My Deliverables</b>--></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Designer</b></td>
						<td class="value" width="45%" align="left" nowrap><!--   --></td>
						<td class="value" width="40%" align="left" nowrap>
							<img border="0" src="../i/icon_notification.gif" class="star" alt="Deadline near" width="10" height="10"> 00.00.00 00:00 AM <a href="#" class="coderTextOrange">ivern</a>: Approval Scorecard
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
%>
<%
	} else if (phaseIndex == 10) {
		if (role.equalsIgnoreCase("manager")) {
%>
				<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="title"><b>My Role</b></td>
						<td class="title"><b>My Deliverables</b></td>
						<td class="title"><b>Outstanding Deliverables:</b></td>
					</tr>
					<tr class="light">
						<td class="value" width="15%" align="left"><b>Manager/Approver<br>Payment: </b>N/A</td>
						<td class="value" width="45%" align="left" nowrap>&nbsp;</td>
						<td class="value" width="40%" align="left" nowrap>None.<br>This project is complete.</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"></td>
					</tr>
				</table>
<%
		}
	}
%><br />