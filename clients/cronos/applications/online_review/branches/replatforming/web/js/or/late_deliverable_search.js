var submitForm = true;

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
    if (!emptyString.test(project_id) && 'Any' != project_id) {
        if (!isAllDigits(project_id)) {
            msg = "project id should be numerics only";
            add_error_message(msg, "", msgDiv, msgList);
        }
    }

    return msg == null;
}

/**
 * Validates the minimum/maximum deadline.
 * 
 * @param thisForm the late deliverable search form
 * @param msgList the list of error messages
 * @param errorDivID ID for span element to display validation error
 * @param fieldName a name of the input field to validate.
 * @return true if there is no failure, otherwise false.
 */
function validate_deadline(thisForm, msgList, errorDivID, fieldName) {
    var msg = null;
    var msgDiv = document.getElementById(errorDivID);

    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    var value = thisForm[fieldName].value;
    if (value == 'MM.DD.YYYY') {
        value = '';
    }
    
    // empty is allowed
    if (!emptyString.test(value)) {
        var parsedDate = getDateString(value);
        if (parsedDate == 'Invalid') {
            msg = "invalid date";
            add_error_message(msg, "", msgDiv, msgList);
        } else {
            thisForm[fieldName].value = parsedDate;
        }
    }

    return msg == null;
}

/**
 * Validates the minimum/maximum deadline.
 * 
 * @param input the input field providing the date value
 * @return a value to be set for input field on focus 
 */
function getDateValueOnFocus(input, isOnFocus) {
    var val = input.value;
    if (isOnFocus) {
        input.style.color= '#333333';
        if (val == 'MM.DD.YYYY') {
            return '';
        } else {
            return val;
        }
    } else {
        if (val.length == 0 || emptyString.test(val)) {
            input.style.color= 'gray';
            return 'MM.DD.YYYY';
        } else {
            return val;
        }
    }
}

/**
 * Validates the minimum/maximum deadline.
 * 
 * @param input the input field providing the date value
 * @return a value to be set for input field on focus 
 */
function getTextValueOnFocus(input, isOnFocus) {
    var val = input.value;
    if (isOnFocus) {
        input.style.color= '#333333';
        if (val == 'Any') {
            return '';
        } else {
            return val;
        }
    } else {
        if (val.length == 0 || emptyString.test(val)) {
            input.style.color= 'gray';
            return 'Any';
        } else {
            return val;
        }
    }
}

/**
 * Colors the form fields based on their current values.
 */
function colorFormFields() {
    var form = document.getElementById('ViewLateDeliverablesForm');
    if (form['project_id'].value == '') {
        form['project_id'].value = 'Any';
    }
    if (form['handle'].value == '') {
        form['handle'].value = 'Any';
    }
    if (form['min_deadline'].value == '') {
        form['min_deadline'].value = 'MM.DD.YYYY';
    }
    if (form['max_deadline'].value == '') {
        form['max_deadline'].value = 'MM.DD.YYYY';
    }
    if (form['project_id'].value == 'Any') {
        form['project_id'].style.color = 'gray';
    }
    if (form['handle'].value == 'Any') {
        form['handle'].style.color = 'gray';
    }
    if (form['min_deadline'].value == 'MM.DD.YYYY') {
        form['min_deadline'].style.color = 'gray';
    }
    if (form['max_deadline'].value == 'MM.DD.YYYY') {
        form['max_deadline'].style.color = 'gray';
    }
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
    validate_deadline(thisForm, msgList, "min_deadline_validation_msg", "min_deadline");
    validate_deadline(thisForm, msgList, "max_deadline_validation_msg", "max_deadline");

    // try to show an alert window
    if (popup && msgList.length != 0) {
        document.getElementById('globalMesssage').style.display = 'block';
    }

    return msgList.length == 0;
}

/**
 * Sets the value of specified input element to empty string if current value of that element is equal to specified
 * default value.
 * 
 * @param input a form input element.
 * @param defaultValue a default value for the input value.
 */
function emptyIfNotSet(input, defaultValue) {
    var value = input.value;
    if (value == defaultValue) {
        input.value = '';
    }
}

/**
 * submit the form, it will validate the form and submit it, or just reset the form.
 * @param thisForm the form to submit/clear
 * @param popup whether to show the alert window
 * @return false if it mean to clear form, or true if validation passed, otherwise false
 */
function submit_form(thisForm, popup) {
    if (submitForm) {
        clearServerSideMessages();
        var isFormValid = validate_form(thisForm, popup);
        if (isFormValid) {
            thisForm.submit();
        }
        return false;
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

    msgDiv = document.getElementById("min_deadline_validation_msg");
    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    msgDiv = document.getElementById("max_deadline_validation_msg");
    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    clearServerSideMessages();
    
    colorFormFields();
}

/**
 * Hides the server side validation errors if any are displayed.
 */
function clearServerSideMessages() {
    var msgDiv = document.getElementById("handle_serverside_validation");
    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";
    
    msgDiv = document.getElementById("project_id_serverside_validation");
    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";
    
    msgDiv = document.getElementById("deadline_serverside_validation");
    msgDiv.innerHTML = "";
    msgDiv.style.display = "none";

    msgDiv = document.getElementById("globalMesssage");
    if (msgDiv != null) {
        msgDiv.style.display = "none";
    }
}

/**
 * Clears the form.
 * @param thisForm the form to clear.
 */
function clearForm(thisForm) {
    var elements = thisForm.elements;

    for (i = 0; i < elements.length; i++) {
        var field_type = elements[i].type.toLowerCase();

        switch (field_type) {
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

/**
 * Handles the event on clicking the collapse/expand button.
 * 
 * @param srcElement a source element of the event.
 */
function expcollHandler(srcElement) {
    var rowId = srcElement.id + "r";
    var iconId = srcElement.id + "i";
    var rowElement = document.getElementById(rowId);
    if (rowElement.style.display == "none") {
        rowElement.style.display = "";
        document.getElementById(iconId).src = "/i/or/minus.gif";
    } else {
        rowElement.style.display = "none";
        document.getElementById(iconId).src = "/i/or/plus.gif";
    }
    if (srcElement.blur) {
        srcElement.blur();
    }
    return false;
}

/**
 * Submits the Edit Late Deliverable form having it's input fileds validated first. If validation fails teh form
 * is not submitted.
 *  
 * @param thisForm the form to validate
 */
function submitEditLateDeliverableForm(thisForm) {
    var isFormValid = validateEditLateDeliverableForm(thisForm);
    if (isFormValid) {
        var msgDiv = document.getElementById("globalMesssage");
        if (msgDiv != null) {
            msgDiv.style.display = "none";
        }
        return true;
    } else {
        document.getElementById('globalMesssage').style.display = 'block';
        return false;
    }
}


/**
 * Validates the form when saving the changes to the late deliverable.
 * 
 * @param thisForm the form to validate
 * @return true if no validation failures
 */
function validateEditLateDeliverableForm(thisForm) {
    var msgList = new Array();

    validateText(thisForm, msgList, "explanation_validation_msg", "explanation");
    validateText(thisForm, msgList, "response_validation_msg", "response");

    return msgList.length == 0;
}


/**
 * Validates the minimum/maximum deadline.
 * 
 * @param thisForm the late deliverable search form
 * @param msgList the list of error messages
 * @param errorDivID ID for span element to display validation error
 * @param fieldName a name of the input field to validate.
 * @return true if there is no failure, otherwise false.
 */
function validateText(thisForm, msgList, errorDivID, fieldName) {
    var msg = null;
    var msgDiv = document.getElementById(errorDivID);

    if (msgDiv) {
        msgDiv.innerHTML = "";
        msgDiv.style.display = "none";

        var value = thisForm[fieldName].value;

        // empty is allowed
        if (emptyString.test(value)) {
            msg = fieldName + ' must not be empty';
            add_error_message(msg, "", msgDiv, msgList);
        }
    }

    return msg == null;
}

/**
 * Submits the Search Late Deliverables Form if Enter key is pressed on specified input element. 
 * 
 * @param e an event for key pressing.
 */
function submitOnEnter(input, e) {
    var keyCode;
    if (window.event) {
        keyCode = window.event.keyCode;
    }
    else if (e) {
        keyCode = e.which;
    }
    else {
        return true;
    }

    if (keyCode == 13) {
        submitForm = true;
        submit_form(input.form, true);
        return false;
    }
    else {
        return true;
    }
}
