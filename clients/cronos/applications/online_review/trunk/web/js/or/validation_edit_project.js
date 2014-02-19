/**
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 *
 * Provides the validation functions for the editing project forms.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */

/**
 * Adds a validation error message to the message list and shows the message in the message div.
 * @param msg the error message
 * @param msgPrefix the prefix of the error message to add to the list
 * @param msgDiv the div element to show the error message
 * @param msgList the list of error messages
 */
function add_error_message(msg, msgPrefix, msgDiv, msgList) {
    if (emptyString.test(msgDiv.innerHTML)) {
        msgDiv.innerHTML = "&#160;*" + msg;
    } else {
        msgDiv.innerHTML += ";&#160; " + msg;
    }

    if (msgDiv.tagName.toLowerCase() == "div") {
        msgDiv.style.display = "block";
    } else {
        msgDiv.style.display = "inline";
    }

    if (msgList != null) {
        msgList.push(msgPrefix + msg);
    }
}

/**
 * Validates the project name.
 */
function validate_project_name(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("project_name_validation_msg");

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var project_name = thisForm["project_name"].value;
    if (emptyString.test(project_name)) {
        msg = "Project Name is a required field";
        add_error_message(msg, "", msgDiv, msgList);
    }

    return msg == null;
}

/**
 * Validates the forum id.
 */
function validate_forum_id(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("forum_id_validation_msg");

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var forum_id = thisForm["forum_id"].value;
    if (forum_id.length > 0) {
        if (!isAllDigits(forum_id) || !isInteger(forum_id)) {
            msg = "Forum Id should be an integer [0, 2147483647]";
            add_error_message(msg, "", msgDiv, msgList);
        }
    } else {
        msg = "Forum Id is a required field";
        add_error_message(msg, "", msgDiv, msgList);
    }

    return msg == null;
}

/**
 * Validates the component id.
 */
function validate_component_id(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("component_id_validation_msg");

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var component_id = thisForm["component_id"].value;
    if (component_id.length > 0) {
        if (!isAllDigits(component_id) || !isInteger(component_id)) {
            msg = "Component Id should be an integer [0, 2147483647]";
            add_error_message(msg, "", msgDiv, msgList);
        }
    } else {
        var project_type = thisForm["project_type"].value;
        if (project_type == "1") {
            msg = "If project type is Component, Component Id field is required";
            add_error_message(msg, "", msgDiv, msgList);
        }
    }

    return msg == null;
}

/**
 * Validates the external reference id.
 */
function validate_external_reference_id(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("external_reference_id_validation_msg");

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var external_reference_id = thisForm["external_reference_id"].value;
    if (external_reference_id.length > 0) {
        if (!isAllDigits(external_reference_id) || !isInteger(external_reference_id)) {
            msg = "Component Version Id should be an integer [0, 2147483647]";
            add_error_message(msg, "", msgDiv, msgList);
        }
    } else {
        var project_type = thisForm["project_type"].value;
        if (project_type == "1") {
            msg = "If project type is Component, Component Version Id field is required";
            add_error_message(msg, "", msgDiv, msgList);
        }
    }

    return msg == null;
}

/**
 * Validates the dr points.
 */
function validate_dr_points(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("dr_points_validation_msg");

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var points = thisForm["dr_points"].value;
    if (points.length > 0) {
        if (!isFloat(points)) {
            msg = "Invalid Points";
            add_error_message(msg, "", msgDiv, msgList);
        }
    }

    return msg == null;
}

/**
 * Validates the SVN Module field.
 */
function validate_svn_module(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("SVN_module_validation_msg");

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var svnModule = thisForm["SVN_module"].value;

    //This check actually not required
    /*

     if (emptyString.test(svnModule)) {
     msg = "SVN Module is a required field";
     add_error_message(msg, "", msgDiv, msgList);
     }

     */

    return msg == null;
}

/**
 * Checks whether a double number is at most accurate to 0.01.
 *
 * @param value the double number to check.
 * @return {boolean} true if the double number is at most accurate to 0.01, false otherwise.
 */
function checkDoubleWith2Decimal(value) {
    value = "" + value;
    var index = value.indexOf(".");
    if (index < 0) return true;
    for (var ind = value.length - 1; ind > index; ind--) {
        if (value[ind] != '0') return ind - index <= 2;
    }
    return true;
}

/**
 * Validates the prizes amount data.
 *
 * @param table the table containing the prizes amount data.
 * @param prefix the prefix of input field name.
 * @param msgPrefix the prefix of error message.
 * @param thisForm the form containing the prizes amount data.
 * @param msgList the message list.
 * @return {number} the number of prizes, -1 if validation fails.
 */
function validate_prizes_amount(table, prefix, msgPrefix, thisForm, msgList) {
    var rows = table.rows;
    var msg = null;
    var tot = 0;
    var endIndex = rows[rows.length - 2].cells.length == 1 ? rows.length - 2 : rows.length - 1;
    for (var idx = 3; idx < endIndex; idx++) {
        var newIdx = idx - 3;
        var amount = thisForm[prefix + '_amount[' + newIdx + ']'].value;
        var msgDiv = getChildByName(rows[idx], 'prize_amount_validation_msg');
        msgDiv.innerHTML = "";
        msgDiv.style.display = "none";

        amount = trimString(amount);
        if (amount.length == 0 || !isFloat(amount) || parseFloat(amount) <= 0) {
            msg = "Prize Amount should be positive number";
            add_error_message(msg, msgPrefix + " " + (idx - 2) + ": ", msgDiv, msgList);
        } else {
            if (!checkDoubleWith2Decimal(amount)) {
                msg = "Prize Amount can only be accurate to 0.01";
                add_error_message(msg, msgPrefix + " " + (idx - 2) + ": ", msgDiv, msgList);
            } else {
                tot++;
            }
        }
    }
    if (msg != null) {
        return -1;
    }
    return tot;
}

/**
 * Validates the project prizes.
 *
 * @param thisForm the form containing the prizes data.
 * @param msgList the message list.
 * @return {boolean} true if validation passed, false otherwise.
 */
function validate_prizes(thisForm, msgList) {
    var tot = validate_prizes_amount(document.getElementById("contest-prizes-table"), "contest_prizes", "Contest Prize", thisForm, msgList);
    var contestPrizesValid = tot != -1;
    var checkpointPrizesValid = validate_prizes_amount(document.getElementById("checkpoint-prizes-table"), "checkpoint_prizes", "Checkpoint Prize", thisForm, msgList) != -1;
    if (!contestPrizesValid && checkpointPrizesValid) showPrizeTab('contest-prizes-table', document.getElementById("contest-prizes-link"));
    if (contestPrizesValid && !checkpointPrizesValid) showPrizeTab('checkpoint-prizes-table', document.getElementById("checkpoint-prizes-link"));
    return contestPrizesValid && checkpointPrizesValid;
}

/**
 * Validates the project resources.
 * @param thisForm the form to validate
 * @msgList the list of validation error messages
 * @return true if no validation failures
 */
function validate_resources(thisForm, msgList) {
    var msg = null;

    // get resources table node
    var resourcesTable = document.getElementById("resources_tbl");

    // resource index
    var index = 0;
    for (var i = 1; ; ++i) {
        var action = thisForm["resources_action[" + i + "]"];
        // jump out of the loop if there is no more phase
        if (action == null) break;
        // ignore phase with delete action
        if (action.value == "delete") continue;

        var msgPrefix = "Resources " + (++index) + ": ";
        var tableRow = resourcesTable.rows[i + 2];

        // the div to show the error message
        var msgDiv;

        // get the message div and hide it
        msgDiv = getChildByName(tableRow, "role_validation_msg");
        msgDiv.innerHTML = "";
        msgDiv.style.display = "none";

        // validate role
        var role = thisForm["resources_role[" + i + "]"].value;
        if (role == "-1") {
            msg = "Role should be selected";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }

        // get the message div and hide it
        msgDiv = getChildByName(tableRow, "name_validation_msg");
        msgDiv.innerHTML = "";
        msgDiv.style.display = "none";

        // validate name
        var name = thisForm["resources_name[" + i + "]"].value;
        if (emptyString.test(name)) {
            msg = "Name can not be empty";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }
    }

    return msg == null;
}

/**
 * Validates the project timeline.
 * @param thisForm the form to validate
 * @msgList the list of validation error messages
 * @return true if no validation failures
 */
function validate_timeline(thisForm, msgList) {
    var msg = null;

    // phase index
    var index = 0;
    for (var i = 1; ; ++i) {
        var action = thisForm["phase_action[" + i + "]"];
        // jump out of the loop if there is no more phase
        if (action == null) break;
        // ignore phase with delete action
        if (action.value == "delete") continue;

        // the prefix of error message in the alert window
        var msgPrefix = "Phase " + (++index) + ": ";

        // try to get the phase row
        var phase_js_id = thisForm["phase_js_id[" + i + "]"].value;
        var phase_row = document.getElementById(phase_js_id);

        // the div to show the error message
        var msgDiv;

        // get the message div and hide it
        msgDiv = getChildByName(phase_row, "start_date_validation_msg");
        msgDiv.innerHTML = "";
        msgDiv.style.display = "none";

        var isPhaseClosed = thisForm["isPhaseClosed[" + i + "]"].value == 'true';
        var arePhaseDependenciesEditable = thisForm['arePhaseDependenciesEditable'].value == 'true';
        if (!isPhaseClosed) {
            var startByFixedTime = false;
            var startByPhase = false;

            // validate start date
            var startDateInput = thisForm["phase_start_date[" + i + "]"];
            var isPhaseStartTimeEnabled = startDateInput
                && (startDateInput.getAttribute("disabled") == null);

            if (isPhaseStartTimeEnabled) {
                startByFixedTime = true;
                var start_date = thisForm["phase_start_date[" + i + "]"].value;
                var start_time = thisForm["phase_start_time[" + i + "]"].value;
                if (!isDateString(start_date)) {
                    msg = "Start Date should be in the form of \"mm.dd.yy\"";
                    add_error_message(msg, msgPrefix, msgDiv, msgList);
                }

                // validate start time
                if (!isTimeString(start_time)) {
                    msg = "Start Time should be in the form of \"hh:mm\"";
                    add_error_message(msg, msgPrefix, msgDiv, msgList);
                }
            }

            // if the phase starts by another phase, try to validate its additional days/hours
            var depPhaseInput = thisForm["phase_start_by_phase[" + i + "]"];
            var phaseStartPhaseInput;
            var phaseStartAmountInput;
            var isPhaseDependencyEnabled = false;
            var startByPhaseId;
            if (depPhaseInput) {
                if (depPhaseInput.length) {
                    isPhaseDependencyEnabled = depPhaseInput[1].checked;
                    phaseStartPhaseInput = thisForm["phase_start_phase[" + i + "]"][0];
                    startByPhaseId = phaseStartPhaseInput.value;
                    phaseStartAmountInput = thisForm["phase_start_amount[" + i + "]"][0];
                } else {
                    isPhaseDependencyEnabled = depPhaseInput.checked;
                    phaseStartPhaseInput = thisForm["phase_start_phase[" + i + "]"];
                    startByPhaseId = phaseStartPhaseInput.options[phaseStartPhaseInput.selectedIndex].value;
                    phaseStartAmountInput = thisForm["phase_start_amount[" + i + "]"];
                }
            }
            if (isPhaseDependencyEnabled) {
                startByPhase = true;
                if (startByPhaseId == '') {
                    msg = "Dependency phase must be selected";
                    add_error_message(msg, msgPrefix, msgDiv, msgList);
                }

                var start_amount = phaseStartAmountInput.value;
                if (start_amount == '') {
                    start_amount = 0;
                } else {
                    if (!isAllDigits(start_amount) || !isInteger(start_amount)) {
                        msg = "Additional Days/Hours/Minutes should be an integer";
                        add_error_message(msg, msgPrefix, msgDiv, msgList);
                    }
                }
            }

            if (!(startByFixedTime || startByPhase)) {
                msg = "Either fixed start time or dependency phase must be set for phase";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
        }

        // get the message div and hide it
        msgDiv = getChildByName(phase_row, "end_date_validation_msg");
        msgDiv.style.display = "none";
        msgDiv.innerHTML = "";

        var use_duration_true = thisForm["phase_use_duration[" + i + "]"][1].checked;
        var use_duration_false = thisForm["phase_use_duration[" + i + "]"][0].checked;

        // if neither two radio is selected, use duration by default
        if (! (use_duration_true || use_duration_false)) {
            use_duration_true = true;
        }

        var end_date = thisForm["phase_end_date[" + i + "]"].value;
        var end_time = thisForm["phase_end_time[" + i + "]"].value;
        var empty_end_datetime = emptyString.test(end_date) && emptyString.test(end_time);

        if (use_duration_false) {
            if (empty_end_datetime) {
                msg = "End Date should be specified";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }

            if (!empty_end_datetime) {
                // if end date/time are specified, validate them.
                if (!isDateString(end_date)) {
                    msg = "End Date should be in the form of \"mm.dd.yy\"";
                    add_error_message(msg, msgPrefix, msgDiv, msgList);
                }
                if (!isTimeString(end_time)) {
                    msg = "End Time should be in the form of \"hh:mm\"";
                    add_error_message(msg, msgPrefix, msgDiv, msgList);
                }
            }
        }

        var duration = thisForm["phase_duration[" + i + "]"].value;
        var empty_duration = emptyString.test(duration);

        msgDiv = getChildByName(phase_row, "duration_validation_msg");
        msgDiv.style.display = "none";
        msgDiv.innerHTML = "";

        if (use_duration_true) {
            if (empty_duration) {
                msg = "Duration should be specified";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }

            if (!empty_duration) {
                // if duration is specified, validate them.
                if (!isTimeSpanString(duration)) {
                    msg = "Duration should be in the form of \"hh\" or \"hh:mm\"";
                    add_error_message(msg, msgPrefix, msgDiv, msgList);
                }
            }
        }
    }

    return msg == null;
}

/**
 * Validates the Notes field.
 */
function validate_notes(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("notes_validation_msg");

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    //This check actually not required
    /*
     var strNotes = thisForm["notes"].value;
     if (emptyString.test(strNotes)) {
     msg = "Notes is a required field";
     add_error_message(msg, "", msgDiv, msgList);
     }
     */
    return msg == null;
}

/**
 * Validates the Explanation field.
 */
function validate_explanation(thisForm, msgList) {
    var msg = null;
    var msgDiv = document.getElementById("explanation_validation_msg");

    if (msgDiv == null) return true;

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var strExplanation = thisForm["explanation"].value;
    if (emptyString.test(strExplanation)) {
        msg = "Explanation is a required field";
        add_error_message(msg, "", msgDiv, msgList);
    }

    return msg == null;
}

/**
 * Validates the form when saving the changes to the project.
 * @param thisForm the form to validate
 * @param whether to show the alert window
 * @return true if no validation failures
 */
function validate_form(thisForm, popup) {
    // the list of validation error messages
    var msgList = new Array();

    validate_project_name(thisForm, msgList);
    validate_forum_id(thisForm, msgList);
    validate_component_id(thisForm, msgList);
    validate_external_reference_id(thisForm, msgList);
    validate_dr_points(thisForm, msgList);
    validate_timeline(thisForm, msgList);
    validate_resources(thisForm, msgList);
    validate_notes(thisForm, msgList);
    validate_prizes(thisForm, msgList);
    // validate_explanation(thisForm, msgList);

    // try to show an alert window
    if (popup && msgList.length != 0)
        alert("There have been validation errors:\n\n* " + msgList.join(';\n* ') + ".");
    if (msgList.length == 0) {
        disableSelect("contest_prizes_num[", false, canEditContestPrize);
        disableSelect("checkpoint_prizes_num[", false, canEditCheckpointPrize);
    }
    return msgList.length == 0;
}

function fixedStartTimeBoxChanged(box, p) {
    var p1 = box.name.indexOf('[');
    var p2 = box.name.indexOf(']');
    var phaseIdx = box.name.substring(p1 + 1, p2);
    var form = box.form;
    if (box.checked) {
        form["phase_start_date[" + phaseIdx + "]"].removeAttribute("disabled");
        form["phase_start_time[" + phaseIdx + "]"].removeAttribute("disabled");
    } else {
        form["phase_start_date[" + phaseIdx + "]"].setAttribute("disabled", "disabled");
        form["phase_start_time[" + phaseIdx + "]"].setAttribute("disabled", "disabled");
    }
    return true;
}

function phaseStartByPhaseBoxChanged(box, p) {
    var p1 = box.name.indexOf('[');
    var p2 = box.name.indexOf(']');
    var phaseIdx = box.name.substring(p1 + 1, p2);
    var form = box.form;
    if (box.checked) {
        form["phase_start_phase[" + phaseIdx + "]"].removeAttribute("disabled");
        form["phase_start_when[" + phaseIdx + "]"].removeAttribute("disabled");
        form["phase_start_plusminus[" + phaseIdx + "]"].removeAttribute("disabled");
        form["phase_start_amount[" + phaseIdx + "]"].removeAttribute("disabled");
        form["phase_start_dayshrs[" + phaseIdx + "]"].removeAttribute("disabled");
    } else {
        form["phase_start_phase[" + phaseIdx + "]"].setAttribute("disabled", "disabled");
        form["phase_start_when[" + phaseIdx + "]"].setAttribute("disabled", "disabled");
        form["phase_start_plusminus[" + phaseIdx + "]"].setAttribute("disabled", "disabled");
        form["phase_start_amount[" + phaseIdx + "]"].setAttribute("disabled", "disabled");
        form["phase_start_dayshrs[" + phaseIdx + "]"].setAttribute("disabled", "disabled");
    }
    return true;
}


function addPhaseFixedStartTimeBoxChanged(box) {
    var form = box.form;
    if (box.checked) {
        form["addphase_start_date"].removeAttribute("disabled");
        form["addphase_start_time"].removeAttribute("disabled");
    } else {
        form["addphase_start_date"].setAttribute("disabled", "disabled");
        form["addphase_start_time"].setAttribute("disabled", "disabled");
    }
    return true;
}

function addPhasePhaseStartByPhaseBoxChanged(box) {
    var form = box.form;
    if (box.checked) {
        form["addphase_start_phase"].removeAttribute("disabled");
        form["addphase_start_when"].removeAttribute("disabled");
        form["addphase_start_plusminus"].removeAttribute("disabled");
        form["addphase_start_amount"].removeAttribute("disabled");
        form["addphase_start_dayshrs"].removeAttribute("disabled");
    } else {
        form["addphase_start_phase"].setAttribute("disabled", "disabled");
        form["addphase_start_when"].setAttribute("disabled", "disabled");
        form["addphase_start_plusminus"].setAttribute("disabled", "disabled");
        form["addphase_start_amount"].setAttribute("disabled", "disabled");
        form["addphase_start_dayshrs"].setAttribute("disabled", "disabled");
    }
    return true;
}