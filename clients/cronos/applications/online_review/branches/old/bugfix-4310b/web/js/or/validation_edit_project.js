/*
 * Adds a validation error message to the message list and shows the message in the message div.
 * @param msg the error message
 * @param msgPrefix the prefix of the error message to add to the list
 * @param msgDiv the div element to show the error message
 * @param msgList the list of error messages
 */	    
function add_error_message(msg, msgPrefix, msgDiv, msgList) {
    if (emptyString.test(msgDiv.innerHTML)) {
        msgDiv.innerHTML = "*" + msg;
    } else {
        msgDiv.innerHTML += "<br>*" + msg;
    }
	msgDiv.style.display = "block";
	
	if (msgList != null) {
        msgList.push(msgPrefix + msg);
    }
}

/*
 * Validates the project name.
 */
function validate_project_name(thisForm, msgList) {
	var msg = null;
	
	var msgDiv = document.getElementById("project_name_validation_msg");
    msgDiv.innerHTML = "";
	msgDiv.style.display = "none";
	
	var project_name = thisForm["project_name"].value;
    if (emptyString.test(project_name)) {
	    msg = "Project Name can not be empty.";
	    add_error_message(msg, "", msgDiv, msgList);
	}
	
	return msg == null;
}

/*
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
            msg = "Forum Id should be an integer [0, 2147483647].";
	        add_error_message(msg, "", msgDiv, msgList);
    	}
	}
	return msg == null;
}

/*
 * Validates the component id.
 */
function validate_component_id(thisForm, msgList) {
	var msg = null;
	
	var msgDiv = document.getElementById("component_id_validation_msg");
    	msgDiv.innerHTML = "";
	msgDiv.style.display = "none";
	
	var forum_id = thisForm["component_id"].value;
    	if (forum_id.length > 0) {
        	if (!isAllDigits(forum_id) || !isInteger(forum_id)) {
            		msg = "Component Id should be an integer [0, 2147483647].";
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

/*
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
            msg = "Role should be selected.";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }
        
        // get the message div and hide it        
        msgDiv = getChildByName(tableRow, "name_validation_msg");
		msgDiv.innerHTML = "";
		msgDiv.style.display = "none";
		
		// validate name
        var name = thisForm["resources_name[" + i + "]"].value;
        if (emptyString.test(name)) {
            msg = "Name can not be empty.";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }
        
        // get the message div and hide it
        msgDiv = getChildByName(tableRow, "payment_validation_msg");
		msgDiv.innerHTML = "";
		msgDiv.style.display = "none";
		
		// validate payment
        var payment = thisForm["resources_payment[" + i + "]"][0].checked;
        var payment_amount = thisForm["resources_payment_amount[" + i + "]"].value;
        if (payment) {
            // if there is a payment, payment amount should be a float
            if (!isFloat(payment_amount)) {
            	msg = "Payment Amount should be a currency value.";
            	add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
        }
        
        // get the message div and hide it       
        msgDiv = getChildByName(tableRow, "paid_validation_msg");
        msgDiv.innerHTML = "";
		msgDiv.style.display = "none";
		
		// validate paid
		var paid = thisForm["resources_paid[" + i + "]"].value;
        if(emptyString.test(paid)) {
            msg = "Paid should be selected.";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }
    }
    
    return msg == null;
}

/*
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
 		       
        var start_by_phase = thisForm["phase_start_by_phase[" + i + "]"][1].checked;
        if (!start_by_phase) {
            // if the phase does not start by another phase, try to validate its start datetime
            
            // validate start date
            var start_date = thisForm["phase_start_date[" + i + "]"].value;
            if (!isDateString(start_date)) {
                msg = "Start Date should be in the form of \"mm.dd.yy\".";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
             
            // validate start time
            var start_time = thisForm["phase_start_time[" + i + "]"].value;
            if (!isTimeString(start_time)) {
                msg = "Start Time should be in the form of \"hh:mm\".";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
        } else {
            // if the phase starts by another phase, try to validate its additional days/hours
            
            var start_amount = thisForm["phase_start_amount[" + i + "]"].value;
            if (start_amount == '') {
                start_amount = 0;
            }else if (!isAllDigits(start_amount) || !isInteger(start_amount)) {
                msg = "Additional Days/Hours should be an integer.";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
        }
        
        // get the message div and hide it        
        msgDiv = getChildByName(phase_row, "end_date_validation_msg");
        msgDiv.style.display = "none";
        msgDiv.innerHTML = "";
        
        var end_date = thisForm["phase_end_date[" + i + "]"].value;
        var end_time = thisForm["phase_end_time[" + i + "]"].value;
        var empty_end_datetime = emptyString.test(end_date) && emptyString.test(end_time);
        
        if (!empty_end_datetime) {
            // if end date/time are specified, validate them.
            if (!isDateString(end_date)) {
                msg = "End Date should be in the form of \"mm.dd.yy\".";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
            if (!isTimeString(end_time)) {
                msg = "End Time should be in the form of \"hh:mm\".";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
        }
        
        var duration = thisForm["phase_duration[" + i + "]"].value;
        var empty_duration = emptyString.test(duration);
        
        if (!empty_duration) {
            // if duration is specified, validate them.
            if (!isTimeSpanString(duration)) {
                msg = "Duration should be in the form of \"hh\" or \"hh:mm\".";
                add_error_message(msg, msgPrefix, msgDiv, msgList);
            }
        }
        
        // either end datetime or duration must be specified.
        if (empty_end_datetime && empty_duration) {
            msg = "End DateTime or Duration Required.";
            add_error_message(msg, msgPrefix, msgDiv, msgList);
        }
    }
    
    return msg == null;
}
	    
/*
 * Validates the form when saving the changes to the project.
 * @param thisForm the form to validate
 * @param whether to show the alert window
 * @return true if no validation failures
 */
function validate_form(thisForm, popup) {
	//debugger;
	
	// the list of validation error messages
	var msgList = new Array();
	
	validate_project_name(thisForm, msgList);
	
	validate_forum_id(thisForm, msgList);
	
	validate_component_id(thisForm, msgList);
		
	validate_timeline(thisForm, msgList);

	validate_resources(thisForm, msgList);
	
	// try to show an alert window
	if (popup && msgList.length != 0)
	    alert("Validation Failed:\n\n" + msgList.join('\n'));

	return msgList.length == 0;
}