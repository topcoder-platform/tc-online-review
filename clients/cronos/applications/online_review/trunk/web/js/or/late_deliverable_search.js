var submitForm = false;

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
 * Checks whether there is any selection for the given select form element.
 * @param selObj the select form element.
 * @return true if the given select box has some selection, otherwise false
 */
function selectAny(selObj) {
  for (i = 0; i < selObj.options.length; i++) {
    if (selObj.options[i].selected) {
      return true;
    }
  }

  return false;
}


/**
 * Validates the project id.
 * @param thisForm the late deliverable search form
 * @param msgList the list of error messages
 * @return true if there is no failure, otherwise false.
 */
function validate_project_id(thisForm, msgList) {
	var msg = null;
	var msgDiv = document.getElementById("project_id_validation_msg");

	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	var project_id = thisForm["project_id"].value;
	// empty is allowed
	if (!emptyString.test(project_id)) {
		if (!isAllDigits(project_id)) {
		    msg = "project id should be numerics only";
		    add_error_message(msg, "", msgDiv, msgList);
		}
	}

	return msg == null;
}

/**
 * Validates the project categories.
 * @param thisForm the late deliverable search form
 * @param msgList the list of error messages
 * @return true if there is no failure, otherwise false.
 */
function validate_project_categories(thisForm, msgList) {
	var msg = null;
	var msgDiv = document.getElementById("project_categories_validation_msg");

	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	if (!selectAny(thisForm["project_categories"])) {
		msg = "at least one category option should be selected";
		add_error_message(msg, "", msgDiv, msgList);
	}

	return msg == null;
}

/**
 * Validates the project statuses.
 * @param thisForm the late deliverable search form
 * @param msgList the list of error messages
 * @return true if there is no failure, otherwise false.
 */
function validate_project_statuses(thisForm, msgList) {
	var msg = null;
	var msgDiv = document.getElementById("project_statuses_validation_msg");

	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	if (!selectAny(thisForm["project_statuses"])) {
		msg = "at least one status option should be selected";
		add_error_message(msg, "", msgDiv, msgList);
	}

	return msg == null;
}

/**
 * Validates the deliverable types.
 * @param thisForm the late deliverable search form
 * @param msgList the list of error messages
 * @return true if there is no failure, otherwise false.
 */
function validate_deliverable_types(thisForm, msgList) {
	var msg = null;
	var msgDiv = document.getElementById("deliverable_types_validation_msg");

	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	if (!selectAny(thisForm["deliverable_types"])) {
		msg = "at least one deliverable type option should be selected";
		add_error_message(msg, "", msgDiv, msgList);
	}

	return msg == null;
}


/**
 * Validates the form when saving the changes to the project.
 * @param thisForm the form to validate
 * @param popup whether to show the alert window
 * @return true if no validation failures
 */
function validate_form(thisForm, popup) {
    // the list of validation error messages
    var msgList = new Array();

    validate_project_categories(thisForm, msgList);
    validate_project_statuses(thisForm, msgList);
    validate_deliverable_types(thisForm, msgList);
    validate_project_id(thisForm, msgList);

    // try to show an alert window
    if (popup && msgList.length != 0)
        alert("There have been validation errors:\n\n* " + msgList.join(';\n* ') + ".");

    return msgList.length == 0;
}

/**
 * submit the form, it will validate the form and submit it, or just reset the form.
 * @param thisForm the form to submit/clear
 * @param popup whether to show the alert window
 * @return false if it mean to clear form, or true if validation passed, otherwise false
 */
function submit_form(thisForm, popup) {
	if (submitForm) {
		return validate_form(thisForm, popup);
	} else {
		reset_form(thisForm);

		return false;
	}
}

/**
 * Resets the form, by clear the selections and error messages.
 *
 * @param thisForm the form to clear
 */
function reset_form(thisForm) {
	// clear the form
	clearForm(thisForm);

	// clear the error messages.
	var msgDiv = document.getElementById("deliverable_types_validation_msg");
	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	msgDiv = document.getElementById("project_statuses_validation_msg");
	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	msgDiv = document.getElementById("project_categories_validation_msg");
	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	msgDiv = document.getElementById("project_id_validation_msg");
	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	msgDiv = document.getElementById("handle_serverside_validation");
	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	msgDiv = document.getElementById("project_id_serverside_validation");
	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";

	msgDiv = document.getElementById("globalMesssage");
	if (msgDiv != null) {
	    msgDiv.innerHTML = "";
	    msgDiv.style.display = "none";
	}
}

/**
 * Clears the form.
 * @param thisForm the form to clear.
 */
function clearForm(thisForm) {
	var elements = thisForm.elements;

	for(i = 0; i < elements.length; i++) {
		var field_type = elements[i].type.toLowerCase();

		switch(field_type) {
		    case "text":
		        elements[i].value = "";
		        break;

		    case "select-one":
		    case "select-multiple":
		    	// select the first one
		        elements[i].selectedIndex = 0;
		        elements[i].selectedIndex = 0;
		        break;

		    default:
		        break;
		}
    }
}

/**
 * Expands all search results.
 * @return false always
 */
function expandAll() {
    var i = 0;
    while(true) {
        var rowId = "Out" + i + "r";
        var iconId = "Out" + i + "i";
        var rowElement = document.getElementById(rowId);
        if (rowElement == null) {
            break;
        }

        rowElement.style.display = "";
        document.getElementById(iconId).src = "/i/or/minus.gif";

        i++;
    }

    return false;
 }

/**
 * Collapses all search results.
 * @return false always
 */
 function collapseAll() {
    var i = 0;
    while(true) {
        var rowId = "Out" + i + "r";
        var iconId = "Out" + i + "i";
        var rowElement = document.getElementById(rowId);
        if (rowElement == null) {
            break;
        }

        rowElement.style.display = "none";
        document.getElementById(iconId).src = "/i/or/plus.gif";

        i++;
    }

    return false;
 }