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
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/style.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/coders.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/tcStyles.css' />" />

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/new_styles.css' />" />
	<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/scripts/rollovers.js' />"><!-- @ --></script>
	<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/scripts/dojo.js' />"><!-- @ --></script>
	<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/scripts/util.js' />"><!-- @ --></script>

	<script language="JavaScript" type="text/javascript"><!--

		// TODO: Write docs for following vars
		var lastResourceIndex = ${fn:length(projectForm.map['resources_id']) - 1};
		var lastPhaseIndex = ${fn:length(projectForm.map['phase_id']) - 1};
		var nameCellIndex = ${newProject ? 0 : 1};

		function getUniqueId() {
			var idNode = document.getElementsByName("js_current_id")[0];
			var currentId = parseInt(idNode.value, 10);
			currentId++;
			idNode.value = currentId + "";
			return "js_id_" + currentId;
		}

		/*
		 * TODO: Write docs for this function
		 */
		function patchParamIndex(paramNode, newIndex) {
			paramNode.name = paramNode.name.replace(/\[([0-9])+\]/, "[" + newIndex + "]");
			if (paramNode.outerHTML) {
				var pNode = document.createElement(paramNode.outerHTML.replace(/\[([0-9])+\]/, "[" + newIndex + "]"));
				while (paramNode.children.length > 0) {
					pNode.appendChild(paramNode.children[0]);
				}
				paramNode.parentNode.replaceChild(pNode, paramNode);
			}
		}

		/*
		 * TODO: Write docs for this function
		 */
		function patchAllChildParamIndexes(node, newIndex) {
			var allInputs = node.getElementsByTagName("input");
			for (var i = 0; i < allInputs.length; i++) {
				patchParamIndex(allInputs[i], newIndex);
			}
			var allSelects = node.getElementsByTagName("select");
			for (var i = 0; i < allSelects.length; i++) {
				var selectIndex = allSelects[i].selectedIndex;
				patchParamIndex(allSelects[i], newIndex);
				allSelects[i].selectedIndex = selectIndex;
			}
		}

		/*
		 * TODO: Write docs for this function
		 */
		function cloneInputRow(rowNode) {
			var clonedNode = rowNode.cloneNode(true);
			var oldSelectNodes = rowNode.getElementsByTagName("select");
			var newSelectNodes = clonedNode.getElementsByTagName("select");
			for (var i = 0; i < oldSelectNodes.length; i++) {
				newSelectNodes[i].value = oldSelectNodes[i].value;
			}
			var oldInputNodes = rowNode.getElementsByTagName("input");
			var newInputNodes = clonedNode.getElementsByTagName("input");
			for (var i = 0; i < oldInputNodes.length; i++) {
				if (oldInputNodes[i].type == "radio") {
					newInputNodes[i].checked = oldInputNodes[i].checked;
					newInputNodes[i].defaultChecked = oldInputNodes[i].checked;
				}
			}
			return clonedNode;
		}
		/*
		 * This function adds a new row to resources table.
		 */
		function addNewResource() {
			// TODO: Make the combos and possibly radio buttons copy their state correctly

			// Get resources table node
			var resourcesTable = document.getElementById("resources_tbl");
			// Get the number of rows in table
			var rowCount = resourcesTable.rows.length;
			// Create a new row into resources table
			var newRow = cloneInputRow(resourcesTable.rows[2]);
			// Rows should vary colors
			if (rowCount % 2 == 0) {
				newRow.className = "dark";
			} else {
				newRow.className = "light";
			}

			// Make delete button visible and hide add button
			var myCell = newRow.getElementsByTagName("TD")[4];
			var images = myCell.getElementsByTagName("img");
			images[0].style["display"] = "none";
			images[1].removeAttribute("style");
			// Retrieve hidden inputs
			var inputs = myCell.getElementsByTagName("input");
			// Set hidden resources_action parameter to "add"
			var actionInput = inputs[0];
			actionInput.value = "add";

			// Increase resource index
			lastResourceIndex++;
			// Rename all the inputs to have a new index
			patchAllChildParamIndexes(newRow, lastResourceIndex);
			// Insert new row into resources table
			resourcesTable.tBodies[0].insertBefore(newRow, resourcesTable.rows[rowCount - 1]);
		}

		/*
		 * This function deletes the exisiting row from the resources table.
		 */
		function deleteResource(resourceRowNode) {
			// Retrieve hidden inputs
			var inputs = resourceRowNode.cells[4].getElementsByTagName("input");
			// Check if the resource to be deleted has been persisted
			var id = inputs[1].value;

			// Hide the row, don't delete it as the resource
			// should be deleted from DB on submit
			resourceRowNode.style["display"] = "none";

			// Set hidden resources_action parameter to "delete"
			var actionInput = inputs[0];
			actionInput.value = "delete";

			// TODO: Complete it, doesn't work for some reason
			// TODO: Probably the fix is to skip hidden rows, etc.
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
		function populatePhaseParam(destRowNode, srcTableNode, paramName, paramIndex) {
			var srcInputs = getChildrenByName(srcTableNode, "addphase_" + paramName);
			var destInputs = getChildrenByName(destRowNode, "phase_" + paramName + "[" + paramIndex + "]");
			if (destInputs[0].tagName != "SELECT") {
				if (destInputs[0].type == "checkbox") {
					destInputs[0].checked = srcInputs[0].checked;
					destInputs[0].defaultChecked = srcInputs[0].checked;
				} else if (destInputs[0].type == "radio") {
					var selectedValue = null;
					for (var i = 0; i < srcInputs.length; i++) {
						if (srcInputs[i].checked) {
							selectedValue = srcInputs[i].value;
							break;
						}
					}
					for (var i = 0; i < destInputs.length; i++) {
						if (destInputs[i].value == selectedValue) {
							destInputs[i].checked = true;
							destInputs[i].defaultChecked = true;
							break;
						}
					}
				} else {
					destInputs[0].value = srcInputs[0].value;
				}
			} else {
				destInputs[0].value = srcInputs[0].value;
			}
		}

		/*
		 * TODO: Write docs.
		 */
		function addPhaseCriterion(phaseName, phaseRow) {
			var criterionRow = null;
			// TODO: Should be done in locale-independent way
			// Check if the phase should have a criterion row and at it if it is needed
			if (phaseName == "Screening" || phaseName == "Review" || phaseName == "Approval" ||
					phaseName == "Registration" || phaseName == "Submission" || phaseName == "Appeals") {
				var templateRow;
				if (phaseName == "Screening") {
				  	templateRow = document.getElementById("screening_scorecard_row_template");
				} else if (phaseName == "Review") {
				  	templateRow = document.getElementById("review_scorecard_row_template");
				} else if (phaseName == "Approval") {
				  	templateRow = document.getElementById("approval_scorecard_row_template");
				} else if (phaseName == "Registration") {
				  	templateRow = document.getElementById("required_registrations_row_template");
				} else if (phaseName == "Submission") {
				  	templateRow = document.getElementById("required_submissions_row_template");
				} else if (phaseName == "Appeals") {
				  	templateRow = document.getElementById("view_appeal_responses_row_template");
				}

 				criterionRow = cloneInputRow(templateRow);

 				// Assign the id
				criterionRow.id = getUniqueId();
				// Remove "display: none;"
				criterionRow.style["display"] = "";

				// Rename all the inputs to have a new index
				patchAllChildParamIndexes(criterionRow, lastPhaseIndex);
				// Insert criterion row into proper position - after new phase row
				dojo.dom.insertAfter(criterionRow, phaseRow);
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
			var phaseNameInput = getChildByName(addPhaseTable, "addphase_type");
			var selectedOption = phaseNameInput.options[phaseNameInput.selectedIndex];
			var phaseName = dojo.dom.textContent(selectedOption);

			// Generate phase id (for use in the DOM)
			var phaseId = getUniqueId();

			// Create a new row to represent the phase
			var newRow = cloneInputRow(document.getElementById("phase_row_template"));
			
			// Assign the id
			newRow.id = phaseId;
			// Remove "display: none;"
			newRow.style["display"] = "";
			
			// Add the name of the added phase to the select options for add phase form
			var whereCombo = getChildByName(addPhaseTable, "addphase_where");
			//whereCombo.add(new Option(phaseName, phaseId), null);
			var whereOption = document.createElement('option');
			whereOption.text = phaseName;
			whereOption.value = phaseId;
			try {
				whereCombo.add(whereOption, null);
			} catch (ex) {
				whereCombo.add(whereOption, whereCombo.selectedIndex);
			}

			var startPhaseCombo = getChildByName(addPhaseTable, "addphase_start_phase");
			//startPhaseCombo.add(new Option(phaseName, phaseId), null);
                        var startPhaseOption = document.createElement('option');
			startPhaseOption.text = phaseName;
			startPhaseOption.value = phaseId;
                        try {
                                startPhaseCombo.add(startPhaseOption, null);
                        } catch (ex) {
                                startPhaseCombo.add(startPhaseOption, startPhaseCombo.selectedIndex);
                        }

			// Also add it to the phase rows
			var startPhaseCombos = getChildrenByNamePrefix(document.documentElement, "phase_start_phase");
			for (var i = 0; i < startPhaseCombos.length; i++) {
                	        var startPhaseOption2 = document.createElement('option');
				startPhaseOption2.text = phaseName;
        	                startPhaseOption2.value = phaseId;
	                        try {	
                        	        startPhaseCombos[i].add(startPhaseOption2, null);
                	        } catch (ex) {
        	                        startPhaseCombos[i].add(startPhaseOption2, startPhaseCombos[i].selectedIndex);
	                        }

//				startPhaseCombos[i].add(new Option(phaseName, phaseId), null);
			}

			
			// Set the phase id hidden control
			var jsIdNode = getChildByNamePrefix(newRow, "phase_js_id");
			jsIdNode.value = phaseId;

			// Increase phase index
			lastPhaseIndex++;
			
			// Rename all the inputs to have a new index
			patchAllChildParamIndexes(newRow, lastPhaseIndex);

			// Populate newly created phase inputs from the add phase form
			var inputNames = ["type",
				"start_date", "start_time", "start_AMPM",
				"start_by_phase", "start_phase", "start_when",
				"start_plusminus", "start_amount", "start_dayshrs",
				"end_date", "end_time", "end_AMPM", "duration"];
			for (var i = 0; i < inputNames.length; i++) {
				populatePhaseParam(newRow, addPhaseTable, inputNames[i], lastPhaseIndex);
			}

			// Populate phase name
			// TODO: Implement numbering of same-typed phases
			var phaseNameCell = newRow.cells[nameCellIndex];
			if (phaseNameCell == null) {
				phaseNameCell = newRow.getElementsByTagName('TD')[nameCellIndex];
			}
			dojo.dom.textContent(phaseNameCell, phaseName);
			getChildByNamePrefix(newRow, "phase_name").value = phaseName;

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
					if (dojo.dom.nextElement(wherePhaseNode) &&
							dojo.dom.nextElement(wherePhaseNode).className == "highlighted") {
						wherePhaseNode = dojo.dom.nextElement(wherePhaseNode);
					}

					dojo.dom.insertAfter(newRow, wherePhaseNode);
				}
			}

			// Add phase criterion row if needed
			addPhaseCriterion(phaseName, newRow);
		}


		/*
		 * This function deletes the option from select input which has the specified value.
		 */
		function deleteOptionWithValue(selectNode, optionValue) {
			for (var i = 0; i < selectNode.options.length; i++) {
				if (selectNode.options[i].value == optionValue) {
					selectNode.remove(i);
					break;
				}
			}
		}

		/*
		 * This function deletes the exisiting phase from the phases table.
		 * It also deletes the corresponding phase criterion if needed.
		 */
		function deletePhase(phaseRowNode) {
			// Hide the row, don't delete it as the phase
			// should be deleted from DB on submit
			phaseRowNode.style["display"] = "none";

			// Set hidden phase_action parameter to "delete"
			var actionInput = getChildByNamePrefix(phaseRowNode, "phase_action");
			actionInput.value = "delete";

			// Get phase JS id
			var phaseId = phaseRowNode.id;

			// Delete phase from addphase form select options
			var addPhaseTable = document.getElementById("addphase_tbl");
			var whereCombo = getChildByName(addPhaseTable, "addphase_where");
			deleteOptionWithValue(whereCombo, phaseId);
			var startPhaseCombo = getChildByName(addPhaseTable, "addphase_start_phase");
			deleteOptionWithValue(startPhaseCombo, phaseId);

			// Also delete it from the phase rows
			var startPhaseCombos = getChildrenByNamePrefix(document.documentElement, "phase_start_phase");
			for (var i = 0; i < startPhaseCombos.length; i++) {
				deleteOptionWithValue(startPhaseCombos[i], phaseId)
			}

			// Remove phase criterion row if needed
			nextRowNode = dojo.dom.nextElement(phaseRowNode);
			if (nextRowNode != null && nextRowNode.className == "highlighted") {
				nextRowNode.parentNode.removeChild(nextRowNode);
			}
		}
	--></script>
</head>

<body>
	<jsp:include page="/includes/inc_header.jsp" />

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins -->
			<td width="180">
				<jsp:include page="/includes/inc_leftnav.jsp" />
			</td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><html:img page="/i/clear.gif" width="15" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="/includes/project/project_tabs.jsp" />

				<div id="mainMiddleContent">
					<html:form action="/actions/SaveProject">
						<html:hidden property="method" value="saveProject" />

						<%-- TODO: Validation errors display should be much more than is here --%>
						<c:if test="${not empty requestScope['org.apache.struts.action.ERROR']}">
							<table cellpadding="0" cellspacing="0" border="0">
								<tr><td width="16"><!-- @ --></td><td><!-- @ --></td></tr>
								<tr>
									<td colspan="2"><bean:message key="error.com.cronos.onlinereview.Errors" /></td>
								</tr>
								<html:errors />
							</table><br />
						</c:if>

						<%-- If editing the existing project, render its pid --%>
						<c:if test="${not newProject}">
							<html:hidden property="pid" />
						</c:if>

						<html:hidden property="js_current_id" />

						<%-- If creating a new project, show project details table --%>
						<c:if test="${newProject}">
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
						</c:if>

						<%-- If editing the exsiting project, include timeline editor here --%>
						<c:if test="${not newProject}">
							<jsp:include page="/includes/project/project_edit_timeline.jsp" />
						</c:if>

						<table class="scorecard" id="preferences">
							<tr>
								<td class="title" colspan="2"><bean:message key="editProject.Preferences.title" /></td>
							</tr>
							<tr class="light">
								<td class="valueB" width="1%"><bean:message key="editProject.Preferences.Autopilot" /></td>
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
								<%-- If creating a new project, name this table as "References" --%>
								<c:if test="${newProject}">
									<td class="title" colspan="2"><bean:message key="editProject.References.title" /></td>
								</c:if>
								<%-- If editing the existing project, name this table as "Project Details" --%>
								<c:if test="${not newProject}">
									<td class="title" colspan="2"><bean:message key="editProject.ProjectDetails.title" /></td>
								</c:if>
							</tr>
							<%-- If editing the existing project, should have project name edited here --%>
							<c:if test="${not newProject}">
								<tr>
									<td class="valueB"><bean:message key="editProject.ProjectDetails.Name" /></td>
									<td class="value" nowrap="nowrap"><html:text styleClass="inputBox" property="project_name" style="width: 350px;" /></td>
								</tr>
							</c:if>

							<tr class="light">
								<td class="value" nowrap="nowrap">
									<b><bean:message key="editProject.References.ForumId" /></b><br />
								</td>
								<td class="value" nowrap="nowrap">
									<html:text styleClass="inputBox" property="forum_id" style="width: 350px;" />
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

						<%-- If creating a new project, include timeline editor here --%>
						<c:if test="${newProject}">
							<jsp:include page="/includes/project/project_edit_timeline.jsp" />
						</c:if>

						<%-- Include resources editor --%>
						<jsp:include page="/includes/project/project_edit_resources.jsp" />

						<c:if test="${not newProject}">
							<table class="scorecard" id="status">
								<tr>
									<td class="title"><b><bean:message key="editProject.Status.title" /></b></td>
								</tr>
								<tr class="light">
									<td class="value"><p align="left"><b>&nbsp;<bean:message key="editProject.Status.CurrentStatus" />&nbsp; </b>
										<html:select styleClass="inputBox" property="status">
											<c:forEach var="status" items="${projectStatuses}">
												<html:option key='ProjectStatus.${fn:replace(status.name, " ", "")}' value="${status.id}" />
											</c:forEach>
										</html:select><br>
										<br><bean:message key="editProject.Status.Explanation.description" /><br>
										<html:textarea styleClass="inputTextBox" property="status_explanation" />
									</td>
								</tr>
								<tr>
									<td class="lastRowTD"><!-- @ --></td>
								</tr>
							</table><br />

							<table class="scorecard" id="Explanation">
								<tr>
									<td class="title"><bean:message key="editProject.Explanation.title" /></td>
								</tr>
								<tr class="light">
									<td class="Value"><bean:message key="editProject.Explanation.description" /><br />
										<html:textarea styleClass="inputTextBox" property="explanation" />
									</td>
								</tr>
								<tr>
									<td class="lastRowTD"><!-- @ --></td>
								</tr>
							</table><br />
						</c:if>

						<div align="right">
							<c:if test="${newProject}">
								<html:image srcKey="btnSave.img" altKey="btnSave.alt" border="0" />&#160;
								<html:link page="/actions/ListProjects.do?method=listProjects"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0" /></html:link>
							</c:if>
							<c:if test="${not newProject}">
								<html:image srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0" />&#160;
								<html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0" /></html:link>
							</c:if>
						</div>
					</html:form>
				</div>
				<br /><br />
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><html:img page="/i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><html:img page="/i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="/includes/inc_footer.jsp" />
</body>
</html:html>
