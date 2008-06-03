<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <c:if test="${empty project}">
        <title><bean:message key="global.title.level2"
            arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
            arg1='${orfn:getMessage(pageContext, "editProject.title.CreateNew")}' /></title>
    </c:if>
    <c:if test="${not empty project}">
        <jsp:include page="/includes/project/project_title.jsp" />
    </c:if>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/phasetabs.css' />" />
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/dojo.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/util.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/validation_util2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/validation_edit_project3.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/parseDate.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<html:rewrite page='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/ajax1.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript"><!--
        // TODO: Write docs for following vars
        var lastResourceIndex = ${fn:length(projectForm.map['resources_id']) - 1};
        var lastPhaseIndex = ${fn:length(projectForm.map['phase_id']) - 1};
        var nameCellIndex = ${newProject ? 0 : 1};

        var resourceRoleToPhaseTypeMap = {};
        <c:forEach items="${resourceRoles}" var="resourceRole">
            resourceRoleToPhaseTypeMap[${resourceRole.id}] = "${empty resourceRole.phaseType ? 'null' : resourceRole.phaseType}";
        </c:forEach>

        var projectCategories = [];
        <c:forEach items="${projectCategories}" var="category">
            projectCategories.push({});
            projectCategories[projectCategories.length - 1]["id"] = ${category.id};
            projectCategories[projectCategories.length - 1]["projectType"] = ${category.projectType.id};
            // TODO: Localize the catagory name
            projectCategories[projectCategories.length - 1]["name"] = "${category.name}";
        </c:forEach>

        var screeningScorecards = [];
        <c:forEach items="${screeningScorecards}" var="scorecard">
            screeningScorecards.push({});
            screeningScorecards[screeningScorecards.length - 1]["id"] = ${scorecard.id};
            screeningScorecards[screeningScorecards.length - 1]["category"] = ${scorecard.category};
            screeningScorecards[screeningScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
        </c:forEach>

        var reviewScorecards = [];
        <c:forEach items="${reviewScorecards}" var="scorecard">
            reviewScorecards.push({});
            reviewScorecards[reviewScorecards.length - 1]["id"] = ${scorecard.id};
            reviewScorecards[reviewScorecards.length - 1]["category"] = ${scorecard.category};
            reviewScorecards[reviewScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
        </c:forEach>

        var approvalScorecards = [];
        <c:forEach items="${approvalScorecards}" var="scorecard">
            approvalScorecards.push({});
            approvalScorecards[approvalScorecards.length - 1]["id"] = ${scorecard.id};
            approvalScorecards[approvalScorecards.length - 1]["category"] = ${scorecard.category};
            approvalScorecards[approvalScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
        </c:forEach>

        var defaultScorecards = [];
        <c:forEach items="${defaultScorecards}" var="scorecard">
            defaultScorecards.push({});
            defaultScorecards[defaultScorecards.length - 1]["id"] = ${scorecard.scorecardId};
            defaultScorecards[defaultScorecards.length - 1]["category"] = ${scorecard.category};
            defaultScorecards[defaultScorecards.length - 1]["type"] = ${scorecard.scorecardType};
            defaultScorecards[defaultScorecards.length - 1]["name"] = "${scorecard.name}";
        </c:forEach>

        var projectTypeNamesMap = {};
        <c:forEach items="${projectTypes}" var="projectType">
            projectTypeNamesMap["${projectType.id}"] = "${projectType.name}";
        </c:forEach>

        <c:forEach items="${projectCategories}" var="category">
	        <c:if test="${category.projectType.name == 'Component'}">
	            <c:if test="${category.name == 'Development'}">
	                var developmentCatId = "${category.id}";</c:if>
	            <c:if test="${category.name == 'Design'}">
	                var designCatId = "${category.id}";</c:if>
	            <c:if test="${category.name == 'Testing Competition'}">
	                var compTestingCatId = "${category.id}";</c:if>
	        </c:if>
	        <c:if test="${category.projectType.name == 'Application'}">
                <c:if test="${category.name == 'Assembly Competition'}">
                    var assemblyCatId = "${category.id}";</c:if>
	        </c:if>
        </c:forEach>

        var phaseTypeIdsMap = {};
        <c:forEach items="${phaseTypes}" var="phaseType">
            phaseTypeIdsMap["${phaseType.name}"] = "${phaseType.id}";
        </c:forEach>

        var screeningScorecardNodes = new Array();;
        var reviewScorecardNodes = new Array();;
        var approvalScorecardNodes = new Array();;

        /*
         * TODO: Document it
         */
        function getUniqueId() {
            var idNode = document.getElementsByName("js_current_id")[0];
            var currentId = parseInt(idNode.value, 10);
            currentId++;
            idNode.value = currentId + "";
            return "js_id_" + currentId;
        }

        /*
         * TODO: Document it
         */
        function addComboOption(comboNode, optionText, optionValue) {
            var option = document.createElement('option');
            option.text = optionText;
            option.value = optionValue;
            try {
                comboNode.add(option, null);
            } catch (ex) {
                // TODO: Check if really selected index should be used
                comboNode.add(option, option.selectedIndex);
            }
        }

        /*
         * TODO: Document it.
         */
        function onProjectTypeChange(projectTypeNode) {
            var projectCategoryNode = document.getElementsByName("project_category")[0];
            // Clear combo options
            while (projectCategoryNode.length > 0) {
                projectCategoryNode.remove(projectCategoryNode.length - 1);
            }
            // Add new combo options
            for (var i = 0; i < projectCategories.length; i++) {
                if (projectTypeNode.value == projectCategories[i]["projectType"]) {
                    addComboOption(projectCategoryNode,
                        projectCategories[i]["name"], projectCategories[i]["id"]);
                }
            }
            onProjectCategoryChange(projectCategoryNode);
        }

        function onProjectCategoryChange(projectCategoryNode) {
            var templateRow = document.getElementById("screening_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, screeningScorecards, 'Screening');

            templateRow = document.getElementById("review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, reviewScorecards, 'Review');

            templateRow = document.getElementById("approval_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, approvalScorecards, 'Client Review');

            for (var i = 0; i < screeningScorecardNodes.length; i++) {
                changeScorecardByCategory(screeningScorecardNodes[i], projectCategoryNode.value, screeningScorecards, 'Screening');
            }
            for (var i = 0; i < reviewScorecardNodes.length; i++) {
                changeScorecardByCategory(reviewScorecardNodes[i], projectCategoryNode.value, reviewScorecards, 'Review');
            }
            for (var i = 0; i < approvalScorecardNodes.length; i++) {
                changeScorecardByCategory(approvalScorecardNodes[i], projectCategoryNode.value, approvalScorecards, 'Client Review');
            }

            var digitalRunChecked = false;
            var publicChecked = false;

            if (projectCategoryNode.value == developmentCatId ||
                projectCategoryNode.value == designCatId ||
                projectCategoryNode.value == assemblyCatId ||
                projectCategoryNode.value == compTestingCatId) {
                
                digitalRunChecked = true;
                
                if (projectCategoryNode.value != assemblyCatId) {
                    publicChecked = true;
                }
            }

            document.getElementById("digitalRunCheckBox").checked = digitalRunChecked;
            document.getElementById("public").checked = publicChecked;
        }

        function changeScorecardByCategory(scorecardNode, category, scorecards, scorecardName) {
            if (scorecardNode) {
                // Clear combo options
                while (scorecardNode.length > 0) {
                    scorecardNode.remove(scorecardNode.length - 1);
                }
                // Add new combo options
                for (var i = 0; i < scorecards.length; i++) {
                    if (category == scorecards[i]["category"]) {
                            addComboOption(scorecardNode,
                                scorecards[i]["name"], scorecards[i]["id"]);
                    }
                }

                // set default scorecard id
                for (var i = 0; i < defaultScorecards.length; i++) {
                    if (defaultScorecards[i]["category"] == category && defaultScorecards[i]["name"] == scorecardName) {
                        scorecardNode.value = defaultScorecards[i]["id"];
                    }
                }
            }
        }

        function updateLabelsInCell(cellToUpdate, newIndex) {
            var labels = cellToUpdate.getElementsByTagName("label");
            var inputs = cellToUpdate.getElementsByTagName("input");

            for (var i = 0; i < labels.length; ++i) {
                var inputId = labels[i].htmlFor;
                var inputField = null;

                for (var j = 0; j < inputs.length; ++j)
                    if (inputs[j].id == inputId) {
                        inputField = inputs[j];
                        break;
                    }

                if (inputField == null) continue;
                inputField.id = inputField.id + "_" + newIndex;
                labels[i].htmlFor = inputField.id;
            }
        }

        /*
         * This function adds a new row to resources table.
         */
        function addNewResource() {
            // Get resources table node
            var resourcesTable = document.getElementById("resources_tbl");
            // Get the number of rows in table
            var rowCount = resourcesTable.rows.length;
            // Create a new row into resources table
            var newRow = cloneInputRow(resourcesTable.rows[2]);

            // Rows should vary colors
            var rows = resourcesTable.rows;
            var strLastRowStyle = "dark"; // This variable will remember the style of the last row
            // Find first non-hidden row, starting from the bottom of the table
            for (var i = rows.length - 2; i >= 0; --i) {
                if (rows[i].style["display"] == "none") continue;
                strLastRowStyle = rows[i].className;
                break;
            }
            newRow.className = (strLastRowStyle == "dark") ? "light" : "dark";

            var allNewCells = newRow.getElementsByTagName("td");
            var paymentCell = allNewCells[2];
            var buttonsCell = allNewCells[4];

            // Make delete button visible and hide add button
            var images = buttonsCell.getElementsByTagName("img");
            images[0].style["display"] = "none";
            images[1].style["display"] = "inline";
            // Retrieve hidden inputs
            var inputs = buttonsCell.getElementsByTagName("input");
            // Set hidden resources_action parameter to "add"
            var actionInput = inputs[0];
            actionInput.value = "add";

            // Increase resource index
            lastResourceIndex++;
            // Rename all the inputs to have a new index
            patchAllChildParamIndexes(newRow, lastResourceIndex);
            // Insert new row into resources table
            resourcesTable.tBodies[0].insertBefore(newRow, resourcesTable.rows[rowCount - 1]);

            // Make labels correspond correct inputs (radio boxes)
            updateLabelsInCell(paymentCell, lastResourceIndex);

            // Get phase parameters input nodes
            var phaseIdNodes = getChildrenByNamePrefix(document.documentElement, "phase_js_id");
            var phaseActionNodes = getChildrenByNamePrefix(document.documentElement, "phase_action");
            var phaseTypeNodes = getChildrenByNamePrefix(document.documentElement, "phase_type[");
            var phaseNumberNodes = getChildrenByNamePrefix(document.documentElement, "phase_number[");

            // Update resource phase combo
            fillResourcePhaseCombo(newRow, phaseIdNodes, phaseActionNodes,
                phaseTypeNodes, phaseNumberNodes);
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

            // Make rows vary color
            var rows = resourceRowNode.parentNode.rows;
            var initial = 0;
            for (var i = 0; i < rows.length; i++) {
                // Skip hidden rows as they shouldn't affect row coloring
                if (rows[i].style["display"] == "none") continue;
                rows[i].className = (initial++ % 2 == 0) ? "light" : "dark";
            }
        }

        function onResourceRoleChange(resourceNode) {
            // Get phase parameters input nodes
            var phaseIdNodes = getChildrenByNamePrefix(document.documentElement, "phase_js_id");
            var phaseActionNodes = getChildrenByNamePrefix(document.documentElement, "phase_action");
            var phaseTypeNodes = getChildrenByNamePrefix(document.documentElement, "phase_type[");
            var phaseNumberNodes = getChildrenByNamePrefix(document.documentElement, "phase_number[");

            // Update resource phase combo
            fillResourcePhaseCombo(resourceNode, phaseIdNodes, phaseActionNodes,
                phaseTypeNodes, phaseNumberNodes);
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
                    for (var i = 0; i < destInputs.length; i++) {
                        if (selectedValue != null && destInputs[i].value != selectedValue) {
                            destInputs[i].checked = false;
                            destInputs[i].defaultChecked = false;
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

                updateLabelsInCell(criterionRow.getElementsByTagName("td")[1], lastPhaseIndex);
                // Rename all the inputs to have a new index
                patchAllChildParamIndexes(criterionRow, lastPhaseIndex);
                // Insert criterion row into proper position - after new phase row
                dojo.dom.insertAfter(criterionRow, phaseRow);

                 if (phaseName == "Screening") {
                    screeningScorecardNodes[screeningScorecardNodes.length] = criterionRow.getElementsByTagName("select")[0];
                } else if (phaseName == "Review") {
                    reviewScorecardNodes[reviewScorecardNodes.length] = criterionRow.getElementsByTagName("select")[0];
                } else if (phaseName == "Approval") {
                    approvalScorecardNodes[approvalScorecardNodes.length] = criterionRow.getElementsByTagName("select")[0];
                }
            }
        }

        /*
         * TODO: Document it
         */
        function getNumberOfPhasesWithType(phaseTypeId) {
            var phaseTypeNodes = getChildrenByNamePrefix(document.documentElement, "phase_type");
            var phaseActionNodes = getChildrenByNamePrefix(document.documentElement, "phase_action");
            var result = 0;
            for (var i = 1; i < phaseTypeNodes.length; i++) {
                if (phaseTypeNodes[i].value == phaseTypeId && phaseActionNodes[i].value != "delete") {
                    result++;
                }
            }
            return result;
        }

        /*
         * TODO: Document it.
         */
        function createNewPhaseRow(phaseName, phaseTypeId, _phaseId) {
            // Retrieve timeline table
            var timelineTable = document.getElementById("timeline_tbl");
            // Retrieve add phase table
            var addPhaseTable = document.getElementById("addphase_tbl");

            // Generate phase id (for use in the DOM)
            var phaseId = _phaseId ? _phaseId : getUniqueId();

            // Create a new row to represent the phase
            var newRow = cloneInputRow(document.getElementById("phase_row_template"));

            // Assign the id
            newRow.id = phaseId;
            // Remove "display: none;"
            newRow.style["display"] = "";

            // Populate phase name
            var phaseNameNode = getChildByNamePrefix(newRow, "phase_name_text");
            dojo.dom.textContent(phaseNameNode, phaseName);
            getChildByNamePrefix(newRow, "phase_name[").value = phaseName;

            // Populate phase number
            var phaseNumber = getNumberOfPhasesWithType(phaseTypeId) + 1;
            var phaseNumberNode = getChildByNamePrefix(newRow, "phase_number_text");
            dojo.dom.textContent(phaseNumberNode, phaseNumber + "");
            getChildByNamePrefix(newRow, "phase_number[").value = phaseNumber;

            // Add the name of the added phase to the select options for add phase form
            var whereCombo = getChildByName(addPhaseTable, "addphase_where");
            addComboOption(whereCombo, phaseName, phaseId);
            var startPhaseCombo = getChildByName(addPhaseTable, "addphase_start_phase");
                addComboOption(startPhaseCombo, phaseName, phaseId);

            // Also add it to the phase rows
            var startPhaseCombos = getChildrenByNamePrefix(document.documentElement, "phase_start_phase");
            for (var i = 0; i < startPhaseCombos.length; i++) {
                            addComboOption(startPhaseCombos[i], phaseName, phaseId);
            }

            // Add phase to resource phase combos
            var resourceRoleNodes = getChildrenByNamePrefix(document.documentElement, "resources_role");
            var resourcePhaseNodes = getChildrenByNamePrefix(document.documentElement, "resources_phase");
            for (var i = 0; i < resourceRoleNodes.length; i++) {
                var curPhaseTypeId = resourceRoleToPhaseTypeMap[resourceRoleNodes[i].value];
                if (curPhaseTypeId == phaseTypeId) {
                    addComboOption(resourcePhaseNodes[i], phaseNumber, phaseId);
                }
            }

            // Set the phase id hidden control
            var jsIdNode = getChildByNamePrefix(newRow, "phase_js_id");
            jsIdNode.value = phaseId;

            // Increase phase index
            lastPhaseIndex++;

            // Rename all the inputs to have a new index
            patchAllChildParamIndexes(newRow, lastPhaseIndex);

            return newRow;
        }


        /*
         * This function adds new phase to phases table, it includes addition of several rows.
         */
        function addNewPhase() {
            // Retrieve timeline table
            var timelineTable = document.getElementById("timeline_tbl");
            // Retrieve add phase table
            var addPhaseTable = document.getElementById("addphase_tbl");

            // Retrieve phase name
            var phaseNameInput = getChildByName(addPhaseTable, "addphase_type");
            var selectedOption = phaseNameInput.options[phaseNameInput.selectedIndex];
            var phaseName = dojo.dom.textContent(selectedOption);
            var phaseTypeId = phaseNameInput.value;

            // Create a new row to represent the phase
            var newRow = createNewPhaseRow(phaseName, phaseTypeId);

            // Populate newly created phase inputs from the add phase form
            var inputNames = ["type",
                "start_date", "start_time", "start_AMPM",
                "start_by_phase", "start_phase", "start_when",
                "start_plusminus", "start_amount", "start_dayshrs", "use_duration",
                "end_date", "end_time", "end_AMPM", "duration"];
            for (var i = 0; i < inputNames.length; i++) {
                populatePhaseParam(newRow, addPhaseTable, inputNames[i], lastPhaseIndex);
            }

            var whereCombo = getChildByName(addPhaseTable, "addphase_where");

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
         * TODO: Document it
         */
        function fillResourcePhaseCombo(resourceNode, phaseIdNodes, phaseActionNodes,
                phaseTypeNodes, phaseNumberNodes) {
            var resourceRoleId = getChildByNamePrefix(resourceNode, "resources_role").value;
            var phaseTypeId = resourceRoleToPhaseTypeMap[resourceRoleId];
            var phaseCombo = getChildByNamePrefix(resourceNode, "resources_phase");
            var lastValue = phaseCombo.value;
            // Clear combo options
            while (phaseCombo.length > 0) {
                phaseCombo.remove(phaseCombo.length - 1);
            }
            // Add new combo options
            for (var i = 0; i < phaseTypeNodes.length; i++) {
                if (phaseTypeNodes[i].value == phaseTypeId && phaseActionNodes[i].value != "delete") {
                    addComboOption(phaseCombo, phaseNumberNodes[i].value, phaseIdNodes[i].value);
                    if (phaseCombo.length == 1) {
                        // Trick needed for IE
                        phaseCombo.selectedIndex = 0;
                    }
                    if (phaseIdNodes[i].value == lastValue) {
                        phaseCombo.selectedIndex = phaseCombo.length - 1;
                    }
                }
            }
            // phaseCombo.value = lastValue;
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

            // Get phase type id
            var phaseTypeId = getChildByNamePrefix(phaseRowNode, "phase_type").value;

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

            // Get resources table node
            var resourcesTable = document.getElementById("resources_tbl");

            // Get phase parameters input nodes
            var phaseIdNodes = getChildrenByNamePrefix(document.documentElement, "phase_js_id");
            var phaseActionNodes = getChildrenByNamePrefix(document.documentElement, "phase_action");
            var phaseTypeNodes = getChildrenByNamePrefix(document.documentElement, "phase_type[");
            var phaseNumberNodes = getChildrenByNamePrefix(document.documentElement, "phase_number[");

            // Renumber the phases left
            var phaseNumberTextNodes = getChildrenByNamePrefix(document.documentElement, "phase_number_text");

            var phaseNumber = 1;
            for (var i = 1; i < phaseActionNodes.length; i++) {
                if (phaseActionNodes[i].value != "delete" && phaseTypeNodes[i].value == phaseTypeId) {
                    phaseNumberNodes[i].value = phaseNumber + "";
                    dojo.dom.textContent(phaseNumberTextNodes[i], phaseNumber + "");
                    phaseNumber++;
                }
            }

            // Update resource phase combos
            var resourceNodes = resourcesTable.getElementsByTagName("TR");

            // Note, that first two rows represent just table header,
            // so we start loop from third row
            // The last node is just footer, so it is also skipped
            for (var i = 2; i < resourceNodes.length - 1; i++) {
                var resourceRoleNode = getChildByNamePrefix(resourceNodes[i], "resources_role");
                var curPhaseTypeId = resourceRoleToPhaseTypeMap[resourceRoleNode.value];
                if (curPhaseTypeId == phaseTypeId) {
                    fillResourcePhaseCombo(resourceNodes[i], phaseIdNodes, phaseActionNodes,
                        phaseTypeNodes, phaseNumberNodes);
                }
            }

            // Remove phase criterion row if needed
            nextRowNode = dojo.dom.nextElement(phaseRowNode);
            if (nextRowNode != null && nextRowNode.className == "highlighted") {
                nextRowNode.parentNode.removeChild(nextRowNode);
            }
        }

        /*
         * TODO: Document it
         */
        function openOrClosePhase(phaseRow, action) {
            var actionNode = document.getElementsByName("action")[0];
            actionNode.value = action;
            var phaseId = phaseRow.id;
            var actionPhaseNode = document.getElementsByName("action_phase")[0];
            actionPhaseNode.value = phaseId;
            actionNode.form.submit();
        }

        /**
         * TODO: Document it
         */
        function loadTimelineTemplate() {
            // Find template name input node
            templateNameNode = document.getElementsByName("template_name")[0];
            // Get html-encoded template name
            var templateName = templateNameNode.value;

            // assemble the request XML
            var content =
                '<?xml version="1.0" ?>' +
                '<request type="LoadTimelineTemplate">' +
                '<parameters>' +
                '<parameter name="TemplateName"><![CDATA[' +
                templateName +
                ']]></parameter>' +
                '<parameter name="ProjectTypeName">' +
                projectTypeNamesMap[document.getElementsByName("project_type")[0].value] +
                '</parameter>' + '</parameters>' +
                '</request>';

            // Send the AJAX request
            sendRequest(content,
                function (result, respXML) {
                    // operation succeeded
                    // Populate project phases using loaded template
                    populateTimeLineFromTemplate(respXML.getElementsByTagName("timeline")[0]);
                },
                function (result, respXML) {
                    // operation failed, alert the error message to the user
                    alert("An error occured while loading timeline template: " + result);
                }
            );
        }

        /**
         * TODO: Document it
         */
        function populateTimeLineFromTemplate(templateXML) {
            // Clear all the project phases
            var phaseActionNodes = getChildrenByNamePrefix(document, "phase_action");
            for (var i = 1; i < phaseActionNodes.length; i++) {
                phaseActionNodes[i].value = "delete";
                var phaseRowNode = phaseActionNodes[i].parentNode.parentNode;
                phaseRowNode.style["display"] = "none";
                // Remove phase criterion row if needed
                nextRowNode = dojo.dom.nextElement(phaseRowNode);
                if (nextRowNode != null && nextRowNode.className == "highlighted") {
                    nextRowNode.parentNode.removeChild(nextRowNode);
                }
            }

            // Retrieve timeline table
            var timelineTable = document.getElementById("timeline_tbl");

            // Add new project phases
            var phaseNodes = templateXML.getElementsByTagName("phase");
            var phaseRows = [];
            // PASS 1
            for (var i = 0; i < phaseNodes.length; i++)  {
                var phaseName = phaseNodes[i].getAttribute("type");
                var phaseId = phaseNodes[i].getAttribute("id");
                var phaseTypeId = phaseTypeIdsMap[phaseName];
                var newPhaseRow = createNewPhaseRow(phaseName, phaseTypeId, "template_" + phaseId);
                phaseRows[i] = newPhaseRow;
                timelineTable.tBodies[0].appendChild(newPhaseRow);

                getChildByNamePrefix(newPhaseRow, "phase_type").value = phaseTypeId;

                var startDate = dojo.dom.textContent(phaseNodes[i].getElementsByTagName("start-date")[0]);
                var startDateParts = startDate.split(" ");

                getChildByNamePrefix(newPhaseRow, "phase_start_date").value = startDateParts[0];
                getChildByNamePrefix(newPhaseRow, "phase_start_time").value = startDateParts[1];
                getChildByNamePrefix(newPhaseRow, "phase_start_AMPM").value = startDateParts[2].toLowerCase();

                var endDate = dojo.dom.textContent(phaseNodes[i].getElementsByTagName("end-date")[0]);
                var endDateParts = endDate.split(" ");

                getChildByNamePrefix(newPhaseRow, "phase_end_date").value = endDateParts[0];
                getChildByNamePrefix(newPhaseRow, "phase_end_time").value = endDateParts[1];
                getChildByNamePrefix(newPhaseRow, "phase_end_AMPM").value = endDateParts[2].toLowerCase();

                var duration = parseInt(dojo.dom.textContent(phaseNodes[i].getElementsByTagName("length")[0])) / 3600 / 1000;
                getChildByNamePrefix(newPhaseRow, "phase_duration").value = duration;


                // Add phase criterion row if needed
                addPhaseCriterion(phaseName, newPhaseRow);
            }
            // PASS 2
            for (var i = 0; i < phaseNodes.length; i++) {
                var newPhaseRow = phaseRows[i];
                var dependencies = phaseNodes[i].getElementsByTagName("dependency");
                var phaseStartButtons = getChildrenByNamePrefix(newPhaseRow, "phase_start_by_phase");
                for (var j = 0; j < phaseStartButtons.length; j++) {
                    if (phaseStartButtons[j].value == "true") {
                        phaseStartButtons[j].checked = (dependencies.length != 0);
                    } else {
                        phaseStartButtons[j].checked = (dependencies.length == 0);
                    }
                }

                if (dependencies.length != 0) {
                    var dependencyId =  dojo.dom.textContent(dependencies[0].getElementsByTagName("dependency-phase-id")[0]);
                    var dependencyStart =  dojo.dom.textContent(dependencies[0].getElementsByTagName("dependency-phase-start")[0]);
                    getChildByNamePrefix(newPhaseRow, "phase_start_phase").value = "template_" + dependencyId;
                    getChildByNamePrefix(newPhaseRow, "phase_start_when").value = (dependencyStart == "true") ? "starts" : "ends";
                }
            }

        }

        // To be done on page load
        function onLoad() {
            var projectCategoryNode = document.getElementsByName("project_category")[0];
            if (projectCategoryNode.length == 0) {
                onProjectTypeChange(document.getElementsByName("project_type")[0]);
            }
            var templateRow = document.getElementById("screening_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, screeningScorecards, 'Screening');

            templateRow = document.getElementById("review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, reviewScorecards, 'Review');

            templateRow = document.getElementById("approval_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, approvalScorecards, 'Client Review');
        }

    //--></script>
</head>

<body onload="onLoad();">
<%-- <body> --%>

<div align="center">
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">
                    <html:form action="/actions/SaveProject" onsubmit="return validate_form(this, true);">
                        <html:hidden property="method" value="saveProject" />

                        <%-- TODO: Validation errors display should be much more than is here --%>
                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr><td width="16"><!-- @ --></td><td><!-- @ --></td></tr>
                                <tr>
                                    <td colspan="2"><span style="color:red;"><bean:message key="Error.saveReview.ValidationFailed" /></span></td>
                                </tr>
                                <html:errors property="org.apache.struts.action.GLOBAL_MESSAGE" />
                            </table><br />
                        </c:if>

                        <%-- If editing the existing project, render its pid --%>
                        <c:if test="${not newProject}">
                            <html:hidden property="pid" />
                        </c:if>

                        <html:hidden property="js_current_id" />
                        <html:hidden property="action" />
                        <html:hidden property="action_phase" />

                        <%-- If creating a new project, show project details table --%>
                        <c:if test="${newProject}">
                            <table class="scorecard" cellpadding="0" width="100%" style="border-collapse:collapse;">
                                    <tr>
                                    <td class="title" colspan="2"><bean:message key="editProject.ProjectDetails.title" /></td>
                                </tr>
                                <tr>
                                    <td class="valueB"><bean:message key="editProject.ProjectDetails.Name" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <html:text styleClass="inputBox" property="project_name" style="width:350px;" />
                                        <span id="project_name_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr>
                                <tr class="dark">
                                    <td width="9%" class="valueB"><bean:message key="editProject.ProjectDetails.Type" /></td>
                                    <td width="91%" class="value" nowrap="nowrap">
                                        <html:select styleClass="inputBox" property="project_type" style="width:150px;"
                                                onchange="onProjectTypeChange(this);">
                                            <c:forEach items="${projectTypes}" var="type">
                                                <html:option key='ProjectType.${fn:replace(type.name, " ", "")}.plural' value="${type.id}" />
                                            </c:forEach>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr class="light">
                                    <td class="valueB"><bean:message key="editProject.ProjectDetails.Category" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <html:select styleClass="inputBox" property="project_category" style="width:150px;"
                                                onchange="onProjectCategoryChange(this);">
                                            <c:forEach items="${projectCategories}" var="category">
                                                <c:if test="${category.projectType.id eq projectForm.map['project_type']}">
                                                    <html:option key='ProjectCategory.${fn:replace(category.name, " ", "")}' value="${category.id}" />
                                                </c:if>
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
                                        <html:checkbox property="public" styleId="public"/>
                                    </td>
                                </tr>
                                <tr class="light">
                                    <td class="value" nowrap="nowrap">
                                        <b><bean:message key="editProject.ProjectDetails.Payments" /></b><br />
                                    </td>
                                    <td class="value" nowrap="nowrap">
                                        <html:text styleClass="inputBox" property="payments" style="width: 350px;" />
                                        <span id="payments_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr>
                                <tr class="light">
                                    <td class="value" nowrap="nowrap">
                                        <b><bean:message key="editProject.ProjectDetails.DRPoints" /></b><br />
                                    </td>
                                    <td class="value" nowrap="nowrap">
                                        <html:text styleClass="inputBox" property="dr_points" style="width: 350px;" />
                                        <b><bean:message key="editProject.ProjectDetails.DRPointsMessage" /></b>
                                        <span id="dr_points_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lastRowTD" colspan="2"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </c:if>
                        <%-- 
                        <c:if test="${not newProject}">
                            <html:hidden property="project_type" />
                            <html:hidden property="project_category" />
                        </c:if>
                        --%>

                        <%-- If editing the existing project, include timeline editor here --%>
                        <c:if test="${not newProject}">
                            <jsp:include page="/includes/project/project_edit_timeline.jsp" />
                        </c:if>

                        <%-- If editing the existing project, include phases details tabs here --%>
                        <c:if test="${not newProject}">
                            <jsp:include page="/includes/project/project_phase.jsp" />
                        </c:if>

                        <table class="scorecard" id="preferences">
                            <tr>
                                <td class="title" colspan="2"><bean:message key="editProject.Preferences.title" /></td>
                            </tr>
                            <tr class="light">
                                <td class="valueB" width="1%"><bean:message key="editProject.Preferences.Autopilot" /></td>
                                <td class="value">
                                    <html:radio styleId="autopilotOnRadioBox" property="autopilot" value="true" /><label
                                        for="autopilotOnRadioBox"><b><bean:message key="editProject.Preferences.Autopilot.Completion" /></b></label>
                                    <bean:message key="editProject.Preferences.Autopilot.Completion.Desc" /><br/>
                                    <html:radio styleId="autopilotOffRadioBox" property="autopilot" value="false" /><label
                                        for="autopilotOffRadioBox"><b><bean:message key="editProject.Preferences.Autopilot.TurnOff" /></b></label></td>
                            </tr>
                            <tr class="dark">
                                <td class="value" colspan="2">
                                    <html:checkbox styleId="emailNotificationsCheckBox" property="email_notifications" /><label
                                        for="emailNotificationsCheckBox"><b><bean:message key="editProject.Preferences.SendEmails" /></b></label>
                                    <bean:message key="editProject.Preferences.SendEmails.Desc" /><br />
                                    <html:checkbox styleId="noRateProjectCheckBox" property="no_rate_project" /><label
                                        for="noRateProjectCheckBox"><b><bean:message key="editProject.Preferences.DoNotRate" /></b></label><br />
                                    <html:checkbox styleId="timelineNotificationsCheckBox" property="timeline_notifications" /><label
                                        for="timelineNotificationsCheckBox"><b><bean:message key="editProject.Preferences.ReceiveTimeline" /></b></label><br />
                                    <html:checkbox styleId="digitalRunCheckBox" property="digital_run_flag" /><label
                                        for="digitalRunCheckBox"><b><bean:message key="editProject.Preferences.DigitalRun" /></b></label></td>
                            </tr>
                            <tr>
                                <td class="lastRowTD" colspan="2"><!-- @ --></td>
                            </tr>
                        </table><br />

                        <c:set var="projDetRowCount" value="0" />
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
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td class="valueB"><bean:message key="editProject.ProjectDetails.Name" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <html:text styleClass="inputBox" property="project_name" style="width: 350px;" />
                                        <span id="project_name_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td width="9%" class="valueB"><bean:message key="editProject.ProjectDetails.Type" /></td>
                                    <td width="91%" class="value" nowrap="nowrap">
                                        <html:select styleClass="inputBox" property="project_type" style="width:150px;"
                                                onchange="onProjectTypeChange(this);">
                                            <c:forEach items="${projectTypes}" var="type">
                                                <html:option key='ProjectType.${fn:replace(type.name, " ", "")}.plural' value="${type.id}" />
                                            </c:forEach>
                                        </html:select>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td class="valueB"><bean:message key="editProject.ProjectDetails.Category" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <html:select styleClass="inputBox" property="project_category" style="width:150px;"
                                                onchange="onProjectCategoryChange(this);">
                                            <c:forEach items="${projectCategories}" var="category">
                                                <c:if test="${category.projectType.id eq projectForm.map['project_type']}">
                                                    <html:option key='ProjectCategory.${fn:replace(category.name, " ", "")}' value="${category.id}" />
                                                </c:if>
                                            </c:forEach>
                                        </html:select>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td class="value" nowrap="nowrap">
                                        <b><bean:message key="editProject.ProjectDetails.Payments" /></b><br />
                                    </td>
                                    <td class="value" nowrap="nowrap">
                                        <html:text styleClass="inputBox" property="payments" style="width: 350px;" />
                                        <span id="payments_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td class="value" nowrap="nowrap">
                                        <b><bean:message key="editProject.ProjectDetails.DRPoints" /></b><br />
                                    </td>
                                    <td class="value" nowrap="nowrap">
                                        <html:text styleClass="inputBox" property="dr_points" style="width: 350px;" />
                                        <b><bean:message key="editProject.ProjectDetails.DRPointsMessage" /></b>
                                        <span id="dr_points_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            </c:if>

                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="editProject.References.ForumId" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <html:text styleClass="inputBox" property="forum_id" style="width: 350px;" />
                                    <span id="forum_id_validation_msg" style="display:none;" class="error"></span>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="editProject.References.ComponentId" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <html:text styleClass="inputBox" property="component_id" style="width: 350px;" />
                                    <span id="component_id_validation_msg" style="display:none;" class="error"></span>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="editProject.References.ExternalReferenceId" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <html:text styleClass="inputBox" property="external_reference_id" style="width: 350px;" />
                                    <span id="external_reference_id_validation_msg" style="display:none;" class="error"></span>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="editProject.References.SVNModule" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <html:text styleClass="inputBox" property="SVN_module" style="width: 350px;" />
                                    <span id="SVN_module_validation_msg" style="display:none;" class="error"></span>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
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
                                    <div id="notes_validation_msg" style="display:none;" class="error"></div>
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
                                    <td class="value"><p align="left"><b>&#160;<bean:message key="editProject.Status.CurrentStatus" />&#160; </b>
                                        <html:select styleClass="inputBox" property="status">
                                            <c:forEach var="status" items="${projectStatuses}">
                                                <html:option key='ProjectStatus.${fn:replace(status.name, " ", "")}' value="${status.id}" />
                                            </c:forEach>
                                        </html:select></p>
                                        <bean:message key="editProject.Status.Explanation.description" /><br />
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
                                    <td class="Value">
                                        <bean:message key="editProject.Explanation.description" /> &#160;
                                        <span id="explanation_validation_msg" class="error"><html:errors property="explanation" prefix="" suffix="" /></span><br />
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
                                <html:image srcKey="btnSave.img" altKey="btnSave.alt" border="0"/>&#160;
                                <html:link page="/actions/ListProjects.do?method=listProjects"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>
                            </c:if>
                            <c:if test="${not newProject}">
                                <html:image srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>&#160;
                                <html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>
                            </c:if>
                        </div>
                    </html:form>
                </div>
            </div>
        <jsp:include page="/includes/inc_footer.jsp" />
    </div>
</div>
</body>
</html:html>
