<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
	<title><bean:message key="OnlineReviewApp.title" /></title>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="../css/coders.css" />
	<link type="text/css" rel="stylesheet" href="../css/tcStyles.css" />

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="../css/new_styles.css" />
	<script language="JavaScript" type="text/javascript" src="../scripts/rollovers.js"><!-- @ --></script>

	<script language="JavaScript" type="text/javascript" src="../scripts/dojo.js"><!-- @ --></script>
	<script language="JavaScript" type="text/javascript" src="../scripts/util.js"><!-- @ --></script>

	<script language="JavaScript" type="text/javascript">
		/*
		 * This function adds a new row to resources table.
		 */
		function addNewResource() {
			// Get resources table node
			var resourcesTable = document.getElementById("resources_tbl");
			// Get the number of rows in table
			var rowCount = resourcesTable.rows.length;
			// Create a new row into resources table
			var newRow = resourcesTable.rows[2].cloneNode(true);
			// Rows should vary colors
			if (rowCount % 2 == 0) {
				newRow.className = "dark";
			} else {
				newRow.className = "light";
			}
			// Insert new row into resources table
			resourcesTable.tBodies[0].insertBefore(newRow, resourcesTable.rows[rowCount - 1]);
			// Make delete button visible and hide add button
			var images = newRow.cells[4].getElementsByTagName("img");
			images[0].style["display"] = "none";
			images[1].removeAttribute("style");
			// Retrieve hidden inputs
			var inputs = newRow.cells[4].getElementsByTagName("input");
			// Set hidden resources_action parameter to "add"
			var actionInput = inputs[0];
			actionInput.value = "add";

		}

		/*
		 * This function deletes the exisiting row from the resources table.
		 */
		function deleteResource(resourceRowNode) {
			// Retrieve hidden inputs
			var inputs = resourceRowNode.cells[4].getElementsByTagName("input");
			// Check if the resource to be deleted has been persisted
			var id = inputs[1].value;
			if (id != "-1") {
				// Delete the resources table row,
				// as the appropriate resource has not been persisted in DB.
				resourceRowNode.parentNode.removeChild(resourceRowNode);

			} else {
				// Hide the row, don't delete it as the resource
				// should be deleted from DB on submit
				resourceRowNode.style["display"] = "none";

				// Set hidden resources_action parameter to "delete"
				var actionInput = inputs[0];
				actionInput.value = "delete";
			}

			// TODO: Complete it, doesn't work for some reason
			// Make rows vary color
			var rows = resourceRowNode.parentNode.getElementsByTagName("tr");
			for (var i = 0; i < rows.length; i++) {
				if (i % 2 == 1) {
					rows[i].className = "dark";
				} else {
					rows[i].className = "light";
				}
			}

		}


		/*
		 * This function populates the specified parameter of the newly added phase row.
		 */
		function populatePhaseParam(destRowNode, srcTableNode, paramName) {
			var srcInput = getChildByName(srcTableNode, "addphase_" + paramName);
			var destInput = getChildByName(destRowNode, "phase_" + paramName);
			if (destInput.tagName != "SELECT" && destInput.type == "checkbox") {
				destInput.checked = srcInput.checked;
			} else {
				destInput.value = srcInput.value;
			}
		}

		/*
		 * This function adds new phase to phases table, it includes addition of several rows.
		 */
		function addNewPhase() {
			// Retrieve timeline and add phase tables
			var timelineTable = document.getElementById("timeline_tbl");
			var addPhaseTable = document.getElementById("addphase_tbl");

			// Retrieve phase name
			var phaseNameInput = getChildByName(addPhaseTable, "addphase");
			var selectedOption = phaseNameInput.options[phaseNameInput.selectedIndex];
			var phaseName = dojo.dom.textContent(selectedOption);

			// Generate phase id (for use in the DOM)
			var phaseId = dojo.dom.getUniqueId();

			// Add the name of the added phase to the select options for add phase form
			var whereCombo = getChildByName(addPhaseTable, "addphase_where");
			whereCombo.add(new Option(phaseName, phaseId), null);
			var startPhaseCombo = getChildByName(addPhaseTable, "addphase_start_phase");
			startPhaseCombo.add(new Option(phaseName, phaseId), null);
			// Also add it to the phase rows
			var startPhaseCombos = document.getElementsByName("phase_start_phase");
			for (var i = 0; i < startPhaseCombos.length; i++) {
				startPhaseCombos[i].add(new Option(phaseName, phaseId), null);
			}

			// Create a new row to reprsent the phase
			var newRow = timelineTable.rows[1].cloneNode(true);
			// Assign the id
			newRow.id = phaseId;
			// Remove "display: none;"
			newRow.style["display"] = "";
			// Add the row to the appropriate position
			var wherePhaseId = whereCombo.value;
			if (wherePhaseId == "") {
				// No phase selected, append to the end of the list
				timelineTable.tBodies[0].appendChild(newRow);
			} else {
				// Find the reference phase row
				var wherePhaseNode = document.getElementById(wherePhaseId);

				if (getChildByName(addPhaseTable, "addphase_when").value == "before") {
					dojo.dom.insertBefore(newRow, wherePhaseNode);
				} else {
					dojo.dom.insertAfter(newRow, wherePhaseNode);
				}
			}


			// Populate newly created phase inputs from the add phase form
			var inputNames = ["start_date", "start_time", "start_AMPM",
				"start_by_phase", "start_phase", "start_when",
				"start_plusminus", "start_amount", "start_dayshrs",
				"end_date", "end_time", "end_AMPM", "duration"];
			for (var i = 0; i < inputNames.length; i++) {
				populatePhaseParam(newRow, addPhaseTable, inputNames[i]);
			}

			// Populate phase name
			var phaseNameCell =  newRow.cells[0];
			dojo.dom.textContent(phaseNameCell, phaseName);
		}
	</script>
</head>

<body>
	<jsp:include page="../includes/inc_header.jsp" />

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins-->
			<td width="180">
				<jsp:include page="../includes/inc_leftnav.jsp" />
			</td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><img src="../i/clear.gif" width="15" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="../includes/project/project_tabs.jsp" />

				<div id="mainMiddleContent">
					<html:form action="/actions/SaveProject">
						<input type="hidden" name="method" value="saveProject" />
						<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
    					<tr>
								<td class="title" colspan="2"><bean:message key="editProject.ProjectDetails.title" /></td>
							</tr>
							<tr>
								<td class="valueB"><bean:message key="editProject.ProjectDetails.Name" /></td>
								<td class="value" nowrap="nowrap"><html:text styleClass="inputBox" property="project_name" style="width: 350px;" /></td>
							</tr>
							<tr class="dark">
								<td width="9%" class="valueB"><bean:message key="editProject.ProjectDetails.Type" /></td>
								<td width="91%" class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="project_type" style="width:150px">
										<c:forEach items="${projectTypes}" var="type">
											<html:option key='ProjectType.${fn:replace(type.name, " ", "")}.plural' value="${type.id}" />
										</c:forEach>
									</html:select>
								</td>
							</tr>
							<tr class="light">
								<td class="valueB"><bean:message key="editProject.ProjectDetails.Category" /></td>
								<td class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="project_category" style="width:150px;">
										<c:forEach items="${projectCategories}" var="category">
											<html:option key='ProjectCategory.${fn:replace(category.name, " ", "")}' value="${category.id}" />
										</c:forEach>
									</html:select>
								</td>
							</tr>
							<tr class="dark">
								<td class="valueB"><bean:message key="editProject.ProjectDetails.Eligibility" /></td>
								<td class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="eligibility" style="width:150px;">
										<html:option key="ProjectEligibility.Open" value="Open" />
										<html:option key="ProjectEligibility.TopCoderPrivate" value="TopCoder Private" />
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="valueB"><bean:message key="editProject.ProjectDetails.Public" /></td>
								<td class="value" nowrap="nowrap">
									<html:checkbox property="public" />
								</td>
							</tr>
							<tr>
								<td class="lastRowTD" colspan="2"><!-- @ --></td>
							</tr>
						</table><br />

						<table class="scorecard" id="preferences">
							<tr>
								<td class="title" colspan="2"><bean:message key="editProject.Preferences.title" /></td>
							</tr>
							<tr class="light">
								<td class="valueB" width="1%"><bean:message key="editProject.Preferences.Autopilot" /><br /></td>
								<td class="value">
									<html:radio property="autopilot" value="true" />
									<b><bean:message key="editProject.Preferences.Autopilot.Completion" /></b>
									<bean:message key="editProject.Preferences.Autopilot.Completion.Desc" /><br/>
									<html:radio property="autopilot" value="false" />
									<b><bean:message key="editProject.Preferences.Autopilot.TurnOff" /></b>
								</td>
							</tr>
							<tr class="dark">
								<td class="value" colspan="2">
									<html:checkbox property="email_notifications" /><b><bean:message key="editProject.Preferences.SendEmails" /></b>
									<bean:message key="editProject.Preferences.SendEmails.Desc" /><br />
									<html:checkbox property="no_rate_project" /><b><bean:message key="editProject.Preferences.DoNotRate" /><br />
									<html:checkbox property="timeline_notifications" /><b><bean:message key="editProject.Preferences.ReceiveTimeline" /></b>
								</td>
							</tr>
							<tr>
								<td class="lastRowTD" colspan="2"><!-- @ --></td>
							</tr>
						</table><br />

						<table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
							<tr>
								<td class="title" width="1%"><bean:message key="editProject.Scorecards.title" /></td>
								<td class="title" width="99%"><!-- @ --></td>
							</tr>
							<tr class="light">
								<td class="valueB" nowrap="nowrap"><bean:message key="editProject.Scorecards.Screening" /></td>
								<td class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="scorecard_screening" style="width:350px;">
										<html:option key="Answer.Select" value="" />
										<html:option value="">Component Design Screening Scorecard v1.0</html:option>
									</html:select>
								</td>
							</tr>
							<tr class="dark">
								<td class="valueB" nowrap="nowrap"><bean:message key="editProject.Scorecards.Review" /></td>
								<td class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="scorecard_review" style="width:350px;">
										<html:option key="Answer.Select" value="" />
										<html:option value="">Component Design Review Scorecard v1.4.2</html:option>
									</html:select>
								</td>
							</tr>
							<tr class="light">
								<td class="valueB" nowrap="nowrap"><bean:message key="editProject.Scorecards.Approval" /></td>
								<td class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="scorecard_approval" style="width:350px;">
										<html:option key="Answer.Select" value="" />
										<html:option key="NotAvailable" value="N/A" />
										<html:option value="">Component Design Review Scorecard v1.4.2</html:option>
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="lastRowTD" colspan="2"><!-- @ --></td>
							</tr>
						</table><br />

						<table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
							<tr>
								<td class="title" width="1%"><bean:message key="editProject.References.title" /></td>
								<td class="title" width="99%"><!-- @ --></td>
							</tr>
							<tr class="light">
								<td class="value" nowrap="nowrap">
									<b><bean:message key="editProject.References.ForumName" /></b><br />
								</td>
								<td class="value" nowrap="nowrap">
									<html:text styleClass="inputBox" property="forum_name" style="width: 350px;" />
								</td>
							</tr>
							<tr class="dark">
								<td class="value" nowrap="nowrap">
									<b><bean:message key="editProject.References.SVNModule" /></b><br />
								</td>
								<td class="value" nowrap="nowrap">
									<html:text styleClass="inputBox" property="SVN_module" style="width: 350px;" />
								</td>
							</tr>
							<tr>
								<td class="lastRowTD" colspan="2"><!-- @ --></td>
							</tr>
						</table><br />

						<table class="scorecard" cellpadding="0" cellspacing="0" width="100%"style="border-collapse: collapse;">
							<tr>
								<td class="title"><bean:message key="editProject.Notes.title" /></td>
							</tr>
							<tr class="light">
								<td class="value">
									<html:textarea styleClass="inputTextBox" property="notes" />
								</td>
							</tr>
							<tr>
								<td class="lastRowTD"><!-- @ --></td>
							</tr>
						</table><br />

						<table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
							<tr>
								<td class="title" width="1%" nowrap="nowrap"><bean:message key="editProject.CreateTimeline.title" /></td>
								<td class="title" width="99%"><!-- @ --></td>
							</tr>
							<tr class="light">
								<td class="value" width="2%" nowrap="nowrap"><b><bean:message key="editProject.CreateTimeline.UseTemplate" /></b></td>
								<td class="value">
									<select class="inputBox" name="template" id="template" style="width:100px;">
										<option>Select</option>
										<option>Design</option>
										<option>Development</option>
										<option>Security</option>
										<option>Process</option>
									</select>
									<a href="pc-manager-create_project.jsp?L=1"><img src="../i/bttn_load_template.gif" name="load_template" id="load_template" /></a>
								</td>
							</tr>
						</table><br />

						<table class="scorecard" id="timeline_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
							<tr>
								<td class="header"><bean:message key="editProject.Phases.PhaseName" /></td>
								<td class="header"><bean:message key="editProject.Phases.PhaseStart" /></td>
								<td class="header"><bean:message key="editProject.Phases.PhaseEnd" /></td>
								<td class="header" nowrap="nowrap"><bean:message key="editProject.Phases.Duration" /></td>
								<td class="header"><bean:message key="editProject.Phases.Delete" /></td>
							</tr>

							<%-- The following three rows are used as a "template" for the newly added project phases --%>
							<tr class="dark" style="display: none;">
								<td class="valueB" nowrap="nowrap"><!-- @ --></td>
								<td class="value" nowrap>
									<html:text styleClass="inputBoxDate" property="phase_start_date" value="" />
									<html:text styleClass="inputBoxTime" property="phase_start_time" value="" />
									<html:select styleClass="inputBox" property="phase_start_AMPM">
										<html:option key="editProject.Phases.AM" value="am" />
										<html:option key="editProject.Phases.PM" value="pm" />
									</html:select>
									<bean:message key="global.Timezone.EST" /><br />
									<html:checkbox property="phase_start_by_phase" />
									<bean:message key="editProject.Phases.When" />
									<html:select styleClass="inputBox" property="phase_start_phase" style="width:120px;">
										<html:option key="editProject.Phases.SelectPhase" value="" />
									</html:select>
									<html:select styleClass="inputBox" property="phase_start_when">
										<html:option key="editProject.Phases.Starts" value="starts" />
										<html:option key="editProject.Phases.Ends" value="ends" />
									</html:select>
									<html:select styleClass="inputBox" property="phase_start_plusminus">
										<html:option value="plus">+</html:option>
										<html:option value="minus">-</html:option>
									</html:select>
									<html:text styleClass="inputBox" property="phase_start_amount" value="" style="width:30px;" />
									<html:select styleClass="inputBox" property="phase_start_dayshrs">
										<html:option key="editProject.Phases.Days" value="days" />
										<html:option key="editProject.Phases.Hrs" value="hrs" />
									</html:select>
								</td>
								<td class="value" nowrap="nowrap">
									<html:text styleClass="inputBoxDate" property="phase_end_date" value="" />
									<html:text styleClass="inputBoxTime" property="phase_end_time" value="" />
									<html:select styleClass="inputBox" property="phase_end_AMPM">
										<html:option key="editProject.Phases.AM" value="am" />
										<html:option key="editProject.Phases.PM" value="pm" />
									</html:select>
									<bean:message key="global.Timezone.EST" />
								</td>
								<td class="value">
									<html:text styleClass="inputBoxDuration" property="phase_duration" value="" />
								</td>
								<td class="value"><!-- @ --></td>
							</tr>
							<tr class="highlighted" style="display: none;">
								<td class="value" colspan="2"><!-- @ --></td>
								<td class="value">Require
									<html:text styleClass="inputBox" property="phase_required_registrations" value="" style="width: 30px;" />
									registrations before ending.
								</td>
								<td class="value" colspan="2"><!-- @ --></td>
							</tr>
							<tr class="highlighted" style="display: none;">
								<td class="value" colspan="2"><!-- @ --></td>
								<td class="value" nowrap="nowrap">Require
									<html:text styleClass="inputBox" property="phase_required_submissions" value="" style="width: 30px;" />
									passing submissions.<br />
									<html:checkbox property="phase_manual_screening" />
									Manual Screening
								</td>
								<td class="value" colspan="2"><!-- @ --></td>
							</tr>
						</table><br />

						<table class="scorecard" id="addphase_tbl">
							<tr class="highlighted">
								<td class="valueB"><bean:message key="editProject.Phases.AddNewPhase" /></td>
								<td class="valueB" colspan="2"><bean:message key="editProject.Phases.PhaseStart" /></td>
								<td class="valueB"><bean:message key="editProject.Phases.PhaseEnd" /></td>
								<td class="valueB"><bean:message key="editProject.Phases.Duration" /></td>
								<td class="valueB" colspan="2"><!-- @ --></td>
							</tr>

							<!-- ADD PHASE FORM BEGINS -->
							<tr class="light">
								<td class="value" colspan="2" nowrap="nowrap">
									<bean:message key="editProject.Phases.NewPhase" />
									<html:select styleClass="inputBox" property="addphase" style="width:197px;margin-bottom:2px;">
										<html:option key="editProject.Phases.Select" value="" />
										<html:option key="ProjectPhase.Registration" value="registration" />
										<html:option key="ProjectPhase.Submission" value="submission" />
										<html:option key="ProjectPhase.Screening" value="screening" />
										<html:option key="ProjectPhase.Review" value="review" />
										<html:option key="ProjectPhase.Appeals" value="appeals" />
										<html:option key="ProjectPhase.AppealsResponse" value="appeals_response" />
										<html:option key="ProjectPhase.Approval" value="approval" />
										<html:option key="ProjectPhase.Aggregation" value="aggregation" />
										<html:option key="ProjectPhase.AggregationReview" value="aggregation_review" />
										<html:option key="ProjectPhase.FinalFix" value="final_fix" />
										<html:option key="ProjectPhase.FinalReview" value="final_review" />
									</html:select><br />
									<bean:message key="editProject.Phases.Placement" />
									<html:select styleClass="inputBox" property="addphase_when" style="margin-left:7px;">
										<html:option key="editProject.Phases.Before" value="before" />
										<html:option key="editProject.Phases.After" value="after" />
									</html:select>
									<html:select styleClass="inputBox" property="addphase_where">
										<html:option key="editProject.Phases.SelectPhase" value="" />
									</html:select>
								</td>
								<td class="value" width="37%" nowrap="nowrap">
									<html:text styleClass="inputBoxDate" property="addphase_start_date" />
									<html:text styleClass="inputBoxTime" property="addphase_start_time" />
									<html:select styleClass="inputBox" property="addphase_start_AMPM">
										<html:option key="editProject.Phases.AM" value="am" />
										<html:option key="editProject.Phases.PM" value="pm" />
									</html:select>
									<bean:message key="global.Timezone.EST" /><br />
									<html:checkbox property="addphase_start_by_phase" />
									<bean:message key="editProject.Phases.When" />
									<html:select styleClass="inputBox" property="addphase_start_phase" style="width:120px;">
										<html:option key="editProject.Phases.SelectPhase" value="" />
									</html:select>
									<html:select styleClass="inputBox" property="addphase_start_when">
										<html:option key="editProject.Phases.Starts" value="starts" />
										<html:option key="editProject.Phases.Ends" value="ends" />
									</html:select>
									<html:select styleClass="inputBox" property="addphase_start_plusminus">
										<html:option value="plus">+</html:option>
										<html:option value="minus">-</html:option>
									</html:select>
									<html:text styleClass="inputBox" property="addphase_start_amount" style="width:30px;" />
									<html:select styleClass="inputBox" property="addphase_start_dayshrs">
										<html:option key="editProject.Phases.Days" value="days" />
										<html:option key="editProject.Phases.Hrs" value="hrs" />
									</html:select>
								</td>
            		<td class="value" width="18%" nowrap="nowrap">
									<html:text styleClass="inputBoxDate" property="addphase_end_date" />
									<html:text styleClass="inputBoxTime" property="addphase_end_time" />
									<html:select styleClass="inputBox" property="addphase_end_AMPM">
										<html:option key="editProject.Phases.AM" value="am" />
										<html:option key="editProject.Phases.PM" value="pm" />
									</html:select>
									<bean:message key="global.Timezone.EST" />
								</td>
								<td class="value" width="6%" colspan="2" nowrap="nowrap">
									<html:text styleClass="inputBoxDuration" property="addphase_duration" />
								</td>
								<td class="value" width="7%">
									<html:img srcKey="editProject.Phases.AddPhase.img" altKey="editProject.Phases.AddPhase.alt" onclick="addNewPhase();" />
								</td>
							</tr>
							<!-- ADD PHASE FORM ENDS -->

							<tr>
								<td class="lastRowTD" colspan="7"><!-- @ --></td>
							</tr>
						</table><br />

						<table class="scorecard" id="resources_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
							<tr>
								<td class="title" colspan="5"><bean:message key="editProject.Resources.title" /></td>
							</tr>
							<tr>
								<td class="header"><bean:message key="editProject.Resources.Role"/></td>
								<td class="header"><bean:message key="editProject.Resources.Name"/></td>
								<td class="header"><bean:message key="editProject.Resources.Payment"/></td>
								<td class="header"><bean:message key="editProject.Resources.Paid"/></td>
								<td class="headerC"><!-- @ --></td>
							</tr>
							<tr class="light">
								<td class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="resources_role" value="-1" style="width:150px;">
										<html:option key="editProject.Resources.SelectRole" value="-1" />
										<c:forEach items="${requestScope.resourceRoles}" var="role">
											<html:option key='ResourceRole.${fn:replace(type.name, " ", "")}' value="${role.id}" />
										</c:forEach>
									</html:select>
								</td>
								<td class="value">
									<html:text styleClass="inputBoxName" property="resources_name" value="" />
								</td>
								<td class="value" nowrap="nowrap">
									<html:checkbox property="resources_payment" />${"$"}
									<html:text styleClass="inputBoxDuration" property="resources_payment_amount" value="" />
								</td>
								<td class="value" nowrap="nowrap">
									<html:select styleClass="inputBox" property="resources_paid" style="width:120px;">
										<%-- TODO: How to decide wheather Select or N/A is displayed (probably by NewProject attr.) --%>
										<html:option key="Answer.Select" value="" />
										<html:option key="NotApplicable" value="N/A" />
										<html:option key="editProject.Resources.Paid.Not Paid" value="No" />
										<html:option key="editProject.Resources.Paid.Paid" value="Yes" />
									</html:select>
								</td>
								<td class="valueC" nowrap="nowrap">
									<html:img srcKey="editProject.Resources.AddResource.img" altKey="editProject.Resources.AddResource.alt" onclick="addNewResource();" />
									<html:img style="display: none;" srcKey="editProject.Resources.DeleteResource.img" altKey="editProject.Resources.DeleteResource.alt" onclick="deleteResource(this.parentNode.parentNode);" />
									<html:hidden property="resources_action" value="none" />
									<html:hidden property="resources_id" value="-1" />
								</td>

								<%-- TODO: Iterate through exisitng resources here (for edit page only) --%>
							</tr>
							<tr>
								<td class="lastRowTD" colspan="5"><!-- @ --></td>
							</tr>
						</table><br />

						<div align="right">
							<html:image srcKey="btnSave.img" altKey="btnSave.alt" border="0" />&#160;
							<a href="javascript:history.go(-1)"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0" /></a>
						</div>
					</html:form>
				</div>
				<p><br /></p>
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><img src="../i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><img src="../i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="../includes/inc_footer.jsp" />
</body>
</html:html>
