<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project edition page.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <c:if test="${empty project}">
        <title><or:text key="global.title.level2"
            arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
            arg1='${orfn:getMessage(pageContext, "editProject.title.CreateNew")}' /></title>
    </c:if>
    <c:if test="${not empty project}">
        <jsp:include page="/includes/project/project_title.jsp" />
    </c:if>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css" />
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_edit_project.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/parseDate.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="/js/or/ajax1.js"><!-- @ --></script>
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
        var genericProjectCategories = [];
        <c:forEach items="${projectCategories}" var="category">
            <c:choose>
                <c:when test="${not category.projectType.generic}">
                projectCategories.push({});
                projectCategories[projectCategories.length - 1]["id"] = ${category.id};
                projectCategories[projectCategories.length - 1]["projectType"] = ${category.projectType.id};
                // TODO: Localize the catagory name
                projectCategories[projectCategories.length - 1]["name"] = "${category.name}";
                </c:when>
                <c:otherwise>
                genericProjectCategories.push({});
                genericProjectCategories[genericProjectCategories.length - 1]["id"] = ${category.id};
                genericProjectCategories[genericProjectCategories.length - 1]["projectType"] = ${category.projectType.id};
                // TODO: Localize the catagory name
                genericProjectCategories[genericProjectCategories.length - 1]["name"] = "${category.name}";
                </c:otherwise>
            </c:choose>
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

        var postMortemScorecards = [];
        <c:forEach items="${requestScope.postMortemScorecards}" var="scorecard">
            postMortemScorecards.push({});
            postMortemScorecards[postMortemScorecards.length - 1]["id"] = ${scorecard.id};
            postMortemScorecards[postMortemScorecards.length - 1]["category"] = ${scorecard.category};
            postMortemScorecards[postMortemScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
        </c:forEach>

        var specificationReviewScorecards = [];
        <c:forEach items="${requestScope.specificationReviewScorecards}" var="scorecard">
        specificationReviewScorecards.push({});
        specificationReviewScorecards[specificationReviewScorecards.length - 1]["id"] = ${scorecard.id};
        specificationReviewScorecards[specificationReviewScorecards.length - 1]["category"] = ${scorecard.category};
        specificationReviewScorecards[specificationReviewScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
        </c:forEach>

        var checkpointScreeningScorecards = [];
        <c:forEach items="${requestScope.checkpointScreeningScorecards}" var="scorecard">
        checkpointScreeningScorecards.push({});
        checkpointScreeningScorecards[checkpointScreeningScorecards.length - 1]["id"] = ${scorecard.id};
        checkpointScreeningScorecards[checkpointScreeningScorecards.length - 1]["category"] = ${scorecard.category};
        checkpointScreeningScorecards[checkpointScreeningScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
        </c:forEach>

        var checkpointReviewScorecards = [];
        <c:forEach items="${requestScope.checkpointReviewScorecards}" var="scorecard">
        checkpointReviewScorecards.push({});
        checkpointReviewScorecards[checkpointReviewScorecards.length - 1]["id"] = ${scorecard.id};
        checkpointReviewScorecards[checkpointReviewScorecards.length - 1]["category"] = ${scorecard.category};
        checkpointReviewScorecards[checkpointReviewScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
        </c:forEach>

        // List of iterative review scorecard
        var iterativeReviewScorecards = [];
        <c:forEach items="${iterativeReviewScorecards}" var="scorecard">
        iterativeReviewScorecards.push({});
        iterativeReviewScorecards[iterativeReviewScorecards.length - 1]["id"] = ${scorecard.id};
        iterativeReviewScorecards[iterativeReviewScorecards.length - 1]["category"] = ${scorecard.category};
        iterativeReviewScorecards[iterativeReviewScorecards.length - 1]["name"] = "${scorecard.name} ${scorecard.version}";
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
            <c:if test="${not projectType.generic}">
            projectTypeNamesMap["${projectType.id}"] = "${projectType.name}";
            </c:if>
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

        var screeningScorecardNodes = new Array();
        var reviewScorecardNodes = new Array();
        var approvalScorecardNodes = new Array();
        var postMortemScorecardNodes = new Array();
        var specReviewScorecardNodes = new Array();
        var checkpointScreeningScorecardNodes = new Array();
        var checkpointReviewScorecardNodes = new Array();
        var iterativeReviewScorecardNodes = new Array(); // Iterative review scorecard nodes

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

        /**
        * Disable or enable the select widget.
        *
        * @param prefix the prefix of select widget name.
        * @param disabled true if to disable, false to enable.
        * @param canEdit whether we can edit the select widget.
         */
        function disableSelect(prefix, disabled, canEdit) {
            if (!canEdit) return;
            var eles = getChildrenByNamePrefix(document.body, prefix);
            for (var i = 0; i < eles.length; i++) {
                var ori = eles[i].disabled;
                eles[i].disabled = disabled ? "disabled" : "";
                if (ori != "disabled" && disabled) eles[i].value = "1";
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
            disableSelect("contest_prizes_num[", projectTypeNode.value != 3, canEditContestPrize);
            disableSelect("contest_prizes_num_dump[", projectTypeNode.value != 3, canEditContestPrize);
        }

        function onProjectCategoryChange(projectCategoryNode) {
            var templateRow = document.getElementById("screening_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, screeningScorecards, 'Screening');

            templateRow = document.getElementById("review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, reviewScorecards, 'Review');

            templateRow = document.getElementById("approval_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, approvalScorecards, 'Approval');

            templateRow = document.getElementById("post_mortem_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, postMortemScorecards, 'Post-Mortem');

            templateRow = document.getElementById("specification_review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, specificationReviewScorecards, 'Specification Review');

            templateRow = document.getElementById("checkpoint_review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, checkpointReviewScorecards, 'Checkpoint Review');

            templateRow = document.getElementById("checkpoint_screening_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, checkpointScreeningScorecards, 'Checkpoint Screening');

            // Populate iterative review scorecards
            templateRow = document.getElementById("iterative_review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, iterativeReviewScorecards, 'Iterative Review');


            for (var i = 0; i < screeningScorecardNodes.length; i++) {
                changeScorecardByCategory(screeningScorecardNodes[i], projectCategoryNode.value, screeningScorecards, 'Screening');
            }
            for (var i = 0; i < reviewScorecardNodes.length; i++) {
                changeScorecardByCategory(reviewScorecardNodes[i], projectCategoryNode.value, reviewScorecards, 'Review');
            }
            for (var i = 0; i < approvalScorecardNodes.length; i++) {
                changeScorecardByCategory(approvalScorecardNodes[i], projectCategoryNode.value, approvalScorecards, 'Approval');
            }

            for (var i = 0; i < postMortemScorecardNodes.length; i++) {
                changeScorecardByCategory(postMortemScorecardNodes[i], projectCategoryNode.value, postMortemScorecards, 'Post-Mortem');
            }
            for (var i = 0; i < specReviewScorecardNodes.length; i++) {
                changeScorecardByCategory(specReviewScorecardNodes[i], projectCategoryNode.value, specificationReviewScorecards, 'Specification Review');
            }
            for (var i = 0; i < checkpointScreeningScorecardNodes.length; i++) {
                changeScorecardByCategory(checkpointScreeningScorecardNodes[i], projectCategoryNode.value, checkpointScreeningScorecards, 'Checkpoint Screening');
            }
            for (var i = 0; i < checkpointReviewScorecardNodes.length; i++) {
                changeScorecardByCategory(checkpointReviewScorecardNodes[i], projectCategoryNode.value, checkpointReviewScorecards, 'Checkpoint Review');
            }

            //var digitalRunChecked = false;
            //var publicChecked = false;

            //if (projectCategoryNode.value == developmentCatId ||
            //    projectCategoryNode.value == designCatId ||
            //    projectCategoryNode.value == assemblyCatId ||
            //    projectCategoryNode.value == compTestingCatId) {

            //    digitalRunChecked = true;

            //    if (projectCategoryNode.value != assemblyCatId) {
            //        publicChecked = true;
            //    }
            //}

            //document.getElementById("digitalRunCheckBox").checked = digitalRunChecked;
            //document.getElementById("public").checked = publicChecked;
        }

        function changeScorecardByCategory(scorecardNode, category, scorecards, scorecardName) {
            if (scorecardNode) {
                // Clear combo options
                while (scorecardNode.length > 0) {
                    scorecardNode.remove(scorecardNode.length - 1);
                }
                // Add new combo options for selected project category and generic project categories
                for (var i = 0; i < scorecards.length; i++) {
                    var valid = false;
                    if (category == scorecards[i]["category"]) {
                        valid = true;
                    } else {
                        for (var j = 0; !valid && (j < genericProjectCategories.length); j++) {
                            var genericCategoryId = genericProjectCategories[j]["id"];
                            if (genericCategoryId == scorecards[i]["category"]) {
                                valid = true;
                            }
                        }
                    }
                    if (valid) {
                        addComboOption(scorecardNode, scorecards[i]["name"], scorecards[i]["id"]);
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
            var buttonsCell = allNewCells[2];

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
            var inputs = resourceRowNode.cells[2].getElementsByTagName("input");
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
            if (phaseName == "Screening" || phaseName == "Specification Review" || phaseName == "Review" || phaseName == "Approval" ||
                    phaseName == "Registration" || phaseName == "Appeals"
                    || phaseName == "Post-Mortem" || phaseName == "Checkpoint Screening" 
                    || phaseName == "Checkpoint Review" || phaseName == "Iterative Review") {
                var templateRow;
                if (phaseName == "Screening") {
                      templateRow = document.getElementById("screening_scorecard_row_template");
                } else if (phaseName == "Review") {
                      templateRow = document.getElementById("review_scorecard_row_template");
                } else if (phaseName == "Approval") {
                      templateRow = document.getElementById("approval_scorecard_row_template");
                } else if (phaseName == "Registration") {
                      templateRow = document.getElementById("required_registrations_row_template");
                } else if (phaseName == "Appeals") {
                      templateRow = document.getElementById("view_appeal_responses_row_template");
                } else if (phaseName == "Post-Mortem") {
                      templateRow = document.getElementById("post_mortem_scorecard_row_template");
                } else if (phaseName == "Specification Review") {
                      templateRow = document.getElementById("specification_review_scorecard_row_template");
                } else if (phaseName == "Checkpoint Screening") {
                      templateRow = document.getElementById("checkpoint_screening_scorecard_row_template");
                } else if (phaseName == "Checkpoint Review") {
                      templateRow = document.getElementById("checkpoint_review_scorecard_row_template");
                } else if (phaseName == "Iterative Review") {
                      templateRow = document.getElementById("iterative_review_scorecard_row_template");
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
                 } else if (phaseName == "Post-Mortem") {
                     postMortemScorecardNodes[postMortemScorecardNodes.length] = criterionRow.getElementsByTagName("select")[0];
                 } else if (phaseName == "Specification Review") {
                     specReviewScorecardNodes[specReviewScorecardNodes.length] = criterionRow.getElementsByTagName("select")[0];
                 } else if (phaseName == "Checkpoint Screening") {
                     checkpointScreeningScorecardNodes[checkpointScreeningScorecardNodes.length] = criterionRow.getElementsByTagName("select")[0];
                 } else if (phaseName == "Checkpoint Review") {
                     checkpointReviewScorecardNodes[checkpointReviewScorecardNodes.length] = criterionRow.getElementsByTagName("select")[0];
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
                    if (resourcePhaseNodes[i].tagName == 'SELECT') {
                        addComboOption(resourcePhaseNodes[i], phaseNumber, phaseId);
                    }
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
                "start_date",
                "start_time",
                "start_phase",
                "start_when",
                "start_plusminus",
                "start_amount",
                "start_dayshrs",
                "use_duration",
                "end_date",
                "end_time",
                "duration",
                "start_by_fixed_time",
                "start_by_phase"];
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

            // Trigger enabling/disabling
            var chkInputNames = ["start_by_fixed_time", "start_by_phase"];
            for (var i = 0; i < chkInputNames.length; i++) {
                var srcInputs = getChildrenByName(addPhaseTable, "addphase_" + chkInputNames[i]);
                var destInputs = getChildrenByName(newRow, "phase_" + chkInputNames[i] + "[" + lastPhaseIndex + "]");
                if (srcInputs[0].checked) {
                    if (chkInputNames[i] == 'start_by_fixed_time') {
                        fixedStartTimeBoxChanged(destInputs[0]);
                    } else {
                        if (chkInputNames[i] == 'start_by_phase') {
                            phaseStartByPhaseBoxChanged(destInputs[0]);
                        }
                    }
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
            var s = validate_form(actionNode.form, true);
            if (s) {
                actionNode.form.submit();
            }
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
                    onProjectCategoryChange(document.getElementsByName("project_category")[0]);
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
                if (startDateParts[0] && startDateParts[0] != '') {
                    getChildByNamePrefix(newPhaseRow, "phase_start_by_fixed_time").checked = true;
                    getChildByNamePrefix(newPhaseRow, "phase_start_date").removeAttribute("disabled");
                    getChildByNamePrefix(newPhaseRow, "phase_start_time").removeAttribute("disabled");
                }

                var endDate = dojo.dom.textContent(phaseNodes[i].getElementsByTagName("end-date")[0]);
                var endDateParts = endDate.split(" ");

                getChildByNamePrefix(newPhaseRow, "phase_end_date").value = endDateParts[0];
                getChildByNamePrefix(newPhaseRow, "phase_end_time").value = endDateParts[1];

                var duration = parseInt(dojo.dom.textContent(phaseNodes[i].getElementsByTagName("length")[0])) / 3600 / 1000;

                getChildByNamePrefix(newPhaseRow, "phase_duration").value = duration;

                // Add phase criterion row if needed
                addPhaseCriterion(phaseName, newPhaseRow);
            }
            // PASS 2
            for (var i = 0; i < phaseNodes.length; i++) {
                var newPhaseRow = phaseRows[i];
                var dependencies = phaseNodes[i].getElementsByTagName("dependency");

                if (dependencies.length != 0) {
                    getChildByNamePrefix(newPhaseRow, "phase_start_date").value = '';
                    getChildByNamePrefix(newPhaseRow, "phase_start_date").setAttribute("disabled", "disabled");
                    getChildByNamePrefix(newPhaseRow, "phase_start_time").value = '';
                    getChildByNamePrefix(newPhaseRow, "phase_start_time").setAttribute("disabled", "disabled");
                    getChildByNamePrefix(newPhaseRow, "phase_start_by_fixed_time").checked = false;

                    var dependencyId =  dojo.dom.textContent(dependencies[0].getElementsByTagName("dependency-phase-id")[0]);
                    var dependencyStart =  dojo.dom.textContent(dependencies[0].getElementsByTagName("dependency-phase-start")[0]);
                    var lagTime =  parseInt(dojo.dom.textContent(dependencies[0].getElementsByTagName("lag-time")[0]));
                    if (lagTime < 0) {
                        lagTime = -lagTime;
                        getChildByNamePrefix(newPhaseRow, "phase_start_plusminus").value = 'minus';
                    }
                    
                    if (lagTime != 0) {
                        getChildByNamePrefix(newPhaseRow, "phase_start_dayshrs").value = 'mins';
                    }
                    
                    getChildByNamePrefix(newPhaseRow, "phase_start_phase").value = "template_" + dependencyId;
                    getChildByNamePrefix(newPhaseRow, "phase_start_when").value = (dependencyStart == "true") ? "starts" : "ends";
                    getChildByNamePrefix(newPhaseRow, "phase_start_amount").value = '' + lagTime / 1000 / 60;
                    var phaseStartButtons = getChildByNamePrefix(newPhaseRow, "phase_start_by_phase");
                    phaseStartButtons.checked = true;
                    getChildByNamePrefix(newPhaseRow, "phase_start_phase").removeAttribute("disabled");
                    getChildByNamePrefix(newPhaseRow, "phase_start_when").removeAttribute("disabled");
                    getChildByNamePrefix(newPhaseRow, "phase_start_plusminus").removeAttribute("disabled");
                    getChildByNamePrefix(newPhaseRow, "phase_start_amount").removeAttribute("disabled");
                    getChildByNamePrefix(newPhaseRow, "phase_start_dayshrs").removeAttribute("disabled");
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
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, approvalScorecards, 'Approval');

            templateRow = document.getElementById("post_mortem_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, postMortemScorecards, 'Post-Mortem');

            templateRow = document.getElementById("specification_review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, specificationReviewScorecards, 'Specification Review');

            templateRow = document.getElementById("checkpoint_screening_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, checkpointScreeningScorecards, 'Checkpoint Screening');

            templateRow = document.getElementById("checkpoint_review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, checkpointReviewScorecards, 'Checkpoint Review');

            // Populate iterative review scorecard on page load
            templateRow = document.getElementById("iterative_review_scorecard_row_template");
            changeScorecardByCategory(templateRow.getElementsByTagName("select")[0], projectCategoryNode.value, iterativeReviewScorecards, 'Iterative Review');
        }

        /**
         * Show the prize tab.
         *
         * @param id the id of the tab to show.
         * @param aObject the DOM object of the link which have been clicked.
         */
        function showPrizeTab(id, aObject) {
            var liEles = aObject.parentNode.parentNode.getElementsByTagName("li");
            for (var i = 0; i < liEles.length; i++) {
                liEles[i].className = "";
            }
            aObject.parentNode.className = "current";

            // Remove focus from the link that triggered the activation
            if (aObject.blur) {
                aObject.blur();
            }

            document.getElementById("contest-prizes-table").style.display = "none";
            document.getElementById("checkpoint-prizes-table").style.display = "none";
            document.getElementById(id).style.display = "table";
        }

        /**
         * Add a prize when user clicking Add button..
         *
         * @param alink the DOM object of the link which have been clicked.
         * @param prefix the prefix of the name.
         */
        function addPrize(alink, prefix) {
            var td = alink.parentNode;
            var tr = td.parentNode;
            var rows = tr.parentNode.rows;
            var newRow = cloneInputRow(rows[2]);

            var curId = rows.length - 5;
            newRow.cells[0].innerHTML = curId + 1;
            newRow.style.display = "table-row";
            getChildByNamePrefix(newRow, prefix + "_amount_dump").name = prefix + "_amount[0]";
            getChildByNamePrefix(newRow, prefix + "_num_dump").name = prefix + "_num[0]";
            patchAllChildParamIndexes(newRow, curId);
            getChildByNamePrefix(newRow, prefix + "_amount[").value = "";
            getChildByNamePrefix(newRow, prefix + "_num[").value = "1";
            if (rows.length - 5 > 0) {
                rows[rows.length - 3].cells[3].innerHTML = "";
            }
            tr.parentNode.insertBefore(newRow, rows[rows.length - 2]);
            rows = tr.parentNode.rows;
            for (var idx = 3; idx < rows.length - 1; idx++) {
                rows[idx].className = (idx - 3) % 2 == 0 ? "light" : "dark";
            }
        }

        /**
         * Remove a prize when user clicking Delete button..
         *
         * @param alink the DOM object of the link which have been clicked.
         */
        function removePrize(alink) {
            var tr = alink.parentNode.parentNode;
            var tbody = tr.parentNode;
            tbody.removeChild(tr);
            var rows = tbody.rows;
            for (var idx = 3; idx < rows.length - 2; idx++) {
                rows[idx].cells[0].innerHTML = idx - 2;
                patchAllChildParamIndexes(rows[idx], idx - 3);
                rows[idx].className = (idx - 3) % 2 == 0 ? "light" : "dark";
            }
            if (rows.length - 5 > 0) {
                rows[rows.length - 3].cells[3].innerHTML = '<img src="/i/or/bttn_delete.gif" style="cursor: pointer; display: inline; " alt="Delete" onclick="removePrize(this);">';
            }
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
                    <s:form action="SaveProject" onsubmit="return validate_form(this, true);" namespace="/actions">

                        <%-- TODO: Validation errors display should be much more than is here --%>
                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr><td><!-- @ --></td><td width="400"><!-- @ --></td></tr>
                                <tr>
                                    <td colspan="2"><span style="color:red;"><or:text key="Error.saveReview.ValidationFailed" /></span></td>
                                </tr>
                                <tr><td><!-- @ --></td><td class="errorText"><s:actionerror escape="false"/></td></tr>
                            </table><br />
                        </c:if>

                        <%-- If editing the existing project, render its pid --%>
                        <c:if test="${not newProject}">
                            <input type="hidden" name="pid"  value="<or:fieldvalue field='pid' />" />
                        </c:if>

                        <input type="hidden" name="js_current_id"  value="<or:fieldvalue field='js_current_id' />" />
                        <input type="hidden" name="action"  value="<or:fieldvalue field='action' />" />
                        <input type="hidden" name="action_phase"  value="<or:fieldvalue field='action_phase' />" />
                        <input type="hidden" name="last_modification_time"  value="<or:fieldvalue field='last_modification_time' />" />

                        <%-- If creating a new project, show project details table --%>
                        <c:if test="${newProject}">
                            <table class="scorecard" cellpadding="0" width="100%" style="border-collapse:collapse;">
                                    <tr>
                                    <td class="title" colspan="2"><or:text key="editProject.ProjectDetails.title" /></td>
                                </tr>
                                <tr>
                                    <td class="valueB"><or:text key="editProject.ProjectDetails.Name" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <input type="text" class="inputBox" name="project_name" style="width:350px;"  value="<or:fieldvalue field='project_name' />" />
                                        <span id="project_name_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr>
                                <tr class="dark">
                                    <td width="9%" class="valueB"><or:text key="editProject.ProjectDetails.Type" /></td>
                                    <td width="91%" class="value" nowrap="nowrap">
                                        <select class="inputBox" name="project_type" style="width:150px;"
                                                onchange="onProjectTypeChange(this);"><c:set var="OR_FIELD_TO_SELECT" value="project_type"/>
                                            <c:forEach items="${projectTypes}" var="type">
                                                <c:if test="${not type.generic}">
                                                    <option  value="${type.id}"  <or:selected value="${type.id}"/>><or:text key='ProjectType.${fn:replace(type.name, " ", "")}.plural' def="${type.name}" /></option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr class="light">
                                    <td class="valueB"><or:text key="editProject.ProjectDetails.Category" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <select class="inputBox" name="project_category" style="width:150px;"
                                                onchange="onProjectCategoryChange(this);"><c:set var="OR_FIELD_TO_SELECT" value="project_category"/>
                                            <c:forEach items="${projectCategories}" var="category">
                                                <c:if test="${category.projectType.id eq projectForm.map['project_type']}">
                                                    <option  value="${category.id}"  <or:selected value="${category.id}"/>><or:text key="ProjectCategory.${fn:replace(category.name, ' ', '')}" def="${category.name}" /></option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr class="light">
                                    <td class="value" nowrap="nowrap">
                                        <b><or:text key="editProject.ProjectDetails.DRPoints" /></b><br />
                                    </td>
                                    <td class="value" nowrap="nowrap">
                                        <input type="text" class="inputBox" name="dr_points" style="width: 350px;"  value="<or:fieldvalue field='dr_points' />" />
                                        <!-- <b><or:text key="editProject.ProjectDetails.DRPointsMessage" /></b> -->
                                        <span id="dr_points_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lastRowTD" colspan="2"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </c:if>

                        <%-- If creating a new project, show the edit prizes section here --%>
                        <c:if test="${newProject}">
                            <jsp:include page="/includes/project/project_edit_prizes.jsp" />
                        </c:if>

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
                                <td class="title" colspan="2"><or:text key="editProject.Preferences.title" /></td>
                            </tr>
                            <tr class="light">
                                <td class="valueB" width="1%"><or:text key="editProject.Preferences.Autopilot" /></td>
                                <td class="value">
                                    <input type="radio" id="autopilotOnRadioBox" name="autopilot" value="true"  <or:checked name='autopilot' value='true' />/><label
                                        for="autopilotOnRadioBox"><b><or:text key="editProject.Preferences.Autopilot.Completion" /></b></label>
                                    <or:text key="editProject.Preferences.Autopilot.Completion.Desc" /><br/>
                                    <input type="radio" id="autopilotOffRadioBox" name="autopilot" value="false"  <or:checked name='autopilot' value='false' />/><label
                                        for="autopilotOffRadioBox"><b><or:text key="editProject.Preferences.Autopilot.TurnOff" /></b></label></td>
                            </tr>
                            <tr class="dark">
                                <td class="value" colspan="2">
                                    <input type="checkbox" id="emailNotificationsCheckBox" name="email_notifications"  <or:checked name='email_notifications' value='on|yes|true' /> /><label
                                        for="emailNotificationsCheckBox"><b><or:text key="editProject.Preferences.SendEmails" /></b></label>
                                    <or:text key="editProject.Preferences.SendEmails.Desc" /><br />
                                    <input type="checkbox" id="noRateProjectCheckBox" name="no_rate_project"  <or:checked name='no_rate_project' value='on|yes|true' /> /><label
                                        for="noRateProjectCheckBox"><b><or:text key="editProject.Preferences.DoNotRate" /></b></label><br />
                                    <input type="checkbox" id="timelineNotificationsCheckBox" name="timeline_notifications"  <or:checked name='timeline_notifications' value='on|yes|true' /> /><label
                                        for="timelineNotificationsCheckBox"><b><or:text key="editProject.Preferences.ReceiveTimeline" /></b></label><br />
                                    <input type="checkbox" id="digitalRunCheckBox" name="digital_run_flag"  <or:checked name='digital_run_flag' value='on|yes|true' /> /><label
                                        for="digitalRunCheckBox"><b><or:text key="editProject.Preferences.DigitalRun" /></b></label></td>
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
                                    <td class="title" colspan="2"><or:text key="editProject.References.title" /></td>
                                </c:if>
                                <%-- If editing the existing project, name this table as "Project Details" --%>
                                <c:if test="${not newProject}">
                                    <td class="title" colspan="2"><or:text key="editProject.ProjectDetails.title" /></td>
                                </c:if>
                            </tr>
                            <%-- If editing the existing project, should have project name edited here --%>
                            <c:if test="${not newProject}">
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td class="valueB"><or:text key="editProject.ProjectDetails.Name" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <input type="text" class="inputBox" name="project_name" style="width: 350px;"  value="<or:fieldvalue field='project_name' />" />
                                        <span id="project_name_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td width="9%" class="valueB"><or:text key="editProject.ProjectDetails.Type" /></td>
                                    <td width="91%" class="value" nowrap="nowrap">
                                        <select class="inputBox" name="project_type" style="width:150px;"
                                                onchange="onProjectTypeChange(this);"><c:set var="OR_FIELD_TO_SELECT" value="project_type"/>
                                            <c:forEach items="${projectTypes}" var="type">
                                                <c:if test="${not type.generic}">
                                                    <option  value="${type.id}"  <or:selected value="${type.id}"/>><or:text key="ProjectType.${fn:replace(type.name, ' ', '')}.plural" def="${type.name}" /></option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td class="valueB"><or:text key="editProject.ProjectDetails.Category" /></td>
                                    <td class="value" nowrap="nowrap">
                                        <select class="inputBox" name="project_category" style="width:150px;"
                                                onchange="onProjectCategoryChange(this);"><c:set var="OR_FIELD_TO_SELECT" value="project_category"/>
                                            <c:forEach items="${projectCategories}" var="category">
                                                <c:if test="${category.projectType.id eq projectForm.map['project_type']}">
                                                    <option  value="${category.id}"  <or:selected value="${category.id}"/>><or:text key="ProjectCategory.${fn:replace(category.name, ' ', '')}" def="${category.name}" /></option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td class="value" nowrap="nowrap">
                                        <b><or:text key="editProject.ProjectDetails.DRPoints" /></b><br />
                                    </td>
                                    <td class="value" nowrap="nowrap">
                                        <input type="text" class="inputBox" name="dr_points" style="width: 350px;"  value="<or:fieldvalue field='dr_points' />" />
                                        <span id="dr_points_validation_msg" style="display:none;" class="error"></span>
                                    </td>
                                </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            </c:if>

                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="editProject.References.ForumId" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <input type="text" class="inputBox" name="forum_id" style="width: 350px;"  value="<or:fieldvalue field='forum_id' />" />
                                    <span id="forum_id_validation_msg" style="display:none;" class="error"></span>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="editProject.References.ComponentId" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <input type="text" class="inputBox" name="component_id" style="width: 350px;"  value="<or:fieldvalue field='component_id' />" />
                                    <span id="component_id_validation_msg" style="display:none;" class="error"></span>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="editProject.References.ExternalReferenceId" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <input type="text" class="inputBox" name="external_reference_id" style="width: 350px;"  value="<or:fieldvalue field='external_reference_id' />" />
                                    <span id="external_reference_id_validation_msg" style="display:none;" class="error"></span>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="editProject.References.SVNModule" /></b><br />
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <c:out value="${projectForm.map['SVN_module']}"/>
                                </td>
                            </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                            <!-- since: Online Review Update - Add Project Dropdown v1.0 -->

                            <c:choose>
                                <c:when test="${allowBillingEdit}">
                                    <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                        <td class="value" nowrap="nowrap">
                                            <b><or:text key="editProject.ProjectDetails.BillingProject" /></b><br />
                                        </td>
                                        <td class="value" nowrap="nowrap">
                                            <select class="inputBox" name="billing_project" style="width:150px;"><c:set var="OR_FIELD_TO_SELECT" value="billing_project"/>
                                                <c:forEach var="billingProject" items="${billingProjects}">
                                                    <option value="${billingProject.id}" <or:selected value="${billingProject.id}"/>>${billingProject.name}</option>
                                                </c:forEach>
                                            </select>
                                            <span id="billing_project_validation_msg" style="display:none;" class="error"></span>
                                        </td>
                                    </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="billing_project" value="<or:fieldvalue field='billing_project' />" />
                                </c:otherwise>
                            </c:choose><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />

                            <c:choose>
                                <c:when test="${allowCockpitProjectEdit}">
                                    <tr class="${(projDetRowCount % 2 == 0) ? 'light' : 'dark'}">
                                        <td class="value" nowrap="nowrap">
                                            <b><or:text key="editProject.ProjectDetails.CockpitProject" /></b><br />
                                        </td>
                                        <td class="value" nowrap="nowrap">
                                            <select class="inputBox" name="cockpit_project" style="width:150px;"><c:set var="OR_FIELD_TO_SELECT" value="cockpit_project"/>
                                                <c:forEach var="cockpitProject" items="${cockpitProjects}">
                                                    <option value="${cockpitProject.id}" <or:selected value="${cockpitProject.id}"/>>${cockpitProject.name}</option>
                                                </c:forEach>
                                            </select>
                                            <span id="cockpit_project_validation_msg" style="display:none;" class="error"></span>
                                        </td>
                                    </tr><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="cockpit_project" value="<or:fieldvalue field='cockpit_project' />" />
                                </c:otherwise>
                            </c:choose><c:set var="projDetRowCount" value="${projDetRowCount + 1}" />

                            <tr>
                                <td class="lastRowTD" colspan="2"><!-- @ --></td>
                            </tr>
                        </table><br />

                        <%-- If edit an existing project, show the edit prizes section here --%>
                        <c:if test="${not newProject}">
                            <jsp:include page="/includes/project/project_edit_prizes.jsp" />
                        </c:if>

                        <table class="scorecard" cellpadding="0" cellspacing="0" width="100%"style="border-collapse: collapse;">
                            <tr>
                                <td class="title"><or:text key="editProject.Notes.title" /></td>
                            </tr>
                            <tr class="light">
                                <td class="value">
                                    <span class="error"><s:fielderror escape="false"><s:param>notes</s:param></s:fielderror></span><br />
                                    <textarea class="inputTextBox" name="notes" ><or:fieldvalue field="notes" /></textarea>
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
                                    <td class="title"><or:text key="editProject.Status.title" /></td>
                                </tr>
                                <tr class="light">
                                    <td class="value">
                                        <p align="left"><b>&#160;<or:text key="editProject.Status.CurrentStatus" />&#160; </b>
                                          <select class="inputBox" name="status"><c:set var="OR_FIELD_TO_SELECT" value="status"/>
                                            <c:forEach var="status" items="${projectStatuses}">
                                                <option  value="${status.id}"  <or:selected value="${status.id}"/>><or:text key="ProjectStatus.${fn:replace(status.name, ' ', '')}" def="${status.name}" /></option>
                                            </c:forEach>
                                          </select><br />
                                          <span class="errorText"><s:fielderror escape="false"><s:param>status</s:param></s:fielderror></span>
                                        </p>
                                        <or:text key="editProject.Status.Explanation.description" /><br />
                                        <textarea class="inputTextBox" name="status_explanation" ><or:fieldvalue field="status_explanation" /></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lastRowTD"><!-- @ --></td>
                                </tr>
                            </table><br />

                            <table class="scorecard" id="Explanation">
                                <tr>
                                    <td class="title"><or:text key="editProject.Explanation.title" /></td>
                                </tr>
                                <tr class="light">
                                    <td class="Value">
                                        <or:text key="editProject.Explanation.description" /> &#160;
                                        <span id="explanation_validation_msg" class="error"><s:fielderror escape="false"><s:param>explanation</s:param></s:fielderror></span><br />
                                        <textarea class="inputTextBox" name="explanation" ><or:fieldvalue field="explanation" /></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lastRowTD"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </c:if>

                        <div align="right">
                            <c:if test="${newProject}">
                                <input type="image"  src="<or:text key='btnSave.img' />" alt="<or:text key='btnSave.alt' />" border="0" />&#160;
                                <a href="<or:url value='/actions/ListProjects?scope=my' />"><img src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0" /></a>
                            </c:if>
                            <c:if test="${not newProject}">
                                <input type="image"  src="<or:text key='btnSaveChanges.img' />" alt="<or:text key='btnSaveChanges.alt' />" border="0"/>&#160;
                                <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0"/></a>
                            </c:if>
                        </div>
                    </s:form>
                </div>
            </div>
        <jsp:include page="/includes/inc_footer.jsp" />
    </div>
</div>
</body>
<script type="text/javascript">
    var canEditContestPrize = ${canEditContestPrize};
    var canEditCheckpointPrize = ${canEditCheckpointPrize};
    var studio = document.getElementsByName('project_type')[0].value == 3;
    disableSelect("contest_prizes_num[", !studio, canEditContestPrize);
    disableSelect("contest_prizes_num_dump[", !studio, canEditContestPrize);
    disableSelect("checkpoint_prizes_num[", false, canEditCheckpointPrize);
    disableSelect("checkpoint_prizes_num_dump[", false, canEditCheckpointPrize);
</script>
</html>
