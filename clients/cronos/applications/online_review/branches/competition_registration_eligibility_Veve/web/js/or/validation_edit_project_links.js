/**
 * The script contains js variables/functions for edit project links page. This
 * is for "OR Project Linking" Assembly.
 *
 * @author BeBetter
 * @version 1.0
 */
// last link index
var lastLinkIndex = -1;

// All possible options
var projectOptions = new Array();

/**
 * Adds a new project link dynamically in link table.
 */
function newProjectLink() {
    //Get add link table node
    var newLinksTable = document.getElementById("newLinks");
    // Get the number of rows in table
    var rowCount = newLinksTable.rows.length;

    // This might only happen in tests. if we have enough rows, we will not add
    // it any more
    if (getActiveLinkCount() >= projectOptions.length) {
        alert("we can not add project link any more. all projects will be used by current links.");
        return;
    }

    // Create a new row into  table
    var templateRow = newLinksTable.rows[2];
    var newRow = cloneInputRow(templateRow);

    // Rows should vary colors
    var rows = newLinksTable.rows;
    var strLastRowStyle = "dark"; // This variable will remember the style of
                                    // the last row
    // Find first non-hidden row, starting from the bottom of the table
    for ( var i = rows.length - 2; i >= 0; --i) {
        if (rows[i].style["display"] == "none")
            continue;
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

    // Increase link index
    lastLinkIndex++;
    // Rename all the inputs to have a new index
    patchAllChildParamIndexes(newRow, lastLinkIndex);
    // Insert new row into links table
    newLinksTable.tBodies[0].insertBefore(newRow,
            newLinksTable.rows[rowCount - 1]);

    // Resets template row
    getProjectDropDown(templateRow).selectedIndex = 0;

    // Resets drop downs
    resetDropDowns();
}

/**
 * Deletes project link from the link table.
 */
function deleteProjectLink(linkRowNode) {
    // Hide the row, don't delete it as the link
    // should be deleted from DB on submit
    linkRowNode.style["display"] = "none";

    // Set hidden link_action parameter to "delete"
    var actionInput = getInputAction(linkRowNode);
    actionInput.value = "delete";

    // Make rows vary color
    var rows = linkRowNode.parentNode.rows;
    var initial = 0;
    for ( var i = 0; i < rows.length; i++) {
        // Skip hidden rows as they shouldn't affect row coloring
        if (rows[i].style["display"] == "none")
            continue;
        rows[i].className = (initial++ % 2 == 0) ? "dark" : "light";
    }

    // reset drop downs
    resetDropDowns();
}

/**
 * Call back function. It is invoked when project input box is changed.
 */
function onProjectInputChange(projectInput) {
    //trim the value first
    projectInput.value = trimString(projectInput.value);

    var projectDropDown = getProjectDropDown(projectInput.parentNode.parentNode);

    // Sets the drop down value
    projectDropDown.selectedIndex = 0;
    for ( var i = 0; i < projectDropDown.options.length; i++) {
        var option = projectDropDown.options[i];
        if (option.value == projectInput.value) {
            projectDropDown.selectedIndex = i;
        }
    }

    // update msg div
    var msgDiv = resetMsgDiv(projectInput);
    if (projectDropDown.selectedIndex == 0) {
        var msg = "project id is empty or invalid or has been used.";
        add_error_message(msg, "", msgDiv, null);
    } else {
        resetMsgDiv(projectDropDown);
    }

    // reset drop downs
    resetDropDowns();
}


/**
 * Call back function. It is invoked when link type drop down is selected different value.
 */
function onLinkTypeDropDownChange(linkTypeDropDown) {
    // update msg div
    if (linkTypeDropDown.selectedIndex != 0) {
        resetMsgDiv(linkTypeDropDown);
    }
}

/**
 * Resets all drop downs to prevent users from selecting same project.
 */
function resetDropDowns() {
    var valueMap = getSelectedProjectValueMap();

    // Get add link table node
    var newLinksTable = document.getElementById("newLinks");

    // Goes over all rows except header/footer
    for ( var i = 2; i < newLinksTable.rows.length - 1; i++) {
        var linkRowNode = newLinksTable.rows[i];
        var projectDropDown = getProjectDropDown(linkRowNode);
        var inputAction = getInputAction(linkRowNode);

        if (inputAction.value != 'add') {
            continue;
        }

        var selectedValue = projectDropDown.value;
        // Reset, excluding ones selected by others
        projectDropDown.options.length = 0;

        for ( var idx = 0; idx < projectOptions.length; idx++) {
            // if the select :
            // 1. the first row
            // 2. already selects it (except the template row)
            // 3. not selected by others
            // we allow to be here
            var option = new Option(projectOptions[idx].text,
                    projectOptions[idx].value);

            if (option.value == '-1'
                    || (option.value == selectedValue && i != 2)
                    || !valueMap[option.value]) {
                projectDropDown.options[projectDropDown.options.length] = option;
            }
        }

        // Sets back value
        projectDropDown.value = selectedValue;
    }
}

/**
 * Gets map for selected project values.
 */
function getSelectedProjectValueMap() {
    var valueMap = {};

    // Get add link table node
    var newLinksTable = document.getElementById("newLinks");

    // Goes over all rows except header/footer, ALSO template row.
    // template row is for trying, not actual values
    for ( var i = 3; i < newLinksTable.rows.length - 1; i++) {
        var linkRowNode = newLinksTable.rows[i];
        var projectDropDown = getProjectDropDown(linkRowNode);
        var inputAction = getInputAction(linkRowNode);
        if (inputAction.value == 'add' && projectDropDown.selectedIndex != 0) {
            valueMap[projectDropDown.value] = true;
        }
    }

    return valueMap;
}

/**
 * It returns link row which is not deleted. The count includes template row.
 *
 * @return the count for active links
 */
function getActiveLinkCount() {
    //Get add link table node
    var newLinksTable = document.getElementById("newLinks");

    // Goes over all rows except header/footer
    var count = 0;
    for ( var i = 2; i < newLinksTable.rows.length - 1; i++) {
        var inputAction = getInputAction(newLinksTable.rows[i]);
        if (inputAction.value == 'add') {
            count++;
        }
    }

    return count;
}

/**
 * Validates the form when saving the changes.

 * @param thisForm the form to validate
 * @param whether to show the alert window
 * @return true if no validation failures
 */
function validate_form(thisForm, popup) {
    // the list of validation error messages
    var msgList = new Array();

    validate_link(msgList);

    // try to show an alert window
    if (popup && msgList.length != 0) {
        alert("There have been validation errors:\n\n* "
                + msgList.join(';\n* ') + ".");
    }

    return msgList.length == 0;
}

/**
 * Validates all links which is marked as "add".
 *
 * @param msgList the error message list
 */
function validate_link(msgList) {
    var newLinksTable = document.getElementById("newLinks");

    // Goes over all rows except header/footer, ALSO template row.
    for ( var i = 3; i < newLinksTable.rows.length - 1; i++) {
        var linkRowNode = newLinksTable.rows[i];

        var projectDropDown = getProjectDropDown(linkRowNode);
        var linkTypeDropDown = getLinkTypeDropDown(linkRowNode);
        var inputAction = getInputAction(linkRowNode);

        if (inputAction.value != 'add') {
            continue;
        }

        var msgPrefix = "Project Link " + (i - 2) + " : ";

        var msgDiv = resetMsgDiv(projectDropDown);
        if (projectDropDown.selectedIndex == 0) {
            var msg = "Project should be selected";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }

        msgDiv = resetMsgDiv(linkTypeDropDown);
        if (linkTypeDropDown.selectedIndex == 0) {
            var msg = "Link type should be selected";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }
    }

}

/**
 * Adds a validation error message to the message list and shows the message in the message div.
 *
 * @param msg the error message
 * @param msgPrefix the prefix of the error message to add to the list
 * @param msgDiv the div element to show the error message
 * @param msgList the list of error messages
 */
function add_error_message(msg, msgPrefix, msgDiv, msgList) {
    // NOTE: "&#160;" is equivalence of "&nbsp;"
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
 * Gets and resets the error message div. It is along side with input element in the same
 * cell.
 *
 * @param inputElement the input element the message div is for
 */
function resetMsgDiv(inputElement) {
    var msgDiv = getChildByName(inputElement.parentNode,
            "project_link_validation_msg");
    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";
    return msgDiv;
}


/**
 * Gets project drop down.
 *
 * @param linkRowNode the row node which is the containing ancestor of this element
 */
function getProjectDropDown(linkRowNode) {
    var dropdownCell = linkRowNode.cells[1];
    return getChildrenByNamePrefix(dropdownCell, "link_dest_id")[0];
}

/**
 * Gets link type drop down.
 *
 * @param linkRowNode the row node which is the containing ancestor of this element
 */
function getLinkTypeDropDown(linkRowNode) {
    var dropdownCell = linkRowNode.cells[0];
    return getChildrenByNamePrefix(dropdownCell, "link_type_id")[0];
}

/**
 * Gets input action hidden element.
 *
 * @param linkRowNode the row node which is the containing ancestor of this element
 */
function getInputAction(linkRowNode) {
    return linkRowNode.cells[2].getElementsByTagName("input")[0];
}
